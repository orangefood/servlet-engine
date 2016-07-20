/* HttpSessionBindingEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;

public class HttpSessionBindingEvent extends HttpSessionEvent
{
    private String name;
    private Object value;
    
    public HttpSessionBindingEvent(HttpSession httpsession, String string) {
	super(httpsession);
	name = string;
    }
    
    public HttpSessionBindingEvent(HttpSession httpsession, String string,
				   Object object) {
	super(httpsession);
	name = string;
	value = object;
    }
    
    public String getName() {
	return name;
    }
    
    public HttpSession getSession() {
	return super.getSession();
    }
    
    public Object getValue() {
	return value;
    }
}
