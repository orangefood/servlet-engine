package com.orangefood.se.filters.logging;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class LoggingServletOutputStream extends ServletOutputStream
{
	private int _nCount=0;
	private ServletOutputStream _sos;
	
	public LoggingServletOutputStream(ServletOutputStream sos)
	{
		_sos = sos;
	}
	
	public int getCount() { return _nCount; }
	public void resetCount() 
	{
		_nCount=0; 
	}
	
	public void write(int b) throws IOException
	{
		_nCount++;
		_sos.write(b);
	}
}
