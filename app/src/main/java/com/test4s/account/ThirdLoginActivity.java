package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.test4s.myapp.R;
import com.test4s.myapp.wxapi.WXEntryActivity;
import com.test4s.net.BaseParams;
import com.view.Evaluation.EvaluationActivity;
import com.view.messagecenter.MessageList;
import com.view.myattention.AttentionActivity;
import com.view.myreport.ReprotListActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.concurrent.Executors;

public class ThirdLoginActivity extends Activity{
    private MyAccount myAccount;
    public final static int BIND_PHONE=20009;



    public void thirdLoginBind(String regtype, String openid, String info) {
        MyLog.i("绑定手机号1");
        Intent intent=new Intent(this, AccountActivity.class);
        intent.putExtra("third",regtype);
        intent.putExtra("uniqueid",openid);
        intent.putExtra("info",info);
        startActivityForResult(intent,BIND_PHONE);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        MyLog.i("绑定手机号2");
    }

    public void loginSuccess() {
        myAccount=MyAccount.getInstance();
        myAccount.isLogin=true;
        MyLog.i("登录成功：："+myAccount.toString());
        setResult(Activity.RESULT_OK);
        finish();
    }
}
