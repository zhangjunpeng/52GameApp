package com.view.myattention;

import android.app.Activity;
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

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.ListActivity;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.view.s4server.CPDetailActivity;
import com.view.s4server.CPSimpleInfo;
import com.view.s4server.IPDetailActivity;
import com.view.s4server.IPSimpleInfo;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.InvesmentSimpleInfo;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.IssueSimpleInfo;
import com.view.s4server.OutSourceActivity;
import com.view.s4server.OutSourceSimpleInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/3/19.
 */
public class AttentionFragment extends Fragment implements View.OnClickListener {

    private String tag;
    String[] titles={"game","cp","ip","inves","issue","outsource"};
    private ListView listView;

    private PtrClassicFrameLayout ptr_atten;
    List<Object> datalist;
    BaseAdapter adapter = new OutSourceAttentionAdapter(getActivity(),datalist,listView);
    TextView wantcare;
    private final int ToDetail=301;
    int p=1;
    private View headview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        tag=getArguments().getString("tag","game");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_attention,container,false);
        listView= (ListView) view.findViewById(R.id.listview_attention_fragment);
        wantcare= (TextView) view.findViewById(R.id.want_care);
        datalist=new ArrayList<>();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        ptr_atten= (PtrClassicFrameLayout) view.findViewById(R.id.ptrframe_attention);
        initAdapter();


        listView.setAdapter(adapter);

        initData("1");
        initListener();
        return view;
    }

    private void initListener() {
        ptr_atten.setHeaderView(headview);

        ptr_atten.setResistance(1.7f);
        ptr_atten.setRatioOfHeaderHeightToRefresh(1.2f);
        ptr_atten.setDurationToClose(200);
        ptr_atten.setDurationToCloseHeader(1000);
// default is false
        ptr_atten.setPullToRefresh(false);
// default is true
        ptr_atten.setKeepHeaderWhenRefresh(true);

//        prt_cp.setPinContent(true);

        ptr_atten.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.i("~~~~下拉刷新");
                p=1;
                datalist.clear();
                listView.setOnScrollListener(new MyScrollViewListener());
                initData(p+"");
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        ptr_atten.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptr_atten.autoRefresh();
            }
        },100);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("Item click");
                Intent intent=null;
                switch (tag){
                    case "game":
                        intent=new Intent(getActivity(), GameDetailActivity.class);
                        GameInfo gameInfo= (GameInfo) datalist.get(position);
                        intent.putExtra("game_id",gameInfo.getGame_id());
                        break;
                    case "cp":
                        intent=new Intent(getActivity(), CPDetailActivity.class);
                        CPSimpleInfo cpinfo= (CPSimpleInfo) datalist.get(position);
                        intent.putExtra("identity_cat","2");
                        intent.putExtra("user_id",cpinfo.getUser_id());
                        break;
                    case "inves":
                        intent=new Intent(getActivity(), InvesmentDetialActivity.class);
                        InvesmentSimpleInfo invesinfo= (InvesmentSimpleInfo) datalist.get(position);
                        intent.putExtra("identity_cat","4");
                        intent.putExtra("user_id",invesinfo.getUser_id());
                        break;
                    case "ip":
                        intent=new Intent(getActivity(), IPDetailActivity.class);
                        IPSimpleInfo ipsimple= (IPSimpleInfo) datalist.get(position);

                        intent.putExtra("id",ipsimple.getId());
                        break;
                    case "issue":
                        intent=new Intent(getActivity(), IssueDetailActivity.class);
                        IssueSimpleInfo issueInfo= (IssueSimpleInfo) datalist.get(position);
                        intent.putExtra("identity_cat","6");
                        intent.putExtra("user_id",issueInfo.getUser_id());
                        break;
                    case "outsource":
                        intent=new Intent(getActivity(), OutSourceActivity.class);
                        OutSourceSimpleInfo osInfo= (OutSourceSimpleInfo) datalist.get(position);
                        intent.putExtra("identity_cat","3");
                        intent.putExtra("user_id",osInfo.getUser_id());
                        break;

                }
                startActivityForResult(intent,ToDetail);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                MyLog.i("Item click finish");
            }
        });


        wantcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (tag){
                    case "game":
                        intent=new Intent(getActivity(), GameListActivity.class);
                        break;
                    case "cp":
                        intent=new Intent(getActivity(), ListActivity.class);
                        intent.putExtra("tag",ListActivity.CP_TAG);
                        break;
                    case "inves":
                        intent=new Intent(getActivity(), ListActivity.class);
                        intent.putExtra("tag",ListActivity.Invesment_TAG);
                        break;
                    case "ip":
                        intent=new Intent(getActivity(), ListActivity.class);
                        intent.putExtra("tag",ListActivity.IP_TAG);
                        break;
                    case "issue":
                        intent=new Intent(getActivity(), ListActivity.class);
                        intent.putExtra("tag",ListActivity.Issue_TAG);
                        break;
                    case "outsource":
                        intent=new Intent(getActivity(), ListActivity.class);
                        intent.putExtra("tag",ListActivity.OutSource_TAG);
                        break;

                }
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }

    public void initData(String p){
        String path="";
        switch (tag){
            case "game":
                path="care/caregame";
                break;
            case "cp":
                path="care/carecompany";
                break;
            case "inves":
                path="care/careInvest";
                break;
            case "ip":
                path="care/careip";
                break;
            case "issue":
                path="care/careissue";
                break;
            case "outsource":
                path="care/careoutsource";
                break;
            
        }
        BaseParams baseParams=new BaseParams(path);
        baseParams.addParams("p",p);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("care back=="+result);
                switch (tag){
                    case "game":
                        gameListParser(result);
                        break;
                    case "cp":
                        cpListParser(result);
                        break;
                    case "inves":
                        invesListParser(result);
                        break;
                    case "ip":
                        ipListParser(result);
                        break;
                    case "issue":
                        issueListParser(result);
                        break;
                    case "outsource":
                        osListParser(result);
                        break;

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptr_atten.refreshComplete();
            }
        });
    }

    private void initAdapter() {
        switch (tag){
            case "game":
                adapter=new GameAttentionAdapter(getActivity(),datalist,listView);
                break;
            case "cp":
                adapter=new CPAttentionAdapter(getActivity(),datalist,listView);
                break;
            case "inves":
                adapter=new InvestAttentionAdapter(getActivity(),datalist,listView);
                break;
            case "ip":
                adapter=new IPAttentionAdapter(getActivity(),datalist,listView);
                break;
            case "issue":
                adapter=new IssuesAttentionAdapter(getActivity(),datalist,listView);
                break;
            case "outsource":
                adapter=new OutSourceAttentionAdapter(getActivity(),datalist,listView);
                break;
        }
    }

    private void osListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONObject careos=jsonObject1.getJSONObject("careOutsourceList");
                JSONArray jsonArray=careos.getJSONArray("project_list");
                if (jsonArray.length()==0){
                    listView.setOnScrollListener(null);
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    OutSourceSimpleInfo outsource=new OutSourceSimpleInfo();
                    outsource.setUser_id(jsonObject2.getString("user_id"));
                    outsource.setLogo(jsonObject2.getString("logo"));
//                    outsource.setOutsource_name(jsonObject2.getString("outsource_name"));
//                    outsource.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    outsource.setCompany_name(jsonObject2.getString("company_name"));
                    outsource.setCompany_scale(jsonObject2.getString("company_scale"));
//                    outsource.setArea_name(jsonObject2.getString("area"));
                    outsource.setOutsource_cat(jsonObject2.getString("outsource_cat"));
                    datalist.add(outsource);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
//        listView.onRefreshComplete();
        isEmty();
    }

    private void issueListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONObject careissue=jsonObject1.getJSONObject("careIssueList");
                JSONArray issues=careissue.getJSONArray("project_list");
                if (issues.length()==0){
                    listView.setOnScrollListener(null);
                }
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    IssueSimpleInfo issueSimpleInfo=new IssueSimpleInfo();
                    issueSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    issueSimpleInfo.setLogo(jsonObject2.getString("logo"));
//                    issueSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    issueSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    issueSimpleInfo.setBusiness_cat(jsonObject2.getString("business_cat"));
                    issueSimpleInfo.setCoop_cat(jsonObject2.getString("coop_cat"));
                    datalist.add(issueSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
//        listView.onRefreshComplete();
        isEmty();
    }

    private void ipListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONObject careip=jsonObject1.getJSONObject("careIpList");
                JSONArray jsonArray=careip.getJSONArray("project_list");
                if (jsonArray.length()==0){
                    listView.setOnScrollListener(null);
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    IPSimpleInfo ipSimpleInfo=new IPSimpleInfo();
                    ipSimpleInfo.setId(jsonObject2.getString("ip_id"));
                    ipSimpleInfo.setLogo(jsonObject2.getString("ip_logo"));
                    ipSimpleInfo.setIp_name(jsonObject2.getString("ip_name"));
                    ipSimpleInfo.setIp_cat(jsonObject2.getString("ip_cat"));
//                    ipSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    ipSimpleInfo.setIp_style(jsonObject2.getString("ip_style"));
                    ipSimpleInfo.setUthority(jsonObject2.getString("uthority"));
                    datalist.add(ipSimpleInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
//        listView.onRefreshComplete();
        isEmty();
    }

    private void invesListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONObject carein=jsonObject1.getJSONObject("careInvestList");
                JSONArray issues=carein.getJSONArray("project_list");
                if (issues.length()==0){
                    listView.setOnScrollListener(null);
                }
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    InvesmentSimpleInfo invesmentSimpleInfo=new InvesmentSimpleInfo();
                    invesmentSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    invesmentSimpleInfo.setLogo(jsonObject2.getString("logo"));
//                    invesmentSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    invesmentSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    invesmentSimpleInfo.setInvest_cat(jsonObject2.getString("invest_cat"));
                    invesmentSimpleInfo.setInvest_stage(jsonObject2.getString("invest_stage"));
                    datalist.add(invesmentSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
//        listView.onRefreshComplete();
        isEmty();

    }

    private void cpListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONObject carCP=jsonObject1.getJSONObject("careCompanyList");
                JSONArray ja=carCP.getJSONArray("project_list");
                if (ja.length()==0){
                    listView.setOnScrollListener(null);
                }
                for (int i=0;i<ja.length();i++){
                    JSONObject jsonObject2=ja.getJSONObject(i);
                    CPSimpleInfo cpSimpleInfo=new CPSimpleInfo();
                    cpSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
//                    cpSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    cpSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    cpSimpleInfo.setArea(jsonObject2.getString("area_id"));
                    cpSimpleInfo.setScale(jsonObject2.getString("company_scale"));
                    cpSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    datalist.add(cpSimpleInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
//        listView.onRefreshComplete();
        isEmty();
    }

    private void gameListParser(String result) {
            try {
                JSONObject jsonObject=new JSONObject(result);
                int code=jsonObject.getInt("code");
                boolean su=jsonObject.getBoolean("success");
                if (su&&code==200){
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    Url.prePic=jsonObject1.getString("prefixPic");
                    JSONObject careGame=jsonObject1.getJSONObject("careGameList");
                    JSONArray jsonArray=careGame.getJSONArray("project_list");
                    if (jsonArray.length()==0){
                        listView.setOnScrollListener(null);
                    }
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject game=jsonArray.getJSONObject(i);
                        GameInfo gameInfo=new GameInfo();
                        gameInfo.setGame_name(game.getString("game_name"));
                        gameInfo.setGame_id(game.getString("gid"));
                        gameInfo.setGame_img(game.getString("game_img"));
                        gameInfo.setGame_type(game.getString("game_type"));
                        gameInfo.setGame_stage(game.getString("game_stage"));
                        gameInfo.setGame_grade(game.getString("game_grade"));
                        datalist.add(gameInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
//            listView.onRefreshComplete();
            isEmty();
    }

    public void isEmty(){
        if (datalist.size()==0){
            ptr_atten.setVisibility(View.GONE);
//            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==ToDetail&&requestCode== Activity.RESULT_OK){
            MyLog.i("fragment onActivityResult");
            initData("1");
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
