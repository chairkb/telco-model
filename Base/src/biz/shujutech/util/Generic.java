package biz.shujutech.util;

import static biz.shujutech.base.App.ERROR_COMPLAIN;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import java.net.URI;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generic {
	 /**
	 * <p>Checks if the String contains only unicode digits.
	 * A decimal point is not a unicode digit and returns false.</p>
	 *
	 * <p><code>null</code> will return <code>false</code>.
	 * An empty String (length()=0) will return <code>true</code>.</p>
	 *
	 * <pre>
	 * StringUtils.isNumeric(null)   = false
	 * StringUtils.isNumeric("")	 = true
	 * StringUtils.isNumeric("  ")   = false
	 * StringUtils.isNumeric("123")  = true
	 * StringUtils.isNumeric("12 3") = false
	 * StringUtils.isNumeric("ab2c") = false
	 * StringUtils.isNumeric("12-3") = false
	 * StringUtils.isNumeric("12.3") = false
	 * </pre>
	 *
	 * @param str  the String to check, may be null
	 * @return <code>true</code> if only contains digits, and is non-null
	 */
	public static boolean IsDigit(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>Checks whether the String a valid Java number.</p>
	 *
	 * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
	 * qualifier, scientific notation and numbers marked with a type
	 * qualifier (e.g. 123L).</p>
	 *
	 * <p><code>Null</code> and empty String will return
	 * <code>false</code>.</p>
	 *
	 * @param str  the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean IsNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9')
						&& (chars[i] < 'a' || chars[i] > 'f')
						&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
			  // for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent   
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns
				&& (chars[i] == 'd'
					|| chars[i] == 'D'
					|| chars[i] == 'f'
					|| chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l'
				|| chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static String Substr(String aStr, int aStart, int aEnd) {
		String result = aStr;
		if (aEnd > aStr.length()) {
			result = aStr.substring(aStart);
		} else {
			result = aStr.substring(aStart, aEnd);
		}
		return(result);
	}

	public static boolean IsEmptyOrNull(String cs) {
		return cs == null || cs.isEmpty();
	}

	public static boolean IsEmptyOrNull(Clasz cs) {
		return cs == null;
	}

	public static boolean IsEmptyOrNull(Integer cs) {
		return cs == null;
	}

	public static String StringBufferNewValue(StringBuffer aBuffer, String aNewStr) {
		aBuffer.replace(0, aBuffer.length(), aNewStr);
		return(aBuffer.toString());
	}

	public static String SubstrFromPosition(String aStr, int aStartPosition) {
		String result = "";
		if (aStr.length() > aStartPosition) {
			result = aStr.substring(aStartPosition);
		}
		return(result);
	}

	public static String TrimLen(String aStr, int aLen) {
		aStr = aStr.substring(0, Math.min(aStr.length(), aLen));
		return(aStr);
	}

	public static String TrimPadVerifyLen(String aFieldName, String aStr, int aLen, boolean aLeftJustify) throws Exception {
		if (aStr.length() > aLen) {
			throw new Hinderance("Error, the field: '" + aFieldName + "'" + ", have invalid length size" + ERROR_COMPLAIN);
		}
		return TrimPadVerifyLen(aStr, aLen, aLeftJustify);
	}

	public static String TrimPadVerifyLen(String aFieldName, String aStr, int aLen) throws Exception {
		if (aStr.length() > aLen) {
			throw new Hinderance("Error, the field: '" + aFieldName + "'" + ", have invalid length size" + ERROR_COMPLAIN);
		}

		return TrimPadVerifyLen(aStr, aLen);
	}

	public static String TrimPadVerifyLen(String aStr, int aLen, boolean aLeftJustify) throws Exception {
		return TrimPadVerifyLenJustify(aStr, aLen, aLeftJustify);
	}

	public static String TrimPadVerifyLen(String aStr, int aLen) throws Exception {
		return TrimPadVerifyLenJustify(aStr, aLen, false);
	}

	private static String TrimPadVerifyLenJustify(String aStr, int aLen, boolean aLeftJustify) throws Exception {
		if (aStr.length() > aLen) {
			throw new Hinderance("Error, string exceeded length to trim: '" + aStr + "'" + ERROR_COMPLAIN);
		}
		return(TrimPadLen(aStr, aLen, aLeftJustify));
	}

	public static String TrimPadLen(String aStr, int aLen) {
		return TrimPadLen(aStr, aLen, false);
	}

	public static String TrimPadLen(String aStr, int aLen, boolean aLeftJustify) {
		String justify = "-";
		if (aLeftJustify) justify = "";
		aStr = TrimLen(aStr, aLen);
		return String.format("%1$" + justify + aLen + "s", aStr);
	}

	public static String GetRandom(int len) {
		char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		char[] theChar = new char[len];
		Random random = new Random();
		for (int cntr = 0; cntr < len; cntr++) {
			theChar[cntr] = ch[random.nextInt(ch.length)];
		}
		return new String(theChar);
	}

	public static String GetRandomNum(int len) {
		char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		char[] theChar = new char[len];
		Random random = new Random();
		for (int cntr = 0; cntr < len; cntr++) {
			theChar[cntr] = ch[random.nextInt(ch.length)];
		}
		return new String(theChar);
	}

	public static String ExtractStartingChar(String aStr) {
		String result = "";
		for(int cntr = 0; cntr < aStr.length(); cntr++) {
			char charOrDigit = aStr.charAt(cntr);
			if (Character.isDigit(charOrDigit) == false) {
				result += charOrDigit;
			}
		}
		return(result);
	}

	public static String Null2Blank(String aStr) {
		if (aStr == null) {
			return "";
		} else {
			return aStr;
		}
	}	

	public static String Null2Zero(Integer aInt) throws Exception {
		if (aInt == null) {
			return "";
		} else {
			return String.valueOf(aInt);
		}
	}

	public static Double String2Double(String aStr) {
		Double result = null;
		if (aStr != null && aStr.isEmpty() == false) {
			String numOrDot = aStr.replaceAll(",", "");
			result = Double.parseDouble(numOrDot);
		}
		return(result);
	}

	public static double String2DoubleOrZero(String aStr) {
		Double result = String2Double(aStr);
		if (result == null) {
			return(0);
		} 
		return(result);
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static boolean IsValidEmail(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public static String GetUrlLeafNameWithoutParam(String aStrUrl) throws Exception {
		URI uri = new URI(aStrUrl);
		String path = uri.getPath();
		String leafName = path.substring(path.lastIndexOf('/') + 1);
		return(leafName);
	}
}
