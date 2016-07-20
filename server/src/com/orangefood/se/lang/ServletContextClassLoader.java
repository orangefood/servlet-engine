package com.orangefood.se.lang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ServletContextClassLoader extends ClassLoader
{
	private File _fileClasses = null;
	private JarFile[] _ajf = null;
	private Hashtable htClasses = new Hashtable();
	
	public ServletContextClassLoader(String strRoot)
	{
		File fileRoot = new File(strRoot);
		// Get the files for /WEB-INF/classes and /WEB-INF/lib/*.jar
		_fileClasses = new File(fileRoot,"WEB-INF/classes/");
		File fileLib = new File(fileRoot,"WEB-INF/lib/");
		ArrayList alJar = new ArrayList();
		if( fileLib.exists() )
		{
			File[] afileLibs = fileLib.listFiles();
			for(int n=0; n<afileLibs.length; n++)
			{
				File fileJar = afileLibs[n];
				if( fileJar.getName().endsWith(".jar") )
				{
					try
					{
						JarFile jf = new JarFile(fileJar);
						alJar.add(jf);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		_ajf = (JarFile[])alJar.toArray(new JarFile[0]);
	}
	
	protected Class findClass(String name)
					   throws ClassNotFoundException
	{
		Class klass=(Class)htClasses.get(name);
		if( klass == null )
		{
			String strClassFile = name.replace('.','/')+".class";
			InputStream is = null;
			is = getClassesClass(strClassFile);
			if( is==null )
			{
				is = getJarClass(strClassFile);
			}
		
			if( is!=null )
			{
				klass = defineClass(name,is);
			}

			if( klass==null )
			{	
				throw new ClassNotFoundException(name);
			}
			htClasses.put(name,klass);
		}
		return klass;
	}
	
	private InputStream getClassesClass(String strClassFile)
	{
		InputStream is = null;
		if( _fileClasses != null)
		{	
			File fileClass = new File(_fileClasses,strClassFile);
			if( fileClass.exists() )
			{
				try
				{
					is = new FileInputStream(fileClass);
				}
				catch (FileNotFoundException e) {}
			}
		}
		return is;
	}
	
	private InputStream getJarClass(String strClassFile)
	{
		InputStream is = null;
		for( int n=0; is==null && n<_ajf.length; n++)
		{
			JarFile jf = _ajf[n];
			ZipEntry ze = jf.getEntry(strClassFile);
			if( ze!=null )
			{	
				try
				{
					is = jf.getInputStream(ze);
				}
				catch (IOException e) {}
			}
		}
		return is;
	}
	
	private Class defineClass(String strName, InputStream is)
	{
		Class klass = null;
		byte[] abyBuffer=new byte[4096];
		int nRead = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			while( (nRead = is.read(abyBuffer))!= -1 )
			{
				baos.write(abyBuffer,0,nRead);
			}
			byte[] abyClass = baos.toByteArray();
			klass = defineClass(strName,abyClass,0,abyClass.length);
			is.close();
		}
		catch (IOException e) {}

		return klass;
	}
}
