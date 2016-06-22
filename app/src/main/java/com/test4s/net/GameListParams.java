package com.test4s.net;

import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/1/20.
 */
@HttpRequest(
        host = "http://app.4stest.com/",
        path = "game/gamelist"
)
public class GameListParams  extends RequestParams{
    public String imei;
    public String version = "1.0";
    public String package_name;
    public String channel_id = "1";
    public String sign;
    public String p;

    public GameListParams(String page){
        imei= MyApplication.imei;
        package_name=MyApplication.packageName;
        p=page;

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",          imei);
        map.put("version",       version);
        map.put("package_name",  package_name);
        map.put("channel_id",    channel_id);
        map.put("p",p);

        sign=Url.getSign(map.entrySet());
        MyLog.i("sign====="+sign);
    }
}
