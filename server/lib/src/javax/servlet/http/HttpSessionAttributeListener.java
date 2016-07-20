/* HttpSessionAttributeListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.EventListener;

public interface HttpSessionAttributeListener extends EventListener
{
    public void attributeAdded
	(HttpSessionBindingEvent httpsessionbindingevent);
    
    public void attributeRemoved
	(HttpSessionBindingEvent httpsessionbindingevent);
    
    public void attributeReplaced
	(HttpSessionBindingEvent httpsessionbindingevent);
}
