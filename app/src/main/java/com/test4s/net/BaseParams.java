package com.test4s.net;

import android.content.res.Resources;

import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/2/23.
 */
public class BaseParams {
    TreeMap<String,String> map;
    RequestParams requestParams;
    StringBuffer mess;
    public static String urlindex="";

    public BaseParams(String uri){
//        if (MyApplication.DeBug) {
//            //测试地址
//            urlindex = MyApplication.mcontext.getString(R.string.url_index_test);
//        }else {
////            //正式地址
//            urlindex =MyApplication.mcontext.getString(R.string.url_index);
//        }
//        urlindex =MyApplication.mcontext.getString(R.string.url_index);

        if (!MyApplication.DeBug){
            urlindex=MyApplication.mcontext.getString(R.string.url_index);
        }

//        MyLog.i("version=="+MyApplication.versionName);
        mess=new StringBuffer(urlindex+uri);
        requestParams=new RequestParams(urlindex+uri);
        requestParams.addBodyParameter("imei", MyApplication.imei);
        requestParams.addBodyParameter("version",MyApplication.versionName);
        requestParams.addBodyParameter("package_name",MyApplication.packageName);
        requestParams.addBodyParameter("channel_id","1");
        requestParams.addBodyParameter("app","2");
        map=new TreeMap<>();
        map.put("imei",MyApplication.imei);
        map.put("version",MyApplication.versionName);
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("app","2");
    }

    public void addParams(String name,String value){
        requestParams.addBodyParameter(name,value);
        map.put(name, value);
    }
    public void addSign(){

        requestParams.addBodyParameter("sign",Url.getSign(map.entrySet()));
        List<KeyValue>  list=requestParams.getBodyParams();
        mess=mess.append("?");
        for (KeyValue keyvalue:requestParams.getStringParams()){
            mess=mess.append(keyvalue.key+"="+keyvalue.value+"&");
        }
        mess.deleteCharAt(mess.length()-1);
        MyLog.i("url==="+mess);

    }
    public RequestParams getRequestParams(){
        return requestParams;
    }
}
