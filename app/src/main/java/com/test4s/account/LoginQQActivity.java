package com.test4s.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.accountsetting.BindotherFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.concurrent.Executors;

public class LoginQQActivity extends ThirdLoginActivity {

    private String qq_openid;
    private String qq_token;
    private String qq_expir;

    private TencentLogin tencentLogin;
    private MyAccount myAccount;
    private ImageView image;

    private static Tencent mtencent;
    private static LoginQQActivity instance;

    private String tag="login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);


        tag=getIntent().getStringExtra("tag");

        MyLog.i("tag=="+tag);

        instance=this;

        myAccount=MyAccount.getInstance();
        image= (ImageView) findViewById(R.id.image_third);
        AnimationDrawable drawable= (AnimationDrawable) image.getBackground();
        drawable.start();
        setFinishOnTouchOutside(false);




        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    switch (tag){
                        case "login":
                            qqlogin();
                            break;
                        case "bind":
                            qqBind();
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void qqBind() {
        tencentLogin=TencentLogin.getIntance(this);
        mtencent=tencentLogin.mtencent;
        if (!mtencent.isSessionValid())
        {
            MyLog.i("bindlisener=="+iulistener.toString());
            MyLog.i("qq bind back::=="+ mtencent.login(this, "all", bindlisener));

        }else {
            MyLog.i("checkLogin");

            mtencent.checkLogin(bindlisener);
        }
    }

    private void qqlogin() {
        tencentLogin=TencentLogin.getIntance(this);
        mtencent=tencentLogin.mtencent;
        MyLog.i("qq登录");
        if (!mtencent.isSessionValid())
        {
            MyLog.i("iulistener=="+iulistener.toString());
            MyLog.i("qq login back::=="+ mtencent.login(this, "all", iulistener));

        }else {
            MyLog.i("checkLogin");

            mtencent.checkLogin(iulistener);
        }
    }
    IUiListener bindlisener=new IUiListener() {
        @Override
        public void onComplete(Object o) {
            MyLog.i("qq back=="+o.toString());
            String res=o.toString();
            try {
                JSONObject jsob=new JSONObject(res);
                String openid=jsob.getString("openid");
                String token=jsob.getString("access_token");
                String expir=jsob.getString("expires_in");

                getQQUserInfo2(openid,token,expir);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            finish();

        }

        @Override
        public void onCancel() {
            finish();
        }
    };

    IUiListener iulistener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            MyLog.i("qq back==" + o.toString());
            String res = o.toString();
            try {
                JSONObject jsob = new JSONObject(res);
                qq_openid = jsob.getString("openid");
                qq_token = jsob.getString("access_token");
                qq_expir = jsob.getString("expires_in");

                send("qq", qq_openid, null, null);

            } catch (JSONException e) {
                e.printStackTrace();
                if (!TextUtils.isEmpty(qq_openid)) {
                    send("qq", qq_openid, null, null);
                }
            }
        }

        @Override
        public void onError(UiError uiError) {
            MyLog.i("qq login error::"+uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            MyLog.i("qq login onCancel");
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==BIND_PHONE && resultCode== Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            if (tag.equals("login")) {
                Tencent.onActivityResultData(requestCode, resultCode, data, iulistener);
            }else {
                Tencent.onActivityResultData(requestCode, resultCode, data, bindlisener);

            }
        }

    }
    private void getQQUserInfo2(final String openid, String toekn, String expir) {
        tencentLogin.mtencent.setOpenId(openid);
        tencentLogin.mtencent.setAccessToken(toekn,expir);
        tencentLogin.getUserInfo(this,new IUiListener() {
            @Override
            public void onComplete(Object o) {
                MyLog.i("qq UserInfo==="+o.toString());
                try {
                    JSONObject userinfo=new JSONObject(o.toString());
                    String nick=userinfo.getString("nickname");
                    String head=userinfo.getString("figureurl_qq_2");
                    if (TextUtils.isEmpty(head)){
                        head=userinfo.getString("figureurl_qq_1");
                    }
                    JSONObject info=new JSONObject();
                    info.put("type","QQ");
                    info.put("name",nick);
                    info.put("nick",nick);
                    info.put("head",head);
                    sendToServer("qq",openid,info.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
    private void getQQUserInfo1(final String logintype) {

        tencentLogin.mtencent.setOpenId(qq_openid);
        tencentLogin.mtencent.setAccessToken(qq_token,qq_expir);
        tencentLogin.getUserInfo(this,new IUiListener() {
            @Override
            public void onComplete(Object o) {
                MyLog.i("qq UserInfo==="+o.toString());
                try {
                    JSONObject userinfo=new JSONObject(o.toString());
                    String nick=userinfo.getString("nickname");
                    String head=userinfo.getString("figureurl_qq_2");
                    if (TextUtils.isEmpty(head)){
                        head=userinfo.getString("figureurl_qq_1");
                    }
                    JSONObject info=new JSONObject();
                    info.put("type","QQ");
                    info.put("name",nick);
                    info.put("nick",nick);
                    info.put("head",head);
                    thirdLoginBind(logintype,qq_openid,info.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
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
                            getQQUserInfo1(type);

                        }else {
                            MyLog.i("第三方登录成功2");
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");

                            myAccount.setNickname(jsonObject1.getString("nickname"));
                            myAccount.setUsername(jsonObject1.getString("username"));
                            myAccount.setToken(jsonObject1.getString("token"));
                            myAccount.setAvatar(jsonObject1.getString("avatar"));
                            CusToast.showToast(LoginQQActivity.this,"登录成功",Toast.LENGTH_SHORT);
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

    private void bindParser(String result) {

    }

    private void loginparser(String result) {

    }

    public static void loginOut(){
        if (mtencent!=null){
            mtencent.logout(instance);
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
                        MyAccount.getInstance().getUserInfo().setQq_sign(data.getString("qq_sign"));
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
