package com.orangefood.se.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import com.orangefood.se.Server;
import com.orangefood.se.http.WebXMLHandler;
import com.orangefood.se.lang.ServletContextClassLoader;
import com.orangefood.xml.Parser;

public class ServletContextImpl implements ServletContext
{
	public static final String TEMP_DIR_PROPERTY="javax.servlet.context.tempdir";
	
	public static final String VERSION = "0.2.0";
	
	public static final int MAJOR_VERSION = 2;
	public static final int MINOR_VERSION = 3;
	
	public static final String SERVER_INFO = "orangefood.com servlet container version 0.0.1";

	private Hashtable _htAttrs = new Hashtable();

	private String _strName = "Default Servlet Context";
	
	private Properties _propInitParams;
	
	private Properties _propFilterClasses = new Properties();
	private ArrayList  _alFilterPatterns = new ArrayList();
	private HashMap _hmFilterInitParams = new HashMap();
	private HashMap _hmFilters = new HashMap();
	
	private Properties _propServletClasses = new Properties();
	private Properties _propServletPatterns = new Properties();
	private HashMap _hmServletInitParams = new HashMap();
	private HashMap _hmServlets = new HashMap();
	private HashMap _hmUnavailableServlets = new HashMap();
	
	private ServletContextListener[] _aServletContextListener = null;
	private ServletContextAttributeListener[] _aServletContextAttributeListener = null;
	private HttpSessionListener[] _aHttpSessionListener = null;
	private HttpSessionAttributeListener[] _aHttpSessionAttributeListener = null;
	
	private Properties _propMimeTypes=new Properties();;
	
	private String[] _astrWelcomFiles = new String[0];
	
	private HashMap _hmErrorPages = null;

	private String _strServerName;
	
	private int _nSessionTimeout;
	
	private File _fileRoot = new File(".");
	private ServletContextClassLoader _sccl = null;
	
	public void init(Properties prop)
	{
		
		String strRoot = prop.getProperty("document-root");
		_sccl = new ServletContextClassLoader(strRoot);
		_fileRoot = new File(strRoot);
		
		_strServerName = prop.getProperty("hostname"); 
		
		//
		// Load the web.xml into the servlet context
		//
		WebXMLHandler webxml = new WebXMLHandler();
		Parser p = new Parser();
		
		try
		{
			InputStream is = Server.getInputStream("web.xml");
			p.parse(is,webxml);
		}
		catch(IOException ioe)
		{
			// TODO Log this exception
			ioe.printStackTrace();
		}
		
		//
		// Load the web.xml from the [docroot]/WEB-INF/web.xml
		//
		File fileRoot = new File(strRoot);
		File fileAppXML = new File(fileRoot,"WEB-INF/web.xml");
		if( fileAppXML.exists() )
		{
			try
			{
				p.parse(new FileInputStream(fileAppXML),webxml);
			}
			catch(IOException ioe)
			{
				// TODO Log this exception
				ioe.printStackTrace();				
			}
		}
		
		_strName=webxml.getDisplayName();
		_propInitParams=webxml.getContextParams();
		
		//
		// Servlet properties
		//
		_propServletClasses=webxml.getServletClasses();
		_propServletPatterns=webxml.getServletPatterns();
		_hmServletInitParams=webxml.getServletInitParams();
		String[] astrStartupServlets = webxml.getLoadOnStartupServlets();
		// Load the load-on-startup servlets
		for( int n=0; n<astrStartupServlets.length; n++ )
		{
			createServlet(astrStartupServlets[n]);
		}
		
		// 
		// Filter properties
		//
		
		_propFilterClasses=webxml.getFilterClasses();
		
		Iterator it = webxml.getFilterPatterns().iterator();
		while( it.hasNext() )
		{
			_alFilterPatterns.add(0,it.next());
		}
		_hmFilterInitParams=webxml.getFilterInitParams();
		
		// Welcome files
		_astrWelcomFiles=(String[])webxml.getWelcomeFiles().toArray(new String[0]);

		// MIME types
		_propMimeTypes=webxml.getMimeTypes();
		
		// Error pages
		_hmErrorPages=webxml.getErrorPages();
		
		// Session timeout
		_nSessionTimeout=webxml.getSessionTimeOut();
		
		// Listeners
		_aServletContextListener = webxml.getServletContextListeners();
		_aServletContextAttributeListener = webxml.getServletContextAttributeListeners();
		_aHttpSessionListener = webxml.getHttpSessionListeners();
		_aHttpSessionAttributeListener = webxml.getHttpSessionAttributeListeners();		

		// Temp directory
		String strTempDir = prop.getProperty(TEMP_DIR_PROPERTY);
		System.err.println("Setting context temporary directory to " + strTempDir );
		File fileTempDir = new File(strTempDir);
		if( !fileTempDir.exists() )
		{
			if( !fileTempDir.mkdirs() )
			{
				// TODO log this
				System.err.println("Unable to create temporary directory " + fileTempDir.getAbsolutePath() );
			}
		}
		if( !fileTempDir.isDirectory() )
		{
			// TODO log this
			System.err.println( fileTempDir.getAbsolutePath() + " is not a directory");
		}
		setAttribute(TEMP_DIR_PROPERTY,fileTempDir);
		
		_notifyContextInitialized();
	}

	public String getContextPath() { return "/"; }
	
	//
	// Context methods
	//
	public int getMajorVersion() { return MAJOR_VERSION; }
	public int getMinorVersion() { return MINOR_VERSION; }
	public String getServerInfo() { return SERVER_INFO; }
	public String getServletContextName() { return _strName; }
	
	/**
	 * Always returns the one and only servlet context and this container
	 * only supports one application.
	 */
	public ServletContext getContext(String arg0) { return this; }
	
	//
	// Attribute Methods
	//
	public void setAttribute(String strKey, Object obj) 
	{ 
		Object objOldValue = _htAttrs.put(strKey,obj);
		if( objOldValue!=null )
		{
			_notifyAttributeReplaced(strKey,objOldValue);
		}
		_notifyAttributeAdded(strKey,obj);
	}
	public Object getAttribute(String strKey) { return _htAttrs.get(strKey); }
	public Enumeration getAttributeNames() { return _htAttrs.keys(); }
	public void removeAttribute(String strKey) 
	{ 
		Object objOldValue = _htAttrs.remove(strKey);
		if( objOldValue!=null)
		{
			_notifyAttributeRemoved(strKey,objOldValue);
		}
	}

	// 
	// Init param messages
	//
	public String getInitParameter(String strKey) { return _propInitParams.getProperty(strKey); }
	public Enumeration getInitParameterNames() { return _propInitParams.keys(); }
	
	//
	// MimeType methods
	//
	public String getMimeType(String strFile) 
	{
		int ndx = strFile.lastIndexOf('.')+1;
		// The default mime type
		String strMimeType="text/plain";
		if( ndx>0 && ndx<strFile.length() )
		{
			String strExtension = strFile.substring(ndx);
			strMimeType = _propMimeTypes.getProperty(strExtension,strMimeType);
		}
		return strMimeType; 
	}
	
	//
	// RequestDispatcher methods
	//
	public RequestDispatcher getNamedDispatcher(String strName)
	{
		RequestDispatcher rd = null;
		Servlet servlet = null;
		try
		{
			servlet = (Servlet) _hmServlets.get(strName);
			if( servlet==null )
			{
				servlet = createServlet(strName);
				if( !(servlet instanceof SingleThreadModel) )
				{
					_hmServlets.put(strName,servlet);	
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new RequestDispatcherImpl(servlet);
	}
	
	public Servlet createServlet(String strName)
	{
		Servlet servlet = null;
		if( strName!=null )
		{
			// Check if the servlet is available
			Long L;
			if( (L=(Long)_hmUnavailableServlets.get(strName))!=null )
			{
				if( System.currentTimeMillis()>L.longValue() )
				{
					_hmUnavailableServlets.remove(strName);
				}
				else
				{
					// That servlet is still unavailable
					return null;
				}
			}
			
			// Attempt to create the new servlet
			try
			{
				Servlet servletNew = (Servlet)Class.forName(_propServletClasses.getProperty( strName ),true,_sccl).newInstance();
				servletNew.init(new ServletConfigImpl(strName,this,(Properties)_hmServletInitParams.get(strName)));
				servlet=servletNew;
			}
			catch(UnavailableException ue)
			{
				// TODO log this
				ue.printStackTrace();
				int nUnavailableSec = ue.getUnavailableSeconds();
				if( nUnavailableSec>0 )
				{
					_hmUnavailableServlets.put(strName,new Long(System.currentTimeMillis()+(nUnavailableSec*1000)));
				}
			}
			catch(ServletException se)
			{
				// TODO log this
				se.printStackTrace();
			}
			catch (InstantiationException e) 
			{ 
				// TODO log this
				e.printStackTrace();
			}
			catch (IllegalAccessException e) 
			{ 
				// TODO log this 
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) 
			{ 
				// TODO log this 
				e.printStackTrace();
			}
		}
		return servlet;
	}
	
	public void destroy()
	{
		Iterator itKeys = _hmServlets.keySet().iterator();
		while( itKeys.hasNext() )
		{
			destroyServlet((String)itKeys.next());
		}
		_notifyContextDestroyed();
	}
	
	public void destroyServlet(String strName)
	{
		Servlet servlet = (Servlet) _hmServlets.remove(strName);
		if( servlet!=null )
		{
			servlet.destroy();
		}
	}
	
	public RequestDispatcher getRequestDispatcher(String strURI)
	{
		Iterator it = _propServletPatterns.entrySet().iterator();
		
		String strName = null;
		String strServletPath = "";
		String strPathInfo = "";
		while( it.hasNext() && strName==null )
		{
			Map.Entry me = (Map.Entry)it.next();
			String strPattern = (String)me.getKey();
			if ( _match(strURI,strPattern) )
			{
				// Get the pathinfo
				if( strPattern.charAt(strPattern.length()-1)=='*' )
				{
					strServletPath=strPattern.substring(0,strPattern.length()-1);
					strPathInfo='/'+strURI.substring(strServletPath.length());
				}
				strName = (String)me.getValue();
			}
		}
		if( strName ==  null )
		{
			strName = _propServletPatterns.getProperty("/");
		}
		RequestDispatcherImpl rd = (RequestDispatcherImpl)getNamedDispatcher(strName);
		rd.setPathInfo(strPathInfo);
		rd.setServletPath(strServletPath);
		return rd;
	}

	//
	// Path methods
	//
	public String getRealPath(String strPath)
	{
		return new File(_fileRoot,strPath).getAbsolutePath();
	}

	//
	// Resource methods
	//
	public URL getResource(String strResource) throws MalformedURLException
	{
		String strFile = getRealPath(strResource);
		URL url = new URL("file:"+strFile);
		return url;
	}
	
	public InputStream getResourceAsStream(String strResource)
	{
		try
		{
			return getResource(strResource).openStream();	
		}
		catch(Exception e)
		{
			return null;
		}
		
	}
	
	public Set getResourcePaths(String strResource)
	{
		HashSet hs = new HashSet();
		String strRealResource = getRealPath(strResource);
		String strRoot = _fileRoot.getAbsolutePath();
		if( strRealResource.startsWith(strRoot) )
		{	
			File fileResource = new File(strRealResource);
			addResources(fileResource,hs,strRoot.length());
		}
		return hs;
	}
	
	private void addResources(File fileResource,Set s,int nPrefix)
	{
		File[] aFile = fileResource.listFiles();
		for( int n=0; n<aFile.length; n++)
		{
			if( aFile[n].isDirectory() )
			{
				addResources(aFile[n],s,nPrefix);
			}
			else
			{
				s.add(aFile[n].getAbsolutePath().substring(nPrefix));
			}
		}
	}

	//
	// log message
	// TODO - log elsewhere
	public void log(String strMessage, Throwable e)
	{
		System.err.println("log: " + strMessage);
		e.printStackTrace(System.out);
	}
	public void log(Exception e, String strMessage)
	{
		System.err.println("log: " + strMessage);
		e.printStackTrace(System.out);
	}
	public void log(String strMessage)
	{
		System.err.println("log: " + strMessage);
	}
	
	//
	// Deprecated servlet methods
	//
	public Servlet getServlet(String arg0) throws ServletException { return null; }
	public Enumeration getServletNames()
	{
		return (new Vector()).elements(); 
	}

	public Enumeration getServlets()
	{
		return (new Vector()).elements();
	}

	//
	// Filter utility methods
	//
	public FilterChain getFilterChain(HttpServletRequest request)
		throws Exception
	{
		Iterator it = _alFilterPatterns.iterator();
		String strURI = request.getRequestURI();
		
		// TODO Correctly handle urls of the type
		// Request-URI = "*" | absoluteURI | abs_path | authority
		
		RequestDispatcher rd = getRequestDispatcher(strURI);
		String strServletName = "ServletName"+((RequestDispatcherImpl)rd).getServletName();
		
		RequestDispatcherFilterChain fc = new FilterChainLast((RequestDispatcherImpl)rd);
		
		while( it.hasNext() )
		{
			String[] astrFliter = (String[]) it.next();
			String strPattern = astrFliter[0];
			String strName = astrFliter[1];

			if( _match(strURI,strPattern) || strPattern.equals(strServletName) )
			{
				Filter filter = (Filter)_hmFilters.get(strName);
				if( filter==null )
				{
					filter = _createFilter(strName);
					_hmFilters.put(strName,filter);	
				}
				fc=new FilterChainImpl(filter,fc);
			}
		}
		return fc;
	}
	
	private Filter _createFilter(String strName)
		throws Exception
	{
		Filter fliter = (Filter)Class.forName(_propFilterClasses.getProperty( strName ),true,_sccl).newInstance();
		fliter.init(new FilterConfigImpl(strName,this,(Properties)_hmFilterInitParams.get(strName)));
		return fliter;
	}	
	
	
	//
	// Our own utility methods
	//
	private boolean _match(String strURI,String strPattern)
	{
		return (( strPattern.charAt(0)=='*' && strURI.endsWith(strPattern.substring(1) ) ||
			    ( strPattern.charAt(strPattern.length()-1))=='*' && strURI.startsWith(strPattern.substring(0,strPattern.length()-1)) ) ||
			    ( strPattern.equals(strURI) ));

	}
	
	public String[] getWelcomeFiles() { return _astrWelcomFiles; }
	public String getErrorPage(int n) { return (String)_hmErrorPages.get(new Integer(n)); }
	public int getSessionTimeout() { return _nSessionTimeout; }
	public String getServerName() { return _strServerName; }
	public HttpSessionListener[] getHttpSessionListeners() { return _aHttpSessionListener; }
	public HttpSessionAttributeListener[] getHttpSessionAttributeListeners() { return _aHttpSessionAttributeListener; }	
	
	private void _notifyContextDestroyed()
	{
		ServletContextEvent sce = new ServletContextEvent(this);
		for( int n=0; n<_aServletContextListener.length; n++)
		{
			_aServletContextListener[n].contextDestroyed(sce);
		}
	}
	
	private void _notifyContextInitialized()
	{
		ServletContextEvent sce = new ServletContextEvent(this);
		for( int n=0; n<_aServletContextListener.length; n++)
		{
			_aServletContextListener[n].contextInitialized(sce);
		}
	}
	
	private void _notifyAttributeAdded(String strAttribute, Object objValue) 
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			ServletContextAttributeEvent scae = new ServletContextAttributeEvent(this,strAttribute,objValue);
			_aServletContextAttributeListener[n].attributeAdded(scae);
		}
	}
	
	private void _notifyAttributeRemoved(String strAttribute, Object objValue)  
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			ServletContextAttributeEvent scae = new ServletContextAttributeEvent(this,strAttribute,objValue);
			_aServletContextAttributeListener[n].attributeRemoved(scae);
		}
	}
	
	private void _notifyAttributeReplaced(String strAttribute, Object objValue)  
	{
		for(int n=0; n<_aHttpSessionAttributeListener.length; n++)
		{
			ServletContextAttributeEvent scae = new ServletContextAttributeEvent(this,strAttribute,objValue);
			_aServletContextAttributeListener[n].attributeReplaced(scae);
		}
	}	
	
}
