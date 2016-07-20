/* ServletContextAttributeEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;

public class ServletContextAttributeEvent extends ServletContextEvent
{
    private String name;
    private Object value;
    
    public ServletContextAttributeEvent(ServletContext servletcontext,
					String string, Object object) {
	super(servletcontext);
	name = string;
	value = object;
    }
    
    public String getName() {
	return name;
    }
    
    public Object getValue() {
	return value;
    }
}
