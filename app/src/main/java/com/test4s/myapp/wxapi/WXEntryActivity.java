package com.test4s.myapp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.account.WeiXinLogin;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.accountsetting.BindotherFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/13.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    private MyAccount myAccount;

    public static String access_token;

    public String refresh_token;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        text= (TextView) findViewById(R.id.text_wexinentry);
        WeiXinLogin.getInstance(this).api.handleIntent(getIntent(),this);
        MyLog.i("弹出WXEntryActivity");
        myAccount=MyAccount.getInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeiXinLogin.getInstance(this).api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //微信发送的请求
//        MyLog.i("weixin login back=="+baseReq.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        //发送到微信请求的响应结果
//        MyLog.i("weixin login back=="+resp.openId);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Bundle bundle=new Bundle();
                resp.toBundle(bundle);
                MyLog.i("weixin login back=="+bundle.toString());
                refresh_token=bundle.getString("_wxapi_sendauth_resp_token");
                MyLog.i("wx =="+resp.checkArgs());
                String data=bundle.getString("_wxapi_sendauth_resp_url");
                String[]  params=data.split("\\?");
                MyLog.i("params 1=="+params[1]);
                String code=params[1].split("&")[0];
                String code2=code.split("=")[1];

                String state=bundle.getString("_wxapi_sendauth_resp_state");
                if (state.equals("52game_login")){
                    sendToServer("weixin",code2,null,null);

                }else if (state.equals("52game_bind")){
                    sendToBind("weixin",code2,null,MyAccount.getInstance().getToken());
                }


                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                text.setText("取消登录");
                MyLog.i("ERR_USER_CANCEL");
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                MyLog.i("ERR_AUTH_DENIED");
                break;
            default:
                break;
        }

    }

    private void sendToBind(String type, String uid, final String info, String token) {
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
                MyLog.i("bind weixin back=="+result);
                try {
                    JSONObject jsob=new JSONObject(result);
                    boolean su=jsob.getBoolean("success");
                    int code=jsob.getInt("code");
                    if (su&&code==200){
                        text.setText("绑定成功!!");
                        JSONObject data=jsob.getJSONObject("data");
                        MyAccount.getInstance().getUserInfo().setWeixin_sign(data.getString("weixin_sign"));
                        setResult(Activity.RESULT_OK);
                        finish();
                    }else {
                        String mess=jsob.getString("msg");
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


    public void sendToServer(String type, String uid, final String info, String token) {
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
                MyLog.i("weixin send code back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean login=jsonObject.getBoolean("success");
                    if (login){

                        boolean firstlogin=false;
                        if (result.contains("uniqueid")) {
                            firstlogin = true;
                        }
//                        WeiXinLogin.getInstance(WXEntryActivity.this).refreshToken(refresh_token, new CommonCallback<String>() {
//                            @Override
//                            public void onSuccess(String result) {
//                                MyLog.i("Refresh token back=="+result);
//                            }
//
//                            @Override
//                            public void onError(Throwable ex, boolean isOnCallback) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(CancelledException cex) {
//
//                            }
//
//                            @Override
//                            public void onFinished() {
//
//                            }
//                        });

                        if (firstlogin){

                            JSONObject data=jsonObject.getJSONObject("data");
                            String uid=data.getString("uniqueid");
                            String otherInfo=data.getString("otherinfo");
                            Intent intent=new Intent(WXEntryActivity.this, AccountActivity.class);
                            intent.putExtra("third","weixin");
                            intent.putExtra("uniqueid",uid);
                            intent.putExtra("info",otherInfo);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }else {
                            MyLog.i("第三方登录成功2");
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            myAccount.setNickname(jsonObject1.getString("nickname"));
                            myAccount.setUsername(jsonObject1.getString("username"));
                            myAccount.setToken(jsonObject1.getString("token"));
                            myAccount.setAvatar(jsonObject1.getString("avatar"));
                            myAccount.saveUserInfo();
                            MyLog.i("第三方登录成功1");
                            myAccount.isLogin=true;
                            setResult(Activity.RESULT_OK);
//                            MyLog.i("登录成功2");
                            finish();
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

}
