package com.view.s4server;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.account.MyAccount;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.myattention.AttentionChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class InvesmentDetialActivity extends BaseActivity implements View.OnClickListener{

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView name_title;
    private ImageView care_title;
    private ImageView care;
    private TextView info;
    private TextView intro;
    private LinearLayout continar;
    private TextView all;
    private TextView name;
    private ImageView icon;
    private ImageView back;

    String user_id;
    String identity_cat;

    String namestring;
    String logostring;
    String scalestring;
    String websitstring;
    String introstring;
    String areastring;
    String invest_catstring;
    List<InvestCaseInfo> invescases;
    String invest_stagestring;
    List<LinearLayout> content;
    private boolean flag_showall=false;
    private float density;
    private boolean focus=false;

    LinearLayout div_intro;
    LinearLayout div_anli;

    private ImageLoader imageLoader= ImageLoader.getInstance();

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_tz_detial);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));


        view=findViewById(R.id.coordinatorlayout_tzdetail);
        view.setVisibility(View.INVISIBLE);

        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_tzdetail);
        toolbar= (Toolbar) findViewById(R.id.toolbar_tzDetail);
        care= (ImageView) findViewById(R.id.attention_tzdetail);
        name_title= (TextView) findViewById(R.id.title_tzdetail);
        care_title= (ImageView) findViewById(R.id.care_tzdetatil);
        info= (TextView) findViewById(R.id.info_tzdetail);
        intro= (TextView) findViewById(R.id.introduction_tzdetail);
        all= (TextView) findViewById(R.id.all_tzdetail);
        name= (TextView) findViewById(R.id.name_tzdetail);
        icon= (ImageView) findViewById(R.id.roundImage_tzdetail);
        continar= (LinearLayout) findViewById(R.id.contianer_tzdetail);

        div_intro= (LinearLayout) findViewById(R.id.div_intro_tzdetail);
        div_anli= (LinearLayout) findViewById(R.id.div_anli_tzdetail);


        content=new ArrayList<>();

        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        initListener();


        initData();
    }

    private void initData() {
        user_id=getIntent().getStringExtra("user_id");
        identity_cat=getIntent().getStringExtra("identity_cat");
        BaseParams baseParams=new BaseParams("index/detail");
        baseParams.addParams("user_id",user_id);
        baseParams.addParams("identity_cat",identity_cat);
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(60*1000*60);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                res=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                setContentView(R.layout.neterror);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("invesdetail==="+res);
                jsonparser(res);
            }

        });
    }
    private void changeCare(boolean iscare){
        if (iscare){
            care_title.setImageResource(R.drawable.cared);
            care.setImageResource(R.drawable.attention_has);
        }else {
            care_title.setImageResource(R.drawable.care);
            care.setImageResource(R.drawable.attention);
        }
    }

    private void jsonparser(String res) {
        try {
            JSONObject jsonObect=new JSONObject(res);
            boolean su=jsonObect.getBoolean("success");
            int code=jsonObect.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObect.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                JSONObject jinfo=data.getJSONObject("info");
                namestring=jinfo.getString("company_name");
                logostring=jinfo.getString("logo");
                introstring=jinfo.getString("company_intro");
                invest_catstring=jinfo.getString("invest_cat");
                invest_stagestring=jinfo.getString("invest_stage");
                areastring=jinfo.getString("area");
                String fo=jinfo.getString("focus");
                if (fo.equals("false")||fo.equals("0")){
                    focus=false;
                }else if(fo.equals("true")||fo.equals("1")){
                    focus=true;
                }
                invescases=new ArrayList<>();
                JSONArray cases=jinfo.getJSONArray("invest_case");
                for (int i=0;i<cases.length();i++){
                    JSONObject investcase=cases.getJSONObject(i);
                    InvestCaseInfo caseInfo=new InvestCaseInfo();
                    caseInfo.setName(investcase.getString("name"));
                    caseInfo.setLogo(investcase.getString("logo"));
                    caseInfo.setInvest_money(investcase.getString("invest_money"));
                    caseInfo.setInvest_stage(investcase.getString("invest_stage"));
                    caseInfo.setOnline_time(investcase.getString("online_time"));
                    invescases.add(caseInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        name_title.setText(namestring);
        name.setText(namestring);
        if (focus){
            care_title.setImageResource(R.drawable.cared);
            care.setImageResource(R.drawable.attention_has);
        }
        imageLoader.displayImage(Url.prePic+logostring,icon, MyDisplayImageOptions.getroundImageOptions());
//        Picasso.with(this)
//                .load(Url.prePic+logostring)
//                .into(icon);
        if (TextUtils.isEmpty(introstring)){
            div_intro.setVisibility(View.GONE);
        }else {
            intro.setText(introstring);
            all.setVisibility(View.INVISIBLE);
        }

        info.setText("所在区域 ："+areastring+"\n机构类型 ："+invest_catstring+"\n投资阶段 ："+invest_stagestring);
        MyLog.i("ivescase size==="+invescases.size());
        if (invescases.size()==0){
            div_anli.setVisibility(View.GONE);
        }else {
            addcase(invescases);
        }
        view.setVisibility(View.VISIBLE);

    }


    private void initListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
//                if (verticalOffset<-100*density){
//                    toolbar.setBackgroundColor(Color.argb(15,255,255,255));
//                }else {
//                    toolbar.setBackgroundColor(Color.argb( 0,37,37,37));
//                }
                if (verticalOffset>-200*density){
                    name_title.setVisibility(View.INVISIBLE);
                    care_title.setVisibility(View.INVISIBLE);
                }else {
                    name_title.setVisibility(View.VISIBLE);
                    care_title.setVisibility(View.VISIBLE);
                }
                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                } else if(isShow) {

                    isShow = false;
                }
            }
        });

        View.OnClickListener myListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyAccount.isLogin){
                    if (focus){
                        focus=false;
                        AttentionChange.removeAttention("4",user_id, InvesmentDetialActivity.this);
                    }else {
                        focus=true;
                        AttentionChange.addAttention("4",user_id,InvesmentDetialActivity.this);
                    }
                    changeCare(focus);
                }else {
                    //未登录
                    goLogin(InvesmentDetialActivity.this);
                }

            }
        };
        care_title.setOnClickListener(myListener);
        care.setOnClickListener(myListener);
        findViewById(R.id.back_tzdetail).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_tzdetail:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);

                break;
        }

    }
    public void addcase(List<InvestCaseInfo> cases){
        for (int i=0;i<cases.size();i++){
            MyLog.i("addView1");
            ViewHolder viewHolder=new ViewHolder();
            LinearLayout layout= (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_item_invesdetail,null);
            MyLog.i("addView2");
            InvestCaseInfo caseInfo=cases.get(i);
            viewHolder.icon= (ImageView) layout.findViewById(R.id.icon_item_invesdetail);
            viewHolder.time= (TextView) layout.findViewById(R.id.time_item_invesdetail);
            viewHolder.name= (TextView) layout.findViewById(R.id.name_item_invesdetail);
            viewHolder.money= (TextView) layout.findViewById(R.id.money_item_invesdetail);
            viewHolder.stage= (TextView) layout.findViewById(R.id.stage_item_invesdetail);
            imageLoader.displayImage(Url.prePic+caseInfo.getLogo(),viewHolder.icon,MyDisplayImageOptions.getroundImageOptions());
//
//            Picasso.with(this)
//                    .load(Url.prePic+caseInfo.getLogo())
//                    .placeholder(R.drawable.default_icon)
//                    .into(viewHolder.icon);
            viewHolder.time.setText("上线时间 ："+caseInfo.getOnline_time());
            viewHolder.name.setText(caseInfo.getName());
            viewHolder.money.setText("投资金额 ："+caseInfo.getInvest_money());
            viewHolder.stage.setText("投资阶段 ："+caseInfo.getInvest_stage());

            if (i==0){
                layout.findViewById(R.id.topline_item_invesdetail).setVisibility(View.INVISIBLE);
            }
            if (i==cases.size()-1){
                layout.findViewById(R.id.boomline_item_invesdetail).setVisibility(View.INVISIBLE);
            }
            MyLog.i("addView3");
            layout.setTag(viewHolder);
            MyLog.i("addView4");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            continar.addView(layout,layoutParams);
            MyLog.i("addView5");
            content.add(layout);
            MyLog.i("addView6");
        }

    }
    class ViewHolder{
        TextView time;
        ImageView icon;
        TextView name;
        TextView money;
        TextView stage;
    }

}
