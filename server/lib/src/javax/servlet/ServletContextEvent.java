/* ServletContextEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.util.EventObject;

public class ServletContextEvent extends EventObject
{
    public ServletContextEvent(ServletContext servletcontext) {
	super((Object) servletcontext);
    }
    
    public ServletContext getServletContext() {
	return (ServletContext) super.getSource();
    }
}
