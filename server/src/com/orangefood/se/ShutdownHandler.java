package com.orangefood.se;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

import com.orangefood.se.net.SocketHandler;
import com.orangefood.se.servlet.ServletContextImpl;

public class ShutdownHandler implements SocketHandler
{
	private String _strPasswd;
	private String _strName;
	
	public void setName(String strName) { _strName=strName; }
	public String getName() { return _strName; }
	
	public void init(Properties prop)
	{
		_strPasswd = prop.getProperty("password");
	}
	public void destroy(){}

	public void handleSocket(Socket sock)
	{
		byte[] abyBuffer = new byte[_strPasswd.length()];
		try
		{
			InputStream is = sock.getInputStream();
			is.read(abyBuffer);
			String strPass = new String(abyBuffer);
			if( _strPasswd.equals(strPass) )
			{
				Server.stopServer();
			}
			sock.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public void setContext(ServletContextImpl sc) {}
}
