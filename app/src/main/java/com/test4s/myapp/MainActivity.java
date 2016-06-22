package com.test4s.myapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.test4s.gdb.CPDao;
import com.test4s.gdb.DaoSession;
import com.view.activity.BaseActivity;
import com.view.index.GameFragment;
import com.view.index.IndexFragment;
import com.view.index.InformationFragment;
import com.view.index.MySettingFragment;
import com.view.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity  implements View.OnClickListener,GestureDetector.OnGestureListener{

    DaoSession daoSession;
    private List<Fragment> fragments;
    FragmentManager fm;

    TextView title_bar;

    String[] titles={"首页","游戏","资讯","我的"};
    ImageView search;

    //底部导航栏
    List<ImageView> imageViewList;
    List<TextView>  textViewList;

    LinearLayout indexLinear;
    LinearLayout gameLinear;
    LinearLayout infoLinear;
    LinearLayout myLinear;

    ImageView indexImage;
    ImageView gameImage;
    ImageView infoImage;
    ImageView myImage;

    TextView indexText;
    TextView gameText;
    TextView infoText;
    TextView myText;

    GestureDetector mGestureDetector;
    private Dialog dialog;
    private float density;
    private int windowWidth;
    private Fragment mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomView();


        setImmerseLayout(findViewById(R.id.title_main));

        title_bar= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        mGestureDetector=new GestureDetector(this,this);

        fm=getSupportFragmentManager();

        daoSession=MyApplication.daoSession;
        CPDao cpDao=daoSession.getCPDao();

        fragments=new ArrayList<>();

        Fragment indexFragment=new IndexFragment();
        Fragment gameFragment=new GameFragment();
        Fragment informationFragment=new InformationFragment();
        Fragment mySettingFragment=new MySettingFragment();
        mContent=indexFragment;

        fragments.add(indexFragment);
        fragments.add(gameFragment);
        fragments.add(informationFragment);
        fragments.add(mySettingFragment);

        setImageColor(0);
        fm.beginTransaction().replace(R.id.frameLayout_main,indexFragment).commit();


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;

    }



    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void initBottomView() {

        indexLinear= (LinearLayout) findViewById(R.id.index_linear);
        gameLinear= (LinearLayout) findViewById(R.id.game_linear);
        infoLinear= (LinearLayout) findViewById(R.id.info_linear);
        myLinear= (LinearLayout) findViewById(R.id.my_linear);

        indexLinear.setOnClickListener(this);
        gameLinear.setOnClickListener(this);
        infoLinear.setOnClickListener(this);
        myLinear.setOnClickListener(this);


        imageViewList=new ArrayList<>();
        textViewList=new ArrayList<>();

        indexImage= (ImageView) findViewById(R.id.index_image);
        gameImage= (ImageView) findViewById(R.id.game_image);
        infoImage= (ImageView) findViewById(R.id.info_imgae);
        myImage= (ImageView) findViewById(R.id.my_image);

        imageViewList.add(indexImage);
        imageViewList.add(gameImage);
        imageViewList.add(infoImage);
        imageViewList.add(myImage);

        indexText= (TextView) findViewById(R.id.text_index);
        gameText= (TextView) findViewById(R.id.text_game);
        infoText= (TextView) findViewById(R.id.text_info);
        myText= (TextView) findViewById(R.id.text_my);

        textViewList.add(indexText);
        textViewList.add(gameText);
        textViewList.add(infoText);
        textViewList.add(myText);

    }

    private void setImageColor(int position){
        title_bar.setText(titles[position]);
        if (imageViewList==null||textViewList==null){
            return;
        }

        for (int i=0;i<imageViewList.size();i++){
            imageViewList.get(i).setSelected(false);
            textViewList.get(i).setTextColor(Color.rgb(76,76,76));

        }

        imageViewList.get(position).setSelected(true);
        textViewList.get(position).setTextColor(Color.rgb(255,157,0));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.index_linear:
                switchContent(mContent,fragments.get(0));
//                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(0)).commit();
                setImageColor(0);
                break;
            case R.id.game_linear:
                switchContent(mContent,fragments.get(1));

//                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(1)).commit();
                setImageColor(1);
                break;
            case R.id.info_linear:
                switchContent(mContent,fragments.get(2));

//                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(2)).commit();
                setImageColor(2);
                break;
            case R.id.my_linear:
                switchContent(mContent,fragments.get(3));

//                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(3)).commit();
                setImageColor(3);
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        int mini_width=120;
        int mini_speed=0;
        if (e1==null||e2==null){
            return false;
        }
        float distance_right=e2.getX()-e1.getX();
        float distance_left=e1.getX()-e2.getX();
        float distance_down=e2.getY()-e1.getY();
        float distance_up=e1.getY()-e2.getY();
        if(distance_right>mini_width && Math.abs(distanceX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向右滑动");
        }
        else if(distance_left>mini_width && Math.abs(distanceX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向左滑动");
        }
        else if(distance_down>mini_width && Math.abs(distanceX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向下滑动");
            return true;

        }
        else if(distance_up>mini_width && Math.abs(distanceX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向上滑动");
            return true;

        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        int mini_width=120;
        int mini_speed=0;
        if (e1==null||e2==null){
            return false;
        }
        float distance_right=e2.getX()-e1.getX();
        float distance_left=e1.getX()-e2.getX();
        float distance_down=e2.getY()-e1.getY();
        float distance_up=e1.getY()-e2.getY();
        if(distance_right>mini_width && Math.abs(velocityX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向右滑动");
        }
        else if(distance_left>mini_width && Math.abs(velocityX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向左滑动");
        }
        else if(distance_down>mini_width && Math.abs(velocityX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向下滑动");
            return true;

        }
        else if(distance_up>mini_width && Math.abs(velocityX)>mini_speed)
        {
            MyLog.i( "onFling-"+"向上滑动");
            return true;

        }
        return true;
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction =fm.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.frameLayout_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
//            showLoadingDialog();
//            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showDialog(){
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText("确定退出程序吗？");
        clear.setText("退出");
        clear.setTextColor(Color.rgb(255,157,0));
        dialog.show();
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
    }
    public Dialog showLoadingDialog(){
        final Dialog dialog=new Dialog(this,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();
        ImageView imageView= (ImageView) dialog.findViewById(R.id.image_loadingdialog);
        AnimationDrawable ad = (AnimationDrawable)imageView.getBackground();

        ad.start();
        return dialog;
    }
}
