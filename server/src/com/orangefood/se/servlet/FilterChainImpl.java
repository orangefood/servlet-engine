package com.orangefood.se.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FilterChainImpl implements RequestDispatcherFilterChain
{
	private Filter _filter;
	private RequestDispatcherFilterChain _filterChain;
	
	public FilterChainImpl(Filter filter, RequestDispatcherFilterChain filterChain)
	{
		_filter=filter;
		_filterChain=filterChain;
	}
	
	public RequestDispatcherImpl getRequestDispatcher() { return _filterChain.getRequestDispatcher(); }
	
	public void doFilter( ServletRequest request, ServletResponse response )
		throws IOException, ServletException
	{
		_filter.doFilter(request,response,_filterChain);
	}
}
