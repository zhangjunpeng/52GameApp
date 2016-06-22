package com.view.accountsetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tencent.tauth.Tencent;
import com.test4s.account.SinaWeiboLogin;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

public class MyAcountSettingActivity extends BaseActivity {

    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount_setting);
        fragmentManager=getSupportFragmentManager();

        fragment=new MyAcountSettingFragment();
        fragmentManager.beginTransaction().replace(R.id.contianner_mysetting,fragment).commit();


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
}
