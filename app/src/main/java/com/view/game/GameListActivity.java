package com.view.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.app.tools.ScreenUtil;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.activity.ListActivity;
import com.view.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class GameListActivity extends BaseActivity implements View.OnClickListener{

    ListView pullToRefreshListView;

    ImageView back;
    TextView title;
    ImageView search;
    private List<GameInfo> gameInfos;
    MyGameListAdapter gameAdapter;
    int p=1;



    String all_url="game/gamelist";

//    private TextView showall;
    private PtrClassicFrameLayout ptrFrameLayout;
    private MyScrollViewListener listener;
    private View headview;
    private View footview;
    private View nomore;
    private Button refreash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        nomore=LayoutInflater.from(this).inflate(R.layout.nomore,null);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nomore.setLayoutParams(params);


        pullToRefreshListView= (ListView) findViewById(R.id.ptflistView_gamelist);

        setImmerseLayout(findViewById(R.id.title_gamelist));

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);
        ImageView backimg= (ImageView) findViewById(R.id.backimg_titlebar);

//        showall= (TextView) findViewById(R.id.showall_gamelist);
        ptrFrameLayout= (PtrClassicFrameLayout) findViewById(R.id.ptr_gamelist);

        listener=new MyScrollViewListener();
        footview=LayoutInflater.from(this).inflate(R.layout.footerloading,null);
        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
        drable.start();

        headview=LayoutInflater.from(this).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();



        title.setText("全部游戏");



        backimg.setImageResource(R.drawable.back);

        initListener();

        gameInfos=new ArrayList<>();
        gameAdapter=new MyGameListAdapter(this,gameInfos);
        pullToRefreshListView.setAdapter(gameAdapter);
//        initData("1");

        initPtrLayout();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameListActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


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
                pullToRefreshListView.addFooterView(footview);
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

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void initData(String p) {
        BaseParams gameParams=new BaseParams(all_url);

        gameParams.addParams("p",p);
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
                    gameInfos.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gameAdapter.notifyDataSetChanged();
//        pullToRefreshListView.onRefreshComplete();
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
        View headview=LayoutInflater.from(this).inflate(R.layout.handerloading,null);
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
        back.setOnClickListener(this);
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
                Intent intent= new Intent(GameListActivity.this,GameDetailActivity.class);
                MyLog.i("game_id==="+gameInfo.getGame_id());
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        MyLog.i("initView");
        refreash= (Button) findViewById(R.id.refeash_gamelist);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptrFrameLayout.setVisibility(View.VISIBLE);
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.autoRefresh();
                    }
                },100);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_titlebar:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }
    }


    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        MyLog.i("Url==="+url);
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
    }


    private List searchTitle(){
        Query query = getGameTypeDao().queryBuilder()
                .build();
        return query.list();
    }

    private GameTypeDao getGameTypeDao() {
        return MyApplication.daoSession.getGameTypeDao();
    }

}
