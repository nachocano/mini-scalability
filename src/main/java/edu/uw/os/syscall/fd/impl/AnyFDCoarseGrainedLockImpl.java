package edu.uw.os.syscall.fd.impl;

public class AnyFDCoarseGrainedLockImpl extends AbstractAnyFDLock {

  public AnyFDCoarseGrainedLockImpl(final int size, final int threads) {
    super(size, threads);
  }

  @Override
  public synchronized int creat(final String file) {
    final long tid = Thread.currentThread().getId();
    int idx = (int) tid % threads;
    Integer nextAvailableFd = freeFdsArray[idx].poll();
    if (nextAvailableFd == null) {
      // some cores must have already exhausted theirs "FDs", they help the others
      int newIdx = threads - 1;
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
    }
    fdsPerFileArray[idx].put(file, nextAvailableFd);
    return nextAvailableFd;
  }

  @Override
  public String toString() {
    return "anyCoarseLock";
  }

}
