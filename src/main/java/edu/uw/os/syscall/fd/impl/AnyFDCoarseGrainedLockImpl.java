package edu.uw.os.syscall.fd.impl;

public class AnyFDCoarseGrainedLockImpl extends AbstractAnyFDLock {

  public AnyFDCoarseGrainedLockImpl(final int size, final int threads) {
    super(size, threads);
  }

  @Override
  public synchronized int creat(final String file) {
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
    return "anyCoarseLock";
  }

}
