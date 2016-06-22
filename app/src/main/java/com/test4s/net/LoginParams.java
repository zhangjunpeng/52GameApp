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
        path = "user/login"
)
public class LoginParams extends RequestParams{
    String imei;
    String version;
    String package_name;
    String channel_id;
    String username;
    String password;
    String sign;
    public LoginParams(String name,String pwd){
        imei= MyApplication.imei;
        version="1.0";
        package_name=MyApplication.packageName;
        channel_id="1";
        username=name;
        password=pwd;

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",imei);
        map.put("version",version);
        map.put("package_name",package_name);
        map.put("channel_id",channel_id);
        map.put("username",username);
        map.put("password",password);
        sign=Url.getSign(map.entrySet());
        MyLog.i("sign====="+sign);
    }
}
