package com.view.game;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
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
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
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

public class RmGameListActivity extends BaseActivity {

    ListView listView;

    ImageView back;
    TextView title;
    ImageView search;

    View showall;


    List<GameType> titles;
    private int position=-1;

    private List<GameInfo> gameInfos;
    MyGameListAdapter gameAdapter;

    String tj_url="/game/getgameadverts";

    private PtrClassicFrameLayout ptrlayout;
    private View headview;
    private Button refreash;
    private View footview;
    private int p=1;
    private MyScrollViewListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rm_game_list);

        gameInfos=new ArrayList<>();
        gameAdapter=new MyGameListAdapter(this,gameInfos);

        position=getIntent().getIntExtra("position",-1);

        listView= (ListView) findViewById(R.id.list_recom_gamelist);

        setImmerseLayout(findViewById(R.id.title_regamelist));

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);
        ImageView backimg= (ImageView) findViewById(R.id.backimg_titlebar);
        backimg.setImageResource(R.drawable.back);

        headview=LayoutInflater.from(this).inflate(R.layout.handerloading,null);
        ImageView imageView= (ImageView) headview.findViewById(R.id.image_handerloading);
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

        footview=LayoutInflater.from(this).inflate(R.layout.footerloading,null);
        ImageView image= (ImageView) footview.findViewById(R.id.image_footerloading);
        AnimationDrawable drable= (AnimationDrawable) image.getBackground();
        drable.start();

        ptrlayout= (PtrClassicFrameLayout) findViewById(R.id.ptr_rmgamelist);
        showall= LayoutInflater.from(this).inflate(R.layout.showall,null);


        titles=searchTitle();
        title.setText(titles.get(position).getTitle());

        listener=new MyScrollViewListener();

        listView.setAdapter(gameAdapter);


        initPtrLayout();
        initListener();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RmGameListActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

    }
    class MyScrollViewListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                        p++;
//                        initData(p+"");
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view==showall){
                    Intent intent=new Intent(RmGameListActivity.this,GameListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    return;
                }
                GameInfo gameInfo=gameInfos.get((int) id);
                Intent intent= new Intent(RmGameListActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",gameInfo.getGame_id());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });
        refreash= (Button) findViewById(R.id.refeash_rmgamelist);
        refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptrlayout.setVisibility(View.VISIBLE);
                ptrlayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrlayout.autoRefresh();
                    }
                },100);
            }
        });
    }

    private void initPtrLayout() {
        ptrlayout.setHeaderView(headview);

        ptrlayout.setResistance(1.7f);
        ptrlayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrlayout.setDurationToClose(200);
        ptrlayout.setDurationToCloseHeader(1000);
// default is false
        ptrlayout.setPullToRefresh(false);
// default is true
        ptrlayout.setKeepHeaderWhenRefresh(true);

//        prt_cp.setPinContent(true);

        ptrlayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.i("~~~~下拉刷新");
//                p=1;
                gameInfos.clear();

//                listView.setOnScrollListener(listener);
                initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        ptrlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrlayout.autoRefresh();
            }
        },100);
    }

    private void initData() {
        BaseParams gameParams=new BaseParams(tj_url);
        GameType type=titles.get(position);
        gameParams.addParams("catId",type.getAdvert_cat_id());
        gameParams.addParams("p","1");
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
                ptrlayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameList==="+result);
                if (listView.getFooterViewsCount()==0){
                    listView.addFooterView(showall);
                }
                gameListParser(result);

                if (ptrlayout.isRefreshing()){
                    ptrlayout.refreshComplete();
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
                if (position==-1){
                    Url.packageurl=jsonObject1.getString("prefixPackage");
                }
                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");
//                if (jsonArray.length()==0){
////                    CusToast.showToast(this, "没有更多游戏", Toast.LENGTH_SHORT);
//                    listView.removeFooterView(footview);
//                    listView.addFooterView(showall);
//                    listView.setOnScrollListener(null);
//                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setGame_size(game.getString("game_size"));
                    gameInfo.setNorms(game.getString("norms"));
                    gameInfo.setGame_grade(game.getString("game_grade"));
                    gameInfo.setPack(game.getString("pack"));
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
