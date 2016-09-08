package com.view.s4server;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.FiltPopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.activity.ListActivity;
import com.view.game.FiltParamsData;
import com.view.myattention.AttentionChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by Administrator on 2016/2/18.
 */
public class CPListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    View view;

    ListView listView;

    TextView title;
    ImageView back;
    ImageView search;

    View showall;
//    View footview;

    int p=1;

    List<CPSimpleInfo> cpSimpleInfos;

    private CpAdapter adapter;


    private Button refreash;

    boolean recommend=false;

    private String all_url="index/cplist";

    private PtrClassicFrameLayout prt_cp;
    private MyScrollViewListener listener;

    //flag 1为footview 2为showall
    private int Foot_flag=1;
    private View headview;
    private View nomore;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nomore=getTextView(getActivity());

        cpSimpleInfos=new ArrayList<>();
        Bundle bundle=getArguments();
        if (bundle!=null){
            recommend=bundle.getBoolean("recommend",false);
        }
        adapter=new CpAdapter(getActivity(),cpSimpleInfos);
        listener=new MyScrollViewListener();
        super.onCreate(savedInstanceState);

    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams(all_url);
        if (recommend){
            baseParams.addParams("ret","2");
        }
        if (!TextUtils.isEmpty(area_sel)){
            baseParams.addParams("area_id",area_sel);
        }
        if (!TextUtils.isEmpty(companysize_sel)){
            baseParams.addParams("company_scale",companysize_sel);
        }
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                MyLog.i("cplist success=="+result);
                if (listView.getFooterViewsCount()==0){
//                    listView.addFooterView(footview);
                }
                if (Foot_flag!=1){
                    listView.removeFooterView(showall);
                    listView.removeFooterView(nomore);
//                    listView.addFooterView(footview);
                    Foot_flag=1;
                }
                getcplistparser(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i("错误 ::"+ex.toString());
                if (cpSimpleInfos.size()==0) {
                    prt_cp.setVisibility(View.GONE);

                    CusToast.showToast(getActivity(),"请检查网络",Toast.LENGTH_SHORT);

                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

//                listView.onRefreshComplete();


                if (prt_cp.isRefreshing()){
                    prt_cp.refreshComplete();
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void initListView() {

        listView.setOnScrollListener(listener);
        listView.setOnItemClickListener(this);


        Foot_flag=1;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view==nomore){
            return;
        }
        Intent intent=new Intent(getActivity(),CPDetailActivity.class);
        CPSimpleInfo cpSimpleInfo=cpSimpleInfos.get((int) id);
        intent.putExtra("user_id",cpSimpleInfo.getUser_id());
        intent.putExtra("identity_cat",cpSimpleInfo.getIdentity_cat());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }

    class MyScrollViewListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        adddata();
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
    private void adddata() {

        p++;
        initData(p+"");

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        if (recommend){
            view.findViewById(R.id.filttitle_list).setVisibility(View.GONE);
        }else {
            view.findViewById(R.id.filttitle_list).setVisibility(View.VISIBLE);
        }        listView= (ListView) view.findViewById(R.id.pullToRefresh_fglist);
        title= (TextView) view.findViewById(R.id.title_titlebar);
        back= (ImageView) view.findViewById(R.id.back_titlebar);
        search= (ImageView) view.findViewById(R.id.search_titlebar);

        prt_cp= (PtrClassicFrameLayout) view.findViewById(R.id.prt_cplist);
//        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footerloading,null);
//        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
//        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
//        drable.start();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        listView.setAdapter(adapter);

        initFiltView();



        initPtrLayout();
        initListView();

        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prt_cp.setVisibility(View.VISIBLE);
                prt_cp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prt_cp.autoRefresh();
                    }
                },100);
            }
        });
        showall=LayoutInflater.from(getActivity()).inflate(R.layout.showall,null);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.CP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


        return view;
    }


    private int[] linearId={R.id.linear1,R.id.linear2,R.id.linear3};
    private List<LinearLayout> linearList;
    private String[] filttitles={"area","companysize"};
    private String[] filtname={"所在区域","公司规模"};
    private View filtLinear;
    private FiltParamsData filtParamsData=FiltParamsData.getInstance();
    private String area_sel="";
    private String companysize_sel="";

    private List<TextView> nameList;
    private Map<String,List<NameVal>> map;

    FiltPopWindow filtPopWindow;


    private void initFiltView() {
        filtLinear=view.findViewById(R.id.filttitle_list);
        ImageView line= (ImageView) view.findViewById(R.id.line_need);

        nameList=new ArrayList<>();
        linearList=new ArrayList<>();
        for (int i=0;i<linearId.length;i++){
            LinearLayout linearLayout= (LinearLayout) view.findViewById(linearId[i]);
            linearList.add(linearLayout);
            if (i>=filtname.length){
                linearLayout.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
            TextView text= (TextView) linearLayout.getChildAt(0);
            nameList.add(text);
        }
        for (int i=0;i<filttitles.length;i++){
            nameList.get(i).setText(filtname[i]);
            final int index=i;
            linearList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopWindow(index,null);
                }
            });
        }

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
                cpSimpleInfos.clear();
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

    class CpAdapter extends BaseAdapter{

        List<CPSimpleInfo> list;
        Activity context;
        public CpAdapter(Activity context,List<CPSimpleInfo> list){
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
            final ViewHolder viewHolder;
            if (convertView==null){
                convertView=LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
                viewHolder.care= (TextView) convertView.findViewById(R.id.care_item_list);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final CPSimpleInfo cpinfo=list.get(position);
//            Picasso.with(getActivity())
//                    .load(Url.prePic+cpinfo.getLogo())
//                    .into(viewHolder.icon);
            ImageLoader.getInstance().displayImage(Url.prePic+cpinfo.getLogo(),viewHolder.icon, MyDisplayImageOptions.getroundImageOptions());
            viewHolder.name.setText(cpinfo.getCompany_name());
            viewHolder.intro.setText("所在区域："+cpinfo.getArea()+"\n公司规模："+cpinfo.getScale());
            if (MyAccount.isLogin){
                if (cpinfo.iscare()){
                    viewHolder.care.setText("已关注");
                    viewHolder.care.setSelected(true);
                }else {
                    viewHolder.care.setText("关注");
                    viewHolder.care.setSelected(false);

                }
                viewHolder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cpinfo.iscare()){
                            cpinfo.setIscare(false);
                            AttentionChange.removeAttention("2",cpinfo.getUser_id(), context);
                        }else {
                            cpinfo.setIscare(true);
                            AttentionChange.addAttention("2",cpinfo.getUser_id(), context);
                        } if (cpinfo.iscare()){
                            viewHolder.care.setText("已关注");
                            viewHolder.care.setSelected(true);
                        }else {
                            viewHolder.care.setText("关注");
                            viewHolder.care.setSelected(false);

                        }


                    }
                });
            }else {
                viewHolder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, AccountActivity.class);
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

                    }
                });

            }

            return convertView;
        }

        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
            TextView care;
        }
    }

    public void getcplistparser(String result){
//        cpSimpleInfos.clear();
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray ja=jsonObject1.getJSONArray("cpList");
                MyLog.i("cplist size=="+ja.length());
                if (ja.length()==0){
                    listView.setOnScrollListener(null);
                    if (recommend){
//                        listView.removeFooterView(footview);
                        listView.addFooterView(showall);
                        Foot_flag=2;
                    }else {
//                        listView.removeFooterView(footview);
                        listView.addFooterView(nomore);
                        Foot_flag=3;
//                        CusToast.showToast(getActivity(), "没有更多开发者信息", Toast.LENGTH_SHORT);
                    }
                }
                for (int i=0;i<ja.length();i++){
                    JSONObject jsonObject2=ja.getJSONObject(i);
                    CPSimpleInfo cpSimpleInfo=new CPSimpleInfo();
                    cpSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    cpSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    cpSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    cpSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    cpSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    cpSimpleInfo.setScale(jsonObject2.getString("company_scale_name"));
                    cpSimpleInfo.setArea(jsonObject2.getString("area_name"));
                    if (MyAccount.isLogin){
                        cpSimpleInfo.setIscare(jsonObject2.getBoolean("iscare"));
                    }
                    cpSimpleInfos.add(cpSimpleInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  void showPopWindow(final int index, NameVal val ){
//        MyLog.i("datalist size=="+datalist.size());
        map=filtParamsData.getMap();
        if (map==null){
            return;
        }

        final List<NameVal> nameVals = map.get(filttitles[index]);
        filtPopWindow=new FiltPopWindow(getActivity(),nameVals,val);
        filtPopWindow.showPopupWindow(filtLinear);

//        popupWindow.showAtLocation();
        MyLog.i("showPopWindow3");

        filtPopWindow.setOnclickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView=nameList.get(index);
                textView.setText(nameVals.get(position).getVal());
                switch (index) {
                    case 0:
                        if (position==0){
                            textView.setText(filtname[index]);
                            area_sel="";
                        }else {
                            area_sel = nameVals.get(position).getId();
                        }
                        break;
                    case 1:
                        if (position==0){
                            textView.setText(filtname[index]);
                            companysize_sel="";
                        }else {
                            companysize_sel = nameVals.get(position).getId();
                        }
                        break;
                }
                filtPopWindow.dismiss();
                prt_cp.autoRefresh();
            }

        });

    }

}
