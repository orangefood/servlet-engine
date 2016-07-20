/* ServletContext - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

public interface ServletContext
{
    public Object getAttribute(String string);
    
    public Enumeration getAttributeNames();
    
    public ServletContext getContext(String string);
    
    public String getInitParameter(String string);
    
    public Enumeration getInitParameterNames();
    
    public int getMajorVersion();
    
    public String getMimeType(String string);
    
    public int getMinorVersion();
    
    public RequestDispatcher getNamedDispatcher(String string);
    
    public String getRealPath(String string);
    
    public RequestDispatcher getRequestDispatcher(String string);
    
    public URL getResource(String string) throws MalformedURLException;
    
    public InputStream getResourceAsStream(String string);
    
    public Set getResourcePaths(String string);
    
    public String getServerInfo();
    
    /**
     * @deprecated
     */
    public Servlet getServlet(String string) throws ServletException;
    
    public String getServletContextName();
    
    /**
     * @deprecated
     */
    public Enumeration getServletNames();
    
    /**
     * @deprecated
     */
    public Enumeration getServlets();
    
    /**
     * @deprecated
     */
    public void log(Exception exception, String string);
    
    public void log(String string);
    
    public void log(String string, Throwable throwable);
    
    public void removeAttribute(String string);
    
    public void setAttribute(String string, Object object);
}
