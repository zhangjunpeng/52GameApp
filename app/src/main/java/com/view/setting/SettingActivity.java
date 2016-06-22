package com.view.setting;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;
import com.view.index.MySettingFragment;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private Fragment fragment;
    private ImageView back;
    private TextView title;
    private TextView save;

    private String tag;

    private String fragment_tag="";

    public enum fragmenttag{
        index,adisereport,aboutus,servicedeal;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        fragmentManager=getSupportFragmentManager();




        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressback();
            }
        });
        title.setText("设 置");
        save.setVisibility(View.INVISIBLE);

        tag="index";
        fragment_tag=getIntent().getStringExtra("tag");
        if (TextUtils.isEmpty(fragment_tag)){
            fragment=new Settingfragment();
            transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.contianer_setting,fragment).commit();
        }else {
            switch (fragment_tag){
                case "agreement":
                    toServiceDeal();
                    break;
            }
        }



        setImmerseLayout(findViewById(R.id.titlebar_setting));

    }

    private void pressback() {
        switch (tag){
            case "index":
                setResult(Activity.RESULT_OK);
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                finish();
                break;
            case "aboutus":
                backToSetting();
                break;
            case "advisereport":
                backToSetting();
                break;
            case "servicedeal":
                backAboutus();
                break;
        }
    }


    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public void toAboutUs(){

        tag="aboutus";
        fragment=new aboutusfragment();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        fragmentManager.beginTransaction().replace(R.id.contianer_setting,fragment).commit();
        title.setText("关于我们");
        MyLog.i("点击about_us2");
    }
    public void toAdviseReprot(){

        tag="advisereport";
        fragment=new AdviceReportFragment();
        transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        transaction.replace(R.id.contianer_setting,fragment).commit();
        title.setText("意见反馈");
        MyLog.i("点击advisereport2");
    }
    public void toServiceDeal(){

        tag="servicedeal";
        fragment=new ServiceDealFragment();
        transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        transaction.replace(R.id.contianer_setting,fragment).commit();
        title.setText("用户服务协议");
        MyLog.i("点击servicedeal2");
    }
    public void backToSetting(){
        tag="index";
        fragment=new Settingfragment();
        transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
        transaction.replace(R.id.contianer_setting,fragment).commit();
        title.setText("设 置");

    }
    public void backAboutus() {
        tag="aboutus";
        fragment=new aboutusfragment();
        transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
        transaction.replace(R.id.contianer_setting,fragment).commit();
        title.setText("关于我们");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        pressback();
    }
}
