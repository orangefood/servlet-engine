package com.orangefood.se.servlets.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangefood.se.servlet.ServletContextImpl;

public class FileServlet extends HttpServlet
{
	private String[] _astrDefaultFiles = new String[0];
	private String _strIndexServlet = null;
	private String _strRoot;
	private String _strWEB_INF;
	
	public void init(ServletConfig config)
			  throws ServletException
	{
		super.init(config);
		_astrDefaultFiles=((ServletContextImpl)getServletContext()).getWelcomeFiles();
		_strIndexServlet = config.getInitParameter("index_servlet");
		_strRoot = getServletContext().getRealPath("/");
		_strWEB_INF = getServletContext().getRealPath("/WEB-INF");
	}
	
	protected void service (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		String strRequest = request.getRequestURI();
		String strFile = getServletContext().getRealPath(strRequest);
		// insure the file is below the root and not in WEB-INF
		if( !strFile.startsWith(_strRoot) || strFile.startsWith(_strWEB_INF))
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		File file = new File(strFile);
		if( !file.exists() )
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if( file.isDirectory() )
		{
			// The request should contain an ending /
			if( strRequest.charAt(strRequest.length()-1)!='/')
			{
				// Location: http://robertrh.corp.roxint.com/test/
				StringBuffer sb = new StringBuffer();
				String strScheme = request.getScheme();
				sb.append(strScheme);
				sb.append("://");
				sb.append(request.getServerName());
				int nPort = request.getServerPort();
				if( (nPort!=80 && "http".equals(strScheme)) ||
					(nPort!=443 && "https".equals(strScheme)) )
				{
					sb.append(':');
					sb.append(nPort);
				}
				sb.append(request.getRequestURI());
				sb.append('/');
				String strQueryString=request.getQueryString();
				if( strQueryString.length()>0 )
				{
					sb.append('?');
					sb.append(strQueryString);
				}
				response.setHeader("Location",sb.toString());
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				return;
			}
			// Look for default files
			boolean bFound=false;
			for(int n=0;!bFound && n<_astrDefaultFiles.length; n++)
			{
				File fileIndex = new File(file,_astrDefaultFiles[n]);
				if( (bFound = (fileIndex.exists() && !fileIndex.isDirectory())) )
				{
					file = fileIndex;
				}
			}
			if( !bFound)
			{
				if( _strIndexServlet != null )
				{
					getServletContext().getNamedDispatcher(_strIndexServlet).forward(request,response);
					return;
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			}
		}

		sendFile(file,response);

	}
	
	private void sendFile(File file, HttpServletResponse response)
		throws IOException
	{
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentLength((int)file.length());
		FileInputStream fis = new FileInputStream(file);
		ServletOutputStream sos = response.getOutputStream();
		int n;
		while( (n=fis.read())!=-1 )
		{
			sos.write(n);
		}		
	}
}
