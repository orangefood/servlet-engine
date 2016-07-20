/* ServletRequest - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public interface ServletRequest
{
    public Object getAttribute(String string);
    
    public Enumeration getAttributeNames();
    
    public String getCharacterEncoding();
    
    public int getContentLength();
    
    public String getContentType();
    
    public ServletInputStream getInputStream() throws IOException;
    
    public Locale getLocale();
    
    public Enumeration getLocales();
    
    public String getParameter(String string);
    
    public Map getParameterMap();
    
    public Enumeration getParameterNames();
    
    public String[] getParameterValues(String string);
    
    public String getProtocol();
    
    public BufferedReader getReader() throws IOException;
    
    /**
     * @deprecated
     */
    public String getRealPath(String string);
    
    public String getRemoteAddr();
    
    public String getRemoteHost();
    
    public RequestDispatcher getRequestDispatcher(String string);
    
    public String getScheme();
    
    public String getServerName();
    
    public int getServerPort();
    
    public boolean isSecure();
    
    public void removeAttribute(String string);
    
    public void setAttribute(String string, Object object);
    
    public void setCharacterEncoding(String string)
	throws UnsupportedEncodingException;
}
