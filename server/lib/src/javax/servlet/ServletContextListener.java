/* ServletContextListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.util.EventListener;

public interface ServletContextListener extends EventListener
{
    public void contextDestroyed(ServletContextEvent servletcontextevent);
    
    public void contextInitialized(ServletContextEvent servletcontextevent);
}
