package com.orangefood.se.filters.logging;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class LoggingHttpServletResponse extends HttpServletResponseWrapper
{
	private LoggingServletOutputStream _csos=null;
	private int _nStatus = -1;
	private PrintWriter _pw=null;
	
	public LoggingHttpServletResponse(HttpServletResponse response)
	{
		super(response);
	}
	
	public int getCount()
	{
		int nCount=-1;
		if( _csos!=null )
		{
			nCount=_csos.getCount();
		}
		return nCount;
	}
	
	public int getStatus() { return _nStatus; } 
	
	//
	// Stream Methods
	//
	public ServletOutputStream getOutputStream() throws IOException
	{
		if( _csos == null )
		{
			_csos = new LoggingServletOutputStream(super.getOutputStream());
		}
		return _csos;
	}

	public PrintWriter getWriter() throws IOException
	{
		if( _pw == null )
		{
			_pw = new PrintWriter(new OutputStreamWriter(getOutputStream())); 
		}
		return _pw;
	}

	//
	// Buffer methods
	//
	public void reset() 
	{
		if( _csos!=null )
		{
			_csos.resetCount();
		}
		super.reset();
	}
	public void resetBuffer() 
	{ 
		if( _csos!=null )
		{
			_csos.resetCount();
		}
		super.resetBuffer(); 
	}
	public void flushBuffer() throws IOException 
	{
		if( _pw!=null )
		{
			_pw.flush();
		}
		if( _csos!=null )
		{	
			_csos.flush();
		}
		super.flushBuffer();
	}
	
	public void setStatus(int n, String string) 
	{ 
		_nStatus=n;
		super.setStatus(n,string);
	}
	public void setStatus(int n) 
	{ 
		_nStatus=n;
		super.setStatus(n);
	}
	
	public void sendError(int nError, String string) throws IOException 
	{ 
		_nStatus=nError;
		super.sendError(nError,string);		
	}
	
	public void sendError(int nError) throws IOException 
	{
		_nStatus=nError;
		super.sendError(nError);
	}
	
}
