/* RequestDispatcher - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;

public interface RequestDispatcher
{
    public void forward
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws ServletException, IOException;
    
    public void include
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws ServletException, IOException;
}
