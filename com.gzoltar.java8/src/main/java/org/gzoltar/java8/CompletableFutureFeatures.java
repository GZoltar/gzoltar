package org.gzoltar.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Exercises java.util.concurrent.CompletableFuture : async composition with
 * thenApply/thenCombine/exceptionally. Every method blocks on get() before returning so the
 * tests stay deterministic instead of racing real background threads.
 */
public class CompletableFutureFeatures {

  public int square(int value) throws ExecutionException, InterruptedException {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> value * value);
    return future.get();
  }

  public String combineGreeting(String first, String second)
      throws ExecutionException, InterruptedException {
    CompletableFuture<String> firstFuture = CompletableFuture.supplyAsync(() -> "Hello " + first);
    CompletableFuture<String> secondFuture = CompletableFuture.supplyAsync(() -> "Hello " + second);
    return firstFuture.thenCombine(secondFuture, (a, b) -> a + " & " + b).get();
  }

  public int chain(int value) throws ExecutionException, InterruptedException {
    return CompletableFuture.supplyAsync(() -> value)
        .thenApply(v -> v + 1)
        .thenApply(v -> v * 2)
        .get();
  }

  public String recoverFromFailure(boolean shouldFail)
      throws ExecutionException, InterruptedException {
    return CompletableFuture.supplyAsync(() -> {
          if (shouldFail) {
            throw new IllegalStateException("boom");
          }
          return "ok";
        })
        .exceptionally(ex -> "recovered: " + ex.getCause().getMessage())
        .get();
  }

}
