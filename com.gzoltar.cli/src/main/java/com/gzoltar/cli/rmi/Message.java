package com.gzoltar.cli.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.gzoltar.cli.Command;

public class Message extends UnicastRemoteObject implements IMessage {

  private static final long serialVersionUID = 1598755164497706252L;

  private Command command = null;

  private Response response = null;

  /**
   * 
   * @throws RemoteException
   */
  public Message() throws RemoteException {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public Command getCommand() throws RemoteException {
    return this.command;
  }

  /**
   * {@inheritDoc}
   */
  public void setCommand(final Command command) throws RemoteException {
    this.command = command;
  }

  /**
   * {@inheritDoc}
   */
  public Response getResponse() throws RemoteException {
    return this.response;
  }

  /**
   * {@inheritDoc}
   */
  public void setResponse(final Response response) throws RemoteException {
    this.response = response;
  }
}
