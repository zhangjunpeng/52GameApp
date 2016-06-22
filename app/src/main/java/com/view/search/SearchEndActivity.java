package com.view.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.ClearWindows;
import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchEndActivity extends BaseActivity {

    private EditText editText;
    private ImageView clearInput;
    private ImageView search;
    private ImageView back;

    ViewPager viewpager;
    private MyViewPagerAdapter adapter;

    private List<SearchEndFragment> fragmentList;

    private LinearLayout linearLayout;

    private HorizontalScrollView horizontalScrollView;
    private String[] titles={"game","cp","ip","inves","issue","outsource"};

    public String keyword;
    private String identity_cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_end);
        setImmerseLayout(findViewById(R.id.titlebar_searchend));


        keyword=getIntent().getStringExtra("keyword");

        editText= (EditText) findViewById(R.id.edit_search);
        clearInput= (ImageView) findViewById(R.id.clearinput_search);
        search= (ImageView) findViewById(R.id.search_search);
        back= (ImageView) findViewById(R.id.back_search);

        horizontalScrollView= (HorizontalScrollView) findViewById(R.id.srollview_searchend);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        linearLayout= (LinearLayout) findViewById(R.id.contianer_searchend);

        viewpager= (ViewPager) findViewById(R.id.viewpager_searchend);


        editText.setText(keyword);
        Editable etext = editText.getText();
        Selection.setSelection(etext, etext.length());

        fragmentList=new ArrayList<>();

        if (etext.length()>0){
            clearInput.setVisibility(View.VISIBLE);
        }

        initListener();

        initData(keyword);
        initView();
    }

    private void initListener() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword=editText.getText().toString();
                if (!TextUtils.isEmpty(keyword)){
                    ClearWindows.clearInput(SearchEndActivity.this,editText);
                    MyLog.i("keyword==="+keyword);
                    fragmentList.clear();
                    MyLog.i("size=="+fragmentList.size());
                    horizontalScrollView.arrowScroll(View.FOCUS_LEFT);
                    selectTitle(0);
                    initData(keyword);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearWindows.clearInput(SearchEndActivity.this,editText);
                finish();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    clearInput.setVisibility(View.VISIBLE);
                }else if (s.length()==0){
                    clearInput.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void initData(String keyword) {
        MyLog.i("kkeyword==="+keyword);
        for (int i=0;i<titles.length;i++){
            String tag=titles[i];
            switch (tag){
                case "game":
                    identity_cat="1";
                    break;
                case "cp":
                    identity_cat="2";
                    break;
                case "inves":
                    identity_cat="4";
                    break;
                case "ip":
                    identity_cat="5";
                    break;
                case "issue":
                    identity_cat="6";
                    break;
                case "outsource":
                    identity_cat="3";
                    break;
            }
            SearchEndFragment fragment=new SearchEndFragment();
            Bundle bundle=new Bundle();
            bundle.putString("identity_cat",identity_cat);
            bundle.putString("keyword", keyword);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        adapter=new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewpager.setAdapter(adapter);

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
    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<SearchEndFragment> list;


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public MyViewPagerAdapter(FragmentManager fm,List<SearchEndFragment> fragments) {
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


}
