package com.orangefood.se.http.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.orangefood.se.servlet.ServletContextImpl;
import com.orangefood.se.servlet.ServletInputStreamImpl;
import com.orangefood.se.util.DateFormats;

public class HttpServletRequestImpl implements HttpServletRequest
{
	private int _nReadMethod = 0;
	public static final int INPUT_STREAM=1;
	public static final int READER=2;
	private ServletInputStreamImpl _sis;
	private BufferedReader _br;
	
	private Vector _vLocale = new Vector();
	private Locale _locale = Locale.getDefault();
	
	private String _strProto;
	private String _strMethod;
	private boolean _bSecure;
	private String _strScheme;
	
	// RequestURI
	private String _strRequestURI;
	
	// Paths
	private String _strServletPath;
	private String _strPathInfo;
	
	// Headers
	private Hashtable _htHeaders = new Hashtable();
	
	// Session
	private SessionManager _sm;
	private String _strRequestedSessionId=null;
	private String _strSessionId=null;
	
	// Cookies
	private Cookie[] _acookies = null;
	
	// Content Types
	private String _strCharEnc = null; 
	private String _strContentType = null;	
	
	// Parameters
	private String _strQueryString="";
	private Properties _propParams = new Properties();
	private Hashtable _htParams = new Hashtable();

	// Attributes
	private Hashtable _htAttributes = new Hashtable();
	
	// Context
	private ServletContextImpl _sc;
	
	// Socket
	private Socket _sock;
	
	public HttpServletRequestImpl(Socket sock, ServletContextImpl sc) throws IOException
	{
		_sock = sock;
		_sc = sc;
		_sis = new ServletInputStreamImpl(sock.getInputStream());

		// Create a buffer
		byte[] abyBuffer = new byte[1024];
		// Read the request line
		int nRequestLen = _sis.readLine(abyBuffer);
		if( nRequestLen==-1 )
		{
			throw new IllegalStateException("Socket is closed");
		}
		StringTokenizer stRequest = new StringTokenizer(new String(abyBuffer,0,nRequestLen));
		_strMethod = stRequest.nextToken();
		_strRequestURI = stRequest.nextToken();
		_strProto = stRequest.nextToken();
		
		// Read the query string
		int ndxQuery= _strRequestURI.indexOf('?');
		if( ndxQuery>-1 )
		{
			_strQueryString = _strRequestURI.substring(ndxQuery+1);
			_strRequestURI = _strRequestURI.substring(0,ndxQuery);
			addParameters(_strQueryString);
		}
	}

	public String getProtocol() { return _strProto; }
	public String getMethod() { return _strMethod; }
	public boolean isSecure() { return "https".equals(_strScheme); }
	public String getScheme() { return _strScheme; }
	public void setScheme(String strScheme) { _strScheme = strScheme; }
	// TODO Get server name from headers, then from the servlet config
	public String getServerName() {	return _sc.getServerName(); }
	public int getServerPort() { return _sock.getLocalPort(); }

	//
	// Header methods
	//
	public void addHeader(String key, String value)
	{
		key=key.toLowerCase();
		Vector v = (Vector)_htHeaders.get(key.toLowerCase());
		if( v==null )
		{
			v=new Vector();
			_htHeaders.put(key,v);
		}
		v.add(value);
	}
	
	public String getHeader(String key) 
	{ 
		String strHeader = null;
		Enumeration enumHeaders = getHeaders(key);
		if( enumHeaders.hasMoreElements() )
		{
			strHeader = (String)enumHeaders.nextElement();
		}
		return strHeader; 
	}
	public Enumeration getHeaderNames() 
	{ 
		return _htHeaders.keys(); 
	}
	public Enumeration getHeaders(String key) 
	{ 
		Vector v = (Vector)_htHeaders.get(key.toLowerCase());
		if( v==null )
		{
			v=new Vector();
		}
		return v.elements(); 
	}

	public long getDateHeader(String string) 
	{ 
		long lDate=-1;
		String strDate = getHeader(string);
		if( strDate != null )
		{
			Date d = new Date(-1);
			try
			{
				d = DateFormats.RFC_1123_DATE.parse(strDate);
			}
			catch(ParseException pe)
			{
				try
				{
					d = DateFormats.RFC_1036_DATE.parse(strDate);
				}
				catch(ParseException pe1)
				{
					try
					{
						d = DateFormats.ANSI_C_DATE.parse(strDate);
					}
					catch(ParseException pe2) {}
				}				
			}
			lDate = d.getTime();
		}
		return lDate; 
	}
	
	public int getIntHeader(String string) 
	{ 
		int n=-1;
		String strInt = getHeader(string);
		if( strInt!=null )
		{
			n=Integer.parseInt(strInt);
		}
		return n;
	}

	//
	// Cookie methods
	//
	public void setCookie(Cookie[] acookie) { _acookies=acookie; }
	public Cookie[] getCookies() { return _acookies; }
	
	// 
	// Path methods
	// TODO
	public String getContextPath() { return _sc.getContextPath(); }
	public String getServletPath() { return _strServletPath; }	
	public void setServletPath(String strServletPath) { _strServletPath=strServletPath; }
	public String getPathInfo()	{ return _strPathInfo; }
	public void setPathInfo(String strPathInfo) { _strPathInfo=strPathInfo; }
	public String getPathTranslated() { return _sc.getRealPath(_strPathInfo); }
	public String getRealPath(String string) { return _sc.getRealPath(string); }

	//
	// URL/URI methods
	//
	public String getRequestURI() { return _strRequestURI; }
	// TODO
	public StringBuffer getRequestURL() 
	{
		StringBuffer sb = new StringBuffer();
		String strScheme = getScheme();
		sb.append(strScheme);
		sb.append("://");
		sb.append(this.getServerName());
		int nPort = this.getServerPort();
		if( (nPort!=80 && "http".equals(strScheme)) ||
			(nPort!=443 && "https".equals(strScheme)) )
		{
			sb.append(':');
			sb.append(nPort);
		}
		sb.append(getRequestURI());
		if( _strQueryString.length()>0 )
		{
			sb.append('?');
			sb.append(_strQueryString);
		}
		
		return sb; 
	}

	// 
	// Session methods
	// TODO
	public void setSessionManager(SessionManager sm) { _sm = sm; }
	public void setRequestSessionId(String strSessionId) { _strRequestedSessionId=strSessionId; }
	public void setSessionId(String strSessionId) { _strSessionId = strSessionId; }
	
	public HttpSession getSession() { return getSession(true); }
	public HttpSession getSession(boolean bCreate) 
	{ 
		HttpSession session = null;
		if( _sm!=null )
		{	
			session = _sm.getSession(this,_strSessionId,bCreate);
		}
		return session; 
	}
	 
	public String getRequestedSessionId() { return _strRequestedSessionId; }
	public boolean isRequestedSessionIdValid() 
	{ 
		boolean bValid=false;
		if( _sm!=null )
		{	
			bValid = _sm.isRequestedSessionIdValid(_strRequestedSessionId);
		}
		return bValid; 
	}
	// Only support cookie based sessions
	public boolean isRequestedSessionIdFromCookie() { return true; }
	public boolean isRequestedSessionIdFromURL() { return false; }
	public boolean isRequestedSessionIdFromUrl() { return false; }
	
	
	//
	// Auth methods
	// TODO
	public String getAuthType() { return null; }
	public String getRemoteUser() {	return null; }
	public Principal getUserPrincipal() { return null; }	
	public boolean isUserInRole(String string) { return false; }

	//
	// Attribute methods
	//
	public Object getAttribute(String strKey) { return _htAttributes.get(strKey); }
	public Enumeration getAttributeNames() { return _htAttributes.keys(); }
	public void removeAttribute(String strKey) { _htAttributes.remove(strKey); }
	public void setAttribute(String strKey, Object obj) { _htAttributes.put(strKey,obj); }

	//
	// Parameter methods
	// 
	public void addParameters(String strParams)
	{
		StringTokenizer stParameters = new StringTokenizer(strParams,"&");
		while( stParameters.hasMoreTokens() )
		{
			String strParameter = stParameters.nextToken();
			int ndxParam = strParameter.indexOf('=');
			String strName = strParameter.substring(0,ndxParam);
			String strValue = strParameter.substring(ndxParam+1);
			addParameter(strName,strValue);
		}
	}	
	
	public void addParameter(String strName, String strValue)
	{
		String[] astrValues = (String[])_htParams.get(strName);
		String[] astrNewValues = null;
		if( astrValues==null )
		{
			astrNewValues= new String[] { strValue };
		}
		else
		{
			astrNewValues=new String[astrValues.length+1];
			System.arraycopy(astrValues,0,astrNewValues,0,astrValues.length);
			astrNewValues[astrValues.length]=strValue;
		}
		_htParams.put(strName,astrNewValues);
	}
	public String getQueryString() { return _strQueryString; }
	public Enumeration getParameterNames() { return _htParams.keys(); }
	public String getParameter(String string) 
	{ 
		String[] astr=getParameterValues(string);
		if( astr==null )
		{
			return null;
		}
		return astr[0]; 
	}
	public String[] getParameterValues(String string) 
	{
		return (String[])_htParams.get(string);
	}
	public Map getParameterMap() 
	{
		HashMap hm = new HashMap();
		hm.putAll(_htParams);
		return hm; 
	}
	
	//
	// Content methods
	// TODO
	public void setCharacterEncoding(String string)
		throws UnsupportedEncodingException
	{
		_strCharEnc=string;
	}
	
	public int getContentLength() 
	{ 
		return getIntHeader("content-length"); 
	}
	public String getCharacterEncoding() { return _strCharEnc; }
	public void setContentType(String strContentType) { _strContentType = strContentType; }
	public String getContentType() { return _strContentType; }
	
	//
	// Stream methods
	//
	public ServletInputStream getInputStream() throws IOException 
	{
		if( _nReadMethod==READER )
		{
			throw new IllegalStateException();
		}
		_nReadMethod=INPUT_STREAM;
		return _sis;
	}
	
	public BufferedReader getReader() throws IOException
	{
		if( _nReadMethod==INPUT_STREAM )
		{
			throw new IllegalStateException();
		}
		if( _br == null )
		{
			_br = new BufferedReader(new InputStreamReader(_sis));
		}
		_nReadMethod=READER;
		return _br;
	}

	//
	// Locale methods
	//
	public Locale getLocale() { return (Locale)_vLocale.elementAt(0); }
	public Enumeration getLocales() { return _vLocale.elements(); }

	//
	// Remote methods
	//
	public String getRemoteAddr() 
	{ 
		InetAddress addr = _sock.getInetAddress();
		return addr.getHostAddress();
	}
	public String getRemoteHost()
	{ 
		InetAddress addr = _sock.getInetAddress();
		return addr.getHostName();
	}

	public RequestDispatcher getRequestDispatcher(String string)
	{
		return _sc.getRequestDispatcher(string);
	}
}
