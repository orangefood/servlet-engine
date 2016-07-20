package com.orangefood.se.filters.cookie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.orangefood.se.filter.HttpServletImplFliter;
import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;

public class CookieFilter extends HttpServletImplFliter
{
	public void doFilter(HttpServletRequestImpl request, HttpServletResponseImpl response, FilterChain chain)
		throws IOException, ServletException
	{
		//
		// Parse the cookies
		//
		Enumeration enumCookies = request.getHeaders("Cookie");
		ArrayList al = new ArrayList();
		while( enumCookies.hasMoreElements() )
		{
			String strCookies=(String)enumCookies.nextElement();
			StringTokenizer st=new StringTokenizer(strCookies,";");
			while( st.hasMoreTokens() )
			{
				String strCookie = st.nextToken().trim();
				int ndx = strCookie.indexOf('=');
				String strName = strCookie.substring(0,ndx);
				String strValue = strCookie.substring(ndx+1);
				Cookie cookie = new Cookie(strName,strValue);
				al.add(cookie);
			}
		}
		if( al.size()>0 )
		{
			request.setCookie((Cookie[])al.toArray(new Cookie[0]));
		}
		chain.doFilter(request,response);
	}
}
