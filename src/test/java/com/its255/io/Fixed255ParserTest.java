package com.its255.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.its255.schema.FieldSpec;
import com.its255.schema.FieldType;
import com.its255.schema.RecordType;

class Fixed255ParserTest {
    private static final Charset CP037 = Charset.forName("Cp037");

    @TempDir
    Path tempDir;

    @Test
    void parsePreservesWidthWhenNormalizingFm1F0ServiceTimeBraces() throws Exception {
        int recordLength = 255;
        byte[] record = new byte[recordLength];
        Arrays.fill(record, (byte) 0x40);
        putCp037(record, 21, "F0");
        putCp037(record, 67, "0{");
        putCp037(record, 69, "{ ");

        Path input = tempDir.resolve("input.dat");
        Files.write(input, record);

        Fixed255Parser parser = new Fixed255Parser(
                Map.of(RecordType.RT_F0, List.of(
                        new FieldSpec("FM1F0-SERV-HH", 68, 2, FieldType.NUMERIC_TEXT),
                        new FieldSpec("FM1F0-SERV-MM", 70, 2, FieldType.NUMERIC_TEXT))),
                CP037,
                recordLength,
                22,
                2);
        AtomicReference<Map<String, String>> parsed = new AtomicReference<>();

        parser.parse(input, (recNo, rt, values) -> parsed.set(values));

        assertEquals("00", parsed.get().get("FM1F0-SERV-HH"));
        assertEquals("0", parsed.get().get("FM1F0-SERV-MM"));
    }

    private static void putCp037(byte[] record, int zeroBasedOffset, String value) {
        byte[] encoded = value.getBytes(CP037);
        System.arraycopy(encoded, 0, record, zeroBasedOffset, encoded.length);
    }
}
