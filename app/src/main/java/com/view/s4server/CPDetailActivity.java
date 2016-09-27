package com.view.s4server;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.account.MyAccount;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.net.BaseParams;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.game.GameDetailActivity;
import com.view.myattention.AttentionChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CPDetailActivity extends BaseActivity {

    boolean flag_showall=false;


    ImageView back;
    TextView name_title;
    ImageView share;
    private ImageView care_title;
    Dialog download_dialog;


    int[] locations = new int[2];

    private String user_id;
    private String identity_cat;

    private AppBarLayout appBarLayout;
    private ImageView icon;
    private ImageView care;
    private TextView name;
    private TextView intro;
    private TextView info;
    private HorizontalListView horizontalListView;
    private Toolbar toolbar;
    private TextView all;

    private String namestring;
    private String logostring;
    private String scalestring;
    private String phonestring;
    private String introstring;

    List<GameInfo> othergames;
    private float density;
    boolean focus=false;

    private LinearLayout div_other;
    private LinearLayout div_intro;

    private TextView othergamemore;

    private ImageLoader imageloder=ImageLoader.getInstance();

    private View content;
    private String areaName;
    private String privinceName;
    private String cityName;
    private String countryName;
    private String addr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_cpdetail);

        setVisible(false);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));

        content=findViewById(R.id.coordinatorlayout_cpdetail);
        content.setVisibility(View.INVISIBLE);

        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_cpdetail);
        toolbar= (Toolbar) findViewById(R.id.toolbar_cpDetail);
        back= (ImageView) findViewById(R.id.back_cpdetail);
        name_title= (TextView) findViewById(R.id.title_cpdetail);
        care_title= (ImageView) findViewById(R.id.care_cpdetatil);
        share= (ImageView) findViewById(R.id.share_cpdetail);
        icon= (ImageView) findViewById(R.id.roundImage_cpdetail);
        name= (TextView) findViewById(R.id.name_cpdetail);

        care= (ImageView) findViewById(R.id.attention_cpdetail);
        intro= (TextView) findViewById(R.id.introduction_cpdetail);
        info= (TextView) findViewById(R.id.info_cpdetail);
        horizontalListView= (HorizontalListView) findViewById(R.id.horListView_cpdetail);
        share= (ImageView) findViewById(R.id.share_titlebar_de);
        div_other= (LinearLayout) findViewById(R.id.div_othergame_cpdetail);
        div_intro= (LinearLayout) findViewById(R.id.div_intro_cpdetail);
        othergamemore= (TextView) findViewById(R.id.other_game_cpdetail);


        user_id=getIntent().getStringExtra("user_id");
        identity_cat=getIntent().getStringExtra("identity_cat");



        care= (ImageView) findViewById(R.id.attention_cpdetail);


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        initListener();

        initData();

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
                        AttentionChange.removeAttention("2",user_id, CPDetailActivity.this);
                    }else {
                        focus=true;
                        AttentionChange.addAttention("2",user_id,CPDetailActivity.this);
                    }
                    changeCare(focus);
                }else {
                    //未登录
                    goLogin(CPDetailActivity.this);
                }

            }
        };
        care_title.setOnClickListener(myListener);
        care.setOnClickListener(myListener);
        findViewById(R.id.back_cpdetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_SCROLL:
                MyLog.i("care位置：："+locations[0]+"==="+locations[1]);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("index/cpdetail");
        baseParams.addParams("user_id",user_id);
        baseParams.addParams("identity_cat",identity_cat);
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        baseParams.getRequestParams().setCacheMaxAge(1000*60*30);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result;

            @Override
            public void onSuccess(String result) {
                this.result=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                setContentView(R.layout.layout_neterror);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("cpdetail"+result);
                parser(result);

            }
        });

    }

    private void parser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject detail=data.getJSONObject("detail");
                namestring=detail.getString("company_name");
                logostring=detail.getString("logo");
                scalestring=detail.getString("company_scale");
                phonestring=detail.getString("company_phone");
                introstring=detail.getString("company_intro");
                areaName=detail.getString("area_name");

                privinceName=detail.getString("province_name");
                cityName=detail.getString("city_name");
                countryName=detail.getString("county_name");
                addr=detail.getString("addr");

                String fo=detail.getString("focus");
                if (fo.equals("false")||fo.equals("0")){
                    focus=false;
                }else if(fo.equals("true")||fo.equals("1")){
                    focus=true;
                }
                othergames=new ArrayList<>();
                JSONArray cpGames=data.getJSONArray("cpGameList");
                for (int i=0;i<cpGames.length();i++){
                    JSONObject game=cpGames.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_id(game.getString("id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_dev(game.getString("identity_cat"));
                    othergames.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyLog.i("cpdetail=="+e.toString());
        }
        try {
            initView();

        }catch (Exception e){
            MyLog.i("cpdetail exception==="+e.toString());
        }

    }

    private void initView() {
        name_title.setText(namestring);
        name.setText(namestring);
        MyLog.i("namestring=="+namestring);


        imageloder.displayImage(Url.prePic+logostring,icon, MyDisplayImageOptions.getroundImageOptions());

        MyLog.i("introstring=="+introstring);

        if (TextUtils.isEmpty(introstring)){
            div_intro.setVisibility(View.GONE);
            MyLog.i("div_intro GONE");

        }else {
            intro.setText(introstring);
//            all.setVisibility(View.INVISIBLE);
        }


        info.setText("所在区域 ："+areaName+"\n公司规模 ："+scalestring);

        if (focus){
            care_title.setImageResource(R.drawable.cared);
            care.setImageResource(R.drawable.attention_has);
        }

        if (othergames.size()==0){
            div_other.setVisibility(View.GONE);
        }else {
            if (othergames.size()<10){
                othergamemore.setVisibility(View.INVISIBLE);
            }
            MyLog.i("othergames=="+othergames.size());

            Game_HL_Adapter gameAdaper=new Game_HL_Adapter(this,othergames);
            horizontalListView.setAdapter(gameAdaper);
            horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(CPDetailActivity.this, GameDetailActivity.class);
                    intent.putExtra("game_id",othergames.get(position).getGame_id());
                    intent.putExtra("ident_cat",othergames.get(position).getGame_dev());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//                    finish();
                }
            });
        }
        MyLog.i("initView4");

        content.setVisibility(View.VISIBLE);
        setVisible(true);
    }


    private void initDialog() {
        download_dialog=new Dialog(CPDetailActivity.this);
        download_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        download_dialog.setCanceledOnTouchOutside(false);
        download_dialog.setContentView(R.layout.dialog_download);
        download_dialog.findViewById(R.id.channel_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download_dialog.dismiss();
            }
        });
        download_dialog.findViewById(R.id.ok_downdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
