/* ServletException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;

public class ServletException extends Exception
{
    private Throwable rootCause;
    
    public ServletException() {
	/* empty */
    }
    
    public ServletException(String string) {
	super(string);
    }
    
    public ServletException(String string, Throwable throwable) {
	super(string);
	rootCause = throwable;
    }
    
    public ServletException(Throwable throwable) {
	super(throwable.getLocalizedMessage());
	rootCause = throwable;
    }
    
    public Throwable getRootCause() {
	return rootCause;
    }
}
