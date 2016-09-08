package com.view.index.info;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.NewsInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Information.InfomaionDetailActivity;
import com.view.index.adapter.GameLayoutAdapter;
import com.view.index.adapter.LayoutAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class IndexInfoFragment extends Fragment{

    private int p=1;

    public static List<NewsInfo> newsList;
    public static List<NewsInfo> testNewsList;

    private List<Adverts> advertList;

    private List<Fragment> fragmentList;

    private ViewPager viewPager_fragment;

    FragemgAdapter fragemgAdapter;
    private RecyclerViewPager recyclerViewPager;
    private GameLayoutAdapter adapter;


    private List<TextView> textViewList;
    private List<ImageView> lineList;

    private RelativeLayout rela1;
    private RelativeLayout rela2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            MyLog.i("info 重新获取数据");
            initData(p+"");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_indexinfo,container,false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager_fragment= (ViewPager) view.findViewById(R.id.viewpager_fragment);
        recyclerViewPager= (RecyclerViewPager) view.findViewById(R.id.viewpager);

        TextView textView1= (TextView) view.findViewById(R.id.text1);
        TextView textView2= (TextView) view.findViewById(R.id.text2);

        ImageView line1= (ImageView) view.findViewById(R.id.line1);
        ImageView line2= (ImageView) view.findViewById(R.id.line2);

        rela1= (RelativeLayout) view.findViewById(R.id.rela1);
        rela2= (RelativeLayout) view.findViewById(R.id.rela2);

        textViewList=new ArrayList<>();
        lineList=new ArrayList<>();

        textViewList.add(textView1);
        textViewList.add(textView2);
        lineList.add(line1);
        lineList.add(line2);


        rela1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(0);
            }
        });
        rela2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(1);
            }
        });

        viewPager_fragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        initViewPager();
    }

    private void selectFragment(int position) {
        for (int i=0;i<textViewList.size();i++){
            TextView text = textViewList.get(i);
            if (i==position){
                text.setTextColor(Color.rgb(255, 156, 0));
                lineList.get(i).setVisibility(View.VISIBLE);
            }else {
                text.setTextColor(Color.rgb(76, 76, 76));
                lineList.get(i).setVisibility(View.INVISIBLE);
            }
        }
        viewPager_fragment.setCurrentItem(position);
    }

    private void initViewPager() {
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewPager.setLayoutManager(layout);
        recyclerViewPager.setHasFixedSize(true);
        recyclerViewPager.setLongClickable(true);

//        recyclerViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (recyclerViewPager.getChildCount() < 3) {
//                    if (recyclerViewPager.getChildAt(1) != null) {
//                        if (recyclerViewPager.getCurrentPosition() == 0) {
//                            View v1 = recyclerViewPager.getChildAt(1);
//                            v1.setScaleY(0.9f);
//                            v1.setScaleX(0.9f);
//                        } else {
//                            View v1 = recyclerViewPager.getChildAt(0);
//                            v1.setScaleY(0.9f);
//                            v1.setScaleX(0.9f);
//                        }
//                    }
//                } else {
//                    if (recyclerViewPager.getChildAt(0) != null) {
//                        View v0 = recyclerViewPager.getChildAt(0);
//                        v0.setScaleY(0.9f);
//                        v0.setScaleX(0.9f);
//                    }
//                    if (recyclerViewPager.getChildAt(2) != null) {
//                        View v2 = recyclerViewPager.getChildAt(2);
//                        v2.setScaleY(0.9f);
//                        v2.setScaleX(0.9f);
//                    }
//                }
//
//            }
//        });


    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("news/newslist");
        baseParams.addParams("p","1");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                jsonparser(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                fragmentList=new ArrayList<>();
                InformationFragment fragment1=new InformationFragment();
                InformationFragment fragment2=new InformationFragment();
                fragmentList.add(fragment1);
                fragmentList.add(fragment2);

                Bundle bundle1=new Bundle();
                bundle1.putString("cat_id","4");

                Bundle bundle2=new Bundle();
                bundle2.putString("cat_id","1");

                fragment1.setArguments(bundle1);
                fragment2.setArguments(bundle2);

                fragemgAdapter=new FragemgAdapter(getChildFragmentManager());
                viewPager_fragment.setAdapter(fragemgAdapter);


                adapter=new GameLayoutAdapter(getActivity(),recyclerViewPager,advertList);
                recyclerViewPager.setAdapter(adapter);
            }
        });
    }

    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (code==200&&su){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                InfomaionDetailActivity.prefixUrl=data.getString("prefixUrl");

                JSONArray advertArray=data.getJSONArray("adverts");
                advertList=new ArrayList<>();
                for (int i=0;i<advertArray.length();i++){
                    Adverts advert=new Adverts();
                    JSONObject advertObj=advertArray.getJSONObject(i);
                    advert.setAdvert_pic(advertObj.getString("advert_pic"));
                    advert.setAdvert_name(advertObj.getString("advert_name"));
                    advertList.add(advert);
                }

                JSONArray array=data.getJSONArray("newsList");
                newsList=new ArrayList<>();
                for (int i=0;i<array.length();i++){
                    NewsInfo newsinfo=new NewsInfo();
                    JSONObject info=array.getJSONObject(i);
                    newsinfo.setUeser_id(info.getString("id"));
                    newsinfo.setTitle(info.getString("title"));
                    newsinfo.setViews(info.getString("views"));
                    newsinfo.setComments(info.getString("comments"));
                    newsinfo.setCover_img(info.getString("cover_img"));
                    newsinfo.setUrl(info.getString("url"));
                    newsinfo.setTime(info.getString("time"));
                    newsList.add(newsinfo);
                }

                JSONArray testNewsArray=data.getJSONArray("testNewsList");
                testNewsList=new ArrayList<>();
                for (int i=0;i<testNewsArray.length();i++){
                    NewsInfo newsinfo=new NewsInfo();
                    JSONObject info=testNewsArray.getJSONObject(i);
                    newsinfo.setUeser_id(info.getString("id"));
                    newsinfo.setTitle(info.getString("title"));
                    newsinfo.setViews(info.getString("views"));
                    newsinfo.setComments(info.getString("comments"));
                    newsinfo.setCover_img(info.getString("cover_img"));
                    newsinfo.setUrl(info.getString("url"));
                    newsinfo.setTime(info.getString("time"));
                    testNewsList.add(newsinfo);
                }


            }
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    class FragemgAdapter extends FragmentPagerAdapter{

        public FragemgAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
