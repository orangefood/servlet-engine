package com.orangefood.se.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import com.orangefood.xml.Handler;

public class WebXMLHandler implements Handler
{
	private ArrayList _alTags = new ArrayList();
	
	private String _strDisplayName = "";
	
	private Properties _propContextParams = new Properties();
	private String _strContextParamName = null;
	
	private Properties _propServletClasses = new Properties();
	private Properties _propServletURLPatterns = new Properties();
	private HashMap _hmServletInitParams = new HashMap();
	private String _strServletName = null;
	private String _strServletInitName = null;
	private int _nLoadOnStartup = -1;
	private ArrayList _alLoadOnStartup = new ArrayList();
	private ArrayList _alServletContextListeners = new ArrayList();
	private ArrayList _alServletContextAttributeListeners = new ArrayList();
	private ArrayList _alHttpSessionListeners = new ArrayList();
	private ArrayList _alHttpSessionAttributeListeners = new ArrayList();
	

	
	private Properties _propFilterClasses = new Properties();
	private ArrayList _alFilterPatters = new ArrayList();
	private HashMap _hmFilterInitParams = new HashMap();
	private String _strFilterName = null;
	private String _strFilterInitName = null;
	
	private int _nSessionTimeOut;

	private Properties _propMimeTypes = new Properties();
	private String _strExtension =  null;
	
	private ArrayList _alWelcomFiles = new ArrayList();
	
	private HashMap _hmErrorPages = new HashMap();
	private Integer _NErrorCode = null;
	
	public void open(String tag, Properties attributes) 
	{ 
		_alTags.add(tag); 
	}

	public void close(String tag) 
	{ 
		String path = _getTagPath();
		if( "web-app.servlet.load-on-startup".equals(path) )
		{
			Object[] objLoadOnStartup = new Object[] { new Integer(_nLoadOnStartup),_strServletName };
			_alLoadOnStartup.add(objLoadOnStartup);
			_nLoadOnStartup=-1;
		}
		_alTags.remove(_alTags.size()-1); 
	}

	public void comment(String comment) { /*NOOP*/ }
	
	public void processingInstruction(String pi, Properties attributes) { /*NOOP*/ }

	public void text(String text)
	{
		text=text.trim();
		String path = _getTagPath();

		if( "web-app.display-name".equals(path) )
		{
			_strDisplayName=text;
		}
		else if( "web-app.context-param.param-name".equals(path) ) 
		{
			_strContextParamName=text;
		}
		else if( "web-app.context-param.param-value".equals(path) ) 
		{
			_propContextParams.put(_strContextParamName,text);
		}
		else if( "web-app.filter.filter-name".equals(path) ) 
		{
			_strFilterName=text;
			Properties prop=new Properties();
			_hmFilterInitParams.put(_strFilterName,prop);
			
		}
		else if( "web-app.filter.filter-class".equals(path) ) 
		{
			_propFilterClasses.setProperty(_strFilterName,text);
		}
		else if( "web-app.filter.init-param.param-name".equals(path) ) 
		{
			_strFilterInitName=text;
		}
		else if( "web-app.filter.init-param.param-value".equals(path) ) 
		{
			((Properties)_hmFilterInitParams.get(_strFilterName)).setProperty(_strFilterInitName,text);
		}
		else if( "web-app.filter-mapping.filter-name".equals(path) )
		{
			_strFilterName=text;
		}
		else if( "web-app.filter-mapping.url-pattern".equals(path) )
		{
			_alFilterPatters.add(new String[] { text, _strFilterName });
		}
		else if( "web-app.filter-mapping.servlet-name".equals(path) )
		{
			_alFilterPatters.add(new String[] { "ServletName"+text, _strFilterName });
		}
		else if( "web-app.listener.listener-class".equals(path) )
		{
			Object objListener;
			try
			{
				objListener = Class.forName(text).newInstance();
				
				if( objListener instanceof ServletContextListener )
				{
					_alServletContextAttributeListeners.add(objListener);
				}
				else if( objListener instanceof ServletContextAttributeListener )
				{
					_alServletContextAttributeListeners.add(objListener);
				}
				else if( objListener instanceof HttpSessionListener )
				{
					_alHttpSessionListeners.add(objListener);
				}
				else if( objListener instanceof HttpSessionAttributeListener )
				{
					_alHttpSessionAttributeListeners.add(objListener);
				}
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if( "web-app.servlet.servlet-name".equals(path) ) 
		{
			_strServletName=text;
			Properties prop=new Properties();
			_hmServletInitParams.put(_strServletName,prop);			
		}
		else if( "web-app.servlet.servlet-class".equals(path) ) 
		{
			_propServletClasses.setProperty(_strServletName,text);
		}
		else if( "web-app.servlet.init-param.param-name".equals(path) ) 
		{
			_strServletInitName=text;
		}
		else if( "web-app.servlet.init-param.param-value".equals(path) ) 
		{
			((Properties)_hmServletInitParams.get(_strServletName)).setProperty(_strServletInitName,text);
		}
		else if( "web-app.servlet.load-on-startup".equals(path) )
		{
			if( text.length()>0 )
			{
				_nLoadOnStartup=Integer.parseInt(text);
			}
		}
		else if( "web-app.servlet-mapping.servlet-name".equals(path) ) 
		{
			_strServletName=text;
		}
		else if( "web-app.servlet-mapping.url-pattern".equals(path) ) 
		{
			_propServletURLPatterns.setProperty(text,_strServletName);
		}
		else if( "web-app.sesssion-config.session-timeout".equals(path) ) 
		{
			_nSessionTimeOut=Integer.parseInt(text);
		}
		else if( "web-app.mime-mapping.extension".equals(path) ) 
		{
			_strExtension=text;
		}
		else if( "web-app.mime-mapping.mime-type".equals(path) ) 
		{
			_propMimeTypes.setProperty(_strExtension,text);
		}
		else if( "web-app.welcome-file-list.welcome-file".equals(path) ) 
		{
			_alWelcomFiles.add(text);
		}
		else if( "web-app.error-page.error-code".equals(path) ) 
		{
			_NErrorCode = new Integer(text);
		}
		else if( "web-app.error-page.location".equals(path) ) 
		{
			_hmErrorPages.put(_NErrorCode,text);
		}
	}

	private String _getTagPath()
	{
		StringBuffer sbPath=new StringBuffer();
		Iterator it = _alTags.iterator();
		while( it.hasNext() )
		{
			sbPath.append((String)it.next());
			if( it.hasNext() )
			{
				sbPath.append('.');
			}
		}
		return sbPath.toString();
	}
	
	public static int hashCode(String str) 
	{
		int h=0;
		char[] ach = str.toCharArray();
		for (int n=0; n <ach.length; n++)
			h = 31*h + ach[n];
		return h;
	}	
	
	public String getDisplayName() { return _strDisplayName; }
	public Properties getContextParams() {	return _propContextParams; }
	public Properties getServletClasses() { return _propServletClasses; }
	public Properties getServletPatterns() { return _propServletURLPatterns; }
	public HashMap getServletInitParams() { return _hmServletInitParams; }
	public Properties getFilterClasses() { return _propFilterClasses; }
	public ArrayList getFilterPatterns() { return _alFilterPatters; }
	public HashMap getFilterInitParams() { return _hmFilterInitParams; }
	public Properties getMimeTypes() { return _propMimeTypes; }
	public HashMap getErrorPages() { return _hmErrorPages; }
	public ArrayList getWelcomeFiles() { return _alWelcomFiles; }
	public int getSessionTimeOut() { return _nSessionTimeOut; }
	
	public ServletContextListener[] getServletContextListeners()
	{
		ServletContextListener[] ascl=new ServletContextListener[_alServletContextListeners.size()];
		ascl = (ServletContextListener[])_alServletContextListeners.toArray(ascl);
		return ascl;
	}
	public ServletContextAttributeListener[] getServletContextAttributeListeners()
	{
		ServletContextAttributeListener[] ascal=new ServletContextAttributeListener[_alServletContextAttributeListeners.size()];
		ascal=(ServletContextAttributeListener[])_alServletContextAttributeListeners.toArray(ascal);
		return ascal;
	}
	public HttpSessionListener[] getHttpSessionListeners()
	{
		HttpSessionListener[] ahsl=new HttpSessionListener[_alHttpSessionListeners.size()];
		ahsl=(HttpSessionListener[])_alHttpSessionListeners.toArray(ahsl);
		return ahsl;
	}
	public HttpSessionAttributeListener[] getHttpSessionAttributeListeners()
	{
		HttpSessionAttributeListener[] ahsal=new HttpSessionAttributeListener[_alHttpSessionAttributeListeners.size()];
		ahsal=(HttpSessionAttributeListener[])_alHttpSessionAttributeListeners.toArray(ahsal);
		return ahsal;
	}	
	 
	public String[] getLoadOnStartupServlets()
	{
		String[] astrServlets = new String[_alLoadOnStartup.size()];
		Object[] aobjStartup = _alLoadOnStartup.toArray();
		Arrays.sort(aobjStartup,
			new Comparator()
			{
				public int compare(Object obj1, Object obj2)
				{
					return ((Integer)((Object[])obj1)[0]).intValue() - ((Integer)((Object[])obj2)[0]).intValue(); 
				}
			});
		for( int n=0; n<astrServlets.length; n++ )
		{
			astrServlets[n]=(String)((Object[])aobjStartup[n])[1];
		}
		return astrServlets;
	}
}
