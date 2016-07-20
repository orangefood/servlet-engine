/* HttpServletRequest - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

public interface HttpServletRequest extends ServletRequest
{
    public static final String BASIC_AUTH = "BASIC";
    public static final String FORM_AUTH = "FORM";
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
    public static final String DIGEST_AUTH = "DIGEST";
    
    public String getAuthType();
    
    public String getContextPath();
    
    public Cookie[] getCookies();
    
    public long getDateHeader(String string);
    
    public String getHeader(String string);
    
    public Enumeration getHeaderNames();
    
    public Enumeration getHeaders(String string);
    
    public int getIntHeader(String string);
    
    public String getMethod();
    
    public String getPathInfo();
    
    public String getPathTranslated();
    
    public String getQueryString();
    
    public String getRemoteUser();
    
    public String getRequestURI();
    
    public StringBuffer getRequestURL();
    
    public String getRequestedSessionId();
    
    public String getServletPath();
    
    public HttpSession getSession();
    
    public HttpSession getSession(boolean bool);
    
    public Principal getUserPrincipal();
    
    public boolean isRequestedSessionIdFromCookie();
    
    public boolean isRequestedSessionIdFromURL();
    
    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl();
    
    public boolean isRequestedSessionIdValid();
    
    public boolean isUserInRole(String string);
}
