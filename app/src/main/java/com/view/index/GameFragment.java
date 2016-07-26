package com.view.index;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.tools.Timer;
import com.app.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.AdvertsDao;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.gdb.IP;
import com.test4s.myapp.MyApplication;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.myapp.R;
import com.test4s.jsonparser.GameJsonParser;
import com.view.game.RmGameListActivity;
import com.view.s4server.IPDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import de.greenrobot.dao.query.Query;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    ViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;


    List<GameType> titles;
    Map<String,List> map;

    LinearLayout continar;
    List<LinearLayout> content;

    private ViewPagerAdapter adapter;
    List<ImageView> imageViewList;
    private float density;
    private LinearLayout whiteDots;

    private DaoSession daoSession;
    private ArrayList<Adverts> gameAdverts;

    private List<GameInfo> allgames;

    View view;

    private ImageLoader imageloder=ImageLoader.getInstance();
    private Thread thread;
    private Activity context;

    private int currentItem;
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (viewPager!=null&&gameAdverts!=null) {
                        currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem == gameAdverts.size()) {
                            currentItem = 0;
                        }
                        viewPager.setCurrentItem(currentItem);

                    }
                    break;
            }
        }
    };

    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initView();
                    break;
                case 1:
                    initViewPager();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (thread==null){
            thread=new Timer(handler);
            thread.start();
        }
        context=getActivity();
        content=new ArrayList<>();
        imageViewList=new ArrayList<>();
        adapter=new ViewPagerAdapter(imageViewList);
        allgames=new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_game,null);
        continar= (LinearLayout) view.findViewById(R.id.contianer_game);
        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear_game);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager_game);
        mcontext=getContext();
        daoSession= MyApplication.daoSession;

        viewPager.setAdapter(adapter);

        getDensity();
//        parser=GameJsonParser.getIntance();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getDateFromDB();

                mhander.sendEmptyMessage(0);
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                initViewPagerFromDB();

                mhander.sendEmptyMessage(1);
            }
        });


        initData();

        return view;
    }

    private void getDateFromDB() {
        MyLog.i("获取数据库中数据");
        titles= (ArrayList<GameType>) searchTitle();
        if (titles==null){
            return;
        }
        map=new HashMap<>();
        for (int i=0;i<titles.size();i++){
            GameType gametype=titles.get(i);
            String name=gametype.getTitle();
            ArrayList<GameInfo> gamelist1= (ArrayList<GameInfo>) searchGameInfo(name);
            MyLog.i("gamelist size=="+gamelist1.size());
            map.put(name,gamelist1);
        }
    }

    private void initData() {
        getAllGameData();
        BaseParams baseParams=new BaseParams("game/index");
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*30);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result=null;
            boolean update=true;

            @Override
            public void onSuccess(String result) {
                MyLog.i("联网更新数据");

                this.result=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                update=false;
//                getDateFromDB();
                initViewPagerFromDB();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameIndex====="+result);
                if (update){
                    deleteAll();
                    parser(result);
                    MyLog.i("解析完成");
                    initView();
                    initViewPager();
                }

            }
        });
    }

    private void initViewPagerFromDB() {
        gameAdverts= (ArrayList<Adverts>) searchAdverts();
    }

    private void getAllGameData() {
        BaseParams gameParams=new BaseParams("game/gamelist");
        gameParams.addParams("p","1");
        gameParams.addSign();
        gameParams.getRequestParams().setCacheMaxAge(1000*60*5);
        x.http().post(gameParams.getRequestParams(),new Callback.CacheCallback<String>() {
            private String result;
            @Override
            public boolean onCache(String result) {
                this.result=result;
                MyLog.i("使用缓存");
                return true;
            }
            @Override
            public void onSuccess(String result) {
                MyLog.i("访问网络");
                this.result=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameList==="+result);
                gameparser(result);
                addAllGame();

            }
        });
    }

    private void gameparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setNorms(game.getString("norms"));
                    gameInfo.setGame_grade(game.getString("game_grade"));
                    gameInfo.setPack(game.getString("pack"));
                    gameInfo.setChecked(game.getString("checked"));
                    gameInfo.setGame_dev(game.getString("identity_cat"));

                    allgames.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initViewPager() {

        whiteDots.removeAllViews();
        imageViewList.clear();
        MyLog.i("initViewPager");
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(viewPager.getLayoutParams().width,viewPager.getLayoutParams().height);
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));

        MyLog.i("advertsList size"+gameAdverts.size());
        for (int i=0;i<gameAdverts.size();i++){
            ImageView imageView=new ImageView(context);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
            if (i>0){
                params1.leftMargin=(int)(12.66*density);
            }
            ImageView dot=new ImageView(context);
            dot.setImageResource(R.drawable.whitedotselected);
            dot.setLayoutParams(params1);
            whiteDots.addView(dot);
            MyLog.i("imageUrl==="+Url.prePic+gameAdverts.get(i).getAdvert_pic());
            imageloder.displayImage(Url.prePic+gameAdverts.get(i).getAdvert_pic(),imageView, MyDisplayImageOptions.getdefaultBannerOptions());

        }
        setDot(0);
        MyLog.i("setAdapter");
        adapter.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setDot(int position) {
        for (int i=0;i<whiteDots.getChildCount();i++){
            if (i==position){
                whiteDots.getChildAt(i).setSelected(true);
            }else {
                whiteDots.getChildAt(i).setSelected(false);
            }
        }

    }

    private void initView() {


        //初始化三个HorizontalListView
        MyLog.i("初始化view");
        content.clear();
        continar.removeAllViews();
//        if (parser==null){
//            return;
//        }
        MyLog.i("map size=="+map.size());
        for (int i=0;i<map.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_horilistview_game,null);
            viewHolder.listView= (HorizontalScrollView) layout.findViewById(R.id.list_rm_game);

            OverScrollDecoratorHelper.setUpOverScroll(viewHolder.listView);

            viewHolder.tj= (TextView) layout.findViewById(R.id.tjrm_game);
            viewHolder.more= (TextView) layout.findViewById(R.id.more_rm_game);
            viewHolder.tj.setText(titles.get(i).getTitle());
            layout.setTag(viewHolder);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            continar.addView(layout,layoutParams);
            content.add(layout);

            final int j=i;
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),RmGameListActivity.class);
                    intent.putExtra("position",j);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            ArrayList<GameInfo> gameInfos= (ArrayList<GameInfo>) map.get(titles.get(i).getTitle());
            viewHolder.listView.addView(getLinearInScroll(gameInfos));


        }
    }
    private LinearLayout getLinearInScroll(final List<GameInfo> gameinfos) {
        LinearLayout linear=new LinearLayout(context);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0;i<gameinfos.size();i++){
            View convertView= LayoutInflater.from(context).inflate(R.layout.item_horizaontal_index,null);
            ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
            TextView textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
            GameInfo game=gameinfos.get(i);
            String imageUrl= Url.prePic+game.getGame_img();
            String name=game.getGame_name();

            ImageView grade= (ImageView) convertView.findViewById(R.id.gamerating_index);

//            Picasso.with(context)
//                    .load(imageUrl)
//                    .into(imageView);
//            MyLog.i(game.getGame_name()+"=="+imageUrl);
            imageloder.displayImage(imageUrl,imageView,MyDisplayImageOptions.getroundImageOptions());
            if (!TextUtils.isEmpty(game.getGame_grade())){
                if (!game.getGame_grade().contains("P.jpg")) {
                    imageloder.displayImage(Url.prePic + game.getGame_grade(), grade, MyDisplayImageOptions.getdefaultImageOptions());
                }
            }

            textView.setText(name);
            linear.addView(convertView);
            final int j=i;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameInfo gameinfo=gameinfos.get(j);
                    goDetail(gameinfo.getGame_id(),gameinfo.getGame_dev());
                }
            });
        }
        return linear;
    }

    private void addAllGame() {
        LinearLayout layout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_horilistview_game,null);
        HorizontalScrollView listView= (HorizontalScrollView) layout.findViewById(R.id.list_rm_game);
        TextView tj= (TextView) layout.findViewById(R.id.tjrm_game);
        TextView more= (TextView) layout.findViewById(R.id.more_rm_game);
        tj.setText("全部游戏");
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        continar.addView(layout,layoutParams);
        content.add(layout);

        OverScrollDecoratorHelper.setUpOverScroll(listView);

        listView.addView(getLinearInScroll(allgames));

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GameListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        layout.findViewById(R.id.line_gameindex).setVisibility(View.INVISIBLE);
        continar.notify();
    }



    class ViewHolder{
        HorizontalScrollView listView;
        TextView tj;
        TextView more;
    }
    private void goDetail(String gameid,String ident_cat){
        Intent intent= new Intent(getActivity(),GameDetailActivity.class);
        intent.putExtra("game_id",gameid);
        intent.putExtra("ident_cat",ident_cat);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

    }

    private void setdot(int position) {
        if (dots==null){
            return;
        }
        for (ImageView iamge:dots){
            iamge.setImageResource(R.drawable.lucencydot);
        }
        dots.get(position).setImageResource(R.drawable.yellowdot);
    }

    @Override
    public void onClick(View v) {

    }


    public void parser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean success=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (success&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                //
                JSONArray advert=data.getJSONArray("adverts");
                gameAdverts=new ArrayList<>();
                for (int i=0;i<advert.length();i++){
                    Adverts adverts=new Adverts();
                    JSONObject adv=advert.getJSONObject(i);
                    adverts.setAdvert_name(adv.getString("advert_name"));
                    adverts.setAdvert_pic(adv.getString("advert_pic"));
                    adverts.setAdvert_url(adv.getString("advert_url"));
                    gameAdverts.add(adverts);
                    addAdverts(adverts);
                }
                MyLog.i("gameAdverts");
                map=new HashMap<>();
                JSONArray games=data.getJSONArray("games");
                titles=new ArrayList<>();
                for (int i=0;i<games.length();i++){
                    JSONObject game=games.getJSONObject(i);
                    GameType title=new GameType();
                    String title_s=game.getString("title");
                    title.setTitle(title_s);
                    title.setAdvert_cat_id(game.getString("advert_cat_id"));

                    JSONArray content=game.getJSONArray("content");
                    ArrayList<GameInfo> gameInfos=new ArrayList<>();

                    for (int j=0;j<content.length();j++){
                        GameInfo gameInfo=new GameInfo();
                        JSONObject jsonObject1=content.getJSONObject(j);
                        gameInfo.setGame_img(jsonObject1.getString("game_img"));
                        gameInfo.setGame_id(jsonObject1.getString("game_id"));
                        gameInfo.setGame_name(jsonObject1.getString("game_name"));
                        gameInfo.setGame_dev(jsonObject1.getString("identity_cat"));
                        try {
                            gameInfo.setGame_grade(jsonObject1.getString("game_grade"));

                        }catch (Exception e){
                            gameInfo.setGame_grade("");
                        }
                        gameInfo.setTitle(title_s);
                        gameInfos.add(gameInfo);
                        addGameInfo(gameInfo);
                    }
                    titles.add(title);
                    addGameType(title);
                    map.put(title_s,gameInfos);
                }
                MyLog.i("games");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private GameInfoDao getGameInfoDao(){
        return daoSession.getGameInfoDao();
    }
    private AdvertsDao getAdvertsDao(){
        return  daoSession.getAdvertsDao();
    }
    private GameTypeDao getGameTypeDao(){
        return daoSession.getGameTypeDao();
    }

    private void addGameInfo(GameInfo gameInfo){
        getGameInfoDao().insert(gameInfo);
    }
    private void addAdverts(Adverts advert){
        getAdvertsDao().insert(advert);
    }
    private void addGameType(GameType type){
        getGameTypeDao().insert(type);
    }

    private List searchGameInfo(String type){
        Query query = getGameInfoDao().queryBuilder()
                .where(GameInfoDao.Properties.Title.eq(type))
                .build();
        return query.list();
    }
    private List searchAdverts(){
        Query query = getAdvertsDao().queryBuilder()
                .build();
        return query.list();
    }
    private List searchTitle(){
        Query query = getGameTypeDao().queryBuilder()
                .build();
        return query.list();
    }

    private void deleteAll(){
        getAdvertsDao().deleteAll();
        getGameInfoDao().deleteAll();
        getGameTypeDao().deleteAll();
    }

}
