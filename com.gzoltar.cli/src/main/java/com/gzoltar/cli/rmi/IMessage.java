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
