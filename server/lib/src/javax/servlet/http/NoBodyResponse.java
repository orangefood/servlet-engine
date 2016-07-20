/* NoBodyResponse - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

class NoBodyResponse implements HttpServletResponse
{
    private HttpServletResponse resp;
    private NoBodyOutputStream noBody;
    private PrintWriter writer;
    private boolean didSetContentLength;
    
    NoBodyResponse(HttpServletResponse httpservletresponse) {
	resp = httpservletresponse;
	noBody = new NoBodyOutputStream();
    }
    
    public void addCookie(Cookie cookie) {
	resp.addCookie(cookie);
    }
    
    public void addDateHeader(String string, long l) {
	resp.addDateHeader(string, l);
    }
    
    public void addHeader(String string, String string_0_) {
	resp.addHeader(string, string_0_);
    }
    
    public void addIntHeader(String string, int i) {
	resp.addIntHeader(string, i);
    }
    
    public boolean containsHeader(String string) {
	return resp.containsHeader(string);
    }
    
    public String encodeRedirectURL(String string) {
	return resp.encodeRedirectURL(string);
    }
    
    /**
     * @deprecated
     */
    public String encodeRedirectUrl(String string) {
	return encodeRedirectURL(string);
    }
    
    public String encodeURL(String string) {
	return resp.encodeURL(string);
    }
    
    /**
     * @deprecated
     */
    public String encodeUrl(String string) {
	return encodeURL(string);
    }
    
    public void flushBuffer() throws IOException {
	resp.flushBuffer();
    }
    
    public int getBufferSize() {
	return resp.getBufferSize();
    }
    
    public String getCharacterEncoding() {
	return resp.getCharacterEncoding();
    }
    
    public Locale getLocale() {
	return resp.getLocale();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
	return noBody;
    }
    
    public PrintWriter getWriter() throws UnsupportedEncodingException {
	if (writer == null) {
	    OutputStreamWriter outputstreamwriter
		= new OutputStreamWriter(noBody, getCharacterEncoding());
	    writer = new PrintWriter(outputstreamwriter);
	}
	return writer;
    }
    
    public boolean isCommitted() {
	return resp.isCommitted();
    }
    
    public void reset() throws IllegalStateException {
	resp.reset();
    }
    
    public void resetBuffer() throws IllegalStateException {
	resp.resetBuffer();
    }
    
    public void sendError(int i) throws IOException {
	resp.sendError(i);
    }
    
    public void sendError(int i, String string) throws IOException {
	resp.sendError(i, string);
    }
    
    public void sendRedirect(String string) throws IOException {
	resp.sendRedirect(string);
    }
    
    public void setBufferSize(int i) throws IllegalStateException {
	resp.setBufferSize(i);
    }
    
    void setContentLength() {
	if (!didSetContentLength)
	    resp.setContentLength(noBody.getContentLength());
    }
    
    public void setContentLength(int i) {
	resp.setContentLength(i);
	didSetContentLength = true;
    }
    
    public void setContentType(String string) {
	resp.setContentType(string);
    }
    
    public void setDateHeader(String string, long l) {
	resp.setDateHeader(string, l);
    }
    
    public void setHeader(String string, String string_1_) {
	resp.setHeader(string, string_1_);
    }
    
    public void setIntHeader(String string, int i) {
	resp.setIntHeader(string, i);
    }
    
    public void setLocale(Locale locale) {
	resp.setLocale(locale);
    }
    
    public void setStatus(int i) {
	resp.setStatus(i);
    }
    
    /**
     * @deprecated
     */
    public void setStatus(int i, String string) {
	resp.setStatus(i, string);
    }
}
