package com.orangefood.se.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;

import com.orangefood.se.Server;

public class ServletOutputStreamImpl extends ServletOutputStream
{
	public static final byte[] CRLF="\r\n".getBytes();
	public static final byte[] CHUNKED="Transfer-Encoding: chunked".getBytes();
	public static final byte[] VERSION="HTTP/1.1".getBytes();
	
	private String _strProtocol; // The client protocol
	
	private String _strStatusCode;
	private String _strResponsePhrase;
	private Properties _propResponseCodes;
	private HashMap _hmHeaders=new HashMap();
	private OutputStream _os;
	
	private ByteArrayOutputStream _baos=new ByteArrayOutputStream();
	private boolean _bCommitted;
	private boolean _bChunked=false;
	private boolean _bKeepAlive=false;
	private int _nBufferSize=1024;

	public ServletOutputStreamImpl(OutputStream os,String strProtocol) throws IOException
	{ 
		_propResponseCodes = Server.getProperties("response_codes");
		_os = os; 
		_strProtocol=strProtocol;
	}
	
	public boolean isCommitted() { return _bCommitted; }
	
	public void reset()
	{
		if( _bCommitted )
		{	
			throw new IllegalStateException();
		}
		_hmHeaders.clear();
		resetBuffer();
	}
	
	public void resetBuffer()
	{
		if( _bCommitted )
		{	
			throw new IllegalStateException();
		}		
		_baos.reset();
	}
	
	public int getBufferSize()
	{
		return _nBufferSize;
	}
	
	public void setBufferSize(int n)
	{
		if( _bCommitted )
		{	
			throw new IllegalStateException();
		}
		if( n<_baos.size())
		{	
			n=_baos.size();
		}
		_nBufferSize=n;
	}
	
	/**
	 * Flushed the buffer to the underlying output stream
	 * @throws IOException
	 */
	public void flushBuffer() throws IOException
	{
		if( !_bCommitted )
		{	
			// Determine if this should be chunked
			_bChunked = ( "http/1.1".equalsIgnoreCase(_strProtocol) &&
				          !_hmHeaders.containsKey("Content-Length") );
			_bKeepAlive=_bChunked||_hmHeaders.containsKey("Content-Length");
			// Write the response
			// HTTP/1.1 200 OK
			_os.write(VERSION);
			_os.write(' ');
			_os.write(_strStatusCode.getBytes());
			_os.write(' ');
			_os.write(_strResponsePhrase.getBytes());
			_os.write(CRLF);
			
			// Write the headers
			Iterator it = _hmHeaders.entrySet().iterator();
			while( it.hasNext() )
			{
				Map.Entry me = (Map.Entry) it.next();
				String strKey = (String)me.getKey();
				ArrayList alValues = (ArrayList)me.getValue();
				Iterator itValues = alValues.iterator();
				while(itValues.hasNext())
				{
					_os.write(strKey.getBytes());
					_os.write(':');
					_os.write(' ');
					_os.write(((String)itValues.next()).getBytes());
					_os.write(CRLF);
				}
			}
			if( _bChunked )
			{
				_os.write(CHUNKED);
			}

			_os.write(CRLF);
			_bCommitted = true;
		}

		
		//	Write the buffer, if there is buffered data
		if( _baos.size()>0 )
		{	
			// Write the chunk size if it is chunked
			if( _bChunked )
			{
				_os.write(CRLF);
				_os.write(Integer.toHexString(_baos.size()).getBytes());
				_os.write(CRLF);
			}
			_baos.writeTo(_os);
		}
		// Empty the buffer
		_baos.reset();
	}
	
	public void setStatusCode(int n) 
	{
		_strStatusCode = String.valueOf(n);
		_strResponsePhrase = _propResponseCodes.getProperty(_strStatusCode);
	}
	
	public boolean containsHeader(String name)
	{
		return _hmHeaders.containsKey(name);
	}
	
	public void setHeader(String name, String value)
	{
		ArrayList al = new ArrayList();
		al.add(value);
		_hmHeaders.put(name,al);
	}
	
	public void addHeader(String name, String value)
	{
		ArrayList al = (ArrayList) _hmHeaders.get(name);
		if( al==null )
		{
			al = new ArrayList();
			_hmHeaders.put(name,al);
		}
		al.add(value);
	}
	
	public void write(int n) throws IOException
	{
		_baos.write(n);
		if( _baos.size()>_nBufferSize )
		{
			flushBuffer();
		}
	}
	
	public boolean finalizeResponse()
		throws IOException
	{
		// Empty the buffer if there is any data remaining
		flushBuffer();
		
		if( _bChunked )
		{	
			// Write the last-chunk
			_os.write(CRLF);
			_os.write('0');
			_os.write(CRLF);
			_os.write(CRLF);
		}
		
		return _bKeepAlive;
	}
}
