/**
 * Copyright (C) 2019 GZoltar contributors.
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
package com.gzoltar.cli.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.gzoltar.cli.Command;

public interface IMessage extends Remote {

  /**
   * 
   * @return
   * @throws RemoteException
   */
  public Command getCommand() throws RemoteException;

  /**
   * 
   * @param command
   * @throws RemoteException
   */
  public void setCommand(final Command command) throws RemoteException;

  /**
   * 
   * @return
   * @throws RemoteException
   */
  public Response getResponse() throws RemoteException;

  /**
   * 
   * @param response
   * @throws RemoteException
   */
  public void setResponse(final Response response) throws RemoteException;
}
