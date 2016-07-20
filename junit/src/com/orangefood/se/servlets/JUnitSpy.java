package com.orangefood.se.servlets;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JUnitSpy extends HttpServlet
{
	protected void service (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		PrintWriter pw = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/xml");
		pw.println("<junit-spy>");
		pw.println("\t<date>"+new Date()+"</date>");

		pw.println("\t<request>");
		pw.println("\t\t<serverName>"+request.getServerName()+"</serverName>");
		pw.println("\t\t<serverPort>"+request.getServerPort()+"</serverPort>");
		pw.println("\t\t<method>"+request.getMethod()+"</method>");
		pw.println("\t\t<scheme>"+request.getScheme()+"</scheme>");
		pw.println("\t\t<secure>"+request.isSecure()+"</secure>");
		pw.println("\t\t<requestURI><![CDATA["+request.getRequestURI()+"]]></requestURI>");
		pw.println("\t\t<requestURL><![CDATA["+request.getRequestURL()+"]]></requestURL>");
		pw.println("\t\t<servletPath>"+request.getServletPath()+"</servletPath>");
		pw.println("\t\t<pathInfo>"+request.getPathInfo()+"</pathInfo>");
		pw.println("\t\t<pathTranslated>"+request.getPathTranslated()+"</pathTranslated>");
		pw.println("\t\t<contentLength>"+request.getContentLength()+"</contentLength>");
		pw.println("\t\t<contentType>"+request.getContentType()+"</contentType>");
		pw.println("\t\t<characterEncoding>"+request.getCharacterEncoding()+"</characterEncoding>");
		pw.println("\t\t<contextPath>"+request.getContextPath()+"</contextPath>");
		pw.println("\t\t<remoteAddr>"+request.getRemoteAddr()+"</remoteAddr>");
		pw.println("\t\t<remoteHost>"+request.getRemoteHost()+"</remoteHost>");
		pw.println("\t\t<remoteUser>"+request.getRemoteUser()+"</remoteUser>");
		pw.println("\t\t<requestedSessionId>"+request.getRequestedSessionId()+"</requestedSessionId>");
		pw.println("\t\t<requestedSessionIdValid>"+request.isRequestedSessionIdValid()+"</requestedSessionIdValid>");
		pw.println("\t\t<requestedSessionIdFromURL>"+request.isRequestedSessionIdFromURL()+"</requestedSessionIdFromURL>");
		pw.println("\t\t<requestedSessionIdFromCookie>"+request.isRequestedSessionIdFromCookie()+"</requestedSessionIdFromCookie>");
		pw.println("\t\t<headers>");
		Enumeration enumHeaders = request.getHeaderNames();
		while( enumHeaders.hasMoreElements() )
		{
			String strHeaderName = (String) enumHeaders.nextElement();
			pw.println("\t\t\t<header name=\""+strHeaderName+"\">");
			Enumeration enumValues = request.getHeaders(strHeaderName);
			while( enumValues.hasMoreElements() )
			{
				pw.println("\t\t\t\t<value>"+enumValues.nextElement()+"</value>");	
			}
			pw.println("\t\t\t</header>");
		}
		pw.println("\t\t</headers>");
		pw.println("\t\t<parameters>");
		Map m = request.getParameterMap();
		Iterator it = m.entrySet().iterator();
		while( it.hasNext() )
		{
			Map.Entry me = (Map.Entry)it.next();
			pw.println("\t\t\t<parameter name=\""+me.getKey()+"\">");
			String[] astr = (String[])me.getValue();
			for( int n=0;n<astr.length;n++ )
			{
				pw.println("\t\t\t\t<value>"+astr[n]+"</value>");	
			}
			pw.println("\t\t\t</parameter>");
		}
		pw.println("\t\t</parameters>");
		pw.println("\t\t<cookies>");
		Cookie[] acookies = request.getCookies();
		int nRequestCount = 0;
		if( acookies!=null )
		{	
			for( int nCookie=0; nCookie<acookies.length; nCookie++ )
			{
				String strName = acookies[nCookie].getName();
				
				if("junit-request".equals(strName))
				{
					nRequestCount = Integer.parseInt(acookies[nCookie].getValue())+1;
				}
				pw.println("\t\t\t<cookie>");
				pw.println("\t\t\t\t<name>"+acookies[nCookie].getName()+"</name>");
				pw.println("\t\t\t\t<value>"+acookies[nCookie].getValue()+"</value>");
				pw.println("\t\t\t</cookie>");
			}
		}
		response.addCookie(new Cookie("junit-request", String.valueOf(nRequestCount)));
		pw.println("\t\t</cookies>");
		
		pw.println("\t</request>");

		ServletContext sc = getServletContext();
		pw.println("\t<servletContext>");
		pw.println("\t\t<servletContextName>"+sc.getServletContextName()+"</servletContextName>");
		pw.println("\t\t<serverinfo>"+sc.getServerInfo()+"</serverinfo>");
		pw.println("\t\t<majorVersion>"+sc.getMajorVersion()+"</majorVersion>");		
		pw.println("\t\t<minorVersion>"+sc.getMinorVersion()+"</minorVersion>");
		pw.println("\t\t<realpath>"+sc.getRealPath("/")+"</realpath>");
		pw.println("\t\t<resource>"+sc.getResource("/")+"</resource>");
		pw.println("\t\t<mime-types>");
		pw.println("\t\t\t<html>"+sc.getMimeType("test.html")+"</html>");
		pw.println("\t\t\t<txt>"+sc.getMimeType("test.txt")+"</txt>");
		pw.println("\t\t\t<gif>"+sc.getMimeType("test.gif")+"</gif>");
		pw.println("\t\t\t<jpg>"+sc.getMimeType("test.jpg")+"</jpg>");
		pw.println("\t\t\t<png>"+sc.getMimeType("test.png")+"</png>");
		pw.println("\t\t</mime-types>");
		pw.println("\t\t<resourcePaths>");
		Set s = sc.getResourcePaths("/");
		Iterator itResourcePaths = s.iterator();
		while( itResourcePaths.hasNext() )
		{
			pw.println("\t\t\t<path>"+itResourcePaths.next()+"</path>");
		}
		pw.println("\t\t</resourcePaths>");
		pw.println("\t</servletContext>");
		
		// Spit out the body unless this is a POST'ed form
		if( "POST".equals(request.getMethod()) && !"application/x-www-form-urlencoded".equals(request.getContentType()))
		{
			pw.println("\t<body><![CDATA[");
			int nContentLength = request.getContentLength();
			InputStream is = request.getInputStream();
			for( int n=0; n<nContentLength; n++)
			{
				pw.print((char)is.read());
			}
			pw.println("\t]]></body>");
		}

		pw.println("</junit-spy>");
	}
}
