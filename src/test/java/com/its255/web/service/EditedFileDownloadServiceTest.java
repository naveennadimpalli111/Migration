package com.its255.web.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.its255.schema.SchemaRegistry;

class EditedFileDownloadServiceTest {

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

    private static byte[] filledRecord(int recordLength, byte value) {
        byte[] record = new byte[recordLength];
        Arrays.fill(record, value);
        return record;
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
