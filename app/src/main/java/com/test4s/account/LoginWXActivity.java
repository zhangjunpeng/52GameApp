package com.test4s.account;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.accountsetting.BindotherFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/6/14.
 */
public class LoginWXActivity extends ThirdLoginActivity {
    private ImageView image;
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

        MyLog.i(getLocalClassName()+" call");

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    switch (tag){
                        case "bind":
                            wxBind();
                            break;
                        case "login":
                            wxlogin();

                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void wxBind() {
        WeiXinLogin weixinLogin=WeiXinLogin.getInstance(this);
        if (weixinLogin.bind()){
        } else {
            setResult(WeiXinLogin.LOGIN_FALSE);
        }
        finish();
    }

    private void wxlogin() {
        boolean islogin=WeiXinLogin.getInstance(this).login();
        if (islogin){
//            setResult(WeiXinLogin.LOGIN_TRUE);
        }else {
            setResult(WeiXinLogin.LOGIN_FALSE);
        }
        finish();
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
                            CusToast.showToast(LoginWXActivity.this,"登录成功", Toast.LENGTH_SHORT);
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

    public void getWXUserInfo(String token,String oid){
        RequestParams params=new RequestParams("https://api.weixin.qq.com/sns/userinfo");
        params.addBodyParameter("access_token",token);
        params.addBodyParameter("openid",oid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("WX UserInfo=="+result);
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
