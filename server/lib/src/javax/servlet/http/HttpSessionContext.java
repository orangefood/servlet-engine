/* HttpSessionContext - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.Enumeration;

public interface HttpSessionContext
{
    /**
     * @deprecated
     */
    public Enumeration getIds();
    
    /**
     * @deprecated
     */
    public HttpSession getSession(String string);
}
