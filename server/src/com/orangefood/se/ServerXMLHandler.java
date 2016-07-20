package com.orangefood.se;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.orangefood.se.net.SocketHandler;
import com.orangefood.se.net.SocketListener;
import com.orangefood.se.servlet.ServletContextImpl;
import com.orangefood.xml.Handler;

public class ServerXMLHandler implements Handler
{
	public static final String TEMP_DIR_PROPERTY="javax.servlet.context.tempdir";
	private ClassLoader _cl = this.getClass().getClassLoader();
	
	private ArrayList _alTags = new ArrayList();
	
	private File _fileTemp = new File(System.getProperty("java.io.tmpdir"));
	
	// Context info
	private HashMap _hmContext=new HashMap();
	private ServletContextImpl _sc=null;
	private String _strContextProperty=null;
	private Properties _propContext=new Properties();
	
	// Handler info
	private HashMap _hmHandlers=new HashMap();
	private SocketHandler _sh=null;
	private String _strHandlerProperty=null;
	private Properties _propHandler=new Properties();
	
	// Listener info
	private ArrayList _alSL = new ArrayList();
	private SocketListener _sl= null;
	private String _strListenerProperty=null;
	private Properties _propListener=new Properties();
	
	
	public void open(String tag, Properties attributes)
	{
		_alTags.add(tag);
		String path = _getTagPath();
		if( "server.handler".equals(path) )
		{
			try
			{
				String strName = attributes.getProperty("name");
				String strClass = attributes.getProperty("class");
				_sh = (SocketHandler)Class.forName(strClass).newInstance();
				_sh.setName(strName);
				_hmHandlers.put(strName,_sh);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if( "server.listener".equals(path) ) 
		{
			try
			{
				String strClass = attributes.getProperty("class");
				String strName = attributes.getProperty("name");
				_sl = (SocketListener)Class.forName(strClass).newInstance();
				_sl.setName(strName);
				_alSL.add(_sl);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if( "server.context".equals(path) )
		{
			try
			{
				String strName=attributes.getProperty("name");
				String strClass=attributes.getProperty("class");
				_sc = (ServletContextImpl)Class.forName(strClass).newInstance();
				// Set the temporary context directory
				_hmContext.put(strName,_sc);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if( path.startsWith("server.handler.") )
		{
			_strHandlerProperty=tag;
		}
		else if( path.startsWith("server.listener.") )
		{
			_strListenerProperty=tag;
		}
		else if( path.startsWith("server.context") )
		{
			_strContextProperty=tag;
		}
	}
	
	public void close(String tag)
	{
		String path = _getTagPath();
		if( "server.handler".equals(path) )
		{
			_sh.init(_propHandler);
			_propHandler.clear();
		}
		else if ( "server.listener".equals(path) )
		{
			_sl.init(_propListener);
			_propListener.clear();
		}
		else if ("server.context".equals(path) )
		{
			// Set the temp file
			String strTempDir = _propContext.getProperty(TEMP_DIR_PROPERTY);
			File fileContextDir=null;
			if( strTempDir==null )
			{
				try
				{
					fileContextDir=File.createTempFile("context.",".tmp",_fileTemp);
					fileContextDir.delete();
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
			else
			{
				fileContextDir = new File(strTempDir);
				if( !fileContextDir.isAbsolute() )
				{
					fileContextDir=new File(_fileTemp,strTempDir);
				}
			}
			_propContext.setProperty(TEMP_DIR_PROPERTY,fileContextDir.getAbsolutePath());
			_sc.init(_propContext);
			_propContext.clear();
		}

		_alTags.remove(_alTags.size()-1); 
	}
	
	public void text(String text)
	{
		text=text.trim();
		String path = _getTagPath();

		if( "server.listener.handler".equals(path) )
		{
			_sl.setSocketHandler((SocketHandler)_hmHandlers.get(text));
		}
		else if( path.startsWith("server.listener.") )
		{
			_propListener.setProperty(_strListenerProperty,text);
		}
		else if( "server.handler.context".equals(path) )
		{
			_sh.setContext((ServletContextImpl)_hmContext.get(text));
		}
		else if( "server.tempdir".equals(path) )
		{
			File file = new File(text);
			if( file.isAbsolute() )
			{
				_fileTemp=file;
			}
			else
			{
				_fileTemp=new File(_fileTemp,text);
			}
			_fileTemp.mkdirs();
			System.out.println("Server tempdir is " + _fileTemp );			
		}
		else if( "server.context.tempdir".equals(path) )
		{
			_propContext.setProperty(TEMP_DIR_PROPERTY,text);
		}
		else if( path.startsWith("server.handler.") ) 
		{
			_propHandler.setProperty(_strHandlerProperty,text);
		}
		else if( path.startsWith("server.context.") )
		{
			_propContext.setProperty(_strContextProperty,text);
		}
	}
	
	public void comment(String comment) {}
	public void processingInstruction(String pi, Properties attributes) {}
	
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
	
	public SocketListener[] getListeners()
	{
		return (SocketListener[])_alSL.toArray(new SocketListener[0]);
	}
	
}
