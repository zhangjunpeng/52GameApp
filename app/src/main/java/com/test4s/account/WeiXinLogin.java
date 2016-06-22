package com.test4s.account;

import android.app.Activity;
import android.content.Context;

import com.app.tools.MyLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.test4s.myapp.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/4/13.
 */
public class WeiXinLogin {
    private static WeiXinLogin instance;
    public static String APP_ID="wx53c55fc6f1efaad2";
    public IWXAPI api;
    public static int LOGIN_FALSE=4001;
    public static int LOGIN_TRUE=4000;


    private WeiXinLogin(Activity context){
        api= WXAPIFactory.createWXAPI(context,APP_ID,true);

        api.registerApp(APP_ID);
    }

    public static WeiXinLogin getInstance(Activity context){
        if (instance==null){
            instance=new WeiXinLogin(context);
        }
        return instance;
    }

    public boolean login(){
        SendAuth.Req req=new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "52game_login";
        boolean flag=api.sendReq(req);
        MyLog.i("weixin login=="+ flag);


        return flag;

    }
    public boolean bind(){
        SendAuth.Req req=new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "52game_bind";
        return api.sendReq(req);
    }

    public void refreshToken(String token, Callback.CommonCallback<String> callback){
        RequestParams params=new RequestParams("https://api.weixin.qq.com/sns/oauth2/refresh_token");
        params.addBodyParameter("appid",APP_ID);
        params.addBodyParameter("grant_type","refresh_token");
        params.addBodyParameter("refresh_token",token);
        x.http().get(params, callback);
    }
}
