package com.sdk.test;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class GameHttpConnection {

	static URL uri;
	static HttpURLConnection uRLConnection;
	static String key="";

	// 向服务器发送post请求
	public static String doGet() {

		// 数据处理

		String url="http://www.52game.com/pay/notify_wx?state=1&customerid=154122&superid=100000&sd51no=51CPS201512160047241541221125153&sdcustomno=132959&ordermoney=1&cardno=32&mark=www.52game.com&sign=63E5B144DD3EAB2708B7A9D48B6AE602&resign=1A356729FFD70CEBC89661801C5BDAAD&Des=51return";



		try {
			uri = new URL(url);
			uRLConnection = (HttpURLConnection) uri.openConnection();
			// 设置是否从httpUrlConnection读入，默认情况下是true;

			uRLConnection.setRequestMethod("GET");
			uRLConnection.setConnectTimeout(5 * 1000);


			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			// uRLConnection.setRequestProperty("Content-type",
			// "application/x-java-serialized-object");
			// uRLConnection.setRequestProperty("accept", "*/*");
			// uRLConnection.setRequestProperty("connection", "Keep-Alive");

			// 连接，url.openConnection()至此的配置必须要在connect之前完成，
			uRLConnection.connect();




			// out.close();

			if (uRLConnection == null || uRLConnection.getResponseCode() != 200) {
				// MyLog.i("发送失败");
				return "服务器连接失败";
			}
			// 在getInputStream()函数调用的时候，就会把准备好的http请求
			// 正式发送到服务器了，然后返回一个输入流，用于读取服务器对于此次http请求的返回信息。
			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				// response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}
