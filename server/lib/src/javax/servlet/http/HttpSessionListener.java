/* HttpSessionListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.EventListener;

public interface HttpSessionListener extends EventListener
{
    public void sessionCreated(HttpSessionEvent httpsessionevent);
    
    public void sessionDestroyed(HttpSessionEvent httpsessionevent);
}
