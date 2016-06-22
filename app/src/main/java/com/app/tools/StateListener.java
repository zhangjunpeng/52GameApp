package com.app.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class StateListener {
	public static boolean isLauncherRunnig(Context context) {
		// 检测某Activity是否在当前Task的栈顶
		boolean result = false;
		List<String> names = getAllTheLauncher(context);
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appList = mActivityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList) {
			if (running.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (int i = 0; i < names.size(); i++) {
					if (names.get(i).equals(running.processName)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public static boolean isAppOnForeground(Context context) {
		// 判断Android应用是否在前台
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		List<RecentTaskInfo> appTask = activityManager.getRecentTasks(
				Integer.MAX_VALUE, 1);

		if (appTask == null) {
			return false;
		}

		if (appTask.get(0).baseIntent.toString().contains(packageName)) {
			return true;
		}
		return false;
	}

	private static List<String> getAllTheLauncher(Context context) {
		// 获取Android手机内安装的所有桌面
		List<String> names = null;
		PackageManager pkgMgt = context.getPackageManager();
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
		if (ra.size() != 0) {
			names = new ArrayList<String>();
		}
		for (int i = 0; i < ra.size(); i++) {
			String packageName = ra.get(i).activityInfo.packageName;
			names.add(packageName);
		}
		return names;
	}

	public static boolean isTopActivy(String cmdName, Context context) {
		// Android 判断程序前后台状态
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager
				.getRunningTasks(Integer.MAX_VALUE);
		String cmpNameTemp = null;
		if (null != runningTaskInfos) {
			cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
		}

		if (null == cmpNameTemp) {
			return false;
		}

		return cmpNameTemp.equals(cmdName);

	}
}
