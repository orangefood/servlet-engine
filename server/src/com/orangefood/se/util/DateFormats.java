/*
 * Created on May 7, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.orangefood.se.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author robert
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateFormats
{
	/*
	Sun, 06 Nov 1994 08:49:37 GMT  ; RFC 822, updated by RFC 1123
	Sunday, 06-Nov-94 08:49:37 GMT ; RFC 850, obsoleted by RFC 1036
	Sun Nov  6 08:49:37 1994       ; ANSI C's asctime() format
	*/	
	public static final SimpleDateFormat RFC_1123_DATE = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
	public static final SimpleDateFormat RFC_1036_DATE = new SimpleDateFormat("EEEE, dd-MMM-yy HH:mm:ss ZZZ");
	public static final SimpleDateFormat ANSI_C_DATE = new SimpleDateFormat("EEE, MMM d HH:mm:ss yyyy");

	{
			RFC_1123_DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
			RFC_1036_DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
			ANSI_C_DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
}
