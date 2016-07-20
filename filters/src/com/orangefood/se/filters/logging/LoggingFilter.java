package com.orangefood.se.filters.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingFilter implements Filter
{
	SimpleDateFormat _sdf = null;
	File _fileLog;
	FileWriter _fw;
	
	public void init(FilterConfig fc) throws ServletException
	{
		DecimalFormat df = new DecimalFormat("00");
		int nMinutes = TimeZone.getDefault().getRawOffset()/60000;
		int nHours = nMinutes/60;
		nMinutes = nMinutes-(nHours*60);
		
		_sdf = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss "+df.format(nHours)+df.format(nMinutes)+"]");
		try
		{
			_fw=new FileWriter(fc.getInitParameter("log-file"),true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain fc)
		throws IOException, ServletException
	{
		if(response instanceof HttpServletResponse)
		{	
			response = new LoggingHttpServletResponse((HttpServletResponse)response);	
		}
		fc.doFilter(request,response);
		if( request instanceof HttpServletRequest &&
			response instanceof HttpServletResponse)
		{
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse res = (HttpServletResponse)response;
			res.flushBuffer();

			// Common LogFile format
			// remotehost rfc931 authuser [date] "request" status bytes
			
			StringBuffer sb = new StringBuffer();
		
			// Host
			String strHost = request.getRemoteAddr();
			if( strHost==null ) { strHost = "-"; }
			sb.append(strHost);
		
			// ref 931 (always '-') we don't use IDENT
			sb.append(" - ");
			
			// authuser
			String strAuthUser = req.getRemoteUser();
			if( strAuthUser == null ) { strAuthUser="-"; }
			sb.append(strAuthUser);
			sb.append(' ');
			
			// date [01/Jan/1997:13:06:51 -0600]
			sb.append( _sdf.format(new Date()));
//			sb.append( new Date() );
			sb.append(' ');
			
			// request
			sb.append('"');
			sb.append(req.getMethod());
			sb.append(' ');
			sb.append(req.getRequestURI());
			sb.append(' ');
			sb.append(req.getProtocol());
			sb.append("\" ");

			// status
			int nStatus = ((LoggingHttpServletResponse)response).getStatus();
			if( nStatus==-1)
			{	
				sb.append('-');
			}
			else
			{
				sb.append(nStatus);
			}
			sb.append(' ');
			
			//bytes
			int nCount = ((LoggingHttpServletResponse)response).getCount();
			if( nCount==-1)
			{	
				sb.append('-');
			}
			else
			{
				sb.append(nCount);
			}
			
			sb.append('\n');

			_fw.write(sb.toString());
			_fw.flush();
		}
	}

	public void destroy() {}
}
