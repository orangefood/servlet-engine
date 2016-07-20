/* ServletInputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet;
import java.io.IOException;
import java.io.InputStream;

public abstract class ServletInputStream extends InputStream
{
    protected ServletInputStream() {
	/* empty */
    }
    
    public int readLine(byte[] is, int i, int i_0_) throws IOException {
	if (i_0_ <= 0)
	    return 0;
	int i_1_ = 0;
	int i_2_;
	while ((i_2_ = this.read()) != -1) {
	    is[i++] = (byte) i_2_;
	    i_1_++;
	    if (i_2_ == 10 || i_1_ == i_0_)
		break;
	}
	return i_1_ > 0 ? i_1_ : -1;
    }
}
