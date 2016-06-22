package com.view.Evaluation;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class PcDetailActivity extends BaseActivity {

    String gameid;


    private ImageView back;
    private RoundImageView icon;
    private ImageView grade;
    private TextView name;
    private TextView timeText;
    private TextView platformText;
    private TextView devText;
    private TextView typeText;
    private TextView stageText;
    private TextView ageText;
    private TextView sexText;
    private TextView jobText;
    private TextView areaText;
    private TextView phoneBrandText;
    private TextView adviseText;

    LinearLayout list;
    List<Question> quetions;
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
        setContentView(R.layout.activity_pc_detail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));
        gameid=getIntent().getStringExtra("game_id");

        back= (ImageView) findViewById(R.id.back_pcdetail);
        icon= (RoundImageView) findViewById(R.id.icon_pcdetail);
        grade= (ImageView) findViewById(R.id.gamerating_pcdetail);
        name= (TextView) findViewById(R.id.gamename_pcdetail);
        timeText= (TextView) findViewById(R.id.time_pcdetail);
        platformText= (TextView) findViewById(R.id.planform_pcdetail);
        devText= (TextView) findViewById(R.id.dev_pcdetail);
        typeText= (TextView) findViewById(R.id.type_pcdetail);
        stageText= (TextView) findViewById(R.id.stage_pcdetail);

        ageText= (TextView) findViewById(R.id.age_pcdetail);
        sexText= (TextView) findViewById(R.id.sex_pcdetail);
        jobText= (TextView) findViewById(R.id.job_pcdetail);
        areaText= (TextView) findViewById(R.id.area_pcdetail);
        phoneBrandText= (TextView) findViewById(R.id.phonebrand_pcdetail);

        adviseText= (TextView) findViewById(R.id.advise_pcdetail);
        list= (LinearLayout) findViewById(R.id.questionListview_pcdetail);

        quetions=new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
    }

    private void initData() {

        BaseParams baseParams=new BaseParams("test/question");
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        BaseParams baseParams1=new BaseParams("test/testgame");
        baseParams1.addParams("token", MyAccount.getInstance().getToken());
        baseParams1.addParams("game_id",gameid);
        baseParams1.addSign();
        x.http().post(baseParams1.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("game userInfo=="+result);
                initInfoView(result);
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
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("questionInfo=="+result);
                initQuestion(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initListView();
            }
        });

    }
    class ViewHolder{
        TextView question;
        LinearLayout stars;
    }

    private void initListView() {
        for (int i=0;i<quetions.size();i++){
            View convertView= LayoutInflater.from(PcDetailActivity.this).inflate(R.layout.item_listview_pcdetail,list,false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.question= (TextView) convertView.findViewById(R.id.question_item_pcdetail);
            viewHolder.stars= (LinearLayout) convertView.findViewById(R.id.stars_item_pcdetail);

            if (i==quetions.size()-1){
                ImageView line= (ImageView) convertView.findViewById(R.id.line_pcdetail);
//                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) line.getLayoutParams();
//                params.rightMargin=0;
                line.setVisibility(View.INVISIBLE);
            }

            Question question=quetions.get(i);
            viewHolder.question.setText(question.getId()+"、"+question.getQues());
            setStar(Integer.parseInt(question.getScore()),viewHolder.stars);

            list.addView(convertView);
        }
//        listview.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                MyLog.i("Question size==="+quetions.size());
//                return quetions.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return quetions.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                ViewHolder viewHolder=null;
//                if (convertView==null){
//                    viewHolder=new ViewHolder();
//                    convertView= LayoutInflater.from(PcDetailActivity.this).inflate(R.layout.item_listview_pcdetail,parent,false);
//                    viewHolder.question= (TextView) convertView.findViewById(R.id.question_item_pcdetail);
//                    viewHolder.stars= (LinearLayout) convertView.findViewById(R.id.stars_item_pcdetail);
//                    convertView.setTag(viewHolder);
//                }else {
//                    viewHolder= (ViewHolder) convertView.getTag();
//                }
//
//                return convertView;
//            }
//
//        });
//        setListViewHeightBasedOnChildren(listview);
    }

    private void initQuestion(String result) {
        try {
            JSONObject json=new JSONObject(result);
            boolean su=json.getBoolean("success");
            int code=json.getInt("code");
            if (su&&code==200){
                JSONObject data=json.getJSONObject("data");
                adviseText.setText(data.getString("advise"));
                JSONArray quesarray=data.getJSONArray("question");
                MyLog.i("question size=="+quesarray.length());
                for (int i=0;i<quesarray.length();i++){
                    JSONObject ques=quesarray.getJSONObject(i);
                    Question question=new Question();
                    question.setId(ques.getString("id"));
                    question.setQues(ques.getString("ques"));
                    quetions.add(question);
                }
                JSONArray scorearray=data.getJSONArray("answerInfo");
                for (int i=0;i<scorearray.length();i++){
                    JSONObject score=scorearray.getJSONObject(i);
                    quetions.get(i).setScore(score.getString("test_score"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStar(int j,LinearLayout stars) {
        for (int i=0;i<stars.getChildCount();i++){
            ImageView star= (ImageView) stars.getChildAt(i);
            if (i<j){
                star.setImageResource(R.drawable.star_question_1);
            }else {
                star.setImageResource(R.drawable.star_question_0);
            }

        }
    }
    private void initInfoView(String result) {
        try {
            JSONObject json=new JSONObject(result);
            boolean su=json.getBoolean("success");
            int code=json.getInt("code");
            if (su&&code==200){
                JSONObject data=json.getJSONObject("data");
                JSONObject info=data.getJSONObject("gameInfo");
//                Picasso.with(this)
//                        .load(Url.prePic+info.getString("game_img"))
//                        .into(icon);
                imageLoader.displayImage(Url.prePic+info.getString("game_img"),icon, MyDisplayImageOptions.getdefaultImageOptions());

//                Picasso.with(this)
//                        .load(Url.prePic+info.getString("game_grade"))
//                        .into(grade);
                imageLoader.displayImage(Url.prePic+info.getString("game_grade"),grade, MyDisplayImageOptions.getdefaultImageOptions());

                name.setText(info.getString("game_name"));
                timeText.setText(info.getString("create_time"));
                if (TextUtils.isEmpty(info.getString("game_platform"))){
                    platformText.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(info.getString("game_dev"))){
                    devText.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(info.getString("game_type"))){
                    typeText.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(info.getString("game_stage"))){
                    stageText.setVisibility(View.GONE);
                }
                platformText.setText(" "+info.getString("game_platform")+" ");
                devText.setText(" "+info.getString("game_dev")+" ");
                typeText.setText(" "+info.getString("game_type")+" ");
                stageText.setText(" "+info.getString("game_stage")+" ");

                ageText.setText(info.getString("age"));
                String sex=info.getString("sex");
                if (sex.equals("1")){
                    sexText.setText("男");
                }else if (sex.equals("2")){
                    sexText.setText("女");
                }
                UserInfo userInfo=MyAccount.getInstance().getUserInfo();
                jobText.setText(userInfo.getJob_name());
                areaText.setText(userInfo.getProvince_name()+" "+userInfo.getCity_name());
                phoneBrandText.setText(info.getString("brand_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
