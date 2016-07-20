package com.orangefood.se.http.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SessionManager
{
	public void remove(HttpSession session);
	public HttpSession getSession(HttpServletRequest request, String strSessionId, boolean bCreate);
	public boolean isRequestedSessionIdValid(String strSessionId);
}
