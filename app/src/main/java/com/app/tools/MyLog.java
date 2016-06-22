package com.app.tools;

import android.util.Log;


public class MyLog {

	public static void i(String mes) {
//		if (!GameSDK.isDebug) {
//			return;
//		}
		Log.i("4STest", StringTools.decodeUnicode(mes));
	}
}
