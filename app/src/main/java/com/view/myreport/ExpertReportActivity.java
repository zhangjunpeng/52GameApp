package com.view.myreport;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
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

    private ViewPager viewPager;

    private List<Fragment> fragmentList;
//    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_report);
//        setImmerseLayout(findViewById(R.id.titlebar_expertreport));
//        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_expert);
//        setSupportActionBar(toolbar);

        MyLog.i("oncreate");

        fm=getSupportFragmentManager();
        fragmentEvaCon=new FragmentEvaCon();
        fragmentEva=new FragmentEva();
        fragmentList=new ArrayList<>();

        fragmentList.add(fragmentEvaCon);
        fragmentList.add(fragmentEva);

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        pczs= (TextView) findViewById(R.id.pczs);
        pcpj= (TextView) findViewById(R.id.pcpj);

//        recyclerView= (RecyclerView) findViewById(R.id.recyler_exp);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new HomeAdapter());

        viewPager= (ViewPager) findViewById(R.id.viewpager_expert_report);

        viewPager.setAdapter(new MyFragmentAdapter(fm,fragmentList));
        viewPager.setCurrentItem(0);
//        mContent=fragmentEvaCon;
//        fm.beginTransaction().add(R.id.contianer_expert_report,fragmentEvaCon).commit();


        MyLog.i("oncreate2");

        try {
            gameid=getIntent().getStringExtra("game_id");

        }catch (Exception e){
            MyLog.i("gameid 获取失败");
        }


        initView();
        initListener();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

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


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ExpertReportActivity.this).inflate(R.layout.item_expertreport,null));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.content.setText("6666666666");
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView content;
            HorizontalScrollView horizontalScrollView;
            public MyViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.name_item_expertreport);
                content= (TextView) itemView.findViewById(R.id.content_item_expertreport);
                horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.imageContinar_expertreport);
            }
        }
    }


}
