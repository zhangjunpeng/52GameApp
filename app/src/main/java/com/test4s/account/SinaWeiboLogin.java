package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/13.
 */
public class SinaWeiboLogin implements WeiboAuthListener{



    private static SinaWeiboLogin instance;

    public static final String SCOPE =                               // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private AuthInfo mAuthInfo;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    public static Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public SsoHandler mSsoHandler;

    private SinaWeiboLogin(Activity context) {
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(context, "963258147", "https://api.weibo.com/oauth2/default.html",SCOPE);
        mSsoHandler = new SsoHandler(context, mAuthInfo);
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
    }

    public static SinaWeiboLogin getInstance(Activity ac){
        if (instance==null){
            MyLog.i("创建实例");
            instance=new SinaWeiboLogin(ac);
        }
        return instance;
    }

    public void login(WeiboAuthListener listener){
        MyLog.i("微博登录");

        mSsoHandler.authorize(listener);
    }
    public void bind(WeiboAuthListener listener){
        mSsoHandler.authorize(listener);
    }

    @Override
    public void onComplete(Bundle values) {

    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onWeiboException(WeiboException e) {
        }
    }
}
