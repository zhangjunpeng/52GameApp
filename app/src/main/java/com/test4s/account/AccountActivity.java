package com.test4s.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.LoginParams;
import com.view.Evaluation.EvaluationActivity;
import com.view.activity.BaseActivity;
import com.view.coustomrequire.CustomizedListActivity;
import com.view.messagecenter.MessageList;
import com.view.myattention.AttentionActivity;
import com.view.myreport.ReprotListActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Iterator;

public class AccountActivity extends BaseActivity {


    LoginFragment loginFragment;
    FragmentManager fragmentManager;
    String tag="login";
    MyAccount myAccount=MyAccount.getInstance();
    static String  third="";
    String uid;

    public final static int QQ_LOGIN=1001;
    public final static int SINA_LOGIN=1002;
    public final static int WX_LOGIN=1003;

    public final static int THIRD_LOGIN=1009;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tag=getIntent().getStringExtra("tag");
        third=getIntent().getStringExtra("third");
        fragmentManager=getSupportFragmentManager();
        if (TextUtils.isEmpty(third)){
            loginFragment=new LoginFragment();
            Bundle bundle=new Bundle();
            bundle.putString("tag",tag);
            loginFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.contianer_loginActivity,loginFragment).commit();
        }else {
            uid=getIntent().getStringExtra("uniqueid");
            String info=getIntent().getStringExtra("info");
            switch (third){
                case "weixin":
                    thirdLoginBind("weixin",uid,info);
                    break;
                case "sina":
                    thirdLoginBind("sina",uid,info);
                    break;
                case "qq":
                    thirdLoginBind("qq",uid,info);
                    break;
            }
        }


    }


    public void backlogin(){

        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
        transaction.replace(R.id.contianer_loginActivity,loginFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.i("AccountActivity  onActivityResult");
        if (requestCode==THIRD_LOGIN&&resultCode==Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            finish();
            overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
        }
        if (requestCode==WX_LOGIN&&resultCode==Activity.RESULT_CANCELED){
            setResult(Activity.RESULT_CANCELED);
            finish();
            overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void thirdLoginBind(String regtype, String openid, String info) {

        TirdLoginBindPhone fragment=new TirdLoginBindPhone();
        Bundle bundle=new Bundle();
        bundle.putString("regtype",regtype);
        bundle.putString("openid",openid);
        bundle.putString("info",info);
        MyLog.i("第三方登录 info=="+info);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        fragmentManager.beginTransaction().replace(R.id.contianer_loginActivity,fragment).commit();
        MyLog.i("第三方登录 绑定手机号3");
    }

    private void loginSuccess() {
        myAccount.isLogin=true;
        Intent intent=null;
        MyLog.i("登录成功1=="+tag);
        switch (tag){
            case "login":

                break;
            case "messagecenter":
                intent= new Intent(this, MessageList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "pc":
                intent= new Intent(this, EvaluationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "attention":
                intent= new Intent(this, AttentionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "report":
                intent= new Intent(this, ReprotListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case "sina":
                break;
            case "customized":
                intent= new Intent(this, CustomizedListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            default:
                break;
        }

        MyLog.i("登录成功：："+myAccount.toString());
        setResult(Activity.RESULT_OK);
        MyLog.i("登录成功2");
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
    }
}
