/* ServletConfig - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.util.Enumeration;

public interface ServletConfig
{
    public String getInitParameter(String string);
    
    public Enumeration getInitParameterNames();
    
    public ServletContext getServletContext();
    
    public String getServletName();
}
