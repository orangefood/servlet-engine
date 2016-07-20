package com.orangefood.se.servlet;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletConfigImpl implements ServletConfig
{
	private Properties _propInit;
	private String _strName; 
	private ServletContext _sc;
	
	public ServletConfigImpl(String strName, ServletContext sc, Properties propInit)
	{
		_propInit = propInit; 
		_strName = strName;
		_sc = sc;
	}
	
	public String getInitParameter(String strKey) { return _propInit.getProperty(strKey); }
	public Enumeration getInitParameterNames() { return _propInit.keys(); }
	public ServletContext getServletContext() { return _sc; }
	
	public void setServletName(String strName) { _strName = strName; }
	public String getServletName() { return _strName; }
}
