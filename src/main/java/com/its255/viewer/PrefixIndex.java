
package com.its255.viewer;

import java.util.*;

public class PrefixIndex {
    private final int maxLen;
    private final Map<Integer, Map<String, int[]>> maps = new HashMap<>();

    public PrefixIndex(int maxLen) { this.maxLen = maxLen; }
    public int getMaxLen() { return maxLen; }

    public void put(int len, String prefix, int[] recordNumbers) {
        maps.computeIfAbsent(len, k -> new HashMap<>()).put(prefix, recordNumbers);
    }

    public Optional<int[]> get(int len, String prefix) {
        Map<String, int[]> m = maps.get(len);
        if (m == null) return Optional.empty();
        return Optional.ofNullable(m.get(prefix));
    }
}
