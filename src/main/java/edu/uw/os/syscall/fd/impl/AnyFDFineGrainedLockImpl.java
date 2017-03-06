package edu.uw.os.syscall.fd.impl;

public class AnyFDFineGrainedLockImpl extends AbstractAnyFDLock {


  public AnyFDFineGrainedLockImpl(final int size, final int threads) {
    super(size, threads);
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
    return "anyFineLock";
  }

}
