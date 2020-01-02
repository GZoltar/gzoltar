/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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
