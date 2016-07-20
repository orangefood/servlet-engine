/* ServletOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class ServletOutputStream extends OutputStream
{
    private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
    private static ResourceBundle lStrings
	= ResourceBundle.getBundle("javax.servlet.LocalStrings");
    
    protected ServletOutputStream() {
	/* empty */
    }
    
    public void print(char c) throws IOException {
	print(String.valueOf(c));
    }
    
    public void print(double d) throws IOException {
	print(String.valueOf(d));
    }
    
    public void print(float f) throws IOException {
	print(String.valueOf(f));
    }
    
    public void print(int i) throws IOException {
	print(String.valueOf(i));
    }
    
    public void print(long l) throws IOException {
	print(String.valueOf(l));
    }
    
    public void print(String string) throws IOException {
	if (string == null)
	    string = "null";
	int i = string.length();
	for (int i_0_ = 0; i_0_ < i; i_0_++) {
	    char c = string.charAt(i_0_);
	    if ((c & '\uff00') != 0) {
		String string_1_ = lStrings.getString("err.not_iso8859_1");
		Object[] objects = new Object[1];
		objects[0] = new Character(c);
		string_1_ = MessageFormat.format(string_1_, objects);
		throw new CharConversionException(string_1_);
	    }
	    this.write(c);
	}
    }
    
    public void print(boolean bool) throws IOException {
	String string;
	if (bool)
	    string = lStrings.getString("value.true");
	else
	    string = lStrings.getString("value.false");
	print(string);
    }
    
    public void println() throws IOException {
	print("\r\n");
    }
    
    public void println(char c) throws IOException {
	print(c);
	println();
    }
    
    public void println(double d) throws IOException {
	print(d);
	println();
    }
    
    public void println(float f) throws IOException {
	print(f);
	println();
    }
    
    public void println(int i) throws IOException {
	print(i);
	println();
    }
    
    public void println(long l) throws IOException {
	print(l);
	println();
    }
    
    public void println(String string) throws IOException {
	print(string);
	println();
    }
    
    public void println(boolean bool) throws IOException {
	print(bool);
	println();
    }
}
