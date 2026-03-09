package com.its255.io;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import com.its255.schema.FieldSpec;
import com.its255.schema.FieldType;
import com.its255.schema.RecordType;

/**
 * Fixed-length 255-byte parser supporting ALPHA, NUMERIC_TEXT, PACKED_DECIMAL (COMP-3) and BINARY (COMP/COMP-4).
 *
 * Performance features:
 *  - Single-thread fast path: parses directly from a heap buffer, no per-record copies.
 *  - Byte-level rtrim using ASCII/EBCDIC space; avoids String.trim().
 *  - Optimized COMP-3 and overpunch decoding.
 *  - Parallel parsing:
 *      * parseParallel(...) — unordered emission for max throughput.
 *      * parseParallelOrdered(...) — ordered emission while parsing in parallel.
 *
 * Java 18+, Windows/Linux.
 */
public final class Fixed255Parser {
    public static final int RECORD_LEN = 255;

    private final Charset charset;
    private final boolean isEBCDIC;
    private final byte spaceByte;

    private final Map<RecordType, List<FieldSpec>> schemas;

    // COBOL 1-based position of the record type and its length
    private final int recTypeStart1Based;
    private final int recTypeLen;

    public Fixed255Parser(Map<RecordType, List<FieldSpec>> schemas,
                          Charset charset,
                          int recTypeStart1Based,
                          int recTypeLen) {
        this.schemas = Objects.requireNonNull(schemas, "schemas");
        this.charset = Objects.requireNonNull(charset, "charset");
        this.recTypeStart1Based = recTypeStart1Based;
        this.recTypeLen = recTypeLen;

        String csn = this.charset.name();
        // Heuristic: EBCDIC charsets typically start with "Cp" and/or contain "EBCDIC"
        this.isEBCDIC = csn.startsWith("Cp") || csn.toUpperCase(Locale.ROOT).contains("EBCDIC");
        this.spaceByte = this.isEBCDIC ? (byte) 0x40 : (byte) 0x20; // EBCDIC 0x40 vs ASCII 0x20
    }

    /* =======================================================================
       PUBLIC API
       ======================================================================= */

    /** Same interface as your original. */
    public interface RecordConsumer {
        void onRecord(long recNo, RecordType type, Map<String, String> values);
        default void onUnknown(long recNo, RecordType type, String typeCode, byte[] raw) {
            System.err.printf("Unknown record type at #%d: '%s'%n", recNo, typeCode);
        }
    }

    /**
     * Backward-compatible entrypoint: uses single-thread fast path with default batch size.
     */
    public void parse(Path input, RecordConsumer consumer) throws IOException {
        parseFast(input, consumer, 32768); // ~8.3MB per read (32768 * 255)
    }

    /** Single-thread, in-order, optimized path. */
    public void parseFast(Path input, RecordConsumer consumer) throws IOException {
        parseFast(input, consumer, 32768);
    }

    /**
     * Single-threaded fast path with tunable recordsPerBatch.
     * Try 8192–65536 (2–16MB) depending on your hardware.
     */
    public void parseFast(Path input, RecordConsumer consumer, int recordsPerBatch) throws IOException {
        if (recordsPerBatch <= 0) throw new IllegalArgumentException("recordsPerBatch must be > 0");
        final int CHUNK = RECORD_LEN * recordsPerBatch;

        try (FileChannel ch = FileChannel.open(input, StandardOpenOption.READ)) {
            long size = ch.size();
            if ((size % RECORD_LEN) != 0) {
                long partialIndex = (size / RECORD_LEN) + 1;
                throw new EOFException("Partial record at #" + partialIndex);
            }

            byte[] buf = new byte[CHUNK];
            int filled = 0;
            long recNo = 0;

            while (true) {
                int read = ch.read(ByteBuffer.wrap(buf, filled, buf.length - filled));
                if (read == -1) {
                    if (filled != 0) {
                        throw new EOFException("Partial record at #" + (recNo + 1));
                    }
                    break;
                }
                filled += read;

                int usable = (filled / RECORD_LEN) * RECORD_LEN;
                int off = 0;

                while (off < usable) {
                    int recBase = off;
                    recNo++;

                    String typeCode = sliceTrim(buf, recBase + (recTypeStart1Based - 1), recTypeLen);
                    RecordType rt = RecordType.from(typeCode);
                    List<FieldSpec> layout = schemas.get(rt);
                    if (layout == null) {
                        byte[] raw = Arrays.copyOfRange(buf, recBase, recBase + RECORD_LEN);
                        consumer.onUnknown(recNo, rt, typeCode, raw);
                    } else {
                        Map<String, String> values = parseFieldsFromArray(buf, recBase, layout);
                        consumer.onRecord(recNo, rt, values);
                    }

                    off += RECORD_LEN;
                }

                int leftover = filled - usable;
                if (leftover > 0) {
                    System.arraycopy(buf, usable, buf, 0, leftover);
                }
                filled = leftover;
            }
        }
    }

    /**
     * High-throughput parallel parsing (unordered emission).
     * Use when consumer is thread-safe and order does not matter.
     */
    public void parseParallel(Path input, RecordConsumer consumer, int workers, int batchBytes) throws IOException {
        if (workers <= 0) throw new IllegalArgumentException("workers must be > 0");
        if (batchBytes < RECORD_LEN) throw new IllegalArgumentException("batchBytes must be >= " + RECORD_LEN);

        final int batchSizeBytes = Math.max(RECORD_LEN, (batchBytes / RECORD_LEN) * RECORD_LEN);
        final int POOL = Math.max(2, workers) + 1;

        class Batch {
            final byte[] data;
            int len;           // usable bytes (multiple of RECORD_LEN)
            long startRecNo;   // first record number (1-based) in this batch
            long seq;          // batch sequence
            Batch(int size) { this.data = new byte[size]; }
            Batch(int size, boolean sentinel) { this.data = new byte[size]; this.len = 0; this.seq = -1; }
        }

        ArrayBlockingQueue<Batch> free = new ArrayBlockingQueue<>(POOL);
        ArrayBlockingQueue<Batch> ready = new ArrayBlockingQueue<>(POOL);
        for (int i = 0; i < POOL; i++) free.add(new Batch(batchSizeBytes));

        ExecutorService exec = Executors.newFixedThreadPool(workers);
        AtomicLong nextGlobalRecNo = new AtomicLong(0);
        AtomicLong seqGen = new AtomicLong(0);
        CountDownLatch readerDone = new CountDownLatch(1);

        Thread reader = new Thread(() -> {
            try (FileChannel ch = FileChannel.open(input, StandardOpenOption.READ)) {
                long size = ch.size();
                if ((size % RECORD_LEN) != 0) {
                    long partialIndex = (size / RECORD_LEN) + 1;
                    throw new EOFException("Partial record at #" + partialIndex);
                }

                byte[] carry = null;
                int carryLen = 0;

                while (true) {
                    Batch b = free.take();

                    int writePos = 0;
                    if (carry != null && carryLen > 0) {
                        System.arraycopy(carry, 0, b.data, 0, carryLen);
                        writePos = carryLen;
                        carry = null;
                        carryLen = 0;
                    }

                    ByteBuffer bb = ByteBuffer.wrap(b.data, writePos, b.data.length - writePos);
                    int n = ch.read(bb);
                    if (n == -1) {
                        if (writePos != 0) throw new EOFException("Partial record at EOF");
                        ready.put(new Batch(0, true)); // sentinel
                        break;
                    }

                    int filled = writePos + n;
                    int usable = (filled / RECORD_LEN) * RECORD_LEN;
                    int leftover = filled - usable;

                    if (leftover > 0) {
                        carry = new byte[leftover];
                        System.arraycopy(b.data, usable, carry, 0, leftover);
                        carryLen = leftover;
                    }

                    b.len = usable;
                    b.startRecNo = nextGlobalRecNo.getAndAdd((long) usable / RECORD_LEN) + 1;
                    b.seq = seqGen.getAndIncrement();
                    ready.put(b);
                }
            } catch (Throwable t) {
                t.printStackTrace(System.err);
                try { ready.put(new Batch(0, true)); } catch (InterruptedException ignored) {}
            } finally {
                readerDone.countDown();
            }
        }, "fixed255-reader");

        for (int i = 0; i < workers; i++) {
            exec.submit(() -> {
                try {
                    while (true) {
                        Batch b = ready.take();
                        if (b.seq == -1) { ready.put(b); return; } // sentinel

                        int off = 0;
                        long recNo = b.startRecNo;
                        while (off < b.len) {
                            int recBase = off;
                            String typeCode = sliceTrim(b.data, recBase + (recTypeStart1Based - 1), recTypeLen);
                            RecordType rt = RecordType.from(typeCode);
                            List<FieldSpec> layout = schemas.get(rt);
                            if (layout == null) {
                                byte[] raw = Arrays.copyOfRange(b.data, recBase, recBase + RECORD_LEN);
                                consumer.onUnknown(recNo, rt, typeCode, raw);
                            } else {
                                Map<String, String> values = parseFieldsFromArray(b.data, recBase, layout);
                                consumer.onRecord(recNo, rt, values);
                            }
                            off += RECORD_LEN;
                            recNo++;
                        }
                        b.len = 0;
                        free.put(b);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (Throwable t) {
                    t.printStackTrace(System.err);
                }
            });
        }

        reader.start();
        try { readerDone.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        exec.shutdown();
        try { exec.awaitTermination(7, TimeUnit.DAYS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /**
     * Ordered parallel parsing:
     * - Reader assigns batch sequence IDs.
     * - Workers parse batches in parallel and stream per-record events into per-batch queues.
     * - A single emitter drains batches in strict sequence (0,1,2,...) and calls the consumer.
     *
     * @param input          input file path
     * @param consumer       consumer (can be non-thread-safe)
     * @param workers        number of parsing threads (e.g., Runtime.getRuntime().availableProcessors() - 1)
     * @param batchBytes     batch size in bytes (aligned down to multiple of 255)
     * @param queueCapacity  per-batch queue capacity (e.g., 2048–8192)
     */
    public void parseParallelOrdered(Path input,
                                     RecordConsumer consumer,
                                     int workers,
                                     int batchBytes,
                                     int queueCapacity) throws IOException {
        if (workers <= 0) throw new IllegalArgumentException("workers must be > 0");
        if (batchBytes < RECORD_LEN) throw new IllegalArgumentException("batchBytes must be >= " + RECORD_LEN);
        if (queueCapacity <= 0) throw new IllegalArgumentException("queueCapacity must be > 0");

        final int batchSizeBytes = Math.max(RECORD_LEN, (batchBytes / RECORD_LEN) * RECORD_LEN);
        final int POOL = Math.max(2, workers) + 1;

        /* ================= Structures ================= */

        class Batch {
            final byte[] data;
            int len;           // usable bytes (multiple of RECORD_LEN)
            long startRecNo;   // 1-based record number of first record in this batch
            long seq;          // 0-based batch sequence
            Batch(int size) { this.data = new byte[size]; }
            Batch(boolean sentinel) { this.data = new byte[0]; this.seq = -1; }
        }

        abstract class RecordEvent {
            final long recNo;
            RecordEvent(long recNo) { this.recNo = recNo; }
            abstract void emit(RecordConsumer c);
        }

        class KnownEvent extends RecordEvent {
            final RecordType type;
            final List<FieldSpec> layout;
            final String[] values; // values aligned to layout order
            KnownEvent(long recNo, RecordType type, List<FieldSpec> layout, String[] values) {
                super(recNo); this.type = type; this.layout = layout; this.values = values;
            }
            @Override void emit(RecordConsumer c) {
                c.onRecord(recNo, type, buildMap(layout, values));
            }
        }

        class UnknownEvent extends RecordEvent {
            final RecordType type;
            final String typeCode;
            final byte[] raw;
            UnknownEvent(long recNo, RecordType type, String typeCode, byte[] raw) {
                super(recNo); this.type = type; this.typeCode = typeCode; this.raw = raw;
            }
            @Override void emit(RecordConsumer c) {
                c.onUnknown(recNo, type, typeCode, raw);
            }
        }

        class BatchOut {
            final BlockingQueue<RecordEvent> q = new LinkedBlockingQueue<>(queueCapacity);
            volatile boolean done = false;
        }

        ArrayBlockingQueue<Batch> free = new ArrayBlockingQueue<>(POOL);
        ArrayBlockingQueue<Batch> ready = new ArrayBlockingQueue<>(POOL);
        for (int i = 0; i < POOL; i++) free.add(new Batch(batchSizeBytes));

        ConcurrentHashMap<Long, BatchOut> outMap = new ConcurrentHashMap<>();

        ExecutorService workersPool = Executors.newFixedThreadPool(workers);
        CountDownLatch readerDone = new CountDownLatch(1);

        AtomicLong nextGlobalRecNo = new AtomicLong(0);
        AtomicLong seqGen = new AtomicLong(0);
        final long[] lastSeqHolder = new long[]{ -1L }; // set by reader on EOF

        /* ================= Reader ================= */
        Thread reader = new Thread(() -> {
            try (FileChannel ch = FileChannel.open(input, StandardOpenOption.READ)) {
                long size = ch.size();
                if ((size % RECORD_LEN) != 0) {
                    long partialIndex = (size / RECORD_LEN) + 1;
                    throw new EOFException("Partial record at #" + partialIndex);
                }

                byte[] carry = null;
                int carryLen = 0;

                while (true) {
                    Batch b = free.take();

                    int writePos = 0;
                    if (carry != null && carryLen > 0) {
                        System.arraycopy(carry, 0, b.data, 0, carryLen);
                        writePos = carryLen;
                        carry = null;
                        carryLen = 0;
                    }

                    ByteBuffer bb = ByteBuffer.wrap(b.data, writePos, b.data.length - writePos);
                    int n = ch.read(bb);
                    if (n == -1) {
                        if (writePos != 0) throw new EOFException("Partial record at EOF");
                        lastSeqHolder[0] = seqGen.get() - 1;
                        ready.put(new Batch(true)); // sentinel to workers
                        break;
                    }

                    int filled = writePos + n;
                    int usable = (filled / RECORD_LEN) * RECORD_LEN;
                    int leftover = filled - usable;

                    if (leftover > 0) {
                        carry = new byte[leftover];
                        System.arraycopy(b.data, usable, carry, 0, leftover);
                        carryLen = leftover;
                    }

                    b.len = usable;
                    b.startRecNo = nextGlobalRecNo.getAndAdd((long) usable / RECORD_LEN) + 1;
                    b.seq = seqGen.getAndIncrement();

                    // Allocate output channel for this batch before handing to workers
                    outMap.put(b.seq, new BatchOut());

                    ready.put(b);
                }
            } catch (Throwable t) {
                t.printStackTrace(System.err);
                try { ready.put(new Batch(true)); } catch (InterruptedException ignored) {}
                lastSeqHolder[0] = seqGen.get() - 1;
            } finally {
                readerDone.countDown();
            }
        }, "fixed255-reader");

        /* ================= Workers ================= */
        for (int i = 0; i < workers; i++) {
            workersPool.submit(() -> {
                try {
                    while (true) {
                        Batch b = ready.take();
                        if (b.seq == -1) { // sentinel
                            ready.put(b);
                            return;
                        }

                        BatchOut bo = outMap.get(b.seq);
                        if (bo == null) {
                            bo = new BatchOut();
                            outMap.put(b.seq, bo);
                        }

                        int off = 0;
                        long recNo = b.startRecNo;

                        while (off < b.len) {
                            int recBase = off;

                            String typeCode = sliceTrim(b.data, recBase + (recTypeStart1Based - 1), recTypeLen);
                            RecordType rt = RecordType.from(typeCode);
                            List<FieldSpec> layout = schemas.get(rt);
                            if (layout == null) {
                                byte[] raw = Arrays.copyOfRange(b.data, recBase, recBase + RECORD_LEN);
                                bo.q.put(new UnknownEvent(recNo, rt, typeCode, raw));
                            } else {
                                String[] vals = parseFieldsToArray(b.data, recBase, layout);
                                bo.q.put(new KnownEvent(recNo, rt, layout, vals));
                            }

                            off += RECORD_LEN;
                            recNo++;
                        }

                        bo.done = true;
                        b.len = 0;
                        free.put(b);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (Throwable t) {
                    t.printStackTrace(System.err);
                }
            });
        }

        /* ================= Emitter (ordered) ================= */
        Thread emitter = new Thread(() -> {
            long nextSeq = 0;
            try {
                while (true) {
                    BatchOut bo;
                    // Wait for the next batch's output to exist
                    while ((bo = outMap.get(nextSeq)) == null) {
                        if (readerDone.getCount() == 0 && nextSeq > lastSeqHolder[0]) {
                            return; // finished all batches
                        }
                        try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    }

                    // Drain this batch in order
                    while (true) {
                        RecordEvent evt = bo.q.poll(2, TimeUnit.MILLISECONDS);
                        if (evt != null) {
                            evt.emit(consumer);
                        } else if (bo.done && bo.q.isEmpty()) {
                            break;
                        }
                    }

                    outMap.remove(nextSeq);
                    nextSeq++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable t) {
                t.printStackTrace(System.err);
            }
        }, "fixed255-emitter");

        /* ================= Run ================= */
        reader.start();
        emitter.start();

        try { readerDone.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        workersPool.shutdown();
        try { workersPool.awaitTermination(7, TimeUnit.DAYS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        try { emitter.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /* =======================================================================
       INTERNALS — tight loop helpers
       ======================================================================= */

    // Parse fields directly from the record's backing array (fewer allocations).
    private Map<String, String> parseFieldsFromArray(byte[] rec, int base, List<FieldSpec> layout) {
        Map<String, String> out = new LinkedHashMap<>(layout.size() * 2);
        String s = null;
        int len = 0;
        Integer x = 0;
        for (FieldSpec f : layout) {
            final int start = base + (f.start1Based - 1);
            switch (f.type) {
                case ALPHA -> out.put(f.name, sliceTrim(rec, start, f.lengthBytes));
                case NUMERIC_TEXT -> {
                    s = sliceTrim(rec, start, f.lengthBytes);
                    len = s.length();
                    if (len == 1) {
                        x = parseOverpunchIntSafe(s);
                        out.put(f.name, x == null ? "" : String.valueOf(x));
                    } else if (s.contains("}")) {
                    	x = parseOverpunchIntSafe(s);
                        out.put(f.name, x == null ? "" : String.valueOf(x));
                    } else if (s.contains("{")) {
                    	x = parseOverpunchIntSafe(s);
                        out.put(f.name, x == null ? "" : String.valueOf(x));
                    } else if (len == 4 && (s.isEmpty() || s.charAt(0) != 'X')) {
                        x = parseOverpunchIntSafe(s);
                        out.put(f.name, x == null ? "" : String.valueOf(x));
                    } else {
                        out.put(f.name, s);
                    }
                }
                case PACKED_DECIMAL -> {
                	s = decodeComp3ToString(rec, start, f.lengthBytes, f.scale);
                	if(s.contains("}")) {
                		x = parseOverpunchIntSafe(s);
                		out.put(f.name, x == null ? "" : String.valueOf(x));
                	} else {
                		out.put(f.name, s);
                	}
                	
                }
                case BINARY -> out.put(f.name, decodeBinary(rec, start, f.lengthBytes, f.scale));
                default -> out.put(f.name, "");
            }
        }
        return out;
    }

    // Same as above, but returns values array for faster worker→emitter path.
    private String[] parseFieldsToArray(byte[] rec, int base, List<FieldSpec> layout) {
        String[] vals = new String[layout.size()];
        int idx = 0;
        for (FieldSpec f : layout) {
            final int start = base + (f.start1Based - 1);
            String v;
            int len = 0;
            String s;
            switch (f.type) {
                case ALPHA -> v = sliceTrim(rec, start, f.lengthBytes);
                case NUMERIC_TEXT -> {
                    s = sliceTrim(rec, start, f.lengthBytes);
                    len = s.length();
                    if (len == 1) {
                        Integer x = parseOverpunchIntSafe(s);
                        v = (x == null) ? "" : String.valueOf(x);
                    } else if (s.contains("}")) {
                    	Integer x = parseOverpunchIntSafe(s);
                        v = (x == null) ? "" : String.valueOf(x);
                    } else if (s.contains("{")) {
                        Integer x = parseOverpunchIntSafe(s);
                        v = (x == null) ? "" : String.valueOf(x);
                    } else if (len == 4 && (s.isEmpty() || s.charAt(0) != 'X')) {
                        Integer x = parseOverpunchIntSafe(s);
                        v = (x == null) ? "" : String.valueOf(x);
                    } else {
                        v = s;
                    }
                }
                case PACKED_DECIMAL -> {
                	v = decodeComp3ToString(rec, start, f.lengthBytes, f.scale);
                	if(v.contains("}")) {
                		Integer x = parseOverpunchIntSafe(v);
                        v = (x == null) ? "" : String.valueOf(x);
                	}
                }
                case BINARY -> v = decodeBinary(rec, start, f.lengthBytes, f.scale);
                default -> v = "";
            }
            vals[idx++] = v;
        }
        return vals;
    }

    private static Map<String, String> buildMap(List<FieldSpec> layout, String[] values) {
        Map<String, String> out = new LinkedHashMap<>(layout.size() * 2);
        for (int i = 0; i < layout.size(); i++) {
            out.put(layout.get(i).name, values[i]);
        }
        return out;
    }

    /** Allocation-free right-trim on bytes for ASCII/EBCDIC space; then single String creation. */
    private String sliceTrim(byte[] a, int off, int len) {
        if (off < 0 || off + len > a.length) return "";
        int endExclusive = off + len;
        int i = endExclusive - 1;
        while (i >= off && a[i] == spaceByte) i--;
        int newLen = (i < off) ? 0 : (i - off + 1);
        return (newLen == 0) ? "" : new String(a, off, newLen, charset);
    }

    /** Decode COMP-3 (packed decimal) with a fast path that avoids BigInteger when it fits in a long. */
    private static String decodeComp3ToString(byte[] a, int off, int lenBytes, int scale) {
        boolean fitsLong = (lenBytes <= 10); // up to ~19 digits (last nibble is sign)
        if (fitsLong) {
            long acc = 0L;
            boolean negative = false;

            for (int i = 0; i < lenBytes; i++) {
                int b = a[off + i] & 0xFF;
                int hi = (b >>> 4) & 0x0F;
                int lo = b & 0x0F;
                if (i < lenBytes - 1) {
                    acc = acc * 10 + hi;
                    acc = acc * 10 + lo;
                } else {
                    acc = acc * 10 + hi;
                    negative = (lo == 0x0D);
                }
            }
            if (scale > 0) {
                BigDecimal bd = new BigDecimal(acc).movePointLeft(scale);
                return negative ? bd.negate().toPlainString() : bd.toPlainString();
            }
            return negative ? Long.toString(-acc) : Long.toString(acc);
        }

        StringBuilder digits = new StringBuilder(lenBytes * 2);
        boolean negative = false;
        for (int i = 0; i < lenBytes; i++) {
            int b = a[off + i] & 0xFF;
            int hi = (b >>> 4) & 0x0F;
            int lo = b & 0x0F;
            if (i < lenBytes - 1) {
                digits.append((char) ('0' + hi)).append((char) ('0' + lo));
            } else {
                digits.append((char) ('0' + hi));
                negative = (lo == 0x0D);
            }
        }
        BigInteger bi = digits.length() == 0 ? BigInteger.ZERO : new BigInteger(digits.toString());
        BigDecimal bd = new BigDecimal(bi).movePointLeft(Math.max(scale, 0));
        return negative ? bd.negate().toPlainString() : bd.toPlainString();
    }

    /** Decode big-endian signed binary (COMP/COMP-4). */
    private static String decodeBinary(byte[] rec, int start, int len, int scale) {
        long val = 0;
        for (int i = 0; i < len; i++) {
            val = (val << 8) | (rec[start + i] & 0xFFL);
        }
        long signBit = 1L << (len * 8 - 1);
        long signed = ((val & signBit) != 0) ? (val - (1L << (len * 8))) : val;
        if (scale > 0) return new BigDecimal(signed).movePointLeft(scale).toPlainString();
        return Long.toString(signed);
    }
    
    /** Safer overpunch decoder that returns null for invalid inputs. */
    private static Integer parseOverpunchIntSafe(String s) {
        if (s == null || s.isEmpty()) return null;
        char last = s.charAt(s.length() - 1);
        String body = s.substring(0, s.length() - 1);

        if (last >= '0' && last <= '9') {
            try { return Integer.parseInt(body + last); } catch (NumberFormatException e) { return null; }
        }

        Integer d = POS.get(last);
        boolean neg = false;
        if (d == null) {
            d = NEG.get(last);
            if (d != null) neg = true;
        }
        if (d == null) return null;

        try {
            int value = Integer.parseInt(body + d);
            return neg ? -value : value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Overpunch maps (adjust 'X' handling if it isn't valid in your data)
    static final Map<Character, Integer> POS = Map.ofEntries(
        Map.entry('{', 0),
        Map.entry('A', 1),
        Map.entry('B', 2),
        Map.entry('C', 3),
        Map.entry('D', 4),
        Map.entry('E', 5),
        Map.entry('F', 6),
        Map.entry('G', 7),
        Map.entry('H', 8),
        Map.entry('I', 9),
        Map.entry('X', 0) // remove if 'X' isn't valid for your data
    );

    static final Map<Character, Integer> NEG = Map.ofEntries(
        Map.entry('}', 0),
        Map.entry('J', 1),
        Map.entry('K', 2),
        Map.entry('L', 3),
        Map.entry('M', 4),
        Map.entry('N', 5),
        Map.entry('O', 6),
        Map.entry('P', 7),
        Map.entry('Q', 8),
        Map.entry('R', 9),
        Map.entry('X', 0) // remove if 'X' isn't valid for your data
    );
}