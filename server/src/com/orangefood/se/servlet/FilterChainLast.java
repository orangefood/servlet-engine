package com.orangefood.se.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FilterChainLast implements RequestDispatcherFilterChain
{
	RequestDispatcherImpl _rd;
	public FilterChainLast(RequestDispatcherImpl rd)
	{
		_rd=rd;
	}
	
	public RequestDispatcherImpl getRequestDispatcher() { return _rd; }
	 
	public void doFilter(ServletRequest request, ServletResponse response)
		throws IOException, ServletException
	{
		_rd.service(request,response);
	}
}
