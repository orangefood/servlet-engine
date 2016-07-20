package com.orangefood.se.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.orangefood.se.http.servlet.HttpServletRequestImpl;

public class RequestDispatcherImpl implements RequestDispatcher
{
	private Servlet _servlet;
	private String _strPathInfo;
	private String _strServletPath;
	
	
	public RequestDispatcherImpl(Servlet servlet)
	{
		_servlet = servlet;
	}
	
	public void service(ServletRequest request, ServletResponse response)
		throws ServletException, IOException
	{
		((HttpServletRequestImpl)request).setServletPath(_strServletPath);
		((HttpServletRequestImpl)request).setPathInfo(_strPathInfo);
		_servlet.service(request,response);
	}
	
	public void forward( ServletRequest request, ServletResponse response)
		throws ServletException, IOException
	{
		response.reset();
		_servlet.service(request,response);
	}
	

	public void include( ServletRequest request, ServletResponse response)
		throws ServletException, IOException
	{
		_servlet.service(request,response);
	}

	public String getServletName() { return _servlet.getServletConfig().getServletName(); }
	public String getPathInfo() { return _strPathInfo; }
	public void setPathInfo(String pathInfo) { _strPathInfo = pathInfo; }
	public String getServletPath() { return _strServletPath; }
	public void setServletPath(String servletPath) { _strServletPath = servletPath; }

}
