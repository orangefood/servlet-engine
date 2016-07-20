package com.orangefood.se.http;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletResponse;

import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;
import com.orangefood.se.net.SocketHandler;
import com.orangefood.se.servlet.RequestDispatcherFilterChain;
import com.orangefood.se.servlet.ServletContextImpl;

public class HttpHandler implements SocketHandler
{
	private boolean _bRun=true;
	private ServletContextImpl _sc;
	private String _strScheme="http";
	private String _strServerName;
	private int _nOutstandingHandlers=0;
	private String _strName;
	
	public void setName(String strName) { _strName=strName; }
	public String getName() { return _strName; }
		
	
	public void setContext(ServletContextImpl sc) 
	{
		_sc=sc; 
	}
	
	public void init(Properties prop)
	{
		_strScheme=prop.getProperty("scheme");
	}
	
	public synchronized void destroy()
	{
		// All HandlerThreads should stop
		_bRun=false;
		if( _nOutstandingHandlers>0 )
		{
			try
			{
				wait();
			}
			catch(InterruptedException ie) {}
		}
		// Send the destroy to the Servlets
		_sc.destroy();
	}
	
	public void handleSocket(Socket sock)
	{
		if( _bRun )
		{	
			HandlerThread ht = new HandlerThread(sock);
			ht.start();
		}
	}
	
	private synchronized void _incrementHandlerCount() 
	{ 
		_nOutstandingHandlers++; 
	}
	
	private synchronized void _decrementHandlerCount()
	{
		_nOutstandingHandlers--;
		if( _nOutstandingHandlers==0 )
		{
			notify();
		}
	}
	
	private class HandlerThread extends Thread
	{
		private Socket _sock;
		
		private HandlerThread(Socket sock)
		{
			super("HttpHandlerThread-"+sock.getPort());
			_sock=sock;
		}

		public void run()
		{
			_incrementHandlerCount();
			boolean bKeepAlive=true;
			try
			{
				while( _bRun && bKeepAlive )
				{	
					// Create the ServletRequest
					HttpServletRequestImpl request = null;
					try
					{
						request = new HttpServletRequestImpl(_sock,_sc);
						request.setScheme(_strScheme);
						
						// Create the ServletResponse
						HttpServletResponseImpl response = new HttpServletResponseImpl(_sock,_sc,request.getProtocol());
			
						RequestDispatcherFilterChain fc = (RequestDispatcherFilterChain) _sc.getFilterChain(request);
						try
						{
							fc.doFilter(request,response);
							// TODO - get Keepalive working
							bKeepAlive = response.finalizeResponse();
						}
						catch(UnavailableException ue)
						{
							// We treat unavailable servlets as permanent
							// per section SRV 2.3.3.2 of the Servlet 2.3 spec
							ue.printStackTrace();
							HttpServletResponseImpl responseError = new HttpServletResponseImpl(_sock,_sc,"http/1.0");
							response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
							response.finalizeResponse();
							bKeepAlive=false;
							_sc.destroyServlet(fc.getRequestDispatcher().getServletName());
						}
					}
					catch(IllegalStateException ies)
					{
						// Thrown when the client closes the socket
						// and there is nothing left to read
						bKeepAlive=false;
					}
					catch(SocketException se)
					{
						// Problem with the socket sp just stop all processing 
						// TODO - Log a debug message
						bKeepAlive=false;
						se.printStackTrace();
					}
					catch(Exception e)
					{
						// TODO log this
						e.printStackTrace();
						HttpServletResponseImpl response = new HttpServletResponseImpl(_sock,_sc,"http/1.0");
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.finalizeResponse();
						bKeepAlive=false;
					}
				}
			}
			catch (Throwable t)
			{
				// TODO Auto-generated catch block
				t.printStackTrace();
			}
			finally
			{
				try
				{
					_sock.close();
				}
				catch (IOException e)
				{
					System.err.println("Exception closing socket");
					e.printStackTrace();
				}			
			}
			_decrementHandlerCount();
		}		
	}
}
