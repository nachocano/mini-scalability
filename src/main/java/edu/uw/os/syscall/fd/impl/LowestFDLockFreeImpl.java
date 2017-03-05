package edu.uw.os.syscall.fd.impl;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.Validate;

import edu.uw.os.syscall.fd.LowestFD;

public class LowestFDLockFreeImpl implements LowestFD {

  private final Queue<Integer> freeFds;
  private final Map<String, Integer> fdsPerFile;

  public LowestFDLockFreeImpl(final int size) {
    Validate.isTrue(size > 0);
    fdsPerFile = new ConcurrentHashMap<>(size);
    freeFds = new ConcurrentLinkedQueue<>();
    for (int i = 0; i < size; i++) {
      freeFds.offer(i);
    }
  }

  @Override
  public int creat(final String file) {
    final Integer nextAvailableFd = freeFds.poll();
    if (nextAvailableFd == null) {
      return -1;
    }
    fdsPerFile.put(file, nextAvailableFd);
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "lowestFDlockfree";
  }
}
