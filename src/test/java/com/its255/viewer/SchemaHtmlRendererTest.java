package com.its255.viewer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import com.its255.schema.SchemaRegistry;

class SchemaHtmlRendererTest {
    private static final Charset CP037 = Charset.forName("Cp037");

    @Test
    void renderVerticalMakesFm1F0SeqNumEditableInEditMode() {
        byte[] record = filledRecord(SchemaRegistry.getRecordLength("SF"), (byte) 0x40);
        putCp037(record, 21, "F0");

        SchemaHtmlRenderer renderer = new SchemaHtmlRenderer(new TestRecordStore(record), CP037, "SF", true,
                Map.of());

        String html = renderer.renderVertical(1);

        assertTrue(html.contains("FM1F0-SEQ-NUM</th><td><input type=\"text\""));
        assertTrue(html.contains("name=\"field_1_FM1F0-SEQ-NUM\""));
        assertTrue(html.contains("data-binary-editable=\"true\""));
    }

    @Test
    void renderHorizontalMakesFm1F0SeqNumEditableForSelectedRecord() {
        byte[] record = filledRecord(SchemaRegistry.getRecordLength("SF"), (byte) 0x40);
        putCp037(record, 21, "F0");

        ViewerSession viewerSession = new ViewerSession();
        viewerSession.lastFiltered = List.of(1);
        viewerSession.selectedEditRecord = 1;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("VIEWER_SESSION", viewerSession);

        SchemaHtmlRenderer renderer = new SchemaHtmlRenderer(new TestRecordStore(record), CP037, "SF", true,
                Map.of());

        String html = renderer.renderHorizontalPage(session, "F0", 0, 1, 1);

        assertTrue(html.contains("name=\"field_1_FM1F0-SEQ-NUM\""));
        assertTrue(html.contains("data-binary-editable=\"true\""));
    }

    public static class TestRecordStore {
        private final byte[] record;

        TestRecordStore(byte[] record) {
            this.record = record;
        }

        public byte[] readRecordBytes(int recordNo) {
            return record;
        }

        public String readType(int recordNo) {
            return "F0";
        }
    }

    private static byte[] filledRecord(int recordLength, byte value) {
        byte[] record = new byte[recordLength];
        java.util.Arrays.fill(record, value);
        return record;
    }

    private static void putCp037(byte[] record, int zeroBasedOffset, String value) {
        byte[] encoded = value.getBytes(CP037);
        System.arraycopy(encoded, 0, record, zeroBasedOffset, encoded.length);
    }
}
