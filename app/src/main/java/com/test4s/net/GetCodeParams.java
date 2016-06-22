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
        path = "sms/index"
)
public class GetCodeParams extends RequestParams{
    String imei;
    String version = "1.0";
    String package_name;
    String channel_id = "1";
    String phone;
    String sign;

    public GetCodeParams(String phoneNum) {
        imei = MyApplication.imei;
        package_name = MyApplication.packageName;
        this.phone = phoneNum;

        TreeMap<String, String> map = new TreeMap<>();
        map.put("imei", imei);
        map.put("version", version);
        map.put("package_name", package_name);
        map.put("channel_id", channel_id);
        map.put("phone", phoneNum);
        sign = Url.getSign(map.entrySet());
        MyLog.i("sign=====" + sign);
    }
}
