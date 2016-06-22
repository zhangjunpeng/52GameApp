package com.app.tools;

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

import android.text.TextUtils;



public class GameHttpConnection {

	static URL url;
	static HttpURLConnection uRLConnection;
	static String key="52game.com!2015168";

	// 向服务器发送post请求
	public static String doPost(String urlAdd, TreeMap<String, String> headers,
			Map<String, String> body) {

		// 数据处理

		if (body != null && headers != null) {
			Iterator<Map.Entry<String, String>> iterator = body.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry1 = iterator.next();
				headers.put(entry1.getKey(), entry1.getValue());
			}
		}
		String sign = getSign(new ArrayList<Map.Entry<String, String>>(
				headers.entrySet()));

		try {
			url = new URL(urlAdd);
			uRLConnection = (HttpURLConnection) url.openConnection();
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			uRLConnection.setDoInput(true);
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setConnectTimeout(5 * 1000);
			// Post 请求不能使用缓存
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);

			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			// uRLConnection.setRequestProperty("Content-type",
			// "application/x-java-serialized-object");
			// uRLConnection.setRequestProperty("accept", "*/*");
			// uRLConnection.setRequestProperty("connection", "Keep-Alive");

			// 连接，url.openConnection()至此的配置必须要在connect之前完成，
			uRLConnection.connect();
			PrintWriter printWriter = new PrintWriter(
					uRLConnection.getOutputStream());
			// DataOutputStream out = new DataOutputStream(
			// uRLConnection.getOutputStream());
			// post写入流
			StringBuffer params = new StringBuffer();
			// List<Map.Entry<String, String>> list = new
			// ArrayList<Map.Entry<String, String>>();

			if (headers != null) {
				MyLog.i(headers.toString());
				Iterator<Map.Entry<String, String>> iterator = headers
						.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry1 = iterator.next();
					// list.add(entry1);
					params.append(entry1.getKey());
					params.append("=");
					params.append(entry1.getValue());
					params.append("&");
				}
			}

			if (!TextUtils.isEmpty(sign)) {
				params.append("sign=" + sign + "&");
			}

			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
			}

			MyLog.i("params===" + params);
			printWriter.write(params.toString());
			printWriter.flush();
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

	public static String getSign(List<Map.Entry<String, String>> list) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}

		StringBuffer params = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map.Entry<String, String> entry = list.get(i);
			params.append(entry.getKey());
			params.append("=");
			params.append(entry.getValue());
			params.append("&");

		}

		params.append(key);
		MyLog.i("unsign===" + params);

		return MD5Test.getMD5(params.toString());

	}

	public static List<Map.Entry<String, String>> sortBykey(
			List<Map.Entry<String, String>> list) {

		MyLog.i("排序前==" + list.toString());
		if (list != null) {
			for (int i = 0; i < list.size() - 1; i++) {

				for (int j = i + 1; j < list.size(); j++) {
					Map.Entry<String, String> entry1 = list.get(i);
					Map.Entry<String, String> entry2 = list.get(j);
					if (entry1.getKey().charAt(0) > entry2.getKey().charAt(0)) {
						// 首字母比较
						list.set(i, entry2);
						list.set(j, entry1);
					} else if (entry1.getKey().charAt(1) > entry2.getKey()
							.charAt(1)) {

					}
				}
			}
		}
		MyLog.i("排序后==" + list.toString());
		return list;
	}
}
