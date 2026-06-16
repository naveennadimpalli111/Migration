package com.its255.viewer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.its255.util.LoggingUtil;

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
    public String transactionType;         // selected transaction type for Horizontal rendering
    public String selectedRecordType;      // selected record type for horizontal rendering
    public Path sessionDir; // per-session temp directory
    public String s3Key; // S3 object key when S3 storage is enabled
    public boolean s3Enabled; // whether this session is backed by S3
    public  boolean editMode=false;  // whether we're in edit mode (vs. view-only)
    public Integer selectedEditRecord; // currently edited horizontal record
    public boolean hasCommittedChanges = false; // saved changes are baked into filePath and can be downloaded

    public boolean hasFile() {
        return filePath != null && store != null;
    }
    public Map<Integer,Map<String,String>> editOverlay = new HashMap<>();
    public Set<Integer> deletedRecords = new HashSet<>();
    @Override
    public void close() {
        if (store instanceof AutoCloseable ac) {
            try {
                ac.close();
            } catch (Exception ignore) {
            	LoggingUtil.error(ignore);
            } finally {
                store = null;
            }
        }
    }
}
