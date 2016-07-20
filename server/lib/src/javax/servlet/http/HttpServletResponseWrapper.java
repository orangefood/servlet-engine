/* HttpServletResponseWrapper - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.io.IOException;

import javax.servlet.ServletResponseWrapper;

public class HttpServletResponseWrapper extends ServletResponseWrapper
    implements HttpServletResponse
{
    public HttpServletResponseWrapper
	(HttpServletResponse httpservletresponse) {
	super((javax.servlet.ServletResponse) httpservletresponse);
    }
    
    private HttpServletResponse _getHttpServletResponse() {
	return (HttpServletResponse) super.getResponse();
    }
    
    public void addCookie(Cookie cookie) {
	_getHttpServletResponse().addCookie(cookie);
    }
    
    public void addDateHeader(String string, long l) {
	_getHttpServletResponse().addDateHeader(string, l);
    }
    
    public void addHeader(String string, String string_0_) {
	_getHttpServletResponse().addHeader(string, string_0_);
    }
    
    public void addIntHeader(String string, int i) {
	_getHttpServletResponse().addIntHeader(string, i);
    }
    
    public boolean containsHeader(String string) {
	return _getHttpServletResponse().containsHeader(string);
    }
    
    public String encodeRedirectURL(String string) {
	return _getHttpServletResponse().encodeRedirectURL(string);
    }
    
    public String encodeRedirectUrl(String string) {
	return _getHttpServletResponse().encodeRedirectUrl(string);
    }
    
    public String encodeURL(String string) {
	return _getHttpServletResponse().encodeURL(string);
    }
    
    public String encodeUrl(String string) {
	return _getHttpServletResponse().encodeUrl(string);
    }
    
    public void sendError(int i) throws IOException {
	_getHttpServletResponse().sendError(i);
    }
    
    public void sendError(int i, String string) throws IOException {
	_getHttpServletResponse().sendError(i, string);
    }
    
    public void sendRedirect(String string) throws IOException {
	_getHttpServletResponse().sendRedirect(string);
    }
    
    public void setDateHeader(String string, long l) {
	_getHttpServletResponse().setDateHeader(string, l);
    }
    
    public void setHeader(String string, String string_1_) {
	_getHttpServletResponse().setHeader(string, string_1_);
    }
    
    public void setIntHeader(String string, int i) {
	_getHttpServletResponse().setIntHeader(string, i);
    }
    
    public void setStatus(int i) {
	_getHttpServletResponse().setStatus(i);
    }
    
    public void setStatus(int i, String string) {
	_getHttpServletResponse().setStatus(i, string);
    }
}
