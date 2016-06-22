package com.test4s.net;

import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/1/15.
 */
@HttpRequest(
        host = "http://app.4stest.com/",
        path = "index/detail"
)
public class CPDetailParams extends RequestParams{
    private String imei;
    private String version="1.0";
    private String package_name;
    private String channel_id="1";
    private String user_id;
    private String identity_cat;
    private String sign;

    public CPDetailParams(String user_id,String identity_cat){
        this.user_id=user_id;
        this.identity_cat=identity_cat;
        imei= MyApplication.imei;
        package_name=MyApplication.packageName;

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",imei);
        map.put("version",version);
        map.put("package_name",package_name);
        map.put("channel_id",channel_id);
        map.put("user_id",user_id);
        map.put("identity_cat",identity_cat);

        sign=Url.getSign(map.entrySet());
        MyLog.i("sign===="+sign);


    }
}
