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
package com.gzoltar.core.rmi;

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
