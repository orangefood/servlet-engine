package com.orangefood.se.net;

import java.net.Socket;
import java.util.Properties;

import com.orangefood.se.servlet.ServletContextImpl;

/**
 * This interface defines methods for classes wishing to haldle socket connections
 * from SocketListener.
 * 
 * @see com.orangefood.java.net.SocketListener
 */
public interface SocketHandler
{
  /**
   * initializes the socket handler
   */
  public void init(Properties prop);
  
  /**
   * provides a context for the handler to run in
   */
  public void setContext(ServletContextImpl sc);

  /** 
   * Accessor methods for the name of this handler
   */
  public void setName(String strName);
  public String getName();  
  
  /**
   * Notifys the handler that the server is killing it
   */
  public void destroy();
  
  /**
   * Called whan a socket connection has been made.
   */
  public void handleSocket( Socket sock );
}

