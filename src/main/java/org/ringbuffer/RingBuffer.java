package org.ringbuffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer<T> {
    private final T[] buffer;
    private final int capacity;
    private int head;
    private int tail;
    private int count;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty =  lock.newCondition();

    @SuppressWarnings("unchecked")
    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        this.buffer = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public void put(T value) throws InterruptedException {
        if (value == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        lock.lockInterruptibly();
        try {
            while (isFull()) {
                notFull.await();
            }

            buffer[tail] = value;
            tail = (tail + 1) % capacity;
            count++;

            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }

            T value = buffer[head];
            buffer[head] = null;
            head = (head + 1) % capacity;
            count--;

            notFull.signal();

            return value;
        } finally {
            lock.unlock();
        }
    }

    private boolean isEmpty() {
        return count == 0;
    }

    private boolean isFull() {
        return count == capacity;
    }
}
