/* HttpSessionActivationListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.EventListener;

public interface HttpSessionActivationListener extends EventListener
{
    public void sessionDidActivate(HttpSessionEvent httpsessionevent);
    
    public void sessionWillPassivate(HttpSessionEvent httpsessionevent);
}
