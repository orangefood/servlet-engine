package com.orangefood.se.http.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.orangefood.se.servlet.ServletContextImpl;
import com.orangefood.se.servlet.ServletOutputStreamImpl;
import com.orangefood.se.util.DateFormats;

public class HttpServletResponseImpl implements HttpServletResponse
{
	private ServletOutputStreamImpl _sos=null;
	private PrintWriter _pw=null;
	
	private ServletContextImpl _sc;
	
	public static final String ERROR_HTML="<html><head><title>Server Error</title></head><body>An Internal Error has occured</body></html>";
	
	
	public HttpServletResponseImpl(Socket sock, ServletContextImpl sc, String strProtocol) throws IOException
	{
		_sc = sc;
		_sos = new ServletOutputStreamImpl(sock.getOutputStream(),strProtocol);
	}

	//
	// Cookie Methods
	//
	public void addCookie(Cookie cookie) 
	{
		//Set-Cookie: NAME=VALUE; expires=DATE;
		//path=PATH; domain=DOMAIN_NAME; secure
		StringBuffer sb = new StringBuffer();
		sb.append(cookie.getName());
		sb.append('=');
		sb.append(cookie.getValue());
		int nMaxAge = cookie.getMaxAge();
		if( nMaxAge>-1 )
		{
			Calendar cal= Calendar.getInstance();
			cal.add(Calendar.SECOND,nMaxAge);
			String strDate = DateFormats.RFC_1123_DATE.format(cal.getTime());
			sb.append("; expires=");
			sb.append(strDate);
		}

		String strPath = cookie.getPath();
		if( strPath!=null )
		{
			sb.append("; path=");
			sb.append(strPath);
		}
		
		String strDomain = cookie.getDomain();
		if( strDomain!=null )
		{
			sb.append("; domain=");
		}
		
		if( cookie.getSecure() )
		{
			sb.append("; secure");
		}
		
		addHeader("Set-Cookie",sb.toString());
	}

	//
	// Encoding methods
	//
	public String encodeURL(String string) { return string; }
	public String encodeUrl(String string) { return string; }
	public String encodeRedirectUrl(String string) { return string; }
	public String encodeRedirectURL(String string) { return string; }
	
	// 
	// Redirect methods
	//
	public void sendRedirect(String string) throws IOException 
	{
		setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		// TODO complete sendRedirect
	}

	//
	// Status methods
	//
	public void setStatus(int n, String string) 
	{ 
		// TODO handle string associated with setStatus
		_sos.setStatusCode(n); 
	}
	
	public void setStatus(int n) 
	{ 
		_sos.setStatusCode(n); 
	}
	
	public void sendError(int nError, String string) throws IOException 
	{ 
		setStatus(nError);
		setContentLength(string.length());
		_sos.print(string);
	}
	
	public void sendError(int nError) throws IOException 
	{
		try
		{
			StringWriter sw = new StringWriter();
			String strErrorPage = _sc.getErrorPage(nError);
			if( strErrorPage != null )
			{	
				strErrorPage = _sc.getRealPath(strErrorPage);
				File file = new File(strErrorPage);
				setStatus(nError);
				setContentType(_sc.getMimeType(strErrorPage));
				setContentLength((int)file.length());
				
				FileInputStream fis = new FileInputStream(file);
				int n;
				while( (n=fis.read())!=-1 )
				{
					sw.write(n);
				}
				sendError(nError,sw.toString());
			}
			else
			{
				setContentType("text/html");
				sendError(SC_INTERNAL_SERVER_ERROR,ERROR_HTML);				
			}
			
		}
		catch(Exception e)
		{
			setContentType("text/html");
			sendError(SC_INTERNAL_SERVER_ERROR,ERROR_HTML);
		}
		
	}

	//
	// Stream Methods
	//
	public ServletOutputStream getOutputStream() throws IOException
	{
		return _sos;
	}

	public PrintWriter getWriter() throws IOException
	{
		if( _pw==null )
		{
			_pw = new PrintWriter(new OutputStreamWriter(_sos)); 
		}
		return _pw;
	}

	//
	// Buffer methods
	//
	public boolean isCommitted() { return _sos.isCommitted(); }
	public void reset() { _sos.reset(); }
	public void resetBuffer() { _sos.resetBuffer(); }
	public void setBufferSize(int n) { _sos.setBufferSize(n); }
	public void flushBuffer() throws IOException 
	{
		if( _pw!=null )
		{
			_pw.flush();
		}
		_sos.flushBuffer(); 
	}
	public int getBufferSize() { return _sos.getBufferSize(); }

	public void setLocale(Locale locale) {}
	public Locale getLocale() { return null; }

	public String getCharacterEncoding() { return null; }
	
	// 
	// Header methods
	//

	public void setContentLength(int i) { setIntHeader("Content-Length",i); }
	public void setContentType(String string) { setHeader("Content-Type",string); }
	
	public void setHeader(String name, String value) { _sos.setHeader(name,value); }
	public void addHeader(String name, String value) { _sos.addHeader(name,value); }
	
	public void setIntHeader(String string, int n) { setHeader(string,String.valueOf(n)); }
	public void addIntHeader(String string, int n) { addHeader(string,String.valueOf(n)); }

	public void setDateHeader(String string, long l) { setHeader(string,DateFormats.RFC_1123_DATE.format(new java.util.Date(l)));}	
	public void addDateHeader(String string, long l) { addHeader(string,DateFormats.RFC_1123_DATE.format(new java.util.Date(l)));}

	public boolean containsHeader(String string) { return _sos.containsHeader(string); }
	
	//
	// State methods
	//
	
	/**
	 * Method that finalizes the response to the client.  The method is forwarded
	 * to the output stream where the final data is writen and if necessary
	 * the chunk-size of 0 is writen.
	 */
	public boolean finalizeResponse()
		throws IOException
	{
		return _sos.finalizeResponse();
	}
}
