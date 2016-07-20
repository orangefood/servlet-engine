/* ServletContextAttributeListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.util.EventListener;

public interface ServletContextAttributeListener extends EventListener
{
    public void attributeAdded
	(ServletContextAttributeEvent servletcontextattributeevent);
    
    public void attributeRemoved
	(ServletContextAttributeEvent servletcontextattributeevent);
    
    public void attributeReplaced
	(ServletContextAttributeEvent servletcontextattributeevent);
}
