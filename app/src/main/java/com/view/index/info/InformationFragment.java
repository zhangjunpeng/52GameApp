package com.view.index.info;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.NewsInfo;
import com.test4s.gdb.NewsInfoDao;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.MyApplication;
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
import java.util.concurrent.Executors;

import de.greenrobot.dao.query.Query;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2015/12/7.
 */
public class InformationFragment extends BaseFragment {

    List<NewsInfo> newInfos;

    RecyclerView listView;

    private DaoSession daoSession;
//    private PtrClassicFrameLayout ptrlayout;

    int p=1;
    private View headview;
    private View nomore;
    private ImageLoader imageloder=ImageLoader.getInstance();


    HomeAdapter adapter;

    String cat_id="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession= MyApplication.daoSession;
        nomore=getTextView(getActivity());


        cat_id=getArguments().getString("cat_id","1");
        switch (cat_id){
            case "1":
                newInfos=IndexInfoFragment.newsList;

                break;
            case "4":
                newInfos=IndexInfoFragment.testNewsList;

                break;
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view= (RecyclerView) inflater.inflate(R.layout.fragment_information,null);
        listView= (RecyclerView) view.findViewById(R.id.ptflistView_infolist);
//        ptrlayout= (PtrClassicFrameLayout) view.findViewById(R.id.ptr_infolist);

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        listView.setItemAnimator(new DefaultItemAnimator());


//        initPtrLayout();
        initListener();

        adapter=new HomeAdapter(newInfos);
        listView.setAdapter(adapter);
        return view;
    }

    private void initListener() {
        listView.addOnScrollListener(new MyScrollListener());
    }

//    private void initPtrLayout() {
//
//        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
//        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
//        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
//        drawable.start();
//
//        ptrlayout.setHeaderView(headview);
//
//        ptrlayout.setResistance(1.7f);
//        ptrlayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        ptrlayout.setDurationToClose(200);
//        ptrlayout.setDurationToCloseHeader(2000);
//// default is false
//        ptrlayout.setPullToRefresh(false);
//// default is true
//        ptrlayout.setKeepHeaderWhenRefresh(true);
//
////        prt_cp.setPinContent(true);
//
//        ptrlayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                MyLog.i("~~~~下拉刷新");
//                p=1;
//                newInfos.clear();
////                removeAllFootView(listView);
//                initData(p+"");
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
//            }
//        });
////        if (first){
////            ptrlayout.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    ptrlayout.autoRefresh();
////                }
////            },100);
////            first=false;
////        }
////        addFootView(listView,footview);
//    }
    private void initData(final String p) {

        BaseParams baseParams=new BaseParams("news/newslist");
        baseParams.addParams("p",p);
        baseParams.addParams("cat_id",cat_id);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            boolean su=true;
            @Override
            public void onSuccess(String result) {
                res=result;
                su=true;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                su=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("infoList=="+res);
                if (su){
                    if (p.equals("1")){
                        deleteAll();
                    }
                    jsonParser(res);
                    adapter.notifyDataSetChanged();
//                    if (ptrlayout.isRefreshing()){
//                        ptrlayout.refreshComplete();
//                    }
//                    listView.onRefreshComplete();
                }else {

                }

            }

        });
    }

    private void jsonParser(String res) {
        try {
            JSONObject jsonObject=new JSONObject(res);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                InfomaionDetailActivity.prefixUrl=data.getString("prefixUrl");

                String arrayName="";
                if(cat_id.equals("1")){
                    arrayName="newsList";
                }else {
                    arrayName="testNewsList";
                }

                JSONArray array=data.getJSONArray(arrayName);


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
                    newInfos.add(newsinfo);
                    addNewsInfo(newsinfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private NewsInfoDao getNewsInfoDao(){
        return daoSession.getNewsInfoDao();
    }
    private void addNewsInfo(NewsInfo info){
        getNewsInfoDao().insert(info);
    }
    private List searchNewsInfo(){
        Query query=getNewsInfoDao().queryBuilder()
                .build();
        return query.list();
    }
    private void deleteAll(){
        getNewsInfoDao().deleteAll();
    }
    private void getDataFromDB(){
        newInfos=searchNewsInfo();
        if (newInfos!=null){
            adapter.notifyDataSetChanged();
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        private List<NewsInfo> datalist;

        public  HomeAdapter(List<NewsInfo> datalist){
            this.datalist=datalist;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder= new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_inforecycler_fragment, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final NewsInfo newsInfo=datalist.get(position);
            imageloder.displayImage(Url.prePic+newsInfo.getCover_img(),holder.icon,MyDisplayImageOptions.getroundImageOptions());
            holder.name.setText(newsInfo.getTitle());
            holder.time.setText(newsInfo.getTime());
            holder.num.setText(newsInfo.getComments());

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), InfomaionDetailActivity.class);
                    intent.putExtra("id",newsInfo.getUeser_id());
                    intent.putExtra("url",newsInfo.getUrl());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });

        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView name;
            TextView num;
            TextView time;
            View item;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon= (ImageView) itemView.findViewById(R.id.icon);
                name= (TextView) itemView.findViewById(R.id.name);
                num= (TextView) itemView.findViewById(R.id.num);
                time= (TextView) itemView.findViewById(R.id.time);
                item=itemView;
            }
        }
    }
    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    class MyScrollListener extends RecyclerView.OnScrollListener {



        private LAYOUT_MANAGER_TYPE layoutManagerType;

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;



        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
//            LogUtils.i("onScrollStateChanged", "visibleItemCount" + visibleItemCount);
//            LogUtils.i("onScrollStateChanged", "lastVisibleItemPosition" + lastVisibleItemPosition);
//            LogUtils.i("onScrollStateChanged", "totalItemCount" + totalItemCount);
            if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
//                loadingData.onLoadMore();
                MyLog.i("RecyclerView 滚动到底部");
                p++;
                initData(p+"");
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }


    }

}
