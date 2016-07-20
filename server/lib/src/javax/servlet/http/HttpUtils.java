/* HttpUtils - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.ServletInputStream;

public class HttpUtils
{
    private static final String LSTRING_FILE
	= "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings
	= ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
    static Hashtable nullHashtable = new Hashtable();
    
    public static StringBuffer getRequestURL
	(HttpServletRequest httpservletrequest) {
	StringBuffer stringbuffer = new StringBuffer();
	String string = httpservletrequest.getScheme();
	int i = httpservletrequest.getServerPort();
	String string_0_ = httpservletrequest.getRequestURI();
	stringbuffer.append(string);
	stringbuffer.append("://");
	stringbuffer.append(httpservletrequest.getServerName());
	if (string.equals("http") && i != 80
	    || string.equals("https") && i != 443) {
	    stringbuffer.append(':');
	    stringbuffer.append(httpservletrequest.getServerPort());
	}
	stringbuffer.append(string_0_);
	return stringbuffer;
    }
    
    private static String parseName(String string, StringBuffer stringbuffer) {
	stringbuffer.setLength(0);
	for (int i = 0; i < string.length(); i++) {
	    char c = string.charAt(i);
	    switch (c) {
	    case '+':
		stringbuffer.append(' ');
		break;
	    case '%':
		try {
		    stringbuffer.append
			((char) Integer.parseInt(string.substring(i + 1,
								  i + 3),
						 16));
		    i += 2;
		} catch (NumberFormatException numberformatexception) {
		    throw new IllegalArgumentException();
		} catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
		    String string_1_ = string.substring(i);
		    stringbuffer.append(string_1_);
		    if (string_1_.length() == 2)
			i++;
		}
		break;
	    default:
		stringbuffer.append(c);
	    }
	}
	return stringbuffer.toString();
    }
    
    public static Hashtable parsePostData
	(int i, ServletInputStream servletinputstream) {
	if (i <= 0)
	    return new Hashtable();
	if (servletinputstream == null)
	    throw new IllegalArgumentException();
	byte[] is = new byte[i];
	try {
	    int i_2_ = 0;
	    do {
		int i_3_ = servletinputstream.read(is, i_2_, i - i_2_);
		if (i_3_ <= 0) {
		    String string = lStrings.getString("err.io.short_read");
		    throw new IllegalArgumentException(string);
		}
		i_2_ += i_3_;
	    } while (i - i_2_ > 0);
	} catch (IOException ioexception) {
	    throw new IllegalArgumentException(ioexception.getMessage());
	}
	try {
	    String string = new String(is, 0, i, "8859_1");
	    return parseQueryString(string);
	} catch (UnsupportedEncodingException unsupportedencodingexception) {
	    throw new IllegalArgumentException(unsupportedencodingexception
						   .getMessage());
	}
    }
    
    public static Hashtable parseQueryString(String string) {
	Object object = null;
	if (string == null)
	    throw new IllegalArgumentException();
	Hashtable hashtable = new Hashtable();
	StringBuffer stringbuffer = new StringBuffer();
	StringTokenizer stringtokenizer = new StringTokenizer(string, "&");
	while (stringtokenizer.hasMoreTokens()) {
	    String string_4_ = stringtokenizer.nextToken();
	    int i = string_4_.indexOf('=');
	    if (i == -1)
		throw new IllegalArgumentException();
	    String string_5_
		= parseName(string_4_.substring(0, i), stringbuffer);
	    String string_6_
		= parseName(string_4_.substring(i + 1, string_4_.length()),
			    stringbuffer);
	    String[] strings;
	    if (hashtable.containsKey(string_5_)) {
		String[] strings_7_ = (String[]) hashtable.get(string_5_);
		strings = new String[strings_7_.length + 1];
		for (int i_8_ = 0; i_8_ < strings_7_.length; i_8_++)
		    strings[i_8_] = strings_7_[i_8_];
		strings[strings_7_.length] = string_6_;
	    } else {
		strings = new String[1];
		strings[0] = string_6_;
	    }
	    hashtable.put(string_5_, strings);
	}
	return hashtable;
    }
}
