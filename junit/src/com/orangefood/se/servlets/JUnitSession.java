package com.orangefood.se.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JUnitSession extends HttpServlet
{
	protected void service (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		HttpSession session = request.getSession(false);
		// Add new name/value pairs if passed in
		String strName = request.getParameter("name");
		String strValue = request.getParameter("value");
		if( strName!=null )
		{
			if( session==null )
			{
				session=request.getSession();
			}
			session.setAttribute(strName,strValue);			
		}
		PrintWriter pw = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/xml");
		pw.println("<junit-session>");
		pw.println("\t<session>");
		if( session!=null )
		{	
			pw.println("\t\t<id>"+session.getId()+"</id>");
			pw.println("\t\t<new>"+session.isNew()+"</new>");
			pw.println("\t\t<creationTime>"+session.getCreationTime()+"</creationTime>");
			pw.println("\t\t<accessedTime>"+session.getLastAccessedTime()+"</accessedTime>");
			pw.println("\t\t<maxInactiveInterval>"+session.getMaxInactiveInterval()+"</maxInactiveInterval>");
			Enumeration enum = session.getAttributeNames();
			pw.println("\t\t<attributes>");
			while( enum.hasMoreElements() )
			{
				String strAttrName = (String)enum.nextElement();
				pw.println("\t\t\t<attribute>");
				pw.println("\t\t\t\t<name>"+strAttrName+"</name>");
				pw.println("\t\t\t\t<value>"+session.getAttribute(strAttrName)+"</value>");
				pw.println("\t\t\t</attribute>");
			}
			pw.println("\t\t</attributes>");
		}
		pw.println("\t</session>");
		pw.println("\t<request>");
		pw.println("\t\t<requestedSessionId>"+request.getRequestedSessionId()+"</requestedSessionId>");
		pw.println("\t\t<requestedSessionIdValid>"+request.isRequestedSessionIdValid()+"</requestedSessionIdValid>");
		pw.println("\t\t<requestedSessionIdFromCookie>"+request.isRequestedSessionIdFromCookie()+"</requestedSessionIdFromCookie>");
		pw.println("\t\t<requestedSessionIdFromURL>"+request.isRequestedSessionIdFromURL()+"</requestedSessionIdFromURL>");
		pw.println("\t</request>");
		pw.println("</junit-session>");
	}
}
