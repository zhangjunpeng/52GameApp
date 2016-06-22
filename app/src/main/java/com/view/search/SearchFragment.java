package com.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test4s.myapp.R;

import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/3/23.
 */
public class SearchFragment extends Fragment{
    String tag="";
    ViewPager viewPager;
    private String[] titles={"game","cp","ip","inves","issue","outsource"};

    private String keyword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        tag=getArguments().getString("tag","game");
        keyword=getArguments().getString("keyword","莽荒纪");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_attention,container,false);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager_attention);

        initData();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        String identity_cat="";
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
        Search.search(keyword, identity_cat, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

    class MyPagerAdapter extends PagerAdapter{

        public MyPagerAdapter() {

        }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }
}
