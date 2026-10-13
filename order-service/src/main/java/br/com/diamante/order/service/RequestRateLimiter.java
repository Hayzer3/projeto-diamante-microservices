package br.com.diamante.order.service;

public final class RequestRateLimiter {
    private final int capacity;
    private double tokens;
    private long lastRefill = System.nanoTime();
    public RequestRateLimiter(int requestsPerSecond) {
        capacity = requestsPerSecond;
        tokens = capacity;
    }

    public synchronized boolean tryAcquire() {
        long now = System.nanoTime();
        tokens = Math.min(capacity, tokens + (now - lastRefill) * capacity / 1_000_000_000d);
        lastRefill = now;
        if (tokens < 1) {
            return false;
        }
        tokens -= 1;
        return true;
    }
}
