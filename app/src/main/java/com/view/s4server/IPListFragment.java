package com.view.s4server;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.view.FiltPopWindow;
import com.test4s.account.MyAccount;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.activity.ListActivity;
import com.view.game.FiltParamsData;

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

/**
 * Created by Administrator on 2016/2/18.
 */
public class IPListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    View view;

    ListView listView;

    int p=1;

    List<IPSimpleInfo> ipSimpleInfos;

    MyIpListAdapter myAdapter;

    private Button refreash;
    private boolean recommend;
    private View showall;
    private PtrClassicFrameLayout prt_cp;
//    private View footview;
    private View headview;
    private int Foot_flag;
    private MyScrollViewListener listener;
    private View nomore;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nomore=getTextView(getActivity());

        Bundle bundle=getArguments();

        if (bundle!=null){
            recommend=bundle.getBoolean("recommend",false);
        }
        listener=new MyScrollViewListener();
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_list,null);
        if (recommend){
            view.findViewById(R.id.filttitle_list).setVisibility(View.GONE);
        }else {
            view.findViewById(R.id.filttitle_list).setVisibility(View.VISIBLE);
        }

        listView= (ListView) view.findViewById(R.id.pullToRefresh_fglist);
        ipSimpleInfos=new ArrayList<>();
        myAdapter=new MyIpListAdapter(getActivity(),ipSimpleInfos);


        prt_cp= (PtrClassicFrameLayout) view.findViewById(R.id.prt_cplist);
//        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footerloading,null);
//        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
//        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
//        drable.start();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        listView.setAdapter(myAdapter);

        refreash= (Button) view.findViewById(R.id.refeash_list);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);

                initData("1");
            }
        });

        initFiltView();

        initPtrLayout();
        initListView();
//        initData(1+"");



        return view;
    }
    private String[] filttitles={"ipcat","ipstyle","uthority"};

    private String[] filtname={"IP类型","IP风格","授权范围"};

    private int[] linearId={R.id.linear1,R.id.linear2,R.id.linear3};
    private List<LinearLayout> linearList;

    private View filtLinear;
    private FiltParamsData filtParamsData=FiltParamsData.getInstance();
    private String ipcat_sel="";
    private String ipstyle_sel="";
    private String uthority_sel="";

    FiltPopWindow filtPopWindow;


    private List<TextView> nameList;
    private Map<String,List<NameVal>> map;

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
                            ipcat_sel="";
                        }else {
                            ipcat_sel = nameVals.get(position).getId();
                        }
                        break;
                    case 1:
                        if (position==0){
                            textView.setText(filtname[index]);
                            ipstyle_sel="";
                        }else {
                            ipstyle_sel = nameVals.get(position).getId();
                        }
                        break;
                    case 2:
                        if (position==0){
                            textView.setText(filtname[index]);
                            uthority_sel="";
                        }else {
                            uthority_sel = nameVals.get(position).getId();
                        }

                        break;
                }
                filtPopWindow.dismiss();
                prt_cp.autoRefresh();
            }

        });

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
                ipSimpleInfos.clear();
                if (recommend){
                    listView.removeFooterView(showall);
                }else {
                    listView.removeFooterView(nomore);
                }
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

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("index/iplist");
        if (recommend){
            baseParams.addParams("ret","2");
        }
        if (!TextUtils.isEmpty(ipcat_sel)){
            baseParams.addParams("ip_cat",ipcat_sel);
        }
        if (!TextUtils.isEmpty(ipstyle_sel)){
            baseParams.addParams("ip_style",ipstyle_sel);
        }
        if (!TextUtils.isEmpty(uthority_sel)){
            baseParams.addParams("uthority",uthority_sel);
        }
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }

        baseParams.addParams("p",p);
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(10*60*1000);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                res=result;
                MyLog.i("onsuccess=="+result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

//                listView.setVisibility(View.GONE);
                prt_cp.setVisibility(View.GONE);

                CusToast.showToast(getActivity(),"请检查网络",Toast.LENGTH_SHORT);


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                if (Foot_flag!=1){
//                    listView.removeFooterView(showall);
//                    listView.removeFooterView(nomore);
////                    listView.addFooterView(footview);
//                    Foot_flag=1;
//                }
                parser(res);
                myAdapter.notifyDataSetChanged();
                if (listView.getFooterViewsCount()==0) {
                    if (recommend) {
//                        listView.removeFooterView(footview);
                        listView.addFooterView(showall);
                        Foot_flag = 2;
                    }
                }
                if (prt_cp.isRefreshing()) {
                    prt_cp.refreshComplete();
                }

            }

        });

    }
    private  void parser(String res){
        try {
            JSONObject jsonObject=new JSONObject(res);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){

                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray jsonArray=jsonObject1.getJSONArray("ipList");
                if (jsonArray.length()==0){
                    listView.setOnScrollListener(null);
                    if (!recommend){
                        listView.addFooterView(nomore);
                        Foot_flag = 3;
                    }
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    IPSimpleInfo ipSimpleInfo=new IPSimpleInfo();
                    ipSimpleInfo.setId(jsonObject2.getString("id"));
                    ipSimpleInfo.setLogo(jsonObject2.getString("ip_logo"));
                    ipSimpleInfo.setIp_name(jsonObject2.getString("ip_name"));
                    ipSimpleInfo.setIp_cat(jsonObject2.getString("ip_cat"));
//                    ipSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    ipSimpleInfo.setIp_style(jsonObject2.getString("ip_style"));
                    ipSimpleInfo.setUthority(jsonObject2.getString("uthority"));
                    if (MyAccount.isLogin){
                        ipSimpleInfo.setIscare(jsonObject2.getBoolean("iscare"));
                    }
                    ipSimpleInfos.add(ipSimpleInfo);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListView() {
        showall=LayoutInflater.from(getActivity()).inflate(R.layout.showall,null);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.IP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        listView.setOnScrollListener(listener);
        listView.setOnItemClickListener(this);


        Foot_flag=1;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view==nomore){
            return;
        }
        Intent intent=new Intent(getActivity(),IPDetailActivity.class);
        IPSimpleInfo ipSimpleInfo=ipSimpleInfos.get((int) id);
        intent.putExtra("id",ipSimpleInfo.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }
}
