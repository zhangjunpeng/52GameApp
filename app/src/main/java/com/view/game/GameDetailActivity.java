package com.view.game;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.HorizontalListView;
import com.app.view.MyDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.adapter.Game_HL_Adapter;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.myattention.AttentionChange;
import com.view.s4server.CPDetailActivity;
import com.view.s4server.IssueDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class GameDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView icon;
    TextView game_name;
    LinearLayout find;
    private TextView companyname;
    private TextView canyu;
    private TextView baseinfo;
    private TextView game_intro;
    private TextView up_info;
    private LinearLayout gameShot;
    private AppBarLayout appBarLayout;
    private ImageView down_title;
    private ImageView care_title;
    private ImageView care;
    private ImageView download;

    private HorizontalListView other_game;
    private TextView conmentnum;
    private LinearLayout continer_advice;

    private TextView find_tz;
    private TextView find_fx;
    private TextView find_ip;
    private TextView find_wb;


    int[] star_id={R.id.star1_gamedetail,R.id.star2_gamedetail,R.id.star3_gamedetail,R.id.star4_gamedetail,R.id.star5_gamedetail};
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    ImageView[] stars={star1,star2,star3,star4,star5};



    private HorizontalScrollView hs;
    Toolbar toolbar;

    private String game_id;
    private float density;

    private String cp_id;

    private GameInfo gameInfo;


    private String create_timestring;
    private String game_test_numsstring;
    private String game_update_introstring;
    private boolean focus;
    private String score;
    private String webpre;
    private int advise_num;
    List<GameInfo> game_list;
    List<Advise> adviseList;
    private String companynamestring;

    private String pic_dir;
    private List<String> game_shots;
    private String game_introstring;
    private String game_download_unit;
    private String game_download_nums;
    private String requirement;
    private boolean flag_showall_inro=false;
    private boolean flag_showall_update=false;

    private LinearLayout div_gameshot;
    private LinearLayout div_updateInfo;
    private LinearLayout div_othergame;
    private LinearLayout div_advise;
    private String game_typestring;

    private ImageView back;
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private String ident_cat="2";

    private View view;
    private ImageView gradeimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_game_detail);
        view=findViewById(R.id.gamedetail);
        view.setVisibility(View.INVISIBLE);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars

        tintManager.setTintColor(Color.parseColor("#252525"));
//        toolbar= (Toolbar) findViewById(R.id.toolbar_gameDetail);
//        setSupportActionBar(toolbar);

        back= (ImageView) findViewById(R.id.back_gamedetail);

        game_id=getIntent().getStringExtra("game_id");

        icon= (ImageView) findViewById(R.id.image_game_detail);
        game_name= (TextView) findViewById(R.id.gamename_game);
        find= (LinearLayout) findViewById(R.id.linear_requir_gamedetail);
        for (int i=0;i<stars.length;i++){
            stars[i]= (ImageView) findViewById(star_id[i]);
        }
        canyu= (TextView) findViewById(R.id.canyu_game_detail);
        baseinfo= (TextView) findViewById(R.id.baseInfo_game_detail);
        gameShot= (LinearLayout) findViewById(R.id.gameshot_game_detail);
        hs= (HorizontalScrollView) findViewById(R.id.scroll_game_shot_detail);
        game_intro= (TextView) findViewById(R.id.gameintro_game_detail);
        up_info= (TextView) findViewById(R.id.update_introduction_gamedetail);
        other_game= (HorizontalListView) findViewById(R.id.other_game_gamedetail);
        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_gamedetail);
        down_title= (ImageView) findViewById(R.id.download_title_gamedetail);
        care_title= (ImageView) findViewById(R.id.care_title_gamedetail);
        care= (ImageView) findViewById(R.id.attention_gametail);
        download= (ImageView) findViewById(R.id.download_gamedetail);

        gameShot= (LinearLayout) findViewById(R.id.gameshot_game_detail);
        conmentnum= (TextView) findViewById(R.id.conment_num);
        companyname= (TextView) findViewById(R.id.name_company_game);
        continer_advice= (LinearLayout) findViewById(R.id.continer_comment_gamedetail);

        div_gameshot= (LinearLayout) findViewById(R.id.div_gameshot_gamedetail);
        div_updateInfo= (LinearLayout) findViewById(R.id.div_updateinfo_gameDetail);
        div_othergame= (LinearLayout) findViewById(R.id.div_othergame_gamedetail);
        div_advise= (LinearLayout) findViewById(R.id.div_advise_gamedetail);
        gradeimage= (ImageView) findViewById(R.id.grade_gamedetail);

        find_fx= (TextView) findViewById(R.id.find_fx);
        find_ip= (TextView) findViewById(R.id.find_ip);
        find_tz= (TextView) findViewById(R.id.find_tz);
        find_wb= (TextView) findViewById(R.id.find_wb);

        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）

        initData();
        initListener();

    }

    private void initListener() {
        download.setOnClickListener(this);
        down_title.setOnClickListener(this);
        companyname.setOnClickListener(this);
        back.setOnClickListener(this);
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
                if (verticalOffset>-250*density){
                    down_title.setVisibility(View.INVISIBLE);
                    care_title.setVisibility(View.INVISIBLE);
                }else {
                    if (!TextUtils.isEmpty(gameInfo.getGame_download_url())){
                        down_title.setVisibility(View.VISIBLE);

                    }else {
                        down_title.setVisibility(View.INVISIBLE);

                    }
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
                        AttentionChange.removeAttention("1",game_id,GameDetailActivity.this);
                    }else {
                        focus=true;
                        AttentionChange.addAttention("1",game_id,GameDetailActivity.this);
                    }
                    changeCare(focus);
                }else {
                    //未登录
                    goLogin(GameDetailActivity.this);
                }

            }
        };
        care_title.setOnClickListener(myListener);
        care.setOnClickListener(myListener);
    }

    private void initData() {

        int id=Integer.parseInt(game_id);
        if (id<5000){
            MyLog.i("GameID错误，id为"+game_id);
            return;
        }
        BaseParams gamedetail=new BaseParams("game/gamedetail");
        gamedetail.addParams("game_id",game_id);
        if (MyAccount.isLogin){
            gamedetail.addParams("token",MyAccount.getInstance().getToken());
        }
        gamedetail.addSign();
        gamedetail.getRequestParams().setCacheMaxAge(1000*60*30);

        x.http().post(gamedetail.getRequestParams(), new Callback.CacheCallback<String>() {

            private String result=null;
            @Override
            public boolean onCache(String result) {
                this.result=result;
                return true;
            }

            @Override
            public void onSuccess(String result) {
               this.result=result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i(ex.toString());
                setContentView(R.layout.neterror);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("GameDetail===~~~~~"+result);
                gameDetailParser(result);
                initView();

            }
        });
    }

    public void gameDetailParser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            boolean su=jsonObject.getBoolean("success");
            if (su&&code==200){
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                webpre=jsonObject1.getString("prefixPackage");

                JSONObject jsonObject2=jsonObject1.getJSONObject("gameInfo");
                JSONObject info=jsonObject2.getJSONObject("info");
                gameInfo=new GameInfo();
                gameInfo.setGame_name(info.getString("game_name"));
                gameInfo.setGame_size(info.getString("game_size"));
                cp_id=info.getString("cp_id");
                gameInfo.setGame_download_nums(info.getString("game_download_nums"));
                gameInfo.setGame_stage(info.getString("game_stage"));
                gameInfo.setGame_download_url(info.getString("game_download_url"));
                gameInfo.setGame_img(info.getString("game_img"));
                gameInfo.setGame_platform(info.getString("game_platform"));

                game_typestring=info.getString("game_type");
                create_timestring=info.getString("create_time");
                game_test_numsstring=info.getString("game_test_nums");
                requirement=info.getString("requirement");
                ident_cat=info.getString("identity_cat");

                gameInfo.setPack(info.getString("pack"));
                gameInfo.setChecked(info.getString("checked"));
                gameInfo.setGame_grade(info.getString("quality"));
                String fo=info.getString("focus");
                if (fo.equals("false")||fo.equals("0")){
                    focus=false;
                }else if(fo.equals("true")||fo.equals("1")){
                    focus=true;
                }

                JSONObject game_shot=info.getJSONObject("game_shot");

                pic_dir=game_shot.getString("pic_dir");
                JSONArray shots=game_shot.getJSONArray("pic_"+pic_dir);

                game_shots=new ArrayList<>();
                for (int i=0;i<shots.length();i++){
                    JSONArray jsonarray=shots.getJSONArray(i);
                    for (int j=0;j<jsonarray.length();j++){
                        String imgs=jsonarray.getString(j);
                        game_shots.add(imgs);
                    }

                }

                game_introstring=info.getString("game_intro");
                game_update_introstring=info.getString("game_update_intro");
                companynamestring=info.getString("company_name");
                score=info.getString("playScore");
                game_download_unit=info.getString("game_download_unit");
                game_download_nums=info.getString("game_download_nums");

                JSONArray cpGame=info.getJSONArray("gameList");
               game_list=new ArrayList<>();

                for (int i=0;i<cpGame.length();i++){
                    JSONObject jsonObject3=cpGame.getJSONObject(i);
                    GameInfo gameInfo1=new GameInfo();
                    gameInfo1.setGame_id(jsonObject3.getString("id"));
                    gameInfo1.setGame_img(jsonObject3.getString("game_img"));
                    gameInfo1.setGame_name(jsonObject3.getString("game_name"));
                    game_list.add(gameInfo1);
                }
                adviseList=new ArrayList<>();
                advise_num=info.getInt("advise_num");
                JSONArray advices=info.getJSONArray("advise");
                for (int i=0;i<advices.length();i++){
                    JSONObject adv=advices.getJSONObject(i);
                    Advise advise=new Advise();
                    advise.setShowname(adv.getString("showname"));
                    advise.setAdvise(adv.getString("advise"));
                    advise.setDate_time(adv.getString("date_time"));
                    advise.setTest_total_score(adv.getString("test_total_score"));
                    advise.setHour_time(adv.getString("hour_time"));
                    adviseList.add(advise);
                }

            }
            MyLog.i("解析完成");
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void initView() {
        MyLog.i("GameDetial~~~~initView()");

        String imageUrl=Url.prePic+gameInfo.getGame_img();

//        Picasso.with(this)
//                .load(imageUrl)
//                .placeholder(R.drawable.default_icon)
//                .into(icon);
        imageLoader.displayImage(imageUrl,icon, MyDisplayImageOptions.getroundImageOptions());

        if (!TextUtils.isEmpty(gameInfo.getGame_grade())){
            imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),gradeimage, MyDisplayImageOptions.getdefaultImageOptions());

        }

        MyLog.i("game_namestring==="+gameInfo.getGame_name());
        game_name.setText(gameInfo.getGame_name());
        companyname.setText(companynamestring);
        if (TextUtils.isEmpty(requirement)){
            find.setVisibility(View.INVISIBLE);
        }else {
            if (requirement.contains("找发行")) {
                find_fx.setVisibility(View.VISIBLE);
            }
            if (requirement.contains("找投资")) {
                find_tz.setVisibility(View.VISIBLE);
            }
            if (requirement.contains("找IP")) {
                find_ip.setVisibility(View.VISIBLE);
            }
            if (requirement.contains("找外包")) {
                find_wb.setVisibility(View.VISIBLE);
            }
        }

        if (focus){
            //已关注
            care_title.setImageResource(R.drawable.cared);
            care.setImageResource(R.drawable.attention_gamedetail_has);
        }else {

        }
        MyLog.i("game_download_url=="+gameInfo.getGame_download_url());
        String info="";
        info="上传时间："+create_timestring+"\n包体大小："+gameInfo.getGame_size()+"M\n标   签："+gameInfo.getGame_stage()+"/"+gameInfo.getGame_platform()+"/"+game_typestring;
        if (TextUtils.isEmpty(gameInfo.getGame_download_url())){
            down_title.setVisibility(View.GONE);
            download.setVisibility(View.GONE);
            down_title.setClickable(false);

        }else {
            down_title.setVisibility(View.VISIBLE);
            download.setVisibility(View.VISIBLE);
            down_title.setClickable(true);
        }
//        if (!gameInfo.getPack().equals("1")||!gameInfo.getChecked().equals("1")){
//            down_title.setVisibility(View.GONE);
//            download.setVisibility(View.GONE);
//            down_title.setClickable(false);
//            info="上传时间："+create_timestring+"\n标   签："+gameInfo.getGame_stage()+" · "+gameInfo.getGame_platform();
//
//        }else {
//            info="上传时间："+create_timestring+"\n包体大小："+gameInfo.getGame_size()+"M\n标   签："+gameInfo.getGame_stage()+"/"+gameInfo.getGame_platform()+"/"+game_typestring;
//
//        }

        MyLog.i("score=="+score);
        if (score.equals("null")||score.equals("0")){
            findViewById(R.id.div_playerscore_gamedetail).setVisibility(View.GONE);
        }else {
            setStarScore(Float.parseFloat(score));
        }

        MyLog.i("game_test_numsstring=="+game_test_numsstring);
        canyu.setText("("+game_test_numsstring+"人参与)");
        baseinfo.setText(info);

//        try {
//            String into=game_update_introstring.substring(0,74);
//            up_info.setText(into+"...");
//        }catch (Exception e){
//            MyLog.i("报错1");
//            up_info.setText(game_update_introstring);
//        }
        if (TextUtils.isEmpty(game_update_introstring)){
            div_updateInfo.setVisibility(View.GONE);
        }else {
            up_info.setText(game_update_introstring);
        }
        game_intro.setText(game_introstring);
//        try {
//            String into=game_introstring.substring(0,78);
//            game_intro.setText(into+"...");
//        }catch (Exception e){
//            MyLog.i("报错2");
//            game_intro.setText(game_introstring);
//        }


        MyLog.i("game_update_introstring=="+game_update_introstring);

        if (game_shots.size()==0){
            div_gameshot.setVisibility(View.GONE);
        }else {
            addshots(game_shots);
        }
        if (game_list.size()==0){
            div_othergame.setVisibility(View.GONE);
        }else {
            other_game.setAdapter(new Game_HL_Adapter(this,game_list));
            other_game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GameInfo gameInfo=game_list.get(position);
                    Intent intent= new Intent(GameDetailActivity.this,GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
        }

        if (adviseList.size()==0){
            div_advise.setVisibility(View.GONE);
        }else {
            conmentnum.setText("(" + advise_num + ")");
            addAdvices(adviseList);
        }
//        setVisible(true);
        view.setVisibility(View.VISIBLE);
    }

    private void addAdvices(List<Advise> adviseList) {
//        MyLog.i("添加评论1,adviseList size=="+adviseList.size());
//        LinearLayoutCompat.LayoutParams params=new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<adviseList.size();i++){
            Advise ad=adviseList.get(i);
//            MyLog.i("添加评论2");
            View view= LayoutInflater.from(this).inflate(R.layout.item_advicelist,null);
            TextView name= (TextView) view.findViewById(R.id.showname_item_advice);
            TextView time= (TextView) view.findViewById(R.id.time_item_advice);
            final TextView advice= (TextView) view.findViewById(R.id.advice_item_advice);
//            MyLog.i("添加评论3");
            name.setText(ad.getShowname());
            time.setText(ad.getDate_time()+"  "+ad.getHour_time());
            advice.setText(ad.getAdvise());
//            MyLog.i("添加评论4");
            continer_advice.addView(view);
//            MyLog.i("添加评论5");
            advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advice.getMaxLines()==5){
                        advice.setEllipsize(null);
                        advice.setMaxLines(100);
                    }else {
                        advice.setEllipsize(TextUtils.TruncateAt.END);
                        advice.setMaxLines(5);
                    }

                }
            });
        }

    }
    private void changeCare(boolean iscare){
        if (iscare){
            care_title.setImageResource(R.drawable.cared);
            care.setImageResource(R.drawable.attention_gamedetail_has);
        }else {
            care_title.setImageResource(R.drawable.care);
            care.setImageResource(R.drawable.attention_gamedetail);
        }
    }

    private void addshots(List<String> game_shots) {
        LinearLayoutCompat.LayoutParams params;
        if (pic_dir.equals("hori")){
            params=new LinearLayoutCompat.LayoutParams((int)(263*density),(int)(148*density));
        }else {
            params=new LinearLayoutCompat.LayoutParams((int)(160*density),(int)(238*density));
        }
        for (int i=0;i<game_shots.size();i++) {
            ImageView imageView = new ImageView(this);

            params.rightMargin = (int) (3 * density);
            params.bottomMargin = (int) (18 * density);
            params.leftMargin=(int) (3*density);
            imageView.setId(i);
            imageView.setLayoutParams(params);

            gameShot.addView(imageView, params);
            imageLoader.displayImage(Url.prePic + game_shots.get(i),imageView, MyDisplayImageOptions.getdefaultImageOptions());

        }
    }

    private void setStarScore(float socre){
        int id= (int) socre;
        float yu=socre-(float) id;
        for (int i=0;i<id;i++){
            stars[i].setImageResource(R.drawable.orangestar);
        }
        if (yu>=0.5){
            stars[id].setImageResource(R.drawable.halforangestar);
        }
    }

    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name_company_game:
                Intent intent=null;
                if (ident_cat.equals("2")) {
                    intent = new Intent(this, CPDetailActivity.class);
                    intent.putExtra("user_id", cp_id);
                    intent.putExtra("identity_cat", "2");
                }else {
                    intent = new Intent(this, IssueDetailActivity.class);
                    intent.putExtra("user_id", cp_id);
                    intent.putExtra("identity_cat", "6");
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.download_gamedetail:
            case R.id.download_title_gamedetail:
                downLoadGame(webpre+gameInfo.getGame_download_url());
                if (MyAccount.isLogin){
                    addMyEvaluation();
                }
                break;
            case R.id.back_gamedetail:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }

    }
    private void addMyEvaluation(){
        BaseParams baseParams=new BaseParams("test/downloadgame");
        baseParams.addParams("game_id",game_id);
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("add game to eva back=="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
