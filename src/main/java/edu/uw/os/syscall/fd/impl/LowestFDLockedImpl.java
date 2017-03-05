package edu.uw.os.syscall.fd.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.lang3.Validate;

import edu.uw.os.syscall.fd.LowestFD;

public class LowestFDLockedImpl implements LowestFD {

  private final Queue<Integer> freeFds;
  private final Map<String, Integer> fdsPerFile;

  public LowestFDLockedImpl(final int size) {
    Validate.isTrue(size > 0);
    fdsPerFile = new HashMap<>(size);
    freeFds = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      freeFds.offer(i);
    }
  }

  @Override
  public int creat(final String file) {
    Integer nextAvailableFd = null;
    synchronized (freeFds) {
      nextAvailableFd = freeFds.poll();
      if (nextAvailableFd == null) {
        return -1;
      }
    }
    synchronized (fdsPerFile) {
      fdsPerFile.put(file, nextAvailableFd);
    }
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "lowestFDlocked";
  }
}
