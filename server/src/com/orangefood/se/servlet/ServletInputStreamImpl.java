package com.orangefood.se.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

public class ServletInputStreamImpl extends ServletInputStream
{
	private InputStream _is;
	public ServletInputStreamImpl(InputStream is) { _is = is; }
	public int read() throws IOException
	{
		int n=-1;
		try
		{
			n=_is.read();
		}
		catch(Exception e) { /*NOOP */ }

		return n;
	}
	
	public int readLine(byte[] aby) throws IOException
	{ 
		return readLine(aby,0,aby.length); 
	}
}
