package com.view.game;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.view.FiltPopWindow;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.game.MyGameListAdapter;

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
 * Created by Administrator on 2016/7/13.
 */
public class GameListFragment extends Fragment implements View.OnClickListener {

    View view;
    ListView pullToRefreshListView;

    private PtrClassicFrameLayout ptrFrameLayout;
    private MyScrollViewListener listener;
    private View headview;
    private View footview;
    private View nomore;

    int p=1;

    private List<GameInfo> gameInfos;
    MyGameListAdapter gameAdapter;

    private String[] title={"gametype","gamestage","require","norms"};
    private FiltParamsData filtParamsData=FiltParamsData.getInstance();

    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear4;

    private LinearLayout filtLinear;

    private TextView gametype;
    private TextView gamestage;
    private TextView require;
    private TextView norms;

    private List<NameVal> typelist;
    private List<NameVal> stageList;
    private List<NameVal> requirList;
    private List<NameVal> normsList;
    private Map<String,List<NameVal>> map;

    private String type_sel;
    private String stage_sel;
    private String requir_sel;
    private String norms_sel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_gamelist,container,false);

        linear1= (LinearLayout) view.findViewById(R.id.gametype_linear);
        linear2= (LinearLayout) view.findViewById(R.id.gamestage_linear);
        linear3= (LinearLayout) view.findViewById(R.id.require_linear);
        linear4= (LinearLayout) view.findViewById(R.id.norms_linear);

        gametype= (TextView) view.findViewById(R.id.text1);
        gamestage= (TextView) view.findViewById(R.id.text2);
        require= (TextView) view.findViewById(R.id.text3);
        norms= (TextView) view.findViewById(R.id.text4);


        filtLinear= (LinearLayout) view.findViewById(R.id.filttitle_gamelist);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        nomore=LayoutInflater.from(getActivity()).inflate(R.layout.nomore,null);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nomore.setLayoutParams(params);

        pullToRefreshListView= (ListView) view.findViewById(R.id.ptflistView_gamelist);

        ptrFrameLayout= (PtrClassicFrameLayout) view.findViewById(R.id.ptr_gamelist);

        listener=new MyScrollViewListener();
        footview=LayoutInflater.from(getActivity()).inflate(R.layout.footerloading,null);
        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
        drable.start();

        headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        initListener();

        gameInfos=new ArrayList<>();
        gameAdapter=new MyGameListAdapter(getActivity(),gameInfos);
        pullToRefreshListView.setAdapter(gameAdapter);
//        initData("1");

        initPtrLayout();

    }

    private void initPtrLayout() {

        ptrFrameLayout.setHeaderView(headview);
//        prt_cp.setPinContent(true);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.i("~~~~下拉刷新");
                p=1;
                gameInfos.clear();
                pullToRefreshListView.removeFooterView(nomore);
                pullToRefreshListView.setOnScrollListener(listener);
                initData(p+"");

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh();
            }
        },100);
    }

    private void initListener(){
//        showall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(GameListActivity.this, GameListActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//            }
//        });
        View headview=LayoutInflater.from(getActivity()).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();
        ptrFrameLayout.setHeaderView(headview);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData("1");
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
//        if (position==-1){
//            pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//        }else {
//            pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
//        }
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view==nomore){
                    return;
                }
                GameInfo gameInfo=gameInfos.get((int) id);
                Intent intent= new Intent(getActivity(),GameDetailActivity.class);
                MyLog.i("game_id==="+gameInfo.getGame_id());
                intent.putExtra("game_id",gameInfo.getGame_id());
                intent.putExtra("ident_cat",gameInfo.getGame_dev());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        MyLog.i("initView");


    }

    String all_url="game/gamelist";

    private void initData(String p) {
        BaseParams gameParams=new BaseParams(all_url);

        gameParams.addParams("p",p);

        if (!TextUtils.isEmpty(type_sel)){
            gameParams.addParams("game_type",type_sel);
        }
        if (!TextUtils.isEmpty(stage_sel)){
            gameParams.addParams("game_stage",stage_sel);
        }
        if (!TextUtils.isEmpty(requir_sel)){
            gameParams.addParams("requirement",requir_sel);
        }
        if (!TextUtils.isEmpty(norms_sel)){
            gameParams.addParams("norms",norms_sel);
        }
        if (MyAccount.isLogin){
            gameParams.addParams("token",MyAccount.getInstance().getToken());

        }

        gameParams.addSign();
        x.http().post(gameParams.getRequestParams(),new Callback.CommonCallback<String>() {
            private String result;

            @Override
            public void onSuccess(String result) {
                MyLog.i("访问网络");
                this.result=result;


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ptrFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameList==="+result);
                if (pullToRefreshListView.getFooterViewsCount()==0){
                    pullToRefreshListView.addFooterView(footview);
                }
                gameListParser(result);

                if (ptrFrameLayout.isRefreshing()) {
                    ptrFrameLayout.refreshComplete();
                }

            }
        });
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);
        linear4.setOnClickListener(this);

    }


    private void gameListParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                Url.packageurl=jsonObject1.getString("prefixPackage");
                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");
                if (jsonArray.length()==0){
//                    CusToast.showToast(this, "没有更多游戏", Toast.LENGTH_SHORT);
                    pullToRefreshListView.removeFooterView(footview);
                    pullToRefreshListView.addFooterView(nomore);
                    pullToRefreshListView.setOnScrollListener(null);
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setGame_grade(game.getString("game_grade"));
                    gameInfo.setPack(game.getString("pack"));
                    gameInfo.setNorms(game.getString("norms"));
                    gameInfo.setChecked(game.getString("checked"));
                    gameInfo.setGame_type(game.getString("game_type"));
                    gameInfo.setGame_stage(game.getString("game_stage"));
                    gameInfo.setGame_dev(game.getString("identity_cat"));

                    if (MyAccount.isLogin){
                        gameInfo.setIscare(game.getBoolean("iscare"));
                    }
                    gameInfos.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gameAdapter.notifyDataSetChanged();
//        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        map=filtParamsData.getMap();

        if (map==null){
            MyLog.i("map null");
            return;
        }
        typelist=map.get(title[0]);
        stageList=map.get(title[1]);
        requirList=map.get(title[2]);
        normsList=map.get(title[3]);

        switch (v.getId()){
            case R.id.gametype_linear:

                showPopWindow(typelist,null);

                filtPopWindow.setOnclickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NameVal nameVal = typelist.get(position);
                        if (position == 0) {
                            gametype.setText("游戏类型");
                            type_sel="";
                        } else{
                            gametype.setText(nameVal.getVal());
                            type_sel=nameVal.getId();
                        }
                        filtPopWindow.dismiss();
                        ptrFrameLayout.autoRefresh();
                    }
                });

                break;
            case R.id.gamestage_linear:

                showPopWindow(stageList,null);
                filtPopWindow.setOnclickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NameVal nameVal = stageList.get(position);
                        if (position == 0) {
                            gamestage.setText("项目阶段");
                            stage_sel="";
                        } else{
                            gamestage.setText(nameVal.getVal());
                            stage_sel=nameVal.getId();
                        }
                        filtPopWindow.dismiss();
                        ptrFrameLayout.autoRefresh();
                    }
                });

                break;
            case R.id.require_linear:
                showPopWindow(requirList,null);

                filtPopWindow.setOnclickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NameVal nameVal = requirList.get(position);
                        if (position == 0) {
                            require.setText("项目需求");
                            requir_sel="";
                        } else{
                            require.setText(nameVal.getVal());
                            requir_sel=nameVal.getId();
                        }
                        filtPopWindow.dismiss();
                        ptrFrameLayout.autoRefresh();
                    }
                });
                break;
            case R.id.norms_linear:
                showPopWindow(normsList,null);

                filtPopWindow.setOnclickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NameVal nameVal = normsList.get(position);
                        if (position == 0) {
                            norms.setText("正版IP");
                            norms_sel="";
                        } else{
                            norms.setText(nameVal.getVal());
                            norms_sel=nameVal.getId();
                        }
                        filtPopWindow.dismiss();
                        ptrFrameLayout.autoRefresh();
                    }
                });
                break;
        }
        p=0;
        initData(p+"");
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
    FiltPopWindow filtPopWindow;
    public  void showPopWindow(List<NameVal> datalist, NameVal val){
        MyLog.i("datalist size=="+datalist.size());

//        View popView=LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_filtlist,null);
//        PopupWindow popupWindow=new PopupWindow(popView,300,300);
//
//
//        MyLog.i("showPopWindow2");
//
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        filtPopWindow=new FiltPopWindow(getActivity(),datalist,val);
        filtPopWindow.showPopupWindow(filtLinear);

//        popupWindow.showAtLocation();
        MyLog.i("showPopWindow3");

    }
}
