
package com.its255.viewer;

import java.util.*;
import java.util.concurrent.*;

public class ParallelPrefixIndexBuilder {
    private final Object store; // any store with getRecordCount(), readSccf(int)
    private final int prefixLen;
    private final int workers;
    private final int progressStep;
    private final java.util.function.DoubleConsumer progressCallback;

    public ParallelPrefixIndexBuilder(Object store, int prefixLen, int workers, int progressStep, java.util.function.DoubleConsumer cb) {
        this.store = store; this.prefixLen = Math.max(1, prefixLen); this.workers = Math.max(1, workers); this.progressStep = Math.max(1, progressStep); this.progressCallback = cb;
    }

    private long getRecordCount() {
        try { return (long) store.getClass().getMethod("getRecordCount").invoke(store); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    private String readSccf(int rn) {
        try { return (String) store.getClass().getMethod("readSccf", int.class).invoke(store, rn); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public PrefixIndex build() throws InterruptedException {
        long n = getRecordCount();
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, List<Integer>>[] buckets = new ConcurrentMap[prefixLen];
        for (int i=0;i<prefixLen;i++) buckets[i] = new ConcurrentHashMap<>();
        int chunk = (int)Math.max(1, n / workers);
        List<Future<?>> futures = new ArrayList<>();
        for (int w=0; w<workers; w++) {
            int start = w*chunk + 1;
            int end = (w==workers-1) ? (int)n : (w+1)*chunk;
            final int S=start, E=end;
            futures.add(pool.submit(() -> {
                for (int r=S; r<=E; r++) {
                    String s = readSccf(r);
                    int max = Math.min(prefixLen, s.length());
                    for (int len=1; len<=max; len++) {
                        String p = s.substring(0,len);
                        buckets[len-1].computeIfAbsent(p, k -> new java.util.concurrent.CopyOnWriteArrayList<>()).add(r);
                    }
                    if (r % progressStep == 0) progressCallback.accept((double)r / (double)n);
                }
            }));
        }
        for (Future<?> f : futures) try { f.get(); } catch (ExecutionException e) { throw new RuntimeException(e.getCause()); }
        pool.shutdown();

        PrefixIndex idx = new PrefixIndex(prefixLen);
        for (int len=1; len<=prefixLen; len++) {
            for (Map.Entry<String,List<Integer>> e : buckets[len-1].entrySet()) {
                List<Integer> list = e.getValue();
                int[] arr = list.stream().mapToInt(Integer::intValue).toArray();
                java.util.Arrays.sort(arr);
                idx.put(len, e.getKey(), arr);
            }
        }
        progressCallback.accept(1.0);
        return idx;
    }
}
