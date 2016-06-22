package com.app.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkState {
	public static boolean getNetState(Context context) {
		ConnectivityManager ctm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo nio = ctm.getActiveNetworkInfo();
		return nio != null && nio.isConnectedOrConnecting();

	}
}
