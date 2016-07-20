/* Servlet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;

public interface Servlet
{
    public void destroy();
    
    public ServletConfig getServletConfig();
    
    public String getServletInfo();
    
    public void init(ServletConfig servletconfig) throws ServletException;
    
    public void service
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws ServletException, IOException;
}
