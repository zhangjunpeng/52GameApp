package com.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerActivity extends AppCompatActivity {

    private List<View> list_views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        setImmerseLayout();

        initView();


    }
    protected void setImmerseLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

           /* int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);*/
        }
    }

    private void initView() {

        LayoutInflater inflater=LayoutInflater.from(this);
        list_views=new ArrayList<>();

        View view1=inflater.inflate(R.layout.view1_viewpager,null);
        View view2=inflater.inflate(R.layout.view2_viewpager,null);

        list_views.add(view1);
        list_views.add(view2);


        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager_VA);
        viewPager.setAdapter(new MyViewPagerAdapter(list_views));


    }
    class MyViewPagerAdapter extends PagerAdapter{

        private List<View> list=null;

        public MyViewPagerAdapter(List<View> mlist){

            this.list=mlist;
        }

        @Override
        public int getCount() {
            return list_views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }

}
