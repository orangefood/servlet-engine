/* Cookie - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.servlet.http;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Cookie implements Cloneable
{
    private static final String LSTRING_FILE
	= "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings
	= ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
    private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxAge = -1;
    private String path;
    private boolean secure;
    private int version = 0;
    private static final String tspecials = ",;";
    
    public Cookie(String string, String string_0_) {
	if (!isToken(string) || string.equalsIgnoreCase("Comment")
	    || string.equalsIgnoreCase("Discard")
	    || string.equalsIgnoreCase("Domain")
	    || string.equalsIgnoreCase("Expires")
	    || string.equalsIgnoreCase("Max-Age")
	    || string.equalsIgnoreCase("Path")
	    || string.equalsIgnoreCase("Secure")
	    || string.equalsIgnoreCase("Version")) {
	    String string_1_ = lStrings.getString("err.cookie_name_is_token");
	    Object[] objects = new Object[1];
	    objects[0] = string;
	    string_1_ = MessageFormat.format(string_1_, objects);
	    throw new IllegalArgumentException(string_1_);
	}
	name = string;
	value = string_0_;
    }
    
    public Object clone() {
	try {
	    return super.clone();
	} catch (CloneNotSupportedException clonenotsupportedexception) {
	    throw new RuntimeException(clonenotsupportedexception
					   .getMessage());
	}
    }
    
    public String getComment() {
	return comment;
    }
    
    public String getDomain() {
	return domain;
    }
    
    public int getMaxAge() {
	return maxAge;
    }
    
    public String getName() {
	return name;
    }
    
    public String getPath() {
	return path;
    }
    
    public boolean getSecure() {
	return secure;
    }
    
    public String getValue() {
	return value;
    }
    
    public int getVersion() {
	return version;
    }
    
    private boolean isToken(String string) {
	int i = string.length();
	for (int i_2_ = 0; i_2_ < i; i_2_++) {
	    char c = string.charAt(i_2_);
	    if (c < ' ' || c >= '\u007f' || ",;".indexOf(c) != -1)
		return false;
	}
	return true;
    }
    
    public void setComment(String string) {
	comment = string;
    }
    
    public void setDomain(String string) {
	domain = string.toLowerCase();
    }
    
    public void setMaxAge(int i) {
	maxAge = i;
    }
    
    public void setPath(String string) {
	path = string;
    }
    
    public void setSecure(boolean bool) {
	secure = bool;
    }
    
    public void setValue(String string) {
	value = string;
    }
    
    public void setVersion(int i) {
	version = i;
    }
}
