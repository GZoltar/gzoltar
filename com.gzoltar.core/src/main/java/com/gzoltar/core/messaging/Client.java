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

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.messaging.Message.AddProbeGroupMessage;
import com.gzoltar.core.messaging.Message.ByeMessage;
import com.gzoltar.core.messaging.Message.EndTransactionMessage;
import com.gzoltar.core.messaging.Message.HandshakeMessage;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class Client implements IEventListener {

  private final String host;

  private final int port;

  private final String id;

  private final Queue<Message> messages;

  private Boolean seenByeMessage = false;

  private Socket socket = null;

  private Thread thread = null;

  public Client(final String host, final int port) {
    this.host = host;
    this.port = port;
    this.id = UUID.randomUUID().toString();
    this.messages = new LinkedList<Message>();
  }

  public Client(final int port) {
    this(null, port);
  }

  private synchronized Thread postMessage(final Message m) {
    this.messages.add(m);

    if (this.thread == null) {
      this.thread = new Thread(new ClientDispatcher());
      this.thread.start();
    }

    return this.thread;
  }

  private void postBlockingMessage(final Message m) {
    try {
      postMessage(new ByeMessage()).join(5000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized Message getMessage() {
    if (this.messages.size() == 0) {
      this.thread = null;
      return null;
    }

    return this.messages.poll();
  }

  private class ClientDispatcher implements Runnable {

    @Override
    public void run() {
      Message message = getMessage();

      while (message != null) {
        try {
          if (socket == null) {
            socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new HandshakeMessage(id));
            out.flush();
          }

          if (!seenByeMessage) {
            seenByeMessage |= message instanceof ByeMessage;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            out.flush();
          }

          message = getMessage();
        } catch (Exception e) {
          System.err.println("Exception, reseting socket");
          e.printStackTrace();

          socket = null;
          try {
            Thread.sleep(10000);
          } catch (Exception e2) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  @Override
  public void regiterProbeGroup(final ProbeGroup probeGroup) {
    this.postMessage(new AddProbeGroupMessage(probeGroup));
  }

  @Override
  public void endTransaction(final Transaction transaction) {
    this.postMessage(new EndTransactionMessage(transaction));
  }

  @Override
  public void endSession() {
    this.postBlockingMessage(new ByeMessage());
  }

}
