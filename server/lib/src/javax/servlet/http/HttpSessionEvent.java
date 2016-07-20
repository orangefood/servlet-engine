/* HttpSessionEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.util.EventObject;

public class HttpSessionEvent extends EventObject
{
    public HttpSessionEvent(HttpSession httpsession) {
	super((Object) httpsession);
    }
    
    public HttpSession getSession() {
	return (HttpSession) super.getSource();
    }
}
