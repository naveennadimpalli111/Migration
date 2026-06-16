
package com.its255.viewer;

import java.util.List;

public class RecordNavigator {
    private final List<Integer> records; // 1-based record numbers
    private int idx;

    public RecordNavigator(List<Integer> records, int startRecordNumber) {
        this.records = records;
        if (startRecordNumber > 0) {
            this.idx = Math.max(0, records.indexOf(startRecordNumber));
        } else this.idx = 0;
    }
    public int size() { return records.size(); }
    public boolean hasPrev() { return idx > 0; }
    public boolean hasNext() { return idx < records.size()-1; }
    public void prev() { if (hasPrev()) idx--; }
    public void next() { if (hasNext()) idx++; }
    public int position() { return (records.isEmpty()?0:idx+1); }
    public int currentRecordNumber() { return records.isEmpty()? -1 : records.get(idx); }
    public void setCurrent(int recordNumber) {
        if (records == null || records.isEmpty()) {
            return;
        }
        int index = records.indexOf(recordNumber);
        if (index >= 0) {
            idx = index;
        }
    }
}
