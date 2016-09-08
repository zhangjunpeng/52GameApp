package com.view.index.game;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.AdvertsDao;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.myapp.MyApplication;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.test4s.myapp.R;
import com.view.game.GameDetailActivity;
import com.view.index.adapter.GameLayoutAdapter;
import com.view.index.adapter.LayoutAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import de.greenrobot.dao.query.Query;

/**
 * Created by Administrator on 2015/12/7.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    RecyclerViewPager viewPager;
    Context mcontext;
    List<ImageView> dots;


    public static List<GameType> titles;
    public static  Map<String,List> map;

    LinearLayout continar;
    List<LinearLayout> content;

    private GameLayoutAdapter adapter;
    List<ImageView> imageViewList;
    private float density;
//    private LinearLayout whiteDots;

    private DaoSession daoSession;
    private ArrayList<Adverts> gameAdverts;
    public static List<GameInfo> newGames;
    public static List<GameInfo> gradeGames;


    List<Fragment> fragmentList=new ArrayList<>();


    View view;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private ImageView line1;
    private ImageView line2;
    private ImageView line3;

    private RelativeLayout rela1;
    private RelativeLayout rela2;
    private RelativeLayout rela3;

    private List<TextView> textViewList;
    private List<ImageView> lineList;
    private List<RelativeLayout> relativeLayouts;

    private ViewPager viewPager_fragment;

    private ImageLoader imageloder=ImageLoader.getInstance();
    private Thread thread;
    private Activity context;

    static String data="";

    private int currentItem;
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (viewPager!=null&&gameAdverts!=null) {
                        currentItem = viewPager.getCurrentPosition();
                        currentItem++;
                        if (currentItem == gameAdverts.size()) {
                            currentItem = 0;
                        }
                        viewPager.scrollToPosition(currentItem);

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

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (thread==null){
//            thread=new Timer(handler);
//            thread.start();
//        }
//
//
//    }

    private GestureDetector gestureDetector;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            MyLog.i("info 重新获取数据");

            initData();

        }else {

        }

        gestureDetector = new GestureDetector(getActivity(),onGestureListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        content=new ArrayList<>();
        imageViewList=new ArrayList<>();
        view=inflater.inflate(R.layout.fragment_game,null);
//        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear_game);
        viewPager= (RecyclerViewPager) view.findViewById(R.id.viewpager);
        viewPager_fragment= (ViewPager) view.findViewById(R.id.viewpager_fragment);
        mcontext=getContext();
        daoSession= MyApplication.daoSession;



        getDensity();
//        parser=GameJsonParser.getIntance();
//
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                getDateFromDB();
//
//                mhander.sendEmptyMessage(0);
//            }
//        });
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                initViewPagerFromDB();
//
//                mhander.sendEmptyMessage(1);
//            }
//        });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        if (!TextUtils.isEmpty(data)){
            deleteAll();
            parser(data);
            initView();
            initViewPager();
            initViewPagerFrag();
        }


        return view;
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    return false;
                }
            };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewList=new ArrayList<>();
        relativeLayouts=new ArrayList<>();
        lineList=new ArrayList<>();

        textView1= (TextView) view.findViewById(R.id.text1);
        textView2= (TextView) view.findViewById(R.id.text2);
        textView3= (TextView) view.findViewById(R.id.text3);
        textViewList.add(textView1);
        textViewList.add(textView2);
        textViewList.add(textView3);

        rela1= (RelativeLayout) view.findViewById(R.id.rela1);
        rela2= (RelativeLayout) view.findViewById(R.id.rela2);
        rela3= (RelativeLayout) view.findViewById(R.id.rela3);
        relativeLayouts.add(rela1);
        relativeLayouts.add(rela2);
        relativeLayouts.add(rela3);

        line1= (ImageView) view.findViewById(R.id.line1);
        line2= (ImageView) view.findViewById(R.id.line2);
        line3= (ImageView) view.findViewById(R.id.line3);
        lineList.add(line1);
        lineList.add(line2);
        lineList.add(line3);

        for (int i=0;i<relativeLayouts.size();i++){
            RelativeLayout relativeLayout=relativeLayouts.get(i);
            final int j=i;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectText(j);
                    viewPager_fragment.setCurrentItem(j);

                }
            });
        }

        viewPager_fragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setSelectText(int j) {
        for (int i=0;i<textViewList.size();i++){
            TextView text = textViewList.get(i);
            if (i==j){
                text.setTextColor(Color.rgb(255, 156, 0));
                lineList.get(i).setVisibility(View.VISIBLE);
            }else {
                text.setTextColor(Color.rgb(76, 76, 76));
                lineList.get(i).setVisibility(View.INVISIBLE);
            }
        }
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
        BaseParams baseParams=new BaseParams("game/index");
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
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
                    initViewPagerFrag();

                }

            }
        });
    }

    private void initViewPagerFrag() {
        RecommendGameFragment fragment=new RecommendGameFragment();
        RecyclerGameFragment recyclerGameFragment1=new RecyclerGameFragment();
        Bundle bundle1=new Bundle();
        bundle1.putString("type","new");
        recyclerGameFragment1.setArguments(bundle1);

        RecyclerGameFragment recyclerGameFragment2=new RecyclerGameFragment();
        Bundle bundle2=new Bundle();
        bundle2.putString("type","grade");
        recyclerGameFragment2.setArguments(bundle2);

        fragmentList.add(fragment);
        fragmentList.add(recyclerGameFragment1);
        fragmentList.add(recyclerGameFragment2);

        MyFragmentViewPager myFragmentViewPager=new MyFragmentViewPager(getChildFragmentManager());
        viewPager_fragment.setAdapter(myFragmentViewPager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initViewPagerFromDB() {
        gameAdverts= (ArrayList<Adverts>) searchAdverts();
    }

    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initViewPager() {

//        whiteDots.removeAllViews();
//        imageViewList.clear();
//        MyLog.i("initViewPager");
//        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(viewPager.getLayoutParams().width,viewPager.getLayoutParams().height);
//        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));
//
//        MyLog.i("advertsList size"+gameAdverts.size());
//        for (int i=0;i<gameAdverts.size();i++){
//            ImageView imageView=new ImageView(context);
//            imageView.setLayoutParams(params);
//
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageViewList.add(imageView);
//            if (i>0){
//                params1.leftMargin=(int)(12.66*density);
//            }
//            ImageView dot=new ImageView(context);
//            dot.setImageResource(R.drawable.whitedotselected);
//            dot.setLayoutParams(params1);
////            whiteDots.addView(dot);
//            MyLog.i("imageUrl==="+Url.prePic+gameAdverts.get(i).getAdvert_pic());
//            imageloder.displayImage(Url.prePic+gameAdverts.get(i).getAdvert_pic(),imageView, MyDisplayImageOptions.getdefaultBannerOptions());
//
//        }
////        setDot(0);
//        MyLog.i("setAdapter");


        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        viewPager.setLayoutManager(layout);
        viewPager.setAdapter(new LayoutAdapter(getActivity(), viewPager));
        viewPager.setHasFixedSize(true);
        viewPager.setLongClickable(true);

//        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (viewPager.getChildCount() < 3) {
//                    if (viewPager.getChildAt(1) != null) {
//                        if (viewPager.getCurrentPosition() == 0) {
//                            View v1 = viewPager.getChildAt(1);
//                            v1.setScaleY(0.9f);
//                            v1.setScaleX(0.9f);
//                        } else {
//                            View v1 = viewPager.getChildAt(0);
//                            v1.setScaleY(0.9f);
//                            v1.setScaleX(0.9f);
//                        }
//                    }
//                } else {
//                    if (viewPager.getChildAt(0) != null) {
//                        View v0 = viewPager.getChildAt(0);
//                        v0.setScaleY(0.9f);
//                        v0.setScaleX(0.9f);
//                    }
//                    if (viewPager.getChildAt(2) != null) {
//                        View v2 = viewPager.getChildAt(2);
//                        v2.setScaleY(0.9f);
//                        v2.setScaleX(0.9f);
//                    }
//                }
//
//            }
//        });

        adapter=new GameLayoutAdapter(getActivity(),viewPager,gameAdverts);
        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


    }
//    private void setDot(int position) {
//        for (int i=0;i<whiteDots.getChildCount();i++){
//            if (i==position){
//                whiteDots.getChildAt(i).setSelected(true);
//            }else {
//                whiteDots.getChildAt(i).setSelected(false);
//            }
//        }
//
//    }

    private void initView() {

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
                        gameInfo.setGame_type(jsonObject1.getString("game_type"));
                        gameInfo.setGame_stage(jsonObject1.getString("game_stage"));
                        gameInfo.setNorms(jsonObject1.getString("norms"));
                        gameInfo.setRequire(jsonObject1.getString("require"));
                        try {
                            gameInfo.setGame_grade(jsonObject1.getString("game_grade"));

                        }catch (Exception e){
                            gameInfo.setGame_grade("");
                        }
                        if (MyAccount.isLogin){
                            gameInfo.setIscare(jsonObject1.getBoolean("iscare"));
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

                JSONArray newsArray=data.getJSONArray("newList");
                newGames=new ArrayList<>();
                for (int i=0;i<newsArray.length();i++){
                    JSONObject newObject=newsArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_id(newObject.getString("game_id"));
                    gameInfo.setGame_name(newObject.getString("game_name"));
                    gameInfo.setGame_img(newObject.getString("game_img"));
                    gameInfo.setGame_dev(newObject.getString("identity_cat"));
                    gameInfo.setNorms(newObject.getString("norms"));
                    gameInfo.setGame_grade(newObject.getString("game_grade"));
                    gameInfo.setGame_type(newObject.getString("game_type"));
                    gameInfo.setGame_stage(newObject.getString("game_stage"));
                    gameInfo.setRequire(newObject.getString("require"));
                    if (MyAccount.isLogin){
                        gameInfo.setIscare(newObject.getBoolean("iscare"));
                    }
                    newGames.add(gameInfo);
                }

                JSONArray gradeArray=data.getJSONArray("gradeList");
                gradeGames=new ArrayList<>();
                for (int i=0;i<gradeArray.length();i++){
                    JSONObject newObject=gradeArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_id(newObject.getString("game_id"));
                    gameInfo.setGame_name(newObject.getString("game_name"));
                    gameInfo.setGame_img(newObject.getString("game_img"));
                    gameInfo.setGame_dev(newObject.getString("identity_cat"));
                    gameInfo.setNorms(newObject.getString("norms"));

                    gameInfo.setGame_grade(newObject.getString("game_grade"));
                    gameInfo.setGame_type(newObject.getString("game_type"));
                    gameInfo.setGame_stage(newObject.getString("game_stage"));
                    gameInfo.setRequire(newObject.getString("require"));

                    if (MyAccount.isLogin){
                        gameInfo.setIscare(newObject.getBoolean("iscare"));
                    }
                    gradeGames.add(gameInfo);
                }

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

    class MyFragmentViewPager extends FragmentPagerAdapter{

        public MyFragmentViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


}
