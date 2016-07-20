package com.orangefood.se;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import com.orangefood.se.net.SocketListener;
import com.orangefood.xml.Parser;

public class Server
{
	private static URL[] s_aurlConfig;
	private static SocketListener[] s_asl=null;
	private static HashMap s_hmProperties=new HashMap();
	
	public static Properties getProperties(String strProperties) throws IOException
	{
		Properties prop = (Properties)s_hmProperties.get(strProperties);
		if( prop==null )
		{
			prop = new Properties();
			prop.load(getInputStream(strProperties+".properties"));
			s_hmProperties.put(strProperties,prop);
		}
		return prop;
	}
	
	public static InputStream getInputStream(String file)
		throws IOException
	{
		InputStream is = null;
		for(int n=0; n<s_aurlConfig.length && is==null; n++)
		{
			URL url =  new URL(s_aurlConfig[n]+"/"+file);
			//System.out.println("Checking " + url );
			try
			{
				is=url.openStream();
			}
			catch(IOException ioe)  {}
		}
		return is;
	}
	
	static void stopServer()
	{
		for( int n=0; n<s_asl.length; n++ )
		{
			s_asl[n].setRun(false);
		}		
	}
	
	public Server(URL[] urlConfig) 
	{
		s_aurlConfig=urlConfig;
	}
	
	public void start() throws IOException
	{
		InputStream is = getInputStream("server.xml");
		ServerXMLHandler xmlHandlerServer = new ServerXMLHandler();
		Parser parser = new Parser();
		parser.parse(is,xmlHandlerServer);
		s_asl = xmlHandlerServer.getListeners();
		
		for( int n=0; n<s_asl.length; n++ )
		{
			SocketListener sl = s_asl[n];
			Thread t = new Thread(sl);
			t.start();
		}
	}
	
	public static void main(String[] astr) throws Exception
	{
		URL[] aurl = new URL[astr.length+1];
		for(int n=0;n<astr.length;n++)
		{
			aurl[n]=new URL(astr[n]);
		}
		String strConfig = Server.class.getResource("/config").toString();
		aurl[astr.length]=new URL(strConfig);
		Server server = new Server(aurl);
		server.start();
	}	
}
