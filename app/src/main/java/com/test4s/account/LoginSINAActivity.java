package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.accountsetting.BindotherFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/6/14.
 */
public class LoginSINAActivity extends ThirdLoginActivity {

    private ImageView image;
    private AuthListener listener;

    private static Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private MyAccount myAccount;

    private String tag="login";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        tag=getIntent().getStringExtra("tag");


        myAccount=MyAccount.getInstance();

        image= (ImageView) findViewById(R.id.image_third);
        AnimationDrawable drawable= (AnimationDrawable) image.getBackground();
        drawable.start();
        setFinishOnTouchOutside(false);

        listener=new AuthListener();


        MyLog.i(getLocalClassName()+" call");


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    sinaLogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==BIND_PHONE && resultCode== Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            MyLog.i("third activity login onActivityResult:sina");
            if (mSsoHandler != null) {
                mSsoHandler .authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    private void sinaLogin() {
        mAuthInfo = new AuthInfo(this, "963258147", "https://api.weibo.com/oauth2/default.html",SinaWeiboLogin.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        if (tag.equals("login")){
            mSsoHandler.authorize(new AuthListener());
        }else if (tag.equals("bind")){
            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle values) {
                    MyLog.i("vaulues=="+values.toString());
                    // 从 Bundle 中解析 Token
                    Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                    //从这里获取用户输入的 电话号码信息
                    String phoneNum = mAccessToken.getPhoneNum();
                    if (mAccessToken.isSessionValid()) {
                        MyLog.i("weibo toke ==" + mAccessToken);
                        String info="";
                        JSONObject jsonObject=new JSONObject();
                        String nickname=values.getString("userName","");
                        if (!TextUtils.isEmpty(nickname)){
                            try {
                                jsonObject.put("type","SINA");
                                jsonObject.put("nick",nickname);
                                jsonObject.put("name",nickname);
                                jsonObject.put("head","");
                                info=jsonObject.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sendToServer("sina",mAccessToken.getUid(),info);
                    } else {
                        // 以下几种情况，您会收到 Code：
                        // 1. 当您未在平台上注册的应用程序的包名与签名时；
                        // 2. 当您注册的应用程序包名与签名不正确时；
                        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                        String code = values.getString("code");
                        if (!TextUtils.isEmpty(code)) {
                            MyLog.i("weibo ");
                        }
                    }
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    finish();

                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
        }

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
            MyLog.i("vaulues=="+values.toString());
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
//                updateTokenView(false);
                MyLog.i("weibo toke ==" + mAccessToken);
                String info="";
                JSONObject jsonObject=new JSONObject();

                String nickname=values.getString("userName","");
                if (!TextUtils.isEmpty(nickname)){
                    try {
                        jsonObject.put("type","SINA");
                        jsonObject.put("nick",nickname);
                        jsonObject.put("name",nickname);
                        jsonObject.put("head","");
                        info=jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                send("sina",mAccessToken.getUid(),info,null);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                if (!TextUtils.isEmpty(code)) {
                    MyLog.i("weibo "+code);
                }
            }
        }
        @Override
        public void onCancel() {

            MyLog.i("取消微博登录");
            finish();
        }
        @Override
        public void onWeiboException(WeiboException e) {
            MyLog.i("sina error::"+e.toString());
            finish();
        }
    }

    public static void WeiBologinout(Context context){
        mAccessToken=new Oauth2AccessToken();
        AccessTokenKeeper.clear(context);
    }
    public void send(final String type, final String uid, final String info, String token) {
        BaseParams baseParams=new BaseParams("user/thirdlogin");
        baseParams.addParams("logintype",type);
        baseParams.addParams("uniqueid",uid);
        if (!TextUtils.isEmpty(info)) {
            baseParams.addParams("otherinfo",info);

        }
        if (!TextUtils.isEmpty(token)) {
            baseParams.addParams("token", token);
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("send uid back=="+result);
                switch (tag){
                    case "login":
                        loginParser(result,type,uid,info);
                        break;
                    case "bind":
                        bindParser(result);
                        break;
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void bindParser(String result) {
        MyLog.i("bind back=="+result);
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                MyAccount.getInstance().getUserInfo().setSina_sign(data.getString("sina_sign"));
                setResult(BindotherFragment.BIND_SUCCESS);
            }else {
                setResult(BindotherFragment.BIND_FAILD);
            }
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loginParser(String result, String type, String uid, String info) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean login=jsonObject.getBoolean("success");
            if (login){

                boolean firstlogin=false;
                if (result.contains("uniqueid")) {
                    firstlogin = true;
                }

                if (firstlogin){
                    thirdLoginBind(type,uid,info);

                }else {
                    MyLog.i("第三方登录成功2");
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");

                    myAccount.setNickname(jsonObject1.getString("nickname"));
                    myAccount.setUsername(jsonObject1.getString("username"));
                    myAccount.setToken(jsonObject1.getString("token"));
                    myAccount.setAvatar(jsonObject1.getString("avatar"));
                    CusToast.showToast(LoginSINAActivity.this,"登录成功", Toast.LENGTH_SHORT);
                    myAccount.saveUserInfo();
                    MyLog.i("第三方登录成功1");
                    loginSuccess();
                }

            }else {
                String mes=jsonObject.getString("msg");
//                        Toast.makeText(AccountActivity.this,mes,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(final String type, final String uid, final String info) {
        BaseParams baseParams=new BaseParams("user/thirdlogin");
        baseParams.addParams("logintype",type);
        baseParams.addParams("uniqueid",uid);
        baseParams.addParams("otherinfo",info);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("bind back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        JSONObject data=jsonObject.getJSONObject("data");
                        MyAccount.getInstance().getUserInfo().setSina_sign(data.getString("sina_sign"));
                        setResult(BindotherFragment.BIND_SUCCESS);
                        finish();
//                        initView();
                    }else {
                        String mess=jsonObject.getString("msg");
                        if (mess.equals("已绑定")){
                            setResult(BindotherFragment.HASBIND);
                        }else {
                            setResult(BindotherFragment.BIND_CANCEL);
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
