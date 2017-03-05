package edu.uw.os.syscall.fd.impl;

public class LowestFDFineGrainedLockImpl extends AbstractLowestFDLock {

  public LowestFDFineGrainedLockImpl(final int size) {
    super(size);
  }

  @Override
  public int creat(final String file) {
    Integer nextAvailableFd = null;
    synchronized (freeFds) {
      nextAvailableFd = freeFds.poll();
    }
    if (nextAvailableFd == null) {
      return -1;
    }
    synchronized (fdsPerFile) {
      fdsPerFile.put(file, nextAvailableFd);
    }
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "lowestFineLock";
  }
}
