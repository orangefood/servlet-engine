package com.orangefood.se.http.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;


public class HttpSessionImpl implements HttpSession
{
	private int _nMaxInactiveInterval;
	private Hashtable _htAttributes = new Hashtable();
	private long _lCreationTime = System.currentTimeMillis();
	private long _lLastAccessTime = System.currentTimeMillis();
	private String _strId=null;
	private boolean _bNew=true;
	private HttpSessionAttributeListener[] _aHttpSessionAttributeListener; 
	
	private transient ServletContext _sc;
	private transient SessionManager _sm;
	
	public HttpSessionImpl(String strId)
	{
		_strId = strId;
	}
	
	public void setSessionManager(SessionManager sm) { _sm=sm; }
	public void setHttpSessionAttributeListeners(HttpSessionAttributeListener[] ahtsal) {_aHttpSessionAttributeListener=ahtsal;}
	
	
	public String getId() { return _strId; }
	
	//
	// Attribute methods
	//
	public void setAttribute(String string, Object object) 
	{ 
		Object objOld=_htAttributes.put(string,object);
		if( objOld!=null )
		{
			_notifyAttributeReplaced(string,objOld);
			if( objOld instanceof HttpSessionBindingListener )
			{
				HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,string);
				((HttpSessionBindingListener)objOld).valueUnbound(sbe);			
			}
						
		}
		_notifyAttributeAdded(string,object);
		if( object instanceof HttpSessionBindingListener )
		{
			HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,string);
			((HttpSessionBindingListener)object).valueBound(sbe);
		}
	}
	public Object getAttribute(String string) { return _htAttributes.get(string); }
	public Enumeration getAttributeNames() { return _htAttributes.keys(); }
	public void removeAttribute(String string) 
	{ 
		Object object=_htAttributes.remove(string);
		if( object!=null )
		{
			_notifyAttributeRemoved(string,object);
			if( object instanceof HttpSessionBindingListener )
			{
				HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,string);
				((HttpSessionBindingListener)object).valueUnbound(sbe);
			}	
		}
	
	}
	
	private void _notifyAttributeAdded(String strAttribute, Object objValue) 
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,strAttribute,objValue);
			_aHttpSessionAttributeListener[n].attributeAdded(sbe);
		}
	}
	
	private void _notifyAttributeRemoved(String strAttribute, Object objValue)  
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,strAttribute,objValue);
			_aHttpSessionAttributeListener[n].attributeRemoved(sbe);
		}
	}
	
	private void _notifyAttributeReplaced(String strAttribute, Object objValue)  
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,strAttribute,objValue);
			_aHttpSessionAttributeListener[n].attributeReplaced(sbe);
		}
	}
	
	public long getCreationTime() { return _lCreationTime; }
	
	public long getLastAccessedTime() { return _lLastAccessTime; }
	public void accessed() { _lLastAccessTime = System.currentTimeMillis(); }

	public int getMaxInactiveInterval() { return _nMaxInactiveInterval; }
	public void setMaxInactiveInterval(int n) { _nMaxInactiveInterval=n; }

	public void setServletContext(ServletContext sc) { _sc = sc; }
	public ServletContext getServletContext() { return _sc; }
	
	public void invalidate()
	{
		Iterator it = _htAttributes.entrySet().iterator();
		while( it.hasNext() )
		{
			Map.Entry me = (Map.Entry)it.next();
			Object object = me.getValue();
			if( object!=null && object instanceof HttpSessionBindingListener)
			{
				HttpSessionBindingEvent sbe = new HttpSessionBindingEvent(this,(String)me.getValue());
				((HttpSessionBindingListener)object).valueUnbound(sbe);
			}
		}
		_htAttributes.clear();
		_sm.remove(this);
	}
	
	public boolean isNew() { return _bNew; }
	public void old() { _bNew=false; }
	

	// Depricated methods
	public HttpSessionContext getSessionContext()
	{
		return new HttpSessionContext()
		{
			public Enumeration getIds() { return (new Vector()).elements(); }
			public HttpSession getSession(String string) { return null; }
		};
		
	}
	
	public void putValue(String string, Object object) { setAttribute(string,object); }
	public Object getValue(String string) { return getAttribute(string); }
	public void removeValue(String string) { removeAttribute(string); }
	public String[] getValueNames()
	{
		Enumeration enumNames = _htAttributes.keys();
		ArrayList al = new ArrayList();
		while( enumNames.hasMoreElements() )
		{
			al.add(enumNames.nextElement());
		}
		return (String[])al.toArray(new String[0]);
	}
	
}
