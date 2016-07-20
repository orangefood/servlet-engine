/* HttpSessionBindingListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.EventListener;

public interface HttpSessionBindingListener extends EventListener
{
    public void valueBound(HttpSessionBindingEvent httpsessionbindingevent);
    
    public void valueUnbound(HttpSessionBindingEvent httpsessionbindingevent);
}
