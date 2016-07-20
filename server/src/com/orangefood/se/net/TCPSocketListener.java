package com.orangefood.se.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.io.InterruptedIOException;
import java.io.IOException;

/**
 * This class listens for connections on the specified port and when
 * a connection is recieved the socket is passed on the the SocketHandler
 * for processing.
 */
public class TCPSocketListener implements SocketListener
{
  public static final int BACK_LOG=50;
  public static final int TIMEOUT=50;
  private boolean _bRun = true;
  private int _nPort=0;
  private int _nListeningPort=-1;
  private InetAddress _inetAddr=null;
  private SocketHandler _sh = null;
  
  protected ServerSocket $srv = null;
  private int _nListenTimeOut=5000;
  private int _nReadTimeOut=10000;

  private String _strName;
	
  public void setName(String strName) { _strName=strName; }
  public String getName() { return _strName; }
	  
  
  public void init(Properties prop)
  {
  	String strBindAddress=prop.getProperty("bind-address");
  	if( strBindAddress!=null )
  	{
		try
		{
			InetAddress inetAddr = InetAddress.getByName(strBindAddress);
			setBindAddress(inetAddr);
		}
		catch (UnknownHostException uhe)
		{
			uhe.printStackTrace();
		}
  	}
  	
  	String strPort=prop.getProperty("port");
  	if( strPort!=null )
  	{
  		setPort(Integer.parseInt(strPort));
  	}
  	
  	String strListenTimeOut=prop.getProperty("listen-timeout");
  	if( strListenTimeOut!=null )
  	{
  		setListenTimeOut(Integer.parseInt(strListenTimeOut));
  	}
  	
	String strReadTimeOut=prop.getProperty("read-timeout");
	if( strReadTimeOut!=null )
	{
		setReadTimeOut(Integer.parseInt(strReadTimeOut));
	}
	
	$createServerSocket();
  }
  
  protected void $createServerSocket()
  {
	try
	{
	  $srv = new ServerSocket( _nPort, BACK_LOG, _inetAddr);
	  // Set the listen timeout
	  $srv.setSoTimeout( _nListenTimeOut );
	  $setListeningPort($srv.getLocalPort());
	}
	catch ( Exception e )
	{
		setRun(false);
	  e.printStackTrace();
	}  	
  }
  
  /**
   * Begins listening for a new connection in new thread
   */
  public void run()
  {
    listen();
  }

  /**
   * Begins listening for a connection.
   */
  public void listen()
  {
    // Run until somebody restarts us
    while (_bRun)
    {
      try
      {
        Socket sock = $srv.accept();
        sock.setTcpNoDelay(true);
        sock.setSoTimeout(_nReadTimeOut);
        _sh.handleSocket( sock );
      }
      catch( InterruptedIOException iioe )
      {
        // Socket timed out. . .
        // do nothing!
      }
      catch( IOException ioe )
      {
      	ioe.printStackTrace();
        _bRun = false;
      }
    }
    // destroy the handler
    _sh.destroy();

    // Close the listening socket
    try
    {
      $srv.close();
    }
    catch ( Exception e )
    {
    	e.printStackTrace();
    }
  }

  public void setRun(boolean run) { _bRun = run; }
  public boolean getRun() { return _bRun; }

  public void setBindAddress(InetAddress inetAddr) { _inetAddr = inetAddr; }
  public InetAddress getBindAddress() { return _inetAddr; }
  
  public void setPort( int port ) { _nPort = port; }
  public int getPort() { return _nPort; }
  public int getListeningPort() { return _nListeningPort; }
  protected void $setListeningPort(int nListenginPort) { _nListeningPort=nListenginPort; }
  

  public void setSocketHandler( SocketHandler sh ) { _sh = sh; }
  public SocketHandler getSocketHandler() { return _sh; }

  public void setListenTimeOut( int timeout ) { _nListenTimeOut = timeout; }
  public int getListenTimeOut() { return _nListenTimeOut; }
  
  public void setReadTimeOut( int timeout ) { _nReadTimeOut = timeout; }
  public int getReadTimeOut() { return _nReadTimeOut; }
  
}
