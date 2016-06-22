package com.view.Introduce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test4s.myapp.MainActivity;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroduceActivity extends BaseActivity {

    ViewPager viewPager;
    private LinearLayout linear;
    private List<Fragment> fragmentlist;
    private SharedPreferences sharedPreferences;
    private static final String SP_NAME = "4stest";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_introduce);
        viewPager= (ViewPager) findViewById(R.id.viewpager_introduce);
        linear= (LinearLayout) findViewById(R.id.dots_linear_intro);
        initView();

        initListener();
    }

    private void initView() {
        fragmentlist=new ArrayList<>();
        for (int i=0;i<3;i++){
            Bundle bundle=new Bundle();
            bundle.putInt("index",i);
            IntroduceFragment fragment=new IntroduceFragment();
            fragment.setArguments(bundle);
            fragmentlist.add(fragment);
        }
        IntroFragementAdapter adapter=new IntroFragementAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class IntroFragementAdapter extends FragmentPagerAdapter{

        public IntroFragementAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences= MyApplication.mcontext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putBoolean("isFirstin",false);
        edit.commit();
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void changeDot(int position){
        for (int i=0;i<linear.getChildCount();i++){
            ImageView dot= (ImageView) linear.getChildAt(i);
            dot.setImageResource(R.drawable.orangedot_gray);
        }
        ImageView dot= (ImageView) linear.getChildAt(position);
        dot.setImageResource(R.drawable.orangedot);
    }

}
