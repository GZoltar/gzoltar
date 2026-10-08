package org.gzoltar.java21;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Virtual Threads (JEP 444, finalized/standard in Java 21, Project Loom).
 *
 * Lightweight threads managed by the JVM instead of the OS, created via
 * Thread.ofVirtual() or Executors.newVirtualThreadPerTaskExecutor(). Using an
 * ExecutorService in a try-with-resources block (AutoCloseable since Java 19)
 * ensures all submitted tasks finish before the block exits, so results are
 * deterministic for testing.
 */
public class VirtualThreadFeatures {

  public boolean currentThreadIsVirtualWhenUsingVirtualExecutor() throws InterruptedException, ExecutionException {
    AtomicBoolean sawVirtualThread = new AtomicBoolean(false);
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      Future<?> future = executor.submit(() -> sawVirtualThread.set(Thread.currentThread().isVirtual()));
      future.get();
    }
    return sawVirtualThread.get();
  }

  public int sumOfSquares(List<Integer> numbers) throws InterruptedException, ExecutionException {
    List<Future<Integer>> futures = new ArrayList<>();
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int n : numbers) {
        futures.add(executor.submit(() -> n * n));
      }
    }
    int sum = 0;
    for (Future<Integer> future : futures) {
      sum += future.get();
    }
    return sum;
  }

  public boolean directlyStartedThreadIsVirtual() throws InterruptedException {
    AtomicBoolean isVirtual = new AtomicBoolean(false);
    Thread thread = Thread.ofVirtual().start(() -> isVirtual.set(Thread.currentThread().isVirtual()));
    thread.join();
    return isVirtual.get();
  }
}
