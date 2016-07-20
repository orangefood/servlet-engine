/* FilterChain - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;

public interface FilterChain
{
    public void doFilter
	(ServletRequest servletrequest, ServletResponse servletresponse)
	throws IOException, ServletException;
}
