package org.ringbuffer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example {
    public static void main(String[] args) throws InterruptedException {
        RingBuffer<String> buffer = new RingBuffer<>(10);

        Runnable producer = () -> {
            Random random = new Random();
            try {
                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    String message = "Message " + "(" + random.nextInt(1000, 5001) + ") " + i++;
                    buffer.put(message);
                    System.out.println(Thread.currentThread().getName() + " produced: " + message);
                    Thread.sleep(random.nextInt(500));
//                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
        };

        Runnable consumer = () -> {
            Random random = new Random();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = buffer.get();
                    System.out.println(Thread.currentThread().getName() + " consumed: " + message);
                    Thread.sleep(random.nextInt(1000));
//                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(5);

        executor.submit(producer, "Producer-1");
        executor.submit(producer, "Producer-2");

        executor.submit(consumer, "Consumer-1");
        executor.submit(consumer, "Consumer-2");
        executor.submit(consumer, "Consumer-3");

        Thread.sleep(5000);

        executor.shutdownNow();
    }
}
