package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpClientFeaturesTest {

  private HttpServer server;
  private String baseUrl;
  private final HttpClientFeatures httpClientFeatures = new HttpClientFeatures();

  @Before
  public void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
    server.createContext("/ok", exchange -> {
      byte[] body = "hello gzoltar".getBytes();
      exchange.sendResponseHeaders(200, body.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(body);
      }
    });
    server.createContext("/missing", exchange -> {
      exchange.sendResponseHeaders(404, -1);
      exchange.close();
    });
    server.start();
    baseUrl = "http://localhost:" + server.getAddress().getPort();
  }

  @After
  public void stopServer() {
    server.stop(0);
  }

  @Test
  public void testGet_ReturnsBody() throws Exception {
    assertEquals("hello gzoltar", httpClientFeatures.get(baseUrl + "/ok"));
  }

  @Test
  public void testIsSuccessful_OkEndpoint() throws Exception {
    assertTrue(httpClientFeatures.isSuccessful(baseUrl + "/ok"));
  }

  @Test
  public void testIsSuccessful_MissingEndpoint_ShouldBeFalse() throws Exception {
    // A 404 is not a success; this exposes the injected FAULT (>= 200 instead of 200-299).
    assertFalse(httpClientFeatures.isSuccessful(baseUrl + "/missing"));
  }

}
