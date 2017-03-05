package edu.uw.os.syscall.fd.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import edu.uw.os.syscall.fd.AnyFD;

public class AnyFDLockedImpl implements AnyFD {

  private final Queue<Integer>[] freeFdsArray;
  private final Map<String, Integer>[] fdsPerFileArray;
  private final int threads;

  @SuppressWarnings("unchecked")
  public AnyFDLockedImpl(final int size, final int threads) {
    this.threads = threads;
    freeFdsArray = new LinkedList[threads];
    fdsPerFileArray = new HashMap[threads];
    final int fdsPerThread = size / threads;
    System.out.println(String.format("fds per thread %s", fdsPerThread));
    int missing = size % threads;
    System.out.println(String.format("missing %s", missing));
    int current = 0;
    for (int i = 0; i < threads; i++) {
      final Queue<Integer> q = new LinkedList<Integer>();
      final int from = current;
      for (int j = 0; j < fdsPerThread; j++) {
        q.offer(current++);
      }
      if (missing > 0) {
        q.offer(current++);
        missing--;
      }
      final int to = current - 1;
      freeFdsArray[i] = q;
      final Map<String, Integer> m = new HashMap<>(q.size());
      fdsPerFileArray[i] = m;
      System.out.println(String.format("core/thread %s from %s to %s", i, from, to));
    }
  }

  @Override
  public int creat(final String file) {
    final long tid = Thread.currentThread().getId();
    int idx = (int) tid % threads;
    Integer nextAvailableFd = null;
    synchronized (freeFdsArray[idx]) {
      nextAvailableFd = freeFdsArray[idx].poll();
    }
    if (nextAvailableFd == null) {
      // some cores must have already exhausted theirs "FDs", they help the others
      int newIdx = threads - 1;
      while (newIdx >= 0) {
        if (newIdx != idx) {
          synchronized (freeFdsArray[newIdx]) {
            nextAvailableFd = freeFdsArray[newIdx].poll();
          }
          if (nextAvailableFd != null) {
            idx = newIdx;
            break;
          }
        }
        newIdx--;
      }
    }
    synchronized (fdsPerFileArray[idx]) {
      fdsPerFileArray[idx].put(file, nextAvailableFd);
    }
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "anyFDlocked";
  }

}
