package com.gzoltar.core.messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ThreadedServer extends Thread {

  private ThreadGroup threads = new ThreadGroup("");

  private ServerSocket serverSocket;

  public ThreadedServer(final ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public final void run() {
    while (!this.serverSocket.isClosed()) {
      try {
        Socket socket = this.serverSocket.accept();
        Thread thread = new Thread(this.threads, handle(socket));
        thread.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  protected abstract Runnable handle(final Socket s);

}
