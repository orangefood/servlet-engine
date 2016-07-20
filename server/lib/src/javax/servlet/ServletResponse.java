/* ServletResponse - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public interface ServletResponse
{
    public void flushBuffer() throws IOException;
    
    public int getBufferSize();
    
    public String getCharacterEncoding();
    
    public Locale getLocale();
    
    public ServletOutputStream getOutputStream() throws IOException;
    
    public PrintWriter getWriter() throws IOException;
    
    public boolean isCommitted();
    
    public void reset();
    
    public void resetBuffer();
    
    public void setBufferSize(int i);
    
    public void setContentLength(int i);
    
    public void setContentType(String string);
    
    public void setLocale(Locale locale);
}
