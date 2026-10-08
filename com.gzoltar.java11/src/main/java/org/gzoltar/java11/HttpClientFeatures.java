package org.gzoltar.java11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Exercises java.net.http.HttpClient, standardized in Java 11 (JEP 321) after living as an
 * incubator module (jdk.incubator.http) since Java 9. Every test that uses this class points it
 * at a local, in-process HTTP server started by the test itself, so the suite has no external
 * network dependency and stays fully deterministic.
 */
public class HttpClientFeatures {

  private final HttpClient client = HttpClient.newHttpClient();

  public String get(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    return response.body();
  }

  public boolean isSuccessful(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    return response.statusCode() >= 200; /* FAULT: should check the 200-299 range, not just >= 200 */
  }

}
