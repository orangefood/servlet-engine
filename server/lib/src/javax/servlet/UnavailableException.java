/* UnavailableException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;

public class UnavailableException extends ServletException
{
    private Servlet servlet;
    private boolean permanent;
    private int seconds;
    
    /**
     * @deprecated
     */
    public UnavailableException(int i, Servlet servlet, String string) {
	super(string);
	this.servlet = servlet;
	if (i <= 0)
	    seconds = -1;
	else
	    seconds = i;
	permanent = false;
    }
    
    public UnavailableException(String string) {
	super(string);
	permanent = true;
    }
    
    public UnavailableException(String string, int i) {
	super(string);
	if (i <= 0)
	    seconds = -1;
	else
	    seconds = i;
	permanent = false;
    }
    
    /**
     * @deprecated
     */
    public UnavailableException(Servlet servlet, String string) {
	super(string);
	this.servlet = servlet;
	permanent = true;
    }
    
    /**
     * @deprecated
     */
    public Servlet getServlet() {
	return servlet;
    }
    
    public int getUnavailableSeconds() {
	return permanent ? -1 : seconds;
    }
    
    public boolean isPermanent() {
	return permanent;
    }
}
