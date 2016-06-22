package com.view.accountsetting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.test4s.account.LoginQQActivity;
import com.test4s.account.LoginSINAActivity;
import com.test4s.account.LoginWXActivity;
import com.test4s.account.MyAccount;
import com.test4s.account.SinaWeiboLogin;
import com.test4s.account.TencentLogin;
import com.test4s.account.WeiXinLogin;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/7.
 */
public class BindotherFragment extends BaseFragment implements View.OnClickListener {

    View view;

    private LinearLayout weixin_linear;
    private LinearLayout qq_linear;
    private LinearLayout sina_linear;

    private TextView weixin_text;
    private TextView qq_text;
    private TextView sina_text;

    private com.test4s.account.UserInfo userinfo;

    private ImageView back;
    private TextView title;
    private TextView save;
    public static IUiListener lisener;

    public static final int BIND=666;

    public static final int BIND_SUCCESS=3001;
    public static final int BIND_CANCEL=3002;
    public static final int BIND_FAILD=3003;
    public static final int LOGIN_FALSE=4001;
    public static final int HASBIND=3009;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lisener=new IUiListener() {
            @Override
            public void onComplete(Object o) {
                MyLog.i("qq back=="+o.toString());
                String res=o.toString();
                try {
                    JSONObject jsob=new JSONObject(res);
                    String openid=jsob.getString("openid");
                    String token=jsob.getString("access_token");
                    String expir=jsob.getString("expires_in");

                    getQQUserInfo(openid,token,expir);

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
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_bindother,null);

        setImmerseLayout(view.findViewById(R.id.title_bindother));

        weixin_linear= (LinearLayout) view.findViewById(R.id.linear_weixin_bind);
        qq_linear= (LinearLayout) view.findViewById(R.id.linear_qq_bind);
        sina_linear= (LinearLayout) view.findViewById(R.id.linear_sina_bind);

        weixin_text= (TextView) view.findViewById(R.id.text_weixin_bind);
        qq_text= (TextView) view.findViewById(R.id.text_qq_bind);
        sina_text= (TextView) view.findViewById(R.id.text_sina_bind);

        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        save= (TextView) view.findViewById(R.id.save_savebar);

        userinfo= MyAccount.getInstance().getUserInfo();

        weixin_linear.setOnClickListener(this);
        qq_linear.setOnClickListener(this);
        sina_linear.setOnClickListener(this);

        title.setText("绑定第三方账号");
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();

    }

    private void initView() {
//        MyLog.i("qq_sina=="+userinfo.getQq_sign());
//        MyLog.i("weixin_sina=="+userinfo.getWeixin_sign());
//        MyLog.i("sina=="+userinfo.getSina_sign());


        if (!TextUtils.isEmpty(userinfo.getQq_sign())){
            qq_linear.setClickable(false);
            qq_text.setText("已绑定");
            qq_text.setTextColor(Color.rgb(124,124,124));
        }
        if (!TextUtils.isEmpty(userinfo.getWeixin_sign())){
            weixin_linear.setClickable(false);
            weixin_text.setText("已绑定");
            weixin_text.setTextColor(Color.rgb(124,124,124));

        }
        if (!TextUtils.isEmpty(userinfo.getSina_sign())){
            sina_linear.setClickable(false);
            sina_text.setText("已绑定");
            sina_text.setTextColor(Color.rgb(124,124,124));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_weixin_bind:
                bindWeixin();
                break;
            case R.id.linear_qq_bind:
                bindQq();
                break;
            case R.id.linear_sina_bind:
                bindSina();
                break;
        }
    }



    private void bindSina() {
        Intent intent=new Intent(getActivity(), LoginSINAActivity.class);
        intent.putExtra("tag","bind");
        startActivityForResult(intent,BIND);
        getActivity().overridePendingTransition(R.anim.in_from_right,0);
//        SinaWeiboLogin sina=SinaWeiboLogin.getInstance(getActivity());
//        sina.bind(new WeiboAuthListener() {
//            @Override
//            public void onComplete(Bundle values) {
//                MyLog.i("vaulues=="+values.toString());
//                // 从 Bundle 中解析 Token
//                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//                //从这里获取用户输入的 电话号码信息
//                String phoneNum = mAccessToken.getPhoneNum();
//                if (mAccessToken.isSessionValid()) {
//                    MyLog.i("weibo toke ==" + mAccessToken);
//                    String info="";
//                    JSONObject jsonObject=new JSONObject();
//                    String nickname=values.getString("userName","");
//                    if (!TextUtils.isEmpty(nickname)){
//                        try {
//                            jsonObject.put("type","SINA");
//                            jsonObject.put("nick",nickname);
//                            jsonObject.put("name",nickname);
//                            jsonObject.put("head","");
//                            info=jsonObject.toString();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    sendToServer("sina",mAccessToken.getUid(),info);
//                } else {
//                    // 以下几种情况，您会收到 Code：
//                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
//                    // 2. 当您注册的应用程序包名与签名不正确时；
//                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//                    String code = values.getString("code");
//                    if (!TextUtils.isEmpty(code)) {
//                        MyLog.i("weibo ");
//                    }
//                }
//            }
//
//            @Override
//            public void onWeiboException(WeiboException e) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });

    }

    private void bindQq() {
//        showDialog();
//        TencentLogin qqLogin=TencentLogin.getIntance(getActivity(), lisener);
//        qqLogin.login();
        MyLog.i("qq bind start");
        Intent intent=new Intent(getActivity(), LoginQQActivity.class);
        intent.putExtra("tag","bind");
        startActivityForResult(intent,BIND);
        getActivity().overridePendingTransition(R.anim.in_from_right,0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.i("bindother fragment bind");
        if (requestCode==BIND){
            switch (resultCode){
                case BIND_SUCCESS:
                    CusToast.showToast(getActivity(),"绑定成功", Toast.LENGTH_SHORT);
                    initView();
                    break;
                case BIND_FAILD:
                    CusToast.showToast(getActivity(),"绑定失败", Toast.LENGTH_SHORT);
                    break;
                case LOGIN_FALSE:
                    CusToast.showToast(getActivity(),"请检查微信客户端",Toast.LENGTH_SHORT);
                    break;
                case BIND_CANCEL:
                    CusToast.showToast(getActivity(),"绑定取消", Toast.LENGTH_SHORT);

                    break;
                case HASBIND:
                    CusToast.showToast(getActivity(),"该账号已经被绑定", Toast.LENGTH_SHORT);
                    break;

            }
        }
    }

    private void getQQUserInfo(final String openid, String toekn, String expir) {

        TencentLogin tencentLogin=TencentLogin.getIntance(this);
        tencentLogin.mtencent.setOpenId(openid);
        tencentLogin.mtencent.setAccessToken(toekn,expir);
        tencentLogin.getUserInfo(getActivity(),new IUiListener() {
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

    private void bindWeixin() {

        Intent intent=new Intent(getActivity(), LoginWXActivity.class);
        intent.putExtra("tag","bind");
        startActivityForResult(intent,BIND);
        getActivity().overridePendingTransition(R.anim.in_from_right,0);
//        WeiXinLogin weixinLogin=WeiXinLogin.getInstance(getActivity());
//        weixinLogin.bind();
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
                        switch (type){
                            case "qq":
                                MyAccount.getInstance().getUserInfo().setQq_sign(data.getString("qq_sign"));
                                break;
                            case "sina":
                                MyAccount.getInstance().getUserInfo().setSina_sign(data.getString("sina_sign"));

                                break;
                        }
                        initView();
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
