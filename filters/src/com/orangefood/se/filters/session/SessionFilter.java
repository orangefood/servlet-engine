package com.orangefood.se.filters.session;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.orangefood.se.filter.HttpServletImplFliter;
import com.orangefood.se.http.servlet.*;
import com.orangefood.se.http.servlet.HttpServletRequestImpl;
import com.orangefood.se.http.servlet.HttpServletResponseImpl;
import com.orangefood.se.http.servlet.HttpSessionImpl;
import com.orangefood.se.servlet.ServletContextImpl;
import com.orangefood.se.util.Base64;

public class SessionFilter extends HttpServletImplFliter implements SessionManager
{
	private Random random = new Random(System.currentTimeMillis());
	private Hashtable _htSessions = new Hashtable();
	private String _strSessionCookieName = "JSESSIONID";
	private String _strDomain = null;
	private HttpSessionListener[] _aHttpSessionListener = null;
	private HttpSessionAttributeListener[] _aHttpSessionAttributeListener = null;	
	private int _nTimeOut = 1800; // 30 minutes
	private int _nInvalidationFrequency = 60; // 60 seconds
	private ServletContextImpl _sc;
	
	public void remove(HttpSession session)
	{
		_htSessions.remove(session.getId());
		_notifySessionDestroyed(session);
	}
	
	public void init(FilterConfig fc) throws ServletException
	{
		String strSessionCookieName = fc.getInitParameter("session-cookie");
		if( strSessionCookieName!=null )
		{
			_strSessionCookieName=strSessionCookieName;
		}

		String strInvalidationFrequency = fc.getInitParameter("invalidation-frequency");
		if( strInvalidationFrequency!=null )
		{
			_nInvalidationFrequency=Integer.parseInt(strInvalidationFrequency);
		}
		
		_strDomain = fc.getInitParameter("domain");
		
		_sc=(ServletContextImpl)fc.getServletContext();
		_nTimeOut=_sc.getSessionTimeout();
		_aHttpSessionListener=_sc.getHttpSessionListeners();
		_aHttpSessionAttributeListener=_sc.getHttpSessionAttributeListeners();
		
		SessionInvalidationThread sit = new SessionInvalidationThread();
		sit.start();
	}

	public void doFilter( HttpServletRequestImpl request, HttpServletResponseImpl response, FilterChain chain )
		throws IOException, ServletException
	{
		HttpSessionImpl session = null;
		String strRequestedSessionId = null;
		String strSessionId = null;
			
		Cookie[] cookies = request.getCookies();
		
		if( cookies!=null )
		{	
			for( int n=0; strSessionId==null && n<cookies.length; n++)
			{
				if( _strSessionCookieName.equals(cookies[n].getName()) )
				{
					strRequestedSessionId = cookies[n].getValue();
					if( _isOurCookie(strRequestedSessionId) )
					{	
						strSessionId = strRequestedSessionId;
					}
				}
			}
		}
		request.setSessionManager(this);
		request.setRequestSessionId(strRequestedSessionId);
		
		// If there is a requested session  and
		// it exists the make it old and update 
		// the last activity time
		if( strSessionId!=null )
		{
			// lookup a session
			session = (HttpSessionImpl)_htSessions.get(strSessionId);
			if( session!=null )
			{
				session.old();
				session.accessed();
			}
		}
		else
		{
			// Create a phantom session
			// put a valid session id on the client
			// but don't create the session
			strSessionId = _createSessionId(request);
			Cookie cookie = new Cookie(_strSessionCookieName,strSessionId);
			cookie.setPath(request.getServletPath());
			if( _strDomain != null )
			{
				cookie.setDomain(_strDomain);
			}
			((HttpServletResponse)response).addCookie(cookie);
		}
		request.setSessionId(strSessionId);

		chain.doFilter(request,response);
	}
	
	private boolean _isOurCookie(String strValue)
	{
		boolean bOurs = false;
		if( strValue!=null )
		{	
			byte[] aby=Base64.decode(strValue.getBytes());
			if( aby.length > 8 )
			{
				// Get the length
				int nIdLen = aby[0] << 24;
				nIdLen |= (aby[1] << 16);
				nIdLen |= (aby[2] << 8);
				nIdLen |= (aby[3]);
	
				// Get the session string
				byte[] abyId = new byte[nIdLen];
				System.arraycopy(aby,4,abyId,0,nIdLen);
	
				// Get the hash
				int nOff = nIdLen+4;
				int nIdHash = (aby[nOff] << 24);
				nIdHash |= ((aby[nOff+1] << 16)&0x00ff0000);
				nIdHash |= ((aby[nOff+2] <<  8)&0x0000ff00);
				nIdHash |= ((aby[nOff+3])      &0x000000ff);
				String strId = new String(abyId);
				bOurs = (nIdHash==strId.hashCode());
			}
		}
		return bOurs;
	}
	
	private String _createSessionId(HttpServletRequest req)
		throws IOException
	{
		String strId = "of:"+req.getRemoteAddr()+":"+random.nextLong();
		int nIdHash = strId.hashCode();
		
		byte[] abyId = strId.getBytes();
		int nIdLen = abyId.length;
		byte[] aby = new byte[nIdLen+8];
		aby[0] = (byte) ( nIdLen >>> 24);
		aby[1] = (byte) ((nIdLen >>> 16) & 0xff);
		aby[2] = (byte) ((nIdLen >>>  8) & 0xff);
		aby[3] = (byte) ((nIdLen       ) & 0xff);
		
		System.arraycopy(abyId,0,aby,4,nIdLen);

		int nOff = nIdLen+4;
		aby[nOff]   = (byte) ( nIdHash >>> 24);
		aby[nOff+1] = (byte) ((nIdHash >>> 16) & 0xff);
		aby[nOff+2] = (byte) ((nIdHash >>>  8) & 0xff);
		aby[nOff+3] = (byte) ((nIdHash       ) & 0xff);

		byte[] abyEncoded = Base64.encode(aby);
		return new String(abyEncoded);
	}
	
	public void destroy() 
	{
		// TODO remove all the session and notify the listeners
	}
	
	public HttpSession getSession(HttpServletRequest request, String strSessionId, boolean bCreate)
	{
		HttpSessionImpl session = (HttpSessionImpl)_htSessions.get(strSessionId);
		
		if( session==null && bCreate )
		{
			// create a session
			session = new HttpSessionImpl(strSessionId);
			session.setSessionManager(this);
			session.setServletContext(_sc);
			session.setHttpSessionAttributeListeners(_aHttpSessionAttributeListener);
			session.setMaxInactiveInterval(_nTimeOut);
			_notifySessionCreated(session);
			_htSessions.put(strSessionId,session);
		}
		return session;
	}
	
	public boolean isRequestedSessionIdValid(String strSessionId)
	{
		if( strSessionId!=null )
		{	
			return _htSessions.containsKey(strSessionId);
		}
		return false;
	}
	
	private void _notifySessionCreated(HttpSession session)
	{
		for(int n=0; n<_aHttpSessionListener.length; n++)
		{
			HttpSessionEvent hse = new HttpSessionEvent(session);
			_aHttpSessionListener[n].sessionCreated(hse);
		}
	}
	
	private void _notifySessionDestroyed(HttpSession session) 
	{
		for(int n=0; n<_aHttpSessionListener.length; n++)
		{
			HttpSessionEvent hse = new HttpSessionEvent(session);
			_aHttpSessionListener[n].sessionDestroyed(hse);
		}	
	}
	
	private class SessionInvalidationThread extends Thread
	{
		private SessionInvalidationThread()
		{
			this.setDaemon(true);
		}
		
		public void run()
		{
			while(true)
			{	
				long lNow = System.currentTimeMillis();
				Iterator it = _htSessions.values().iterator();
				while( it.hasNext() )
				{
					HttpSession session = (HttpSession)it.next();
					if( lNow>session.getLastAccessedTime()+(session.getMaxInactiveInterval()*1000) )
					{
						session.invalidate();
					}
				}
				try
				{
					Thread.sleep(_nInvalidationFrequency*1000);
				}
				catch (InterruptedException e) {}
			}
		}
	}
}
