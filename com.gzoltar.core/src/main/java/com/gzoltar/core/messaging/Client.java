package com.gzoltar.core.messaging;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.messaging.Message.AddNodeMessage;
import com.gzoltar.core.messaging.Message.AddProbeMessage;
import com.gzoltar.core.messaging.Message.ByeMessage;
import com.gzoltar.core.messaging.Message.EndTransactionMessage;
import com.gzoltar.core.messaging.Message.HandshakeMessage;
import com.gzoltar.core.model.Node.Type;

public class Client implements IEventListener {

  private String host;
  private int port;
  private final String id;
  private Queue<Message> messages;
  private Boolean seenByeMessage = false;

  private Socket s = null;
  private Thread t = null;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
    this.id = UUID.randomUUID().toString();
    this.messages = new LinkedList<Message>();
  }

  public Client(int port) {
    this(null, port);
  }

  private synchronized Thread postMessage(Message m) {
    messages.add(m);

    if (t == null) {
      t = new Thread(new ClientDispatcher());
      t.start();
    }

    return t;
  }

  private void postBlockingMessage(Message m) {
    try {
      postMessage(new ByeMessage()).join(5000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized Message getMessage() {
    if (messages.size() == 0) {
      t = null;
      return null;
    }

    return messages.poll();
  }

  private class ClientDispatcher implements Runnable {

    @Override
    public void run() {
      Message message = getMessage();

      while (message != null) {
        try {
          if (s == null) {
            s = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(new HandshakeMessage(id));
            out.flush();
          }

          if (!seenByeMessage) {
            seenByeMessage |= message instanceof ByeMessage;

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(message);
            out.flush();
          }

          message = getMessage();
        } catch (Exception e) {
          System.err.println("Exception, reseting socket");
          e.printStackTrace();

          s = null;
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
  public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
    postMessage(new EndTransactionMessage(transactionName, activity, isError));
  }

  @Override
  public void endTransaction(String transactionName, boolean[] activity, int hashCode,
      boolean isError) {
    postMessage(new EndTransactionMessage(transactionName, activity, isError));
  }

  @Override
  public void endSession() {
    postBlockingMessage(new ByeMessage());
  }

  @Override
  public void addNode(int id, String name, Type type, int parentId) {
    postMessage(new AddNodeMessage(id, name, type, parentId));
  }

  @Override
  public void addProbe(int id, int nodeId) {
    postMessage(new AddProbeMessage(id, nodeId));
  }
}
