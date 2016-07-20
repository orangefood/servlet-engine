package com.orangefood.se.servlets.index;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet
{
	private String _strRoot;
	private String _strWEB_INF;
	public void init(ServletConfig config)
			  throws ServletException
	{
		super.init(config);
		_strRoot = getServletContext().getRealPath("/");
		_strWEB_INF = getServletContext().getRealPath("/WEB-INF");
	}
	
	protected void service (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		String strRequest = request.getRequestURI();
		String strFile = getServletContext().getRealPath(strRequest);
		String strRoot = getServletContext().getRealPath("/");
		if( !strFile.startsWith(strRoot) )
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		File fileDir = new File(strFile);
		if( fileDir.isDirectory() )
		{
			response.setStatus(HttpServletResponse.SC_OK);
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><title>Index of ");
			sb.append(strRequest);
			sb.append("</title></head><body><h1>Index of");
			sb.append(strRequest);
			sb.append("</h1><hr/><a href=\"..\">Up to higher level directory</a><br/>\n<table>");
			File[] afiles = fileDir.listFiles();
			for( int n=0; n<afiles.length; n++ )
			{	
				File file=afiles[n];
				if( !file.getAbsolutePath().equals(_strWEB_INF))
				{
					sb.append("<tr><td>");
					//
					// filename
					//
					String strName;
					if( file.isDirectory() )
					{
						strName = file.getName()+"/";
					}
					else
					{
						strName = file.getName();
					}

					sb.append("<a href=\"");
					sb.append(strName);
					sb.append("\">");
					sb.append(strName);
					sb.append("</a>");
					
					sb.append("</td><td>");
					long l = file.length();
					sb.append(l);
					sb.append("</td><td>");
					
					// 
					// date
					//
					sb.append(new Date(file.lastModified()));
					sb.append("</tr></tr>\n");
				}
			}
			sb.append("</table><hr/></body></html>");
			response.setContentLength(sb.length());
			response.getOutputStream().print(sb.toString());
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return;
	}

}
