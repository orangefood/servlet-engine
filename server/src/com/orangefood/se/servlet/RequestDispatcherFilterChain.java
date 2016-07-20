package com.orangefood.se.servlet;

import javax.servlet.FilterChain;

public interface RequestDispatcherFilterChain extends FilterChain
{
	public RequestDispatcherImpl getRequestDispatcher();
}
