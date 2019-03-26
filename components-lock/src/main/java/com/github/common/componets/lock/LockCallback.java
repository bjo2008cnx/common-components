package com.github.common.componets.lock;

public interface LockCallback {

    Object onGetLock() throws InterruptedException;

    Object onTimeout() throws InterruptedException;
}
