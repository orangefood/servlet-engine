package com.orangefood.se.servlets.cgibin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*

#  SERVER_SOFTWARE
The name and version of the information server software answering the request (and running the gateway). Format: name/version

# SERVER_NAME
The server's hostname, DNS alias, or IP address as it would appear in self-referencing URLs.

# GATEWAY_INTERFACE
The revision of the CGI specification to which this server complies. Format: CGI/revision

#  SERVER_PROTOCOL
The name and revision of the information protcol this request came in with. Format: protocol/revision

# SERVER_PORT
The port number to which the request was sent.

# REQUEST_METHOD
The method with which the request was made. For HTTP, this is "GET", "HEAD", "POST", etc.

# PATH_INFO
The extra path information, as given by the client. In other words, scripts can be accessed by their virtual pathname, followed by extra information at the end of this path. The extra information is sent as PATH_INFO. This information should be decoded by the server if it comes from a URL before it is passed to the CGI script.

# PATH_TRANSLATED
The server provides a translated version of PATH_INFO, which takes the path and does any virtual-to-physical mapping to it.

# SCRIPT_NAME
A virtual path to the script being executed, used for self-referencing URLs.

# QUERY_STRING
The information which follows the ? in the URL which referenced this script. This is the query information. It should not be decoded in any fashion. This variable should always be set when there is query information, regardless of command line decoding.

# REMOTE_HOST
The hostname making the request. If the server does not have this information, it should set REMOTE_ADDR and leave this unset.

# REMOTE_ADDR
The IP address of the remote host making the request.

# AUTH_TYPE
If the server supports user authentication, and the script is protects, this is the protocol-specific authentication method used to validate the user.

# REMOTE_USER
If the server supports user authentication, and the script is protected, this is the username they have authenticated as.

# REMOTE_IDENT
If the HTTP server supports RFC 931 identification, then this variable will be set to the remote user name retrieved from the server. Usage of this variable should be limited to logging only.

# CONTENT_TYPE
For queries which have attached information, such as HTTP POST and PUT, this is the content type of the data.

# CONTENT_LENGTH
The length of the said content as given by the client. 

#  HTTP_ACCEPT
The MIME types which the client will accept, as given by HTTP headers. Other protocols may need to get this information from elsewhere. Each item in this list should be separated by commas as per the HTTP spec.
Format: type/subtype, type/subtype

# HTTP_USER_AGENT
The browser the client is using to send the request. General format: software/version library/version.

 */
public class CgiBinServlet extends HttpServlet
{
	private Runtime _rt = Runtime.getRuntime();
	protected void service (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		// Environment Variables
		ArrayList alEnv = new ArrayList();
		// SERVER_SOFTWARE
		alEnv.add("SERVER_SOFTWARE="+getServletContext().getServerInfo());
		// SERVER_NAME
		alEnv.add("SERVER_NAME="+request.getServerName());
		// GATEWAY_INTERFACE
		alEnv.add("GATEWAY_INTERFACE=CGI/1.1");
		// SERVER_PROTOCOL
		alEnv.add("SERVER_PROTOCOL="+request.getProtocol());
		// SERVER_PORT
		alEnv.add("SERVER_PORT="+request.getServerPort());
		// REQUEST_METHOD
		alEnv.add("REQUEST_METHOD="+request.getMethod());
		// PATH_INFO
		alEnv.add("PATH_INFO="+request.getPathInfo());
		// PATH_TRANSLATED
		alEnv.add("PATH_TRANSLATED="+request.getPathTranslated());
		// SCRIPT_NAME
		alEnv.add("SCRIPT_NAME="+request.getRequestURI());
		// QUERY_STRING
		alEnv.add("QUERY_STRING="+request.getQueryString());
		// REMOTE_HOST
		alEnv.add("REMOTE_HOST="+request.getRemoteHost());
		// REMOTE_ADDR
		alEnv.add("REMOTE_ADDR="+request.getRemoteAddr());
		// AUTH_TYPE
		alEnv.add("AUTH_TYPE"+request.getAuthType());
		// REMOTE_USER
		alEnv.add("REMOTE_USER="+request.getRemoteUser());
		// REMOTE_IDENT
		alEnv.add("REMOTE_IDENT=");
		// CONTENT_TYPE
		alEnv.add("CONTENT_TYPE="+request.getContentType());
		// CONTENT_LENGTH
		alEnv.add("CONTENT_LENGTH="+request.getContentLength());
		
		// Add the request headers to the environment
		Enumeration	enumHeaders = request.getHeaderNames();
		while( enumHeaders.hasMoreElements() )
		{
			String strHeaderName = (String)enumHeaders.nextElement();
			String strHeaderValue = request.getHeader(strHeaderName);
			alEnv.add("HTTP_"+strHeaderName.replace('-','_')+"="+strHeaderValue);
		}
	
	}
}
