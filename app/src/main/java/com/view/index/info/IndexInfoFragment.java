package com.view.index.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test4s.gdb.Adverts;
import com.test4s.gdb.NewsInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Information.InfomaionDetailActivity;

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

    List<NewsInfo> newsList;
    List<NewsInfo> testNewsList;

    private List<Adverts> advertList;


    private ViewPager viewPager_fragment;

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
        initData(p+"");
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("news/newslist");
        baseParams.addParams("p",p);
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


}
