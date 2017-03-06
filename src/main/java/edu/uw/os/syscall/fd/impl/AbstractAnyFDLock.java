package edu.uw.os.syscall.fd.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import edu.uw.os.syscall.fd.AnyFD;

public abstract class AbstractAnyFDLock implements AnyFD {

  // they should be private with protected getters, but protected suffices for our purposes
  protected final Queue<Integer>[] freeFdsArray;
  protected final Map<String, Integer>[] fdsPerFileArray;
  protected final int cores;

  @SuppressWarnings("unchecked")
  public AbstractAnyFDLock(final int size, final int cores) {
    this.cores = cores;
    freeFdsArray = new LinkedList[cores];
    fdsPerFileArray = new HashMap[cores];
    final int fdsPerCore = size / cores;
    System.out.println(String.format("fds per core %s", fdsPerCore));
    int missing = size % cores;
    System.out.println(String.format("missing %s", missing));
    int current = 0;
    for (int i = 0; i < cores; i++) {
      final Queue<Integer> q = new LinkedList<Integer>();
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
      final Map<String, Integer> m = new HashMap<>(q.size());
      fdsPerFileArray[i] = m;
      System.out.println(String.format("core %s from %s to %s", i, from, to));
    }
  }

}
