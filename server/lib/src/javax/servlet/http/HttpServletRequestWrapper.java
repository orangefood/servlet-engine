/* HttpServletRequestWrapper - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.ServletRequestWrapper;

public class HttpServletRequestWrapper extends ServletRequestWrapper
    implements HttpServletRequest
{
    public HttpServletRequestWrapper(HttpServletRequest httpservletrequest) {
	super((javax.servlet.ServletRequest) httpservletrequest);
    }
    
    private HttpServletRequest _getHttpServletRequest() {
	return (HttpServletRequest) super.getRequest();
    }
    
    public String getAuthType() {
	return _getHttpServletRequest().getAuthType();
    }
    
    public String getContextPath() {
	return _getHttpServletRequest().getContextPath();
    }
    
    public Cookie[] getCookies() {
	return _getHttpServletRequest().getCookies();
    }
    
    public long getDateHeader(String string) {
	return _getHttpServletRequest().getDateHeader(string);
    }
    
    public String getHeader(String string) {
	return _getHttpServletRequest().getHeader(string);
    }
    
    public Enumeration getHeaderNames() {
	return _getHttpServletRequest().getHeaderNames();
    }
    
    public Enumeration getHeaders(String string) {
	return _getHttpServletRequest().getHeaders(string);
    }
    
    public int getIntHeader(String string) {
	return _getHttpServletRequest().getIntHeader(string);
    }
    
    public String getMethod() {
	return _getHttpServletRequest().getMethod();
    }
    
    public String getPathInfo() {
	return _getHttpServletRequest().getPathInfo();
    }
    
    public String getPathTranslated() {
	return _getHttpServletRequest().getPathTranslated();
    }
    
    public String getQueryString() {
	return _getHttpServletRequest().getQueryString();
    }
    
    public String getRemoteUser() {
	return _getHttpServletRequest().getRemoteUser();
    }
    
    public String getRequestURI() {
	return _getHttpServletRequest().getRequestURI();
    }
    
    public StringBuffer getRequestURL() {
	return _getHttpServletRequest().getRequestURL();
    }
    
    public String getRequestedSessionId() {
	return _getHttpServletRequest().getRequestedSessionId();
    }
    
    public String getServletPath() {
	return _getHttpServletRequest().getServletPath();
    }
    
    public HttpSession getSession() {
	return _getHttpServletRequest().getSession();
    }
    
    public HttpSession getSession(boolean bool) {
	return _getHttpServletRequest().getSession(bool);
    }
    
    public Principal getUserPrincipal() {
	return _getHttpServletRequest().getUserPrincipal();
    }
    
    public boolean isRequestedSessionIdFromCookie() {
	return _getHttpServletRequest().isRequestedSessionIdFromCookie();
    }
    
    public boolean isRequestedSessionIdFromURL() {
	return _getHttpServletRequest().isRequestedSessionIdFromURL();
    }
    
    public boolean isRequestedSessionIdFromUrl() {
	return _getHttpServletRequest().isRequestedSessionIdFromUrl();
    }
    
    public boolean isRequestedSessionIdValid() {
	return _getHttpServletRequest().isRequestedSessionIdValid();
    }
    
    public boolean isUserInRole(String string) {
	return _getHttpServletRequest().isUserInRole(string);
    }
}
