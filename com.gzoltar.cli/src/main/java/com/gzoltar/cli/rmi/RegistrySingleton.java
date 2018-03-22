package com.gzoltar.cli.rmi;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class RegistrySingleton {

  private static int port = 5000;

  private static Registry reg = null;

  /**
   * 
   */
  public static synchronized void createSingleton() {
    if (reg == null) {
      while (true) {
        try {
          reg = LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
          port++;
          continue;
        } catch (RemoteException e) {
          e.printStackTrace();
          reg = null;
        }
        return;
      }
    }
  }

  /**
   * 
   * @param name
   * @param obj
   */
  public static synchronized void register(final String name, final Remote obj) {
    if (reg != null) {
      try {
        reg.rebind(name, obj);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 
   * @param name
   */
  public static synchronized void unregister(final String name) {
    if (reg != null) {
      try {
        Remote remote = reg.lookup(name);
        reg.unbind(name);
        if (remote instanceof UnicastRemoteObject) {
          UnicastRemoteObject.unexportObject(remote, false);
        }
      } catch (RemoteException | NotBoundException e) {
        e.printStackTrace();
      } finally {
        reg = null;
      }
    }
  }

  /**
   * 
   * @return
   */
  public static synchronized int getPort() {
    return port;
  }
}
