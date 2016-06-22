package com.view.myattention;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AttentionActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout linearLayout;

    private HorizontalScrollView horizontalScrollView;
    private ViewPager viewpager;
    private String[] titles={"game","cp","ip","inves","issue","outsource"};
    private List<AttentionFragment> fragmentList;
    private ImageView back;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        linearLayout= (LinearLayout) findViewById(R.id.contianer_attention_title);
        viewpager= (ViewPager) findViewById(R.id.viewpager_attention);
        horizontalScrollView= (HorizontalScrollView) findViewById(R.id.srollview_attention);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        title.setText("我的关注");
        back.setOnClickListener(this);
        save.setVisibility(View.INVISIBLE);

        setImmerseLayout(findViewById(R.id.titlebar_attention));

        initData();
        initView();
    }

    private void initData() {
        fragmentList=new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            AttentionFragment fragment=new AttentionFragment();
            Bundle bundle=new Bundle();
            bundle.putString("tag",titles[i]);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
    }

    private void initView() {
        for (int i=0;i<titles.length;i++){
            final int j=i;
            linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTitle(j);
                    if (j>3) {
                        horizontalScrollView.arrowScroll(View.FOCUS_RIGHT);
                    }else if(j<2){
                        horizontalScrollView.arrowScroll(View.FOCUS_LEFT);
                    }
                    viewpager.setCurrentItem(j);
                }
            });
        }
        selectTitle(0);
        viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTitle(position);
                if (position>3) {
                    horizontalScrollView.arrowScroll(View.FOCUS_RIGHT);
                }else if(position<2){
                    horizontalScrollView.arrowScroll(View.FOCUS_LEFT);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_savebar:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }

    }

    class MyViewPagerAdapter extends FragmentPagerAdapter{
        private List<AttentionFragment> list;


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public MyViewPagerAdapter(FragmentManager fm,List<AttentionFragment> fragments) {
            super(fm);
            list=fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }


    }
    public void selectTitle(int position){
        for (int i=0;i<linearLayout.getChildCount();i++){
            if (i==position){
                LinearLayout linearLayout1= (LinearLayout) linearLayout.getChildAt(i);
                TextView textView= (TextView) linearLayout1.getChildAt(0);
                ImageView line= (ImageView) linearLayout1.getChildAt(1);
                textView.setTextColor(Color.rgb(255,157,0));
                line.setBackgroundColor(Color.rgb(255,157,0));
            }else {
                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(i);
                TextView textView = (TextView) linearLayout1.getChildAt(0);
                ImageView line = (ImageView) linearLayout1.getChildAt(1);
                textView.setTextColor(Color.rgb(76, 76, 76));
                line.setBackgroundColor(Color.WHITE);
            }
        }
    }


}
