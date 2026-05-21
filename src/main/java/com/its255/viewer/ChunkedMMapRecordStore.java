
package com.its255.viewer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.its255.constants.FileViewerConstants;

/**
 * Windowed (chunked) MMAP store for files > 2GB.
 * Maps the file in windows of size windowSizeBytes and remaps on boundary crossings.
 */
public class ChunkedMMapRecordStore implements AutoCloseable {
    private final int RECORD_LEN;
    private static final int DEFAULT_TYPE_START_1_BASED = 22; // COBOL 1-based
    private static final int PLAN_PROFILE_TYPE_START_1_BASED = 3; // COBOL 1-based
    private static final int TYPE_LEN = 2;

    private final Path path;
    private final Charset cs;
    private final FileChannel ch;
    private final long size;
    private final long recordCount;

    private final long windowSizeBytes;

    private long currentWindowIndex = -1L;
    private long currentWindowStart = 0L;
    private long currentWindowSize = 0L;
    private MappedByteBuffer current;
    private String transactionType;

    public ChunkedMMapRecordStore(Path path, Charset cs, int recordLen, long windowSizeBytes, String transactionType) throws IOException {
        if (windowSizeBytes <= 0) throw new IllegalArgumentException("windowSizeBytes must be > 0");
        this.path = path;
        this.cs = cs;
        this.RECORD_LEN = recordLen;
        this.windowSizeBytes = windowSizeBytes;
        this.ch = FileChannel.open(path, StandardOpenOption.READ);
        this.size = ch.size();
        this.transactionType = transactionType;
	
        if (size % RECORD_LEN != 0) {
        	throw new IOException("File size not multiple of record length: " + RECORD_LEN);
        }
        this.recordCount = size / RECORD_LEN;
        // lazy map on first read
    }

    public long getRecordCount() { return recordCount; }
    public long offsetOf(int recordNumber1Based) { return (long)(recordNumber1Based - 1) * RECORD_LEN; }

    private void ensureWindow(long absoluteOffset) throws IOException {
        long windowIndex = absoluteOffset / windowSizeBytes;
        if (windowIndex != currentWindowIndex) {
            long start = windowIndex * windowSizeBytes;
            long remaining = size - start;
            long mapSize = Math.min(windowSizeBytes, remaining);
            if (mapSize > Integer.MAX_VALUE) {
                mapSize = Integer.MAX_VALUE; // guard for API limit
            }
            this.current = ch.map(FileChannel.MapMode.READ_ONLY, start, mapSize);
            this.currentWindowIndex = windowIndex;
            this.currentWindowStart = start;
            this.currentWindowSize = mapSize;
            
        }
    }

    private void getBytes(long absoluteOffset, byte[] out, int len) throws IOException {
        ensureWindow(absoluteOffset);
        long within = absoluteOffset - currentWindowStart;
        if (within + len <= currentWindowSize) {
            synchronized (current) {
                current.position((int)within);
                current.get(out, 0, len);
            }
        } else {
            int firstLen = (int)(currentWindowSize - within);
            if (firstLen > 0) {
                synchronized (current) {
                    current.position((int)within);
                    current.get(out, 0, firstLen);
                }
            }
            int left = len - firstLen;
            long nextOffset = absoluteOffset + firstLen;
            ensureWindow(nextOffset);
            synchronized (current) {
                current.position((int)(nextOffset - currentWindowStart));
                current.get(out, firstLen, left);
            }
        }
    }

    public String readSccf(int recordNumber1Based) throws IOException {
        long off = offsetOf(recordNumber1Based);
        byte[] b = new byte[15];
        getBytes(off, b, 15);
        return new String(b, cs); // STRICT: no trimming
    }

    public String readType(int recordNumber1Based) throws IOException {
    	if (transactionType.equals(FileViewerConstants.CBFBD)) {
    		return FileViewerConstants.CBFBD;
    	} else {
    	    int start1Based = recordTypeStart1Based();
    		long off = offsetOf(recordNumber1Based) + (start1Based - 1);
            byte[] b = new byte[TYPE_LEN];
            getBytes(off, b, TYPE_LEN);
            return new String(b, cs);
    	}
    }

    public byte[] readRecordBytes(int recordNumber1Based) throws IOException {
        long off = offsetOf(recordNumber1Based);
        byte[] b = new byte[RECORD_LEN];
        getBytes(off, b, RECORD_LEN);
        return b;
    }

    
    @Override
    public void close() throws IOException {
    	 current = null;
         currentWindowIndex = -1;
         currentWindowStart = 0;
         currentWindowSize = 0;

         // Close channel (this is critical)
         if (ch.isOpen()) {
             ch.close();
         }
    }
     
    
    private int recordTypeStart1Based() {
        switch (transactionType) {
            case FileViewerConstants.PLAN_PROFILE_UPDATE, FileViewerConstants.PLAN_PROFILE_ACKNOWLEDGMENT: // "PP"
                return PLAN_PROFILE_TYPE_START_1_BASED;
            case FileViewerConstants.CBFBD:
                return -1; // handled specially
            default:
                return DEFAULT_TYPE_START_1_BASED; // SFI, SFP, DF, CBF, RF
        }
    }

}
