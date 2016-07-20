/* HttpSession - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.Enumeration;

import javax.servlet.ServletContext;

public interface HttpSession
{
    public Object getAttribute(String string);
    
    public Enumeration getAttributeNames();
    
    public long getCreationTime();
    
    public String getId();
    
    public long getLastAccessedTime();
    
    public int getMaxInactiveInterval();
    
    public ServletContext getServletContext();
    
    /**
     * @deprecated
     */
    public HttpSessionContext getSessionContext();
    
    /**
     * @deprecated
     */
    public Object getValue(String string);
    
    /**
     * @deprecated
     */
    public String[] getValueNames();
    
    public void invalidate();
    
    public boolean isNew();
    
    /**
     * @deprecated
     */
    public void putValue(String string, Object object);
    
    public void removeAttribute(String string);
    
    /**
     * @deprecated
     */
    public void removeValue(String string);
    
    public void setAttribute(String string, Object object);
    
    public void setMaxInactiveInterval(int i);
}
