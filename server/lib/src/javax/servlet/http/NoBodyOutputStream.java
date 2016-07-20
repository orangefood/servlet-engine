/* NoBodyOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;

class NoBodyOutputStream extends ServletOutputStream
{
    private static final String LSTRING_FILE
	= "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings
	= ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
    private int contentLength = 0;
    
    int getContentLength() {
	return contentLength;
    }
    
    public void write(int i) {
	contentLength++;
    }
    
    public void write(byte[] is, int i, int i_0_) throws IOException {
	if (i_0_ >= 0)
	    contentLength += i_0_;
	else {
	    String string = lStrings.getString("err.io.negativelength");
	    throw new IOException("negative length");
	}
    }
}
