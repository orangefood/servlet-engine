/* ServletResponseWrapper - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class ServletResponseWrapper implements ServletResponse
{
    private ServletResponse response;
    
    public ServletResponseWrapper(ServletResponse servletresponse) {
	if (servletresponse == null)
	    throw new IllegalArgumentException("Response cannot be null");
	response = servletresponse;
    }
    
    public void flushBuffer() throws IOException {
	response.flushBuffer();
    }
    
    public int getBufferSize() {
	return response.getBufferSize();
    }
    
    public String getCharacterEncoding() {
	return response.getCharacterEncoding();
    }
    
    public Locale getLocale() {
	return response.getLocale();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
	return response.getOutputStream();
    }
    
    public ServletResponse getResponse() {
	return response;
    }
    
    public PrintWriter getWriter() throws IOException {
	return response.getWriter();
    }
    
    public boolean isCommitted() {
	return response.isCommitted();
    }
    
    public void reset() {
	response.reset();
    }
    
    public void resetBuffer() {
	response.resetBuffer();
    }
    
    public void setBufferSize(int i) {
	response.setBufferSize(i);
    }
    
    public void setContentLength(int i) {
	response.setContentLength(i);
    }
    
    public void setContentType(String string) {
	response.setContentType(string);
    }
    
    public void setLocale(Locale locale) {
	response.setLocale(locale);
    }
    
    public void setResponse(ServletResponse servletresponse) {
	if (servletresponse == null)
	    throw new IllegalArgumentException("Response cannot be null");
	response = servletresponse;
    }
}
