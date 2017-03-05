package edu.uw.os.syscall.fd.impl;

public class LowestFDCoarseGrainedLockImpl extends AbstractLowestFDLock {

  public LowestFDCoarseGrainedLockImpl(final int size) {
    super(size);
  }

  @Override
  public synchronized int creat(final String file) {
    final Integer nextAvailableFd = freeFds.poll();
    if (nextAvailableFd == null) {
      return -1;
    }
    fdsPerFile.put(file, nextAvailableFd);
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "lowestCoarseLock";
  }
}
