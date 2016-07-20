package com.orangefood.se.filters.header;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;

import com.orangefood.se.filter.HttpServletImplFliter;
import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;

public class HeaderFilter extends HttpServletImplFliter
{
	private int _nMaxLineLength = 2048;
	public void doFilter(
		HttpServletRequestImpl request,
		HttpServletResponseImpl response,
		FilterChain chain)
		throws IOException, ServletException
	{
		
		String strHeader="";
		byte[] abyBuffer = new byte[2048];
		ServletInputStream sis = request.getInputStream();
		
		while( !"".equals(strHeader=(new String(abyBuffer,0,sis.readLine(abyBuffer,0,abyBuffer.length))).trim())) 
		{
			int ndxHeader = strHeader.indexOf(':');
			String strName = strHeader.substring(0,ndxHeader).trim().toLowerCase();
			String strValues = strHeader.substring(ndxHeader+1,strHeader.length()).trim();
			StringTokenizer st = new StringTokenizer(strValues,",");
			while( st.hasMoreTokens() )
			{
				String strValue = st.nextToken();
				request.addHeader(strName,strValue);
			}
		}
		
		// Parse up the "Content-Type" header
		// e.g. Content-Type: text/html; charset=ISO-8859-4
		String strContentTypeHeader = request.getHeader("content-type");
		if( strContentTypeHeader!=null )
		{
			// Parse up text/html; charset=UTF-8
			StringTokenizer stContentParameters = new StringTokenizer(strContentTypeHeader,";");
			request.setContentType(stContentParameters.nextToken().trim());
			if( stContentParameters.hasMoreTokens() )
			{
				String strContentParam=stContentParameters.nextToken();
				if( strContentParam.startsWith("charset") )
				{
					request.setCharacterEncoding(strContentParam.substring(9));
				} // strContentParam.startsWith("charset")			
			} // stContentParameters.hasMoreTokens()
			
		} // strContentType!=null
		
		chain.doFilter(request,response);
	}
}
