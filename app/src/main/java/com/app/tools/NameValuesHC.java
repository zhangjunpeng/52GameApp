package com.app.tools;

import java.util.HashMap;
import java.util.Set;

public class NameValuesHC extends HashMap<String, String> {

	public NameValuesHC() {
		// TODO Auto-generated constructor stub

		String[][] value = { { "", "" }, };
		// this.
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer mes = new StringBuffer();

		Set<String> set = this.keySet();
		for (String str : set) {
			String value = this.get(str);
			mes.append(str + "=" + value + "&");
		}
		mes.deleteCharAt(mes.length() - 1);

		return new String(mes);
	}

}
