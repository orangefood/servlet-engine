package com.orangefood.se.net;

import java.net.InetAddress;
import java.util.Properties;

public interface SocketListener extends Runnable
{
	/**
	 * initializes the socket listener
	 */
	public void init(Properties prop);
	
	/** 
	 * Accessor methods for the name of this handler
	 */
	public void setName(String strName);
	public String getName();  	
	
	public void setRun(boolean run);
	public boolean getRun();

	public void setBindAddress(InetAddress inetAddr);
	public InetAddress getBindAddress();
  
	public void setPort( int port );
	public int getPort();
	public int getListeningPort();

	public void setSocketHandler( SocketHandler sh );
	public SocketHandler getSocketHandler();

	public void setListenTimeOut( int timeout );
	public int getListenTimeOut();
  
	public void setReadTimeOut( int timeout );
	public int getReadTimeOut();	
}
