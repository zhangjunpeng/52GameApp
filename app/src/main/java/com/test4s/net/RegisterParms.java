package com.test4s.net;

import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/1/27.
 */
@HttpRequest(
        host = "http://app.4stest.com/",
        path = "user/reg"
)
public class RegisterParms  extends RequestParams{

    String imei;
    String version = "1.0";
    String package_name;
    String channel_id = "1";
    String username;
    String pwd;
    String pa;
    String code;
    String sign;

    public RegisterParms(String username,String pwd,String code,String pa) {
        imei = MyApplication.imei;
        package_name = MyApplication.packageName;
        this.username = username;
        this.pwd=pwd;
        this.code=code;
        this.pa=pa;

        TreeMap<String, String> map = new TreeMap<>();
        map.put("imei", imei);
        map.put("version", version);
        map.put("package_name", package_name);
        map.put("channel_id", channel_id);
        map.put("username", username);
        map.put("password",pwd);
        map.put("code",code);
        map.put("pa",pa);
        sign = Url.getSign(map.entrySet());
        MyLog.i("sign=====" + sign);
    }

}
