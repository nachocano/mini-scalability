package edu.uw.os.syscall.fd.impl;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.uw.os.syscall.fd.AnyFD;

public class AnyFDLockFreeImpl implements AnyFD {

  private final Queue<Integer>[] freeFdsArray;
  private final Map<String, Integer>[] fdsPerFileArray;
  private final int cores;

  @SuppressWarnings("unchecked")
  public AnyFDLockFreeImpl(final int size, final int cores) {
    this.cores = cores;
    freeFdsArray = new ConcurrentLinkedQueue[cores];
    fdsPerFileArray = new ConcurrentHashMap[cores];
    final int fdsPerCore = size / cores;
    System.out.println(String.format("fds per core %s", fdsPerCore));
    int missing = size % cores;
    System.out.println(String.format("missing %s", missing));
    int current = 0;
    for (int i = 0; i < cores; i++) {
      final Queue<Integer> q = new ConcurrentLinkedQueue<Integer>();
      final int from = current;
      for (int j = 0; j < fdsPerCore; j++) {
        q.offer(current++);
      }
      if (missing > 0) {
        q.offer(current++);
        missing--;
      }
      final int to = current - 1;
      freeFdsArray[i] = q;
      final Map<String, Integer> m = new ConcurrentHashMap<>(q.size());
      fdsPerFileArray[i] = m;
      System.out.println(String.format("core %s from %s to %s", i, from, to));
    }
  }

  @Override
  public int creat(final String file) {
    final long tid = Thread.currentThread().getId();
    int idx = (int) tid % cores;
    Integer nextAvailableFd = freeFdsArray[idx].poll();
    if (nextAvailableFd == null) {
      // some cores must have already exhausted theirs "fds", they help the others
      int newIdx = cores - 1;
      while (newIdx >= 0) {
        if (newIdx != idx) {
          nextAvailableFd = freeFdsArray[newIdx].poll();
          if (nextAvailableFd != null) {
            idx = newIdx;
            break;
          }
        }
        newIdx--;
      }
      if (nextAvailableFd == null) {
        return -1;
      }
    }
    fdsPerFileArray[idx].put(file, nextAvailableFd);
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "anyLockFree";
  }

}
