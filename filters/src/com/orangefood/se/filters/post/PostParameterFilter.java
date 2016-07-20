package com.orangefood.se.filters.post;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;

import com.orangefood.se.filter.HttpServletImplFliter;
import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;

public class PostParameterFilter extends HttpServletImplFliter
{
	private int _nMaxPost=8192; // Maximum post size 8k
	
	public void init(FilterConfig config) throws ServletException 
	{
		String strMaxPost = config.getInitParameter("max-post-size");
		if( strMaxPost!=null )
		{
			_nMaxPost=Integer.parseInt(strMaxPost);
		}
	}
	
	public void doFilter(
		HttpServletRequestImpl request,
		HttpServletResponseImpl response,
		FilterChain chain)
		throws IOException, ServletException
	{
		if( "POST".equals(request.getMethod()) && "application/x-www-form-urlencoded".equals(request.getContentType()))
		{
			ServletInputStream sis = request.getInputStream();
			
			int n=request.getContentLength();
			if( n>0 && n<_nMaxPost)
			{
				byte[] aby = new byte[n];
				sis.read(aby);
				request.addParameters(new String(aby));
			}
		}
		chain.doFilter(request,response);
	}
}
