package com.view.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/18.
 */
public class aboutusfragment extends Fragment implements View.OnClickListener {

    SettingActivity activity;

    RelativeLayout phone;

    private String phoneNum;

    private Dialog dialog;
    private float density;
    private int windowWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity= (SettingActivity) getActivity();
        super.onCreate(savedInstanceState);
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_aboutus,null);
        initData();
        view.findViewById(R.id.service_deal_aboutus).setOnClickListener(this);
        phone= (RelativeLayout) view.findViewById(R.id.phone_aboutus);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                EditText et_phonenumber = (EditText)findViewById(R.id.phonenumber);

                showDialog();
            }
        });
        return view;
    }
    public void showDialog(){
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
        mes.setText("确定要拨打"+phoneNum+"吗?");
        clear.setText("确定");
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
                String phonenum=getString(R.string.phone_kefu);
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phonenum));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("api/siteinfo");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("aboutus=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject data=jsonObject.getJSONObject("data");
                    JSONObject info=data.getJSONObject("siteInfo");
                    phoneNum=info.getString("site_phone");

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_deal_aboutus:
                activity.toServiceDeal();
                break;
        }
    }

}
