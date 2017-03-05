package edu.uw.os.syscall.fd.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import edu.uw.os.syscall.fd.LowestFD;

public abstract class AbstractLowestFDLock implements LowestFD {

  // they should be private with protected getters, but protected suffices for our purposes
  protected final Queue<Integer> freeFds;
  protected final Map<String, Integer> fdsPerFile;

  public AbstractLowestFDLock(final int size) {
    fdsPerFile = new HashMap<>(size);
    freeFds = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      freeFds.offer(i);
    }
  }

}
