package com.gzoltar.core.messaging;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.gzoltar.core.events.EventListener;
import com.gzoltar.core.messaging.Message.AddNodeMessage;
import com.gzoltar.core.messaging.Message.AddProbeMessage;
import com.gzoltar.core.messaging.Message.ByeMessage;
import com.gzoltar.core.messaging.Message.EndTransactionMessage;
import com.gzoltar.core.messaging.Message.HandshakeMessage;
import com.gzoltar.core.messaging.Service.ServiceFactory;

public class Server extends ThreadedServer {

  private ServiceFactory serviceFactory;

  public Server(ServerSocket serverSocket, ServiceFactory serviceFactory) {
    super(serverSocket);
    this.serviceFactory = serviceFactory;
  }

  @Override
  protected final Runnable handle(Socket s) {
    return new ServerDispatcher(s);
  }

  private class ServerDispatcher implements Runnable {

    private Socket socket;
    private Service service;

    public ServerDispatcher(Socket s) {
      this.socket = s;
    }

    @Override
    public void run() {
      try {
        service = handshake();

        while (true) {
          Object o = new ObjectInputStream(socket.getInputStream()).readObject();

          if (dispatch(o)) {
            service.terminated();
            break;
          }
        }
      } catch (Throwable e) {
        if (service != null)
          service.interrupted();

        e.printStackTrace();
      }

      try {
        socket.close();
      } catch (Throwable e) {
      }
    }

    public Service handshake() throws Exception {
      Object o = new ObjectInputStream(socket.getInputStream()).readObject();

      if (!(o instanceof HandshakeMessage))
        throw new Exception("First message should be a HandshakeMessage. Received instead: " + o);

      String id = ((HandshakeMessage) o).id;
      return serviceFactory.create(id);
    }

    private boolean dispatch(Object o) {
      EventListener eventListener = service.getEventListener();

      if (o instanceof ByeMessage) {
        eventListener.endSession();
        return true;
      } else if (o instanceof EndTransactionMessage) {
        EndTransactionMessage etm = (EndTransactionMessage) o;
        eventListener.endTransaction(etm.transactionName, etm.activity, etm.isError);
      } else if (o instanceof AddNodeMessage) {
        AddNodeMessage anm = (AddNodeMessage) o;
        eventListener.addNode(anm.id, anm.name, anm.type, anm.parentId);
      } else if (o instanceof AddProbeMessage) {
        AddProbeMessage apm = (AddProbeMessage) o;
        eventListener.addProbe(apm.id, apm.nodeId);
      }

      return false;
    }
  }
}
