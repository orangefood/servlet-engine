/* GenericServlet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

public abstract class GenericServlet
    implements Servlet, ServletConfig, Serializable
{
    private transient ServletConfig config;
    
    public void destroy() {
	log("destroy");
    }
    
    public String getInitParameter(String string) {
	return getServletConfig().getInitParameter(string);
    }
    
    public Enumeration getInitParameterNames() {
	return getServletConfig().getInitParameterNames();
    }
    
    public ServletConfig getServletConfig() {
	return config;
    }
    
    public ServletContext getServletContext() {
	return getServletConfig().getServletContext();
    }
    
    public String getServletInfo() {
	return "";
    }
    
    public String getServletName() {
	return config.getServletName();
    }
    
    public void init() throws ServletException {
	/* empty */
    }
    
    public void init(ServletConfig servletconfig) throws ServletException {
	config = servletconfig;
	log("init");
	init();
    }
    
    public void log(String string) {
	getServletContext().log(getServletName() + ": " + string);
    }
    
    public void log(String string, Throwable throwable) {
	getServletContext().log(getServletName() + ": " + string, throwable);
    }
    
    public abstract void service
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws ServletException, IOException;
}
