package com.view.setting;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.view.MyDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.Tencent;
import com.test4s.account.AccountActivity;
import com.test4s.account.LoginQQActivity;
import com.test4s.account.LoginSINAActivity;
import com.test4s.account.MyAccount;
import com.test4s.account.TencentLogin;
import com.test4s.account.ThirdLoginActivity;
import com.test4s.gdb.AdvertsDao;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.GameTypeDao;
import com.test4s.gdb.IPDao;
import com.test4s.gdb.IndexAdvertDao;
import com.test4s.gdb.IndexItemInfoDao;
import com.test4s.gdb.NewsInfoDao;
import com.test4s.gdb.OrderDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;
import com.view.index.MySettingFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/3/18.
 */
public class Settingfragment extends Fragment implements View.OnClickListener {

    private ToggleButton toggleButton;
    private RelativeLayout loginout;

    private SettingActivity activity;
    Dialog dialog;
    Dialog progressdialog;

    private TextView cahesize;
    private MyAccount myaccount;

    private static final String PICASSO_CACHE = "picasso-cache";
    private float density;

    private int windowWidth;

    private final static int LoginCode=201;

    private DaoSession daoSession;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity= (SettingActivity) getActivity();
        myaccount=MyAccount.getInstance();
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;

        daoSession=MyApplication.daoSession;

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,null);
        view.findViewById(R.id.clear_cahe_setting).setOnClickListener(this);
        view.findViewById(R.id.check_update).setOnClickListener(this);
        view.findViewById(R.id.advice_report).setOnClickListener(this);
        view.findViewById(R.id.about_us).setOnClickListener(this);

        loginout= (RelativeLayout) view.findViewById(R.id.loginout_setting);
        cahesize= (TextView) view.findViewById(R.id.cahesize_setting);

        loginout.setOnClickListener(this);

        if (!myaccount.isLogin){
            loginout.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_us:
                MyLog.i("点击about_us");
                activity.toAboutUs();
                break;
            case R.id.advice_report:
                MyLog.i("点击advice_report");
                activity.toAdviseReprot();
                break;
            case R.id.clear_cahe_setting:
                showClearDialog();
                break;
            case R.id.loginout_setting:

                if(myaccount.isLogin){
                    showDialog();
                }else {
                }
                break;
            case R.id.check_update:
                checkAppUpdate();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK&&requestCode==LoginCode){

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showClearDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_dialog);

        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        view.setBackgroundResource(R.drawable.border_dialog);
        dialog.setContentView(view,params);

        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
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
                clearCahe();
                dialog.dismiss();
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressdialog.dismiss();
                    cahesize.setText("0 MB");
                    CusToast.showToast(getActivity(),"清理完成",Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    private void clearCahe() {


        progressdialog=new Dialog(getActivity());

        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_clearcahe,null);

        progressdialog.getWindow().setBackgroundDrawableResource(R.drawable.border_dialog);
//        MyDialog myDialog=new MyDialog(getActivity(),(int) (windowWidth*0.8),(int) (110*density),view,R.style.MyDialog);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        progressdialog.setContentView(view,params);

        progressdialog.show();


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    deleteAll();
                    ImageLoader.getInstance().clearDiskCache();  // 清除本地缓存
                    Thread.sleep(2*1000);
                    handler.sendEmptyMessage(0);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkAppUpdate(){
        BaseParams baseParams=new BaseParams("api/checkappversion");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("checkAppUpdate back==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    int code=jsonObject.getInt("code");
                    boolean su=jsonObject.getBoolean("success");
                    if (su&&code==200){
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONObject version=data.getJSONObject("version");
                        String vers=version.getString("version");
                        String downloadurl=version.getString("download_url");
                        float ver=Float.parseFloat(vers);
                        float old_ver=Float.parseFloat(MyApplication.versionName);
                        MyLog.i("old version "+old_ver);
                        if (ver>old_ver){
                            showUpdateDialog();
                        }else {
                            showDialog("您的版本已经是最新！");
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

            }
        });
    }

    public void showUpdateDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText("发现新版本！");
        clear.setText("立即更新");
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

            }
        });
    }
    public void showDialog(String message){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText(message);
        clear.setText("我知道了");
//        clear.setTextColor(Color.rgb(255,157,0));
        dialog.show();
        channel.setVisibility(View.GONE);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void showDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText("确定退出账号吗？");
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

                MyAccount.getInstance().loginOut();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                dialog.dismiss();
                getActivity().overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
//                if (LoginQQActivity.){
//                    MyLog.i("qq login out1");
////                    Tencent tencent=MyAccount.tencentLogin.mtencent;
//                    MyAccount.tencentLogin.loginOut();
//                }
                LoginQQActivity.loginOut();
                LoginSINAActivity.WeiBologinout(getActivity());


                Set<String> datalist=new TreeSet<String>();
                EventBus.getDefault().post(datalist);

            }
        });
    }

    private IndexItemInfoDao getIndexItemInfoDao(){
        return daoSession.getIndexItemInfoDao();
    }
    private IPDao getIPDao(){
        return daoSession.getIPDao();
    }
    private IndexAdvertDao getIndexAdverDao(){
        return daoSession.getIndexAdvertDao();
    }
    private OrderDao getOrderDao(){
        return daoSession.getOrderDao();
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
    private NewsInfoDao getNewsInfoDao(){
        return daoSession.getNewsInfoDao();
    }

    private void deleteAll(){
        getIndexItemInfoDao().deleteAll();
        getIPDao().deleteAll();
        getIndexAdverDao().deleteAll();
        getOrderDao().deleteAll();
        getGameInfoDao().deleteAll();
        getAdvertsDao().deleteAll();
        getGameInfoDao().deleteAll();
        getGameTypeDao().deleteAll();
        getNewsInfoDao().deleteAll();
    }
}
