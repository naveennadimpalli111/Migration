package com.its255.web.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.its255.schema.SchemaRegistry;

class EditedFileDownloadServiceTest {
    private static final Charset CP037 = Charset.forName("Cp037");

    @TempDir
    Path tempDir;

    @Test
    void streamEditedFileOmitsDeletedRecordsFromGeneratedFile() throws Exception {
        String transactionType = "SF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record1 = filledRecord(recordLength, (byte) 'A');
        byte[] record2 = filledRecord(recordLength, (byte) 'B');
        byte[] record3 = filledRecord(recordLength, (byte) 'C');

        Path original = tempDir.resolve("original.dat");
        Files.write(original, concat(record1, record2, record3));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(), Set.of(2), transactionType, out);

        byte[] generated = out.toByteArray();
        assertEquals(recordLength * 2, generated.length);
        assertArrayEquals(record1, Arrays.copyOfRange(generated, 0, recordLength));
        assertArrayEquals(record3, Arrays.copyOfRange(generated, recordLength, recordLength * 2));
        assertEquals(recordLength * 2, service.getEditedFileSize(original, Set.of(2), transactionType));
    }

    @Test
    void streamEditedFilePersistsFm1A5SeqNumAsBinaryValue() throws Exception {
        String transactionType = "SF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record = filledRecord(recordLength, (byte) 0x40);
        record[21] = (byte) 0xC1; // A in Cp037
        record[22] = (byte) 0xF5; // 5 in Cp037

        Path original = tempDir.resolve("fm1a5.dat");
        Files.write(original, record);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(1, Map.of("FM1A5-SEQ-NUM", "12345")), Set.of(), transactionType, out);

        byte[] generated = out.toByteArray();
        assertEquals(0x30, generated[23] & 0xFF);
        assertEquals(0x39, generated[24] & 0xFF);
    }

    @Test
    void streamEditedFilePersistsFm1F0SeqNumAsBinaryValue() throws Exception {
        String transactionType = "SF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record = filledRecord(recordLength, (byte) 0x40);
        putCp037(record, 21, "F0");

        Path original = tempDir.resolve("fm1f0.dat");
        Files.write(original, record);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(1, Map.of("FM1F0-SEQ-NUM", "12345")), Set.of(), transactionType,
                out);

        byte[] generated = out.toByteArray();
        assertEquals(0x30, generated[23] & 0xFF);
        assertEquals(0x39, generated[24] & 0xFF);
    }

    @Test
    void streamEditedFilePersistsFm37ASeqNumAsPlainNumericText() throws Exception {
        String transactionType = "CBF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record = filledRecord(recordLength, (byte) 0x40);
        record[21] = (byte) 0xF7; // 7 in Cp037
        record[22] = (byte) 0xC1; // A in Cp037

        Path original = tempDir.resolve("fm37a.dat");
        Files.write(original, record);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(1, Map.of("FM37A-SEQ-NUM", "33")), Set.of(), transactionType, out);

        byte[] generated = out.toByteArray();
        assertEquals(0xF3, generated[23] & 0xFF);
        assertEquals(0xF3, generated[24] & 0xFF);
    }

    @Test
    void streamEditedFileDoesNotPadFm37ASeqNumWithGeneratedDigits() throws Exception {
        String transactionType = "CBF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record = filledRecord(recordLength, (byte) 0x40);
        record[21] = (byte) 0xF7; // 7 in Cp037
        record[22] = (byte) 0xC1; // A in Cp037

        Path original = tempDir.resolve("fm37a-single-digit.dat");
        Files.write(original, record);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(1, Map.of("FM37A-SEQ-NUM", "3")), Set.of(), transactionType, out);

        byte[] generated = out.toByteArray();
        assertEquals(0xF3, generated[23] & 0xFF);
        assertEquals(0x40, generated[24] & 0xFF);
    }

    @Test
    void streamEditedFileNormalizesFm1F0ServiceHourAndMinuteBraces() throws Exception {
        String transactionType = "SF";
        int recordLength = SchemaRegistry.getRecordLength(transactionType);

        byte[] record = filledRecord(recordLength, (byte) 0x40);
        putCp037(record, 21, "F0");
        putCp037(record, 67, "0{");
        putCp037(record, 69, "{ ");

        Path original = tempDir.resolve("fm1f0-service-time.dat");
        Files.write(original, record);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EditedFileDownloadService service = new EditedFileDownloadService();

        service.streamEditedFile(original, Map.of(), Set.of(), transactionType, out);

        byte[] generated = out.toByteArray();
        assertEquals("00", new String(generated, 67, 2, CP037));
        assertEquals("0 ", new String(generated, 69, 2, CP037));
    }

    private static byte[] filledRecord(int recordLength, byte value) {
        byte[] record = new byte[recordLength];
        Arrays.fill(record, value);
        return record;
    }

    private static void putCp037(byte[] record, int zeroBasedOffset, String value) {
        byte[] encoded = value.getBytes(CP037);
        System.arraycopy(encoded, 0, record, zeroBasedOffset, encoded.length);
    }

    private static byte[] concat(byte[]... chunks) {
        int length = 0;
        for (byte[] chunk : chunks) {
            length += chunk.length;
        }

        byte[] joined = new byte[length];
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, joined, offset, chunk.length);
            offset += chunk.length;
        }
        return joined;
    }
}
