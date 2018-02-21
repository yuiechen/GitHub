package com.digiwin.mutilangutil.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static boolean isEmptyOrSpace(Object pObject) {
		return isEmpty(pObject) || pObject.toString().trim().equals("");
	}

	/**
	 * 判斷是否為null<p>
	 * 
	 * @param pObject 輸入要判斷的值
	 * @return true or false
	 */
	public static boolean isEmpty(Object pObject) {
		return (pObject == null || pObject.toString().trim().equals(""));
	}

	
	public static String toString(Object o) {
		if (o != null) {
			return o.toString();
		} else 
			return "";
	}
	
	public static Integer toInteger(Object o) {
		if (o != null) {
			return Integer.parseInt(o.toString());
		} else {
			return new Integer(0);
		}
	}
	
	public static String formatDate(Date date, String format) throws Exception {
		String formatStr = "";
		try {
			if (date != null) {
				SimpleDateFormat sf = new SimpleDateFormat(format);
				formatStr = sf.format(date);
			} 
		} catch(Exception e) {
			throw e;
		}
		return formatStr;
	}
	
	/**
	 * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs  the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
			    return false;
			}
		}
		return true;
	}
	
	
    public static String replaceBlank(String str) {  
        String dest = "";  
        if (str!=null) {  
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
            Matcher m = p.matcher(str);  
            dest = m.replaceAll("");  
        }  
        return dest;  
    }

	
}
