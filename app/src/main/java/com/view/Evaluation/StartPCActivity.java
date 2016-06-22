package com.view.Evaluation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.MainActivity;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class StartPCActivity extends AppCompatActivity implements View.OnClickListener {

    String game_id;

    private String game_img;
    private String game_name;
    private String game_type;
    private String game_stage;
    private String game_dev;
    private String game_platform;
    private String game_grade;
    private String age;
    private String sex;
    private String phonebrandid;
    private String create_time;
    private String phonebrandname;


    private TextView ageText;
    private TextView sexText;
    private TextView jobText;
    private TextView areaText;
    private TextView phoneBrandText;
    private TextView nameText;
    private RoundImageView icon;
    private ImageView gamegrade;
    private TextView createtimeText;
    private TextView platfromText;
    private TextView gameDevText;
    private TextView gameTypeText;
    private TextView gameStageText;
    private ImageView back;
    private Button start;


    private UserInfo userInfo;

    final static int RequstCode=301;
    private ImageLoader imageLoader=ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_start_pc);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));
        game_id=getIntent().getStringExtra("game_id");
        userInfo= MyAccount.getInstance().getUserInfo();

        ageText= (TextView) findViewById(R.id.age_pcgame);
        sexText= (TextView) findViewById(R.id.sex_pcgame);
        jobText= (TextView) findViewById(R.id.job_pcgame);
        areaText= (TextView) findViewById(R.id.area_pcgame);
        phoneBrandText= (TextView) findViewById(R.id.phonebrand_pcgame);

        nameText= (TextView) findViewById(R.id.gamename_pcgame);
        icon= (RoundImageView) findViewById(R.id.icon_pcgame);
        createtimeText= (TextView) findViewById(R.id.time_pcgame);
        platfromText= (TextView) findViewById(R.id.planform_pcgame);
        gameDevText= (TextView) findViewById(R.id.dev_pcgame);
        gameTypeText= (TextView) findViewById(R.id.type_pcgame);
        gameStageText= (TextView) findViewById(R.id.stage_pcgame);
        back= (ImageView) findViewById(R.id.back_pcgame);
        start= (Button) findViewById(R.id.startpc_pcgame);
        initListener();

        initData();
    }

    private void initListener() {
        findViewById(R.id.re_age_pcgame).setOnClickListener(this);
        findViewById(R.id.re_sex_pcgame).setOnClickListener(this);
        findViewById(R.id.re_job_pcgame).setOnClickListener(this);
        findViewById(R.id.re_area_pcgame).setOnClickListener(this);
        findViewById(R.id.re_phonebrand_pcgame).setOnClickListener(this);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StartPCActivity.this,PCActivity.class);
                intent.putExtra("game_id",game_id);
                intent.putExtra("game_name",game_name);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                finish();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkInfo() {
        if (!age.equals("0")&&!"0".equals(sex)
                &&!"".equals(userInfo.getJob_name())
                &&!userInfo.getProvince_name().equals("")
                &&!phonebrandid.equals("0")){
            start.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            start.setBackgroundResource(R.drawable.border_button_gray);
        }

    }

    private void initData() {
        BaseParams baseparams=new BaseParams("test/testgame");
        baseparams.addParams("game_id",game_id);
        baseparams.addParams("token", MyAccount.getInstance().getToken());
        baseparams.addSign();
        x.http().post(baseparams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("pcgame back==="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        JSONObject data=json.getJSONObject("data");
                        JSONObject info=data.getJSONObject("gameInfo");
                        game_img=info.getString("game_img");
                        create_time=info.getString("create_time");
                        game_name=info.getString("game_name");
                        game_type=info.getString("game_type");
                        game_dev=info.getString("game_dev");
                        game_stage=info.getString("game_stage");
                        game_platform=info.getString("game_platform");
                        game_grade=info.getString("game_grade");
                        age=info.getString("age");
                        sex=info.getString("sex");
                        userInfo.setAge(age);
                        userInfo.setSex(sex);
                        phonebrandid=info.getString("phone_brand_id");
                        phonebrandname=info.getString("brand_name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initView();
            }
        });
    }

    private void initView() {
        MyLog.i("PC InitView");
        if (!"0".equals(userInfo.getAge())){
            ageText.setText(age);
        }
        if ("1".equals(userInfo.getSex())){
            sexText.setText("男");
        }else if ("2".equals(userInfo.getSex())){
            sexText.setText("女");
        }
        if (!userInfo.getJob_name().equals("")){
            jobText.setText(userInfo.getJob_name());
        }

        if (!userInfo.getProvince_name().equals("")){
            areaText.setText(userInfo.getProvince_name()+" "+userInfo.getCity_name());
        }

        if (!phonebrandid.equals("0")){
            phoneBrandText.setText(phonebrandname);
        }
        createtimeText.setText(create_time);

        if (TextUtils.isEmpty(game_platform)){
            platfromText.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(game_dev)){
            gameDevText.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(game_type)){
            gameTypeText.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(game_stage)){
            gameStageText.setVisibility(View.GONE);
        }
        platfromText.setText(" "+game_platform+" ");
        gameDevText.setText(" "+game_dev+" ");
        gameTypeText.setText(" "+game_type+" ");
        gameStageText.setText(" "+game_stage+" ");
        nameText.setText(game_name);
//        Picasso.with(this)
//                .load(Url.prePic+game_img)
//                .into(icon);
        imageLoader.displayImage(Url.prePic+game_img,icon, MyDisplayImageOptions.getdefaultImageOptions());

//        Picasso.with(this)
//                .load(Url.prePic+game_grade)
//                .into(gamegrade);
        imageLoader.displayImage(Url.prePic+game_grade,gamegrade, MyDisplayImageOptions.getdefaultImageOptions());


        checkInfo();

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.re_age_pcgame:
                intent=new Intent(this,SetPcInfoActivity.class);
                intent.putExtra("tag","age");
                break;
            case R.id.re_sex_pcgame:
                intent=new Intent(this,SetPcInfoActivity.class);
                intent.putExtra("tag","sex");
                intent.putExtra("sex",sex);
                break;
            case R.id.re_job_pcgame:
                intent=new Intent(this,SetPcInfoActivity.class);
                intent.putExtra("tag","job");
                break;
            case R.id.re_area_pcgame:
                intent=new Intent(this,SetPcInfoActivity.class);
                intent.putExtra("tag","area");
                break;
            case R.id.re_phonebrand_pcgame:
                intent=new Intent(this,SetPcInfoActivity.class);
                intent.putExtra("tag","phonebrand");
                intent.putExtra("phonebrand",phonebrandid);
                break;
        }
        startActivityForResult(intent,RequstCode);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RequstCode&&resultCode== Activity.RESULT_OK){
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
