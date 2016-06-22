package com.view.s4server;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;

import com.squareup.picasso.Picasso;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/2/18.
 */
public class InvesmentListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    View view;

    List<InvesmentSimpleInfo> invesmentSimpleInfos;
    ListView listView;
    MyIssueAdapter myAdapter;


    int p=1;

    private Button refreash;
    private boolean recommend;

    View showall;
    private PtrClassicFrameLayout prt_cp;
    private View footview;
    private View headview;
    private MyScrollViewListener listener;
    private int Foot_flag;
    private View nomore;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        listener=new MyScrollViewListener();
        nomore=getTextView(getActivity());

        Bundle bundle=getArguments();
        if (bundle!=null){
            recommend=bundle.getBoolean("recommend",false);
        }
        invesmentSimpleInfos=new ArrayList<>();
        myAdapter=new MyIssueAdapter(getActivity(),invesmentSimpleInfos);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        listView= (ListView) view.findViewById(R.id.pullToRefresh_fglist);
        listView.setAdapter(myAdapter);

        prt_cp= (PtrClassicFrameLayout) view.findViewById(R.id.prt_cplist);
        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footerloading,null);
        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
        drable.start();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        initPtrLayout();

        initLisener();
        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);

                initData("1");
            }
        });


        return view;
    }
    private void initPtrLayout() {

        prt_cp.setHeaderView(headview);

        prt_cp.setResistance(1.7f);
        prt_cp.setRatioOfHeaderHeightToRefresh(1.2f);
        prt_cp.setDurationToClose(200);
        prt_cp.setDurationToCloseHeader(1000);
// default is false
        prt_cp.setPullToRefresh(false);
// default is true
        prt_cp.setKeepHeaderWhenRefresh(true);

//        prt_cp.setPinContent(true);

        prt_cp.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.i("~~~~下拉刷新");
                p=1;
                invesmentSimpleInfos.clear();
                listView.setOnScrollListener(listener);

                initData(p+"");
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        prt_cp.postDelayed(new Runnable() {
            @Override
            public void run() {
                prt_cp.autoRefresh();
            }
        },100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view==nomore){
            return;
        }
        Intent intent=new Intent(getActivity(),InvesmentDetialActivity.class);
        InvesmentSimpleInfo info=invesmentSimpleInfos.get((int) id);
        intent.putExtra("user_id",info.getUser_id());
        intent.putExtra("identity_cat",info.getIdentity_cat());
        startActivity(intent);
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
    private void initLisener() {
        showall=LayoutInflater.from(getActivity()).inflate(R.layout.showall,null);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Invesment_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        listView.setOnScrollListener(listener);
        listView.setOnItemClickListener(this);

        Foot_flag=1;
    }

    private void initData(String p) {

        BaseParams baseParams=new BaseParams("index/investorlist");
        if (recommend){
            baseParams.addParams("ret","2");
        }
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(30*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;

            @Override
            public void onSuccess(String result) {
                res=result;

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                prt_cp.setVisibility(View.GONE);
                CusToast.showToast(getActivity(),"请检查网络",Toast.LENGTH_SHORT);


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (listView.getFooterViewsCount()==0){
                    listView.addFooterView(footview);
                }
                if (Foot_flag!=1){
                    listView.removeFooterView(showall);
                    listView.removeFooterView(nomore);
                    listView.addFooterView(footview);
                    Foot_flag=1;
                }
                jsonParser(res);
                myAdapter.notifyDataSetChanged();
                if (prt_cp.isRefreshing()) {
                    prt_cp.refreshComplete();
                }
            }

        });
    }

    private void jsonParser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray issues=jsonObject1.getJSONArray("investorList");
                if (issues.length()==0){
                    listView.setOnScrollListener(null);
                    if (recommend){
                        MyLog.i("添加查看全部");
                        listView.removeFooterView(footview);
                        listView.addFooterView(showall);
                        Foot_flag=2;
                    }else {
                        listView.removeFooterView(footview);
                        listView.addFooterView(nomore);
                        Foot_flag=3;
//                        CusToast.showToast(getActivity(), "没有更多开发者信息", Toast.LENGTH_SHORT);
                    }
                }
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    InvesmentSimpleInfo invesmentSimpleInfo=new InvesmentSimpleInfo();
                    invesmentSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    invesmentSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    invesmentSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    invesmentSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    invesmentSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    invesmentSimpleInfo.setArea_name(jsonObject2.getString("area_name"));
                    invesmentSimpleInfo.setInvest_cat_name(jsonObject2.getString("invest_cat_name"));
                    invesmentSimpleInfo.setInvest_stage_name(jsonObject2.getString("invest_stage_name"));
                    invesmentSimpleInfos.add(invesmentSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class MyIssueAdapter extends BaseAdapter {
        List<InvesmentSimpleInfo> list;
        Context context;

        public MyIssueAdapter(Context context, List<InvesmentSimpleInfo> list){
            this.context=context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            InvesmentSimpleInfo invesmentSimpleInfo=list.get(position);
            Picasso.with(getActivity())
                    .load(Url.prePic+invesmentSimpleInfo.getLogo())
                    .placeholder(R.drawable.default_icon)
                    .into(viewHolder.icon);
            viewHolder.name.setText(invesmentSimpleInfo.getCompany_name());
            String mess="所在区域: "+invesmentSimpleInfo.getArea_name()+"\n机构类型: "+invesmentSimpleInfo.getInvest_cat_name()+"\n投资阶段： "+invesmentSimpleInfo.getInvest_stage_name();
            viewHolder.intro.setText(mess);
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }

}
