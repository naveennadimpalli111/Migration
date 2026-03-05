package com.its255.viewer;

import java.util.*;
import java.util.concurrent.*;

/**
 * Builds a PrefixIndex over SCCF prefixes in parallel.
 * 
 * Fix 1: Replace write-heavy CopyOnWriteArrayList accumulation with
 * per-worker local buckets and a single merge step at end-of-task.
 * 
 * External behavior is unchanged:
 *  - Same API
 *  - Buckets are published as sorted int[] arrays
 *  - Deterministic output regardless of worker count
 */
public class ParallelPrefixIndexBuilder {

    private final Object store; // any store with getRecordCount(), readSccf(int)
    private final int prefixLen;
    private final int workers;
    private final int progressStep;
    private final java.util.function.DoubleConsumer progressCallback;

    public ParallelPrefixIndexBuilder(Object store,
                                      int prefixLen,
                                      int workers,
                                      int progressStep,
                                      java.util.function.DoubleConsumer cb) {
        this.store = store;
        this.prefixLen = Math.max(1, prefixLen);
        this.workers = Math.max(1, workers);
        this.progressStep = Math.max(1, progressStep);
        this.progressCallback = (cb != null) ? cb : p -> {};
    }

    private long getRecordCount() {
        try {
            return (long) store.getClass().getMethod("getRecordCount").invoke(store);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readSccf(int rn) {
        try {
            return (String) store.getClass().getMethod("readSccf", int.class).invoke(store, rn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build the prefix index in parallel.
     * 
     * Algorithm (per worker):
     *  1) Create local Map<String, ArrayList<Integer>>[] (one map per prefix length).
     *  2) For each record in its chunk, read SCCF and accumulate rN into local[len-1].get(prefix).
     *  3) After the chunk, merge local maps into shared ConcurrentHashMap buckets (amortized).
     *  4) After all workers finish, convert each bucket list to int[], sort, and publish to PrefixIndex.
     */
    public PrefixIndex build() throws InterruptedException {
        final long n = getRecordCount();
        final ExecutorService pool = Executors.newFixedThreadPool(workers);

        // Shared buckets per prefix length, populated via end-of-task merges
        @SuppressWarnings("unchecked")
        final ConcurrentMap<String, ArrayList<Integer>>[] buckets = new ConcurrentMap[prefixLen];
        for (int i = 0; i < prefixLen; i++) {
            buckets[i] = new ConcurrentHashMap<>();
        }

        // Partition records roughly evenly across workers
        final int chunk = (int) Math.max(1, n / workers);
        final List<Future<?>> futures = new ArrayList<>();

        for (int w = 0; w < workers; w++) {
            final int start = w * chunk + 1;
            final int end = (w == workers - 1) ? (int) n : (w + 1) * chunk;

            futures.add(pool.submit(() -> {
                // ---------- Per-worker local buckets: no contention in the hot path ----------
                @SuppressWarnings("unchecked")
                Map<String, ArrayList<Integer>>[] local = new Map[prefixLen];
                for (int i = 0; i < prefixLen; i++) {
                    local[i] = new HashMap<>();
                }

                // Hot loop: read SCCF for each record, add record number to local prefix buckets
                for (int r = start; r <= end; r++) {
                    String s = readSccf(r);
                    int max = Math.min(prefixLen, s.length());
                    for (int len = 1; len <= max; len++) {
                        String p = s.substring(0, len);
                        ArrayList<Integer> lst = local[len - 1].computeIfAbsent(p, k -> new ArrayList<>());
                        lst.add(r);
                    }
                    if (r % progressStep == 0) {
                        progressCallback.accept((double) r / (double) n);
                    }
                }

                // ---------- Merge worker-local buckets into shared concurrent buckets ----------
                for (int len = 1; len <= prefixLen; len++) {
                    Map<String, ArrayList<Integer>> lm = local[len - 1];
                    if (lm.isEmpty()) continue;

                    ConcurrentMap<String, ArrayList<Integer>> target = buckets[len - 1];
                    for (Map.Entry<String, ArrayList<Integer>> e : lm.entrySet()) {
                        String key = e.getKey();
                        ArrayList<Integer> val = e.getValue();
                        // Merge by appending; this is safe due to ConcurrentMap.merge semantics
                        target.merge(key, val, (oldList, newList) -> {
                            oldList.addAll(newList);
                            return oldList;
                        });
                    }
                }
            }));
        }

        // Wait for all tasks
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                pool.shutdownNow();
                throw new RuntimeException(e.getCause());
            }
        }
        pool.shutdown();

        // ---------- Finalization: convert to sorted arrays and publish ----------
        PrefixIndex idx = new PrefixIndex(prefixLen);

        for (int len = 1; len <= prefixLen; len++) {
            for (Map.Entry<String, ArrayList<Integer>> e : buckets[len - 1].entrySet()) {
                ArrayList<Integer> list = e.getValue();
                // Convert to primitive array
                int[] arr = new int[list.size()];
                for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
                // Sort to guarantee deterministic ascending order
                Arrays.sort(arr);
                idx.put(len, e.getKey(), arr);
            }
        }

        // Ensure UI hits 100%
        progressCallback.accept(1.0);
        return idx;
    }
}