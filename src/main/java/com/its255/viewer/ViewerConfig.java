
package com.its255.viewer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "viewer")
public class ViewerConfig {
    @Value("${viewer.recordLength:255}")
    private int recordLength;

    @Value("${viewer.charset:Cp037}")
    private String charset;

    @Value("${viewer.prefixIndexLength:10}")
    private int prefixIndexLength;

    @Value("${viewer.index.workers:4}")
    private int indexWorkers;

    @Value("${viewer.index.progressStep:8192}")
    private int progressStep;

    @Value("${viewer.export.bufferSize:1048576}")
    private int exportBufferSize;

    @Value("${viewer.chunk.enabled:true}")
    private boolean chunkEnabled;

    @Value("${viewer.chunk.windowSizeMB:512}")
    private int windowSizeMB;

    public int getRecordLength() { return recordLength; }
    public String getCharset() { return charset; }
    public int getPrefixIndexLength() { return prefixIndexLength; }
    public int getIndexWorkers() { return indexWorkers; }
    public int getProgressStep() { return progressStep; }
    public int getExportBufferSize() { return exportBufferSize; }
    public boolean isChunkEnabled() { return chunkEnabled; }
    public int getWindowSizeMB() { return windowSizeMB; }
}
