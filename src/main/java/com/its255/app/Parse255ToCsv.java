package com.its255.app;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.its255.io.Fixed255Parser;
import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.SchemaRegistry;

/** CLI that parses a 255-byte file and writes one CSV per record type. */
public final class Parse255ToCsv {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java com.its255.app.Parse255ToCsv <input.dat> <outDir> [charset]");
            System.err.println("Default charset is Cp037 (EBCDIC)");
            System.exit(1);
        }
        Path input = Path.of(args[0]);
        Path outDir = Path.of(args[1]);
        Charset cs = (args.length >= 3) ? Charset.forName(args[2]) : Charset.forName("Cp037");

        Files.createDirectories(outDir);
        Map<RecordType, List<FieldSpec>> schemas = SchemaRegistry.all();

        Map<RecordType, CsvSink> sinks = new HashMap<>();
        int recordLength = SchemaRegistry.getRecordLength("default");
        Fixed255Parser parser = new Fixed255Parser(schemas, cs, recordLength, /*recTypeStart*/22, /*len*/2);

        parser.parse(input, (recNo, rt, values) -> {
        	values.put("REC_NO", String.valueOf(recNo));
            values.put("REC_TYPE", String.valueOf(rt).replace("RT_", ""));
            try {
                CsvSink sink = sinks.computeIfAbsent(rt, key -> {
                    try {
                        Path p = outDir.resolve("type_" + key.code + ".csv");
                        return new CsvSink(p, new ArrayList<>(values.keySet()));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                sink.write(values);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });

        for (CsvSink s : sinks.values()) s.close();
    }

    /** Minimal CSV writer */
    static final class CsvSink implements Closeable {
        private final BufferedWriter w;
        private final List<String> headers;
        private boolean headerWritten = false;

        CsvSink(Path path, List<String> headers) throws IOException {
            this.w = Files.newBufferedWriter(path);
            this.headers = headers;
        }
        void write(Map<String, String> row) throws IOException {
            if (!headerWritten) {
            	headers.add(0, "REC_NO");
            	headers.add(1, "REC_TYPE");
                w.write(String.join(",", headers));
                w.newLine();
                headerWritten = true;
            }
            List<String> vals = new ArrayList<>(headers.size());
            for (String h : headers) {
                vals.add(escape(row.get(h)));
            }
            w.write(String.join(",", vals));
            w.newLine();
        }
        private String escape(String v) {
            if (v == null) return "";
            boolean needsQ = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
            String s = v.replace("\"", "\"\"");
            return needsQ ? "\"" + s + "\"" : s;
        }
        @Override public void close() throws IOException { w.close(); }
    }
}
