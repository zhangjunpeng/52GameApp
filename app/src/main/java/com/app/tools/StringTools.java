package com.app.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	public static boolean isHaveChinese(String mes) {
		boolean temp = false;
		Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m=p.matcher(mes);
		if(m.find()){
			temp =  true;
		}
		return temp;

	}

	public static boolean isHaveBlank(String mes) {
		for (int i = 0; i < mes.length(); i++) {
			String a = mes.charAt(i) + "";
			if (a.equals(" ")) {
				return true;
			}
		}
		return false;
	}

	public static boolean matches(String mes) {
		String regex = "^[0-9A-Za-z]{4,30}$";
		return mes.matches(regex);
	}
}
