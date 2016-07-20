package com.orangefood.se.junit;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Test
{
	private InetAddress _inetAddr;
	private int _nPort;
	
	public Test(String[] astr)
		throws Exception
	{
		_inetAddr = InetAddress.getByName(astr[0]);
		_nPort = Integer.parseInt(astr[1]);
		
		getIndex();
		truncatedRequest();
	}
	
	public void truncatedRequest()
		throws Exception
	{
		System.out.println("Making a truncated request for /");

		System.out.println("Making request for /");
		Socket sock = new Socket(_inetAddr,_nPort);
		InputStream is = sock.getInputStream();
		OutputStream os = sock.getOutputStream();
		os.write("GET / HTTP/1.0\r\n\r\n".getBytes());
		sock.close();
	}
	
	public void getIndex()
		throws Exception
	{
		System.out.println("Making request for /");
		Socket sock = new Socket(_inetAddr,_nPort);
		InputStream is = sock.getInputStream();
		OutputStream os = sock.getOutputStream();
		os.write("GET / HTTP/1.0\r\n\r\n".getBytes());
		int nChar=-1;
		while( (nChar=is.read())!=-1 )
		{
			System.out.print((char)nChar);
		}	
	}
	 
	public static void main(String[] astr)
		throws Exception
	{
		new Test(astr);
	}
}
