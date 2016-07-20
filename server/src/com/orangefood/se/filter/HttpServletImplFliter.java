package com.orangefood.se.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;

public abstract class HttpServletImplFliter implements Filter
{
	public void init(FilterConfig filterconfig) throws ServletException {}
	public void destroy() {}
	
	public abstract void doFilter(HttpServletRequestImpl request, HttpServletResponseImpl response, FilterChain chain)
		throws IOException, ServletException;
		
	public void doFilter( ServletRequest request, ServletResponse response,FilterChain chain)
		throws IOException, ServletException
	{
		if( request instanceof HttpServletRequestImpl &&
			response instanceof HttpServletResponseImpl )
		{
			doFilter((HttpServletRequestImpl)request,(HttpServletResponseImpl)response,chain);
		}
		else
		{
			chain.doFilter(request,response);
		}
	}
	
	

}
