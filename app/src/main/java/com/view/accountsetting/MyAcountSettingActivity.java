package com.view.accountsetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.app.tools.MyLog;
import com.tencent.tauth.Tencent;
import com.test4s.account.MyAccount;
import com.test4s.account.SinaWeiboLogin;
import com.test4s.account.UserInfo;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class MyAcountSettingActivity extends BaseActivity {

    FragmentManager fragmentManager;
    Fragment fragment;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_acount_setting);
        fragmentManager=getSupportFragmentManager();

        initUserInfo();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (BaseFragment.selectedFragment instanceof MyAcountSettingFragment){
            setResult(Activity.RESULT_OK);
            super.onBackPressed();
        }else {
            MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
            transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        MyLog.i("MyAcountSetting setting bind");
        Tencent.onActivityResultData(requestCode,resultCode,data,BindotherFragment.lisener);
        if (SinaWeiboLogin.getInstance(this).mSsoHandler != null) {
            SinaWeiboLogin.getInstance(this).mSsoHandler .authorizeCallBack(requestCode, resultCode, data);
        }

    }

    private void initUserInfo() {
        BaseParams baseParams=new BaseParams("user/index");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("UserInfo==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        userInfo=new UserInfo();
                        JSONObject jsonObject2=jsonObject.getJSONObject("data");
                        JSONObject jsonObject3=jsonObject2.getJSONObject("userInfo");
                        userInfo.setId(jsonObject3.getString("id"));
                        userInfo.setUsername(jsonObject3.getString("username"));
                        userInfo.setNickname(jsonObject3.getString("nickname"));
                        userInfo.setEmail(jsonObject3.getString("email"));
                        userInfo.setPhone(jsonObject3.getString("phone"));
                        userInfo.setAvatar(jsonObject3.getString("avatar"));
                        userInfo.setUser_identity(jsonObject3.getString("user_identity"));
                        userInfo.setGame_like(jsonObject3.getString("game_like"));
                        userInfo.setJob_id(jsonObject3.getString("job_id"));
                        userInfo.setEdu_id(jsonObject3.getString("edu_id"));
                        userInfo.setProvince(jsonObject3.getString("province"));
                        userInfo.setCity(jsonObject3.getString("city"));
                        userInfo.setCounty(jsonObject3.getString("county"));
                        userInfo.setAddr(jsonObject3.getString("addr"));
                        userInfo.setProvince_name(jsonObject3.getString("province_name"));
                        userInfo.setCity_name(jsonObject3.getString("city_name"));
                        userInfo.setCounty_name(jsonObject3.getString("county_name"));
                        userInfo.setEdu_name(jsonObject3.getString("edu_name"));
                        userInfo.setJob_name(jsonObject3.getString("job_name"));

                        MyAccount.getInstance().setUserInfo(userInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i("UserInfo==="+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                initData();
                fragment=new MyAcountSettingFragment();
                fragmentManager.beginTransaction().replace(R.id.contianner_mysetting,fragment).commit();
            }
        });
    }
}
