package com.test4s.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.app.tools.MD5Test;
import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/1/12.
 */
public class Url {
    //网络
    public static String  url_main="http://app.4stest.com/";

    public static String url_index=url_main+"index/index.html";

    public static String url_cplist=url_main+"index/cplist";

    public static String url_investorlist=url_main+"index/investorlist";


    //参数
    public static String key="52game.com!2015168";

    //图片前缀
    public static String prePic="http://s.52game.com/";
    public static String packageurl="";
    final static String SP_NAME="4stest";
    private static SharedPreferences sharedPreferences;


    public static String getSign(Set<Map.Entry<String, String>> set) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        StringBuffer params = new StringBuffer();

        for ( Map.Entry<String, String> entry:set) {
            if (entry.getKey().equals("otherinfo")){

            }else {
                params.append(entry.getKey());
                params.append("=");
                params.append(entry.getValue());
                params.append("&");
            }
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }
        params.append("&");
        params.append(key);
        MyLog.i("unsign===" + params);

        return MD5Test.getMD5(params.toString());

    }
    public static void saveUrl(String url){
        sharedPreferences= MyApplication.mcontext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("picpre",url);
        editor.commit();

    }

}
