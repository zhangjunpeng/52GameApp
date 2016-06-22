package com.view.Evaluation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class PCActivity extends AppCompatActivity{

    FragmentManager fragmentManager;
    String gameid;
    List<Question> questionList;
    List<Fragment> fragmentList;

    GestureDetector mDetector;
    ProgressBar bar;
    TextView thu;

    ViewPager viewpager;
    MyViewPagerAdapter adapter;

    int cur_ques=0;
    List<Integer> scores;

    int thumbLeftmargin;
    int masssper;
    float density;
    int lenth;
    RelativeLayout re_pro;
    Button submit;

    ReportAdviceFragment advicefragment;

    private String name;

    private ImageView back;
    private TextView title;
    private TextView save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc);

        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        density= dm.density;
        int screenWidth=dm.widthPixels;

        viewpager= (ViewPager) findViewById(R.id.viewpager_pc);
        scores=new ArrayList<>();

        bar= (ProgressBar) findViewById(R.id.progressbar_pc);
        thu= (TextView) findViewById(R.id.text_pc);
        re_pro= (RelativeLayout) findViewById(R.id.re_pro_pc);
        submit= (Button) findViewById(R.id.submit_pc);

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        name=getIntent().getStringExtra("game_name");

        title.setText(name);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lenth= (int) (screenWidth-(56*density));

        MyLog.i("lenth=="+lenth+"\nmassper=="+masssper);

//        seekBar.setThumb();



//        setProgressbar(0);

        fragmentList=new ArrayList<>();
        gameid=getIntent().getStringExtra("game_id");

        fragmentManager=getSupportFragmentManager();

        adapter=new MyViewPagerAdapter(fragmentManager,fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(true, new DepthPageTransformer());

        advicefragment=new ReportAdviceFragment();

        initListener();
        initData();
    }

    private void initListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==fragmentList.size()-1){
                    showSubmitButton();
                }else {
                    setProgressbar(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setProgressbar(int pro) {
        bar.setProgress(pro+1);

        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        thumbLeftmargin= (int) (masssper*(pro+1)-11*density);

        MyLog.i("左偏移=="+thumbLeftmargin);
        params.leftMargin=thumbLeftmargin;

        int mes=pro+1;
        thu.setText(mes+"");
        thu.setLayoutParams(params);

    }

    private void initData() {
        BaseParams baseParams=new BaseParams("test/question");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("game_id",gameid);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("pc back==="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        JSONObject data=json.getJSONObject("data");
                        JSONArray quesarray=data.getJSONArray("question");
                        questionList=new ArrayList<Question>();
                        for (int i=0;i<quesarray.length();i++){
                            JSONObject ques=quesarray.getJSONObject(i);
                            Question question=new Question();
                            question.setId(ques.getString("id"));
                            question.setQues(ques.getString("ques"));
                            questionList.add(question);
                            scores.add(0);
                        }
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
                masssper=lenth/(questionList.size()+1);
                initQuestion();
            }
        });
    }

    public void initQuestion() {
        for (int i=0;i<questionList.size();i++){
            QuestionFragment questionFragment=new QuestionFragment();
            Question question=questionList.get(i);
            Bundle bundle=new Bundle();
            bundle.putString("id",question.getId());
            bundle.putString("ques",question.getQues());
            bundle.putInt("score",scores.get(i));
            questionFragment.setArguments(bundle);
            fragmentList.add(questionFragment);
//            MyLog.i("fragment添加");
        }
        fragmentList.add(advicefragment);
       adapter.notifyDataSetChanged();
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> list;
        public MyViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
    public void changeQuestion(){
//        MyLog.i("viewpager前");
        if (viewpager.getCurrentItem()==(fragmentList.size()-1)){
            setProgressbar(viewpager.getCurrentItem());
        }else {
            int item=viewpager.getCurrentItem();
            MyLog.i("viewpager切换=="+item);
            viewpager.setCurrentItem(item+1);
            setProgressbar(item);
        }
    }
    public void showSubmitButton(){
        re_pro.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.VISIBLE);
        submit.setClickable(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPC();       
            }
        });
    }

    private void reportPC() {
        BaseParams baseParams=new BaseParams("test/subtest");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("game_id",gameid);

        baseParams.addParams("advise",advicefragment.getEditText());
        JSONArray array=new JSONArray();
        for (int i=0;i<scores.size();i++){
            array.put(scores.get(i));
        }
        MyLog.i("jsonarray=="+array.toString());
        baseParams.addParams("starts",array.toString());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("pc submit back=="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        finish();
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

            }
        });
    }

}
