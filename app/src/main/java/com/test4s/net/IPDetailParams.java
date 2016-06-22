package com.test4s.net;

import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;

import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/1/15.
 */


@HttpRequest(
        host = "http://app.4stest.com/",
        path = "index/ipdetail"
)
public class IPDetailParams {
    public String imei;
    public String version="1.0";
    public String package_name;
    public String channel_id="1";
    public String id;
    public String sign;

    public IPDetailParams(String id){
        imei= MyApplication.imei;
        package_name=MyApplication.packageName;
        this.id=id;

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",imei);
        map.put("version",version);
        map.put("package_name",package_name);
        map.put("channel_id",channel_id);
        map.put("id",id);

        sign=Url.getSign(map.entrySet());
        MyLog.i("sign====="+sign);


    }




}
