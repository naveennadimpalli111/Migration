package com.its255.viewer;

import java.nio.file.Path;
import java.util.List;

/**
 * One-per-HTTP-session state bucket for Fast Viewer.
 * This replaces controller fields (which were singletons).
 */
public class ViewerSession implements AutoCloseable {

    public Path filePath;                 // path to uploaded file
    public String originalFilename;       // file name for UI
    public Object store;                  // ChunkedMMapRecordStore
    public PrefixIndex pidx;              // SCCF prefix index
    public FastRecordFilter filter;       // filter using pidx
    public RecordNavigator nav;           // navigation state
    public List<Integer> lastFiltered;    // last result set
    public double progress;               // prefix index progress (0..1)

    public boolean hasFile() {
        return filePath != null && store != null;
    }
    
    @Override public void close() {
        if (store instanceof AutoCloseable ac) try { ac.close(); } catch (Exception ignore) {}
    }
}