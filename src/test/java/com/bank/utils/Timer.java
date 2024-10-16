package com.bank.utils;

public final class Timer {
    private volatile long nanoTime = 0;

    public void startTimer() {
        nanoTime = System.nanoTime();
    }

    public synchronized void stopTimer() {
        nanoTime = System.nanoTime() - nanoTime;
    }

    public long getTime() {
        return nanoTime;
    }
}
