package com.view.index;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    ListView listView;
    MyAdapter myapapter;

    View footview;

    private DaoSession daoSession;
    private PtrClassicFrameLayout ptrlayout;

    int p=1;
    private MyScrollViewListener listener;
    private View headview;
    private View nomore;
    private ImageLoader imageloder=ImageLoader.getInstance();

    private static boolean first=true;
    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (myapapter!=null) {
                        myapapter.notifyDataSetChanged();
//            listView.onRefreshComplete();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession= MyApplication.daoSession;
        nomore=getTextView(getActivity());

        newInfos=new ArrayList<>();
        listener=new MyScrollViewListener();
        myapapter=new MyAdapter(getActivity());
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_information,null);
        listView= (ListView) view.findViewById(R.id.ptflistView_infolist);
        listView.setAdapter(myapapter);

        ptrlayout= (PtrClassicFrameLayout) view.findViewById(R.id.ptr_infolist);

        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footerloading,null);
        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
        drable.start();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();
        getDataFromDB();

//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });


//        initData("1");
        initPtrLayout();
        initListener();
        return view;
    }

    private void initListener() {
        listView.setOnScrollListener(listener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MyLog.i("position==="+position);
//                MyLog.i(" long id==="+id);
//                MyLog.i("id=="+newInfos.get( position).getUeser_id());
                if (view==nomore){
                    return;
                }
                NewsInfo newsInfo=newInfos.get(position);
                Intent intent=new Intent(getActivity(), InfomaionDetailActivity.class);
                intent.putExtra("id",newsInfo.getUeser_id());
                intent.putExtra("url",newsInfo.getUrl());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }

    private void initPtrLayout() {

        ptrlayout.setHeaderView(headview);

        ptrlayout.setResistance(1.7f);
        ptrlayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrlayout.setDurationToClose(200);
        ptrlayout.setDurationToCloseHeader(2000);
// default is false
        ptrlayout.setPullToRefresh(false);
// default is true
        ptrlayout.setKeepHeaderWhenRefresh(true);

//        prt_cp.setPinContent(true);

        ptrlayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.i("~~~~下拉刷新");
                p=1;
                newInfos.clear();
                removeAllFootView(listView);
                initData(p+"");
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        if (first){
            ptrlayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrlayout.autoRefresh();
                }
            },100);
            first=false;
        }
        addFootView(listView,footview);
    }
    class MyScrollViewListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        p++;
                        initData(p+"");
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
    private void initData(final String p) {

        BaseParams baseParams=new BaseParams("news/newslist");
        baseParams.addParams("p",p);
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
                    if (listView.getFooterViewsCount()==0){
                        addFootView(listView,footview);
                    }
                    jsonParser(res);
                    myapapter.notifyDataSetChanged();
                    if (ptrlayout.isRefreshing()){
                        ptrlayout.refreshComplete();
                    }
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
                JSONArray array=data.getJSONArray("newsList");
                if (array.length()==0){
                    removeAllFootView(listView);
                    addFootView(listView,nomore);
//                    CusToast.showToast(getActivity(),"没有更多信息", Toast.LENGTH_SHORT);
                }
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
    class MyAdapter extends BaseAdapter{

        private Context mcontext;

        public MyAdapter(Context contex){
            mcontext=contex;
        }
        @Override
        public int getCount() {
            return newInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return newInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_infolist,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.icon= (RoundImageView) convertView.findViewById(R.id.image_item_infoList);
                viewHolder.name= (TextView) convertView.findViewById(R.id.title_item_infoList);
                viewHolder.conment_num= (TextView) convertView.findViewById(R.id.conmentnum_item_infolist);
                viewHolder.time= (TextView) convertView.findViewById(R.id.time_item_infolist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            NewsInfo info=newInfos.get(position);
            imageloder.displayImage(Url.prePic+info.getCover_img(),viewHolder.icon, MyDisplayImageOptions.getdefaultImageOptions());
//            Picasso.with(mcontext)
//                    .load(Url.prePic+info.getCover_img())
//                    .placeholder(R.drawable.a2)
//                    .into(viewHolder.icon);
            viewHolder.name.setText(info.getTitle());
            viewHolder.conment_num.setText(info.getComments());
            viewHolder.time.setText(info.getTime());
            return convertView;
        }
        class ViewHolder{
            private RoundImageView icon;
            private TextView name;
            private TextView conment_num;
            private TextView time;
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
            myapapter.notifyDataSetChanged();
        }
    }
}
