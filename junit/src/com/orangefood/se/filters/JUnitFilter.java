/*
 * Created on Feb 25, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.orangefood.se.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class JUnitFilter implements Filter
{
	String _strComment="default comment"; 
	public void destroy() {}
	
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain filterchain)
		throws IOException, ServletException
	{
		PrintWriter w = response.getWriter();
		w.println("<!-- JUNIT TEST FLITER: " + _strComment + "-->");
		w.flush();
		filterchain.doFilter(request,response);
	}
	public void init(FilterConfig filterconfig) throws ServletException
	{
		_strComment = filterconfig.getInitParameter("comment");
	}
}
