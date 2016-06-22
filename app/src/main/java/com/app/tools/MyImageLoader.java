package com.app.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

public class MyImageLoader extends AsyncTask<String, Integer, Object> {
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	/**
	 * 显示图片的控件
	 */
	private ImageView mImageView;

	public MyImageLoader(ImageView imageView) {
		mImageView = imageView;
	}

	@Override
	protected Object doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		Drawable drawable = null;
		try {

			if (!"".equals(url) && url != null) {
				String fileName = url.hashCode() + ".jpg";
				// 如果缓存过就从缓存中取出数据
				if (imageCache.containsKey(fileName)) {
					SoftReference<Drawable> softReference = imageCache
							.get(fileName);
					drawable = softReference.get();
					if (drawable != null) {
						return drawable;
					}
				}
				//
				File dir = new File("52game" + "/img");
				if (!dir.exists()) {
					boolean m = dir.mkdirs();
				}
				File file = new File(dir, fileName);
				if (file.exists() && file.length() > 0) {
					// 如果文件存在则直接读取sdcard
					drawable = readFromSdcard(file);
				} else {
					// file.createNewFile();
					// Log.i(TAG, “load image from network”);
					URL imageUrl = new URL(url);
					// 写入sdcard
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						MyLog.i("写入sd卡::" + file.toString());
						saveImageFile(imageUrl, file);
						drawable = Drawable.createFromStream(
								new FileInputStream(file), fileName);
					} else {
						// 直接从流读取
						drawable = Drawable.createFromStream(
								imageUrl.openStream(), fileName);
					}
				}
				if (drawable != null) {
					// 保存在缓存中
					imageCache.put(fileName, new SoftReference<Drawable>(
							drawable));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return drawable;
	}

	private void saveImageFile(URL url, File file) {
		FileOutputStream out = null;
		InputStream in = null;
		try {
			file.deleteOnExit();
			out = new FileOutputStream(file);
			in = url.openStream();
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Drawable readFromSdcard(File file) throws Exception {
		FileInputStream in = new FileInputStream(file);
		return Drawable.createFromStream(in, file.getName());
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		Drawable drawable = (Drawable) result;
		if (mImageView != null && drawable != null) {
			mImageView.setBackgroundDrawable(drawable);
		}
	}
}
