/* HttpServlet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class HttpServlet extends GenericServlet
    implements Serializable
{
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";
    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    private static final String LSTRING_FILE
	= "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings
	= ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
    
    protected void doDelete
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = httpservletrequest.getProtocol();
	String string_0_
	    = lStrings.getString("http.method_delete_not_supported");
	if (string.endsWith("1.1"))
	    httpservletresponse.sendError(405, string_0_);
	else
	    httpservletresponse.sendError(400, string_0_);
    }
    
    protected void doGet
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = httpservletrequest.getProtocol();
	String string_1_ = lStrings.getString("http.method_get_not_supported");
	if (string.endsWith("1.1"))
	    httpservletresponse.sendError(405, string_1_);
	else
	    httpservletresponse.sendError(400, string_1_);
    }
    
    protected void doHead
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	NoBodyResponse nobodyresponse
	    = new NoBodyResponse(httpservletresponse);
	doGet(httpservletrequest, nobodyresponse);
	nobodyresponse.setContentLength();
    }
    
    protected void doOptions
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	Method[] methods = getAllDeclaredMethods(this.getClass());
	boolean bool = false;
	boolean bool_2_ = false;
	boolean bool_3_ = false;
	boolean bool_4_ = false;
	boolean bool_5_ = false;
	boolean bool_6_ = true;
	boolean bool_7_ = true;
	for (int i = 0; i < methods.length; i++) {
	    Method method = methods[i];
	    if (method.getName().equals("doGet")) {
		bool = true;
		bool_2_ = true;
	    }
	    if (method.getName().equals("doPost"))
		bool_3_ = true;
	    if (method.getName().equals("doPut"))
		bool_4_ = true;
	    if (method.getName().equals("doDelete"))
		bool_5_ = true;
	}
	String string = null;
	if (bool && string == null)
	    string = "GET";
	if (bool_2_) {
	    if (string == null)
		string = "HEAD";
	    else
		string += ", HEAD";
	}
	if (bool_3_) {
	    if (string == null)
		string = "POST";
	    else
		string += ", POST";
	}
	if (bool_4_) {
	    if (string == null)
		string = "PUT";
	    else
		string += ", PUT";
	}
	if (bool_5_) {
	    if (string == null)
		string = "DELETE";
	    else
		string += ", DELETE";
	}
	if (bool_6_) {
	    if (string == null)
		string = "TRACE";
	    else
		string += ", TRACE";
	}
	if (bool_7_) {
	    if (string == null)
		string = "OPTIONS";
	    else
		string += ", OPTIONS";
	}
	httpservletresponse.setHeader("Allow", string);
    }
    
    protected void doPost
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = httpservletrequest.getProtocol();
	String string_8_
	    = lStrings.getString("http.method_post_not_supported");
	if (string.endsWith("1.1"))
	    httpservletresponse.sendError(405, string_8_);
	else
	    httpservletresponse.sendError(400, string_8_);
    }
    
    protected void doPut
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = httpservletrequest.getProtocol();
	String string_9_ = lStrings.getString("http.method_put_not_supported");
	if (string.endsWith("1.1"))
	    httpservletresponse.sendError(405, string_9_);
	else
	    httpservletresponse.sendError(400, string_9_);
    }
    
    protected void doTrace
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = "\r\n";
	String string_10_ = ("TRACE " + httpservletrequest.getRequestURI()
			     + " " + httpservletrequest.getProtocol());
	Enumeration enumeration = httpservletrequest.getHeaderNames();
	while (enumeration.hasMoreElements()) {
	    String string_11_ = (String) enumeration.nextElement();
	    string_10_ += ((String) string + string_11_ + ": "
			   + httpservletrequest.getHeader(string_11_));
	}
	string_10_ += (String) string;
	int i = string_10_.length();
	httpservletresponse.setContentType("message/http");
	httpservletresponse.setContentLength(i);
	ServletOutputStream servletoutputstream
	    = httpservletresponse.getOutputStream();
	servletoutputstream.print(string_10_);
	servletoutputstream.close();
    }
    
    private Method[] getAllDeclaredMethods(Class var_class) {
	if (var_class.getName().equals("javax.servlet.http.HttpServlet"))
	    return null;
	int i = 0;
	Method[] methods = getAllDeclaredMethods(var_class.getSuperclass());
	Method[] methods_12_ = var_class.getDeclaredMethods();
	if (methods != null) {
	    Method[] methods_13_
		= new Method[methods.length + methods_12_.length];
	    for (int i_14_ = 0; i_14_ < methods.length; i_14_++) {
		methods_13_[i_14_] = methods[i_14_];
		i = i_14_;
	    }
	    for (int i_15_ = ++i; i_15_ < methods_12_.length + i; i_15_++)
		methods_13_[i_15_] = methods_12_[i_15_ - i];
	    return methods_13_;
	}
	return methods_12_;
    }
    
    protected long getLastModified(HttpServletRequest httpservletrequest) {
	return -1L;
    }
    
    private void maybeSetLastModified(HttpServletResponse httpservletresponse,
				      long l) {
	if (!httpservletresponse.containsHeader("Last-Modified")) {
	    if (l >= 0L)
		httpservletresponse.setDateHeader("Last-Modified", l);
	}
    }
    
    public void service
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws ServletException, IOException {
	HttpServletRequest httpservletrequest;
	HttpServletResponse httpservletresponse;
	try {
	    httpservletrequest = (HttpServletRequest) servletrequest;
	    httpservletresponse = (HttpServletResponse) servletresponse;
	} catch (ClassCastException classcastexception) {
	    throw new ServletException("non-HTTP request or response");
	}
	service(httpservletrequest, httpservletresponse);
    }
    
    protected void service
	(HttpServletRequest httpservletrequest,
	 HttpServletResponse httpservletresponse)
	throws ServletException, IOException {
	String string = httpservletrequest.getMethod();
	if (string.equals("GET")) {
	    long l = getLastModified(httpservletrequest);
	    if (l == -1L)
		doGet(httpservletrequest, httpservletresponse);
	    else {
		long l_16_
		    = httpservletrequest.getDateHeader("If-Modified-Since");
		if (l_16_ < l / 1000L * 1000L) {
		    maybeSetLastModified(httpservletresponse, l);
		    doGet(httpservletrequest, httpservletresponse);
		} else
		    httpservletresponse.setStatus(304);
	    }
	} else if (string.equals("HEAD")) {
	    long l = getLastModified(httpservletrequest);
	    maybeSetLastModified(httpservletresponse, l);
	    doHead(httpservletrequest, httpservletresponse);
	} else if (string.equals("POST"))
	    doPost(httpservletrequest, httpservletresponse);
	else if (string.equals("PUT"))
	    doPut(httpservletrequest, httpservletresponse);
	else if (string.equals("DELETE"))
	    doDelete(httpservletrequest, httpservletresponse);
	else if (string.equals("OPTIONS"))
	    doOptions(httpservletrequest, httpservletresponse);
	else if (string.equals("TRACE"))
	    doTrace(httpservletrequest, httpservletresponse);
	else {
	    String string_17_
		= lStrings.getString("http.method_not_implemented");
	    Object[] objects = new Object[1];
	    objects[0] = string;
	    string_17_ = MessageFormat.format(string_17_, objects);
	    httpservletresponse.sendError(501, string_17_);
	}
    }
}
