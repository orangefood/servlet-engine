/* ServletRequestWrapper - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class ServletRequestWrapper implements ServletRequest
{
    private ServletRequest request;
    
    public ServletRequestWrapper(ServletRequest servletrequest) {
	if (servletrequest == null)
	    throw new IllegalArgumentException("Request cannot be null");
	request = servletrequest;
    }
    
    public Object getAttribute(String string) {
	return request.getAttribute(string);
    }
    
    public Enumeration getAttributeNames() {
	return request.getAttributeNames();
    }
    
    public String getCharacterEncoding() {
	return request.getCharacterEncoding();
    }
    
    public int getContentLength() {
	return request.getContentLength();
    }
    
    public String getContentType() {
	return request.getContentType();
    }
    
    public ServletInputStream getInputStream() throws IOException {
	return request.getInputStream();
    }
    
    public Locale getLocale() {
	return request.getLocale();
    }
    
    public Enumeration getLocales() {
	return request.getLocales();
    }
    
    public String getParameter(String string) {
	return request.getParameter(string);
    }
    
    public Map getParameterMap() {
	return request.getParameterMap();
    }
    
    public Enumeration getParameterNames() {
	return request.getParameterNames();
    }
    
    public String[] getParameterValues(String string) {
	return request.getParameterValues(string);
    }
    
    public String getProtocol() {
	return request.getProtocol();
    }
    
    public BufferedReader getReader() throws IOException {
	return request.getReader();
    }
    
    public String getRealPath(String string) {
	return request.getRealPath(string);
    }
    
    public String getRemoteAddr() {
	return request.getRemoteAddr();
    }
    
    public String getRemoteHost() {
	return request.getRemoteHost();
    }
    
    public ServletRequest getRequest() {
	return request;
    }
    
    public RequestDispatcher getRequestDispatcher(String string) {
	return request.getRequestDispatcher(string);
    }
    
    public String getScheme() {
	return request.getScheme();
    }
    
    public String getServerName() {
	return request.getServerName();
    }
    
    public int getServerPort() {
	return request.getServerPort();
    }
    
    public boolean isSecure() {
	return request.isSecure();
    }
    
    public void removeAttribute(String string) {
	request.removeAttribute(string);
    }
    
    public void setAttribute(String string, Object object) {
	request.setAttribute(string, object);
    }
    
    public void setCharacterEncoding(String string)
	throws UnsupportedEncodingException {
	request.setCharacterEncoding(string);
    }
    
    public void setRequest(ServletRequest servletrequest) {
	if (servletrequest == null)
	    throw new IllegalArgumentException("Request cannot be null");
	request = servletrequest;
    }
}
