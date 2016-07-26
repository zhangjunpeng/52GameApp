package com.view.myreport;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.app.view.ViewPagerNoScroll;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ExpertReportActivity extends BaseActivity {

    String gameid;

    ImageView back;
    TextView title;
    TextView save;

    private TextView pczs;//评测综述
    private TextView pcpj;//评测评价


    private FragmentManager fm;
    private Fragment mContent;

    private FragmentEvaCon fragmentEvaCon;
    private FragmentEva fragmentEva;

    private ViewPagerNoScroll viewPager;

    private List<Fragment> fragmentList;
//    private RecyclerView recyclerView;


    public static String score="";
    public static String game_grade="";

    private LinearLayout linear_appbar;

    private TextView pjyqText;
    private TextView scoreText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_expert_report);
//        setImmerseLayout(findViewById(R.id.titlebar_expert));
//        int statusBarHeight = ScreenUtil.getStatusBarHeight(getBaseContext());
//        findViewById(R.id.titlebar_expert).setPadding(0, statusBarHeight, 0, 0);
//        setImmerseLayout(findViewById(R.id.titlebar_expert));
//        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_expert);
//        setSupportActionBar((Toolbar) findViewById(R.id.titlebar_expert));


        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars

        tintManager.setTintColor(Color.parseColor("#252525"));

        try {
            gameid=getIntent().getStringExtra("game_id");

        }catch (Exception e){
            MyLog.i("gameid 获取失败");
        }
        MyLog.i("oncreate");
        Bundle bundle=new Bundle();
        bundle.putString("gameid",gameid);

        fm=getSupportFragmentManager();
        fragmentEvaCon=new FragmentEvaCon();
        fragmentEvaCon.setArguments(bundle);
        fragmentEva=new FragmentEva();
        fragmentEva.setArguments(bundle);
        fragmentList=new ArrayList<>();

        fragmentList.add(fragmentEvaCon);
        fragmentList.add(fragmentEva);

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        pczs= (TextView) findViewById(R.id.pczs);
        pcpj= (TextView) findViewById(R.id.pcpj);
        linear_appbar= (LinearLayout) findViewById(R.id.linear_appbar);

        pjyqText= (TextView) findViewById(R.id.pjyq);
        scoreText= (TextView) findViewById(R.id.gamescore);


//        recyclerView= (RecyclerView) findViewById(R.id.recyler_exp);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new HomeAdapter());

        viewPager= (ViewPagerNoScroll) findViewById(R.id.viewpager_expert_report);

        viewPager.setAdapter(new MyFragmentAdapter(fm,fragmentList));
        viewPager.setCurrentItem(0);

//        mContent=fragmentEvaCon;
//        fm.beginTransaction().add(R.id.contianer_expert_report,fragmentEvaCon).commit();


        MyLog.i("oncreate2");
        initView();
        initListener();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void changeScore(GameScoreEvent gameScoreEvent){
        MyLog.i("接受到EventBus");
        pjyqText.setText(gameScoreEvent.grade);
        scoreText.setText(gameScoreEvent.score);
    }

    private void initListener() {
        pczs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pczs.setSelected(true);
                pcpj.setSelected(false);
                pczs.setClickable(false);
                pcpj.setClickable(true);
                pczs.setTextColor(Color.rgb(255,255,255));
                pcpj.setTextColor(Color.rgb(76,76,76));

                viewPager.setCurrentItem(0);
                linear_appbar.setVisibility(View.GONE);


//                switchContent(mContent,fragmentEvaCon);
            }
        });
        pcpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pczs.setSelected(false);
                pcpj.setSelected(true);
                pczs.setClickable(true);
                pcpj.setClickable(false);
                pczs.setTextColor(Color.rgb(76,76,76));
                pcpj.setTextColor(Color.rgb(255,255,255));
//                switchContent(mContent,fragmentEva);
                viewPager.setCurrentItem(1);
                linear_appbar.setVisibility(View.VISIBLE);


            }
        });
    }

    private void initView() {

        title.setText("专家评测报告");
        save.setVisibility(View.INVISIBLE);

        pczs.setSelected(true);
        pczs.setTextColor(Color.rgb(255,255,255));
    }

    class MyFragmentAdapter extends FragmentPagerAdapter{

        private List<Fragment> fragments;
        public MyFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
            super(fm);
            fragments=fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}
