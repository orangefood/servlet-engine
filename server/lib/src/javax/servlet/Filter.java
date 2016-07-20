/* Filter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;

public interface Filter
{
    public void destroy();
    
    public void doFilter
	(ServletRequest servletrequest, ServletResponse servletresponse,
	 FilterChain filterchain)
	throws IOException, ServletException;
    
    public void init(FilterConfig filterconfig) throws ServletException;
}
