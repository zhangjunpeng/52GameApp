package com.test4s.account;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.ClearWindows;
import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.myapp.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/4/15.
 */
public class TirdLoginBindPhone extends BaseFragment implements View.OnClickListener{
    private EditText phoneNum;
    private EditText code;
    private EditText pwd;
    private TextView getCode;
    private TextView reg;

    private ImageView back;
    private TextView title;
    private TextView save;

    private LinearLayout warning;
    private TextView warntext;

    private String regtype;
    private String openId;
    private String otherinfo;


    private String pa;
    private int time=60;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    getCode.setText(time+"s");
                    break;
                case 1:
                    getCode.setBackgroundResource(R.drawable.border_getcode_orange);
                    getCode.setText("重新获取");
                    getCode.setClickable(true);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reg,null);
        phoneNum= (EditText) view.findViewById(R.id.editText_phone);
        code= (EditText) view.findViewById(R.id.editText_code);
        pwd= (EditText) view.findViewById(R.id.editText_pwd);
        getCode= (TextView) view.findViewById(R.id.getcode_reg);
        reg= (TextView) view.findViewById(R.id.button_reg_reg);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        save= (TextView) view.findViewById(R.id.save_savebar);

        warning= (LinearLayout) view.findViewById(R.id.warning_reg);
        warntext= (TextView) view.findViewById(R.id.waringtext_reg);

        save.setVisibility(View.INVISIBLE);
        title.setText("绑定手机号");
        reg.setClickable(false);
        setImmerseLayout(view.findViewById(R.id.titlebar_reg));
        reg.setText("绑定");
        MyLog.i("fragment 绑定手机");

        regtype=getArguments().getString("regtype","");
        openId=getArguments().getString("openid","");
        otherinfo=getArguments().getString("info","");

        initListener();
        return view;
    }

    private void initListener() {
        getCode.setOnClickListener(this);
        reg.setOnClickListener(this);
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        phoneNum.addTextChangedListener(textWatcher);
        code.addTextChangedListener(textWatcher);
        pwd.addTextChangedListener(textWatcher);
        back.setOnClickListener(this);
    }

    private void changeButton() {
        if (phoneNum.getText().toString().length()==11&&code.getText().toString().length()==4&&pwd.getText().toString().length()>=6){
            reg.setClickable(true);
            reg.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            reg.setClickable(false);
            reg.setBackgroundResource(R.drawable.border_button_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getcode_reg:
                if (phoneNum.getText().toString().length()!=11){
                    showwarn("请输入正确的手机号码");
                    return;
                }
                getCode();
                getCode.setClickable(false);

                break;
            case R.id.button_reg_reg:
                reg();
                break;
            case R.id.back_savebar:
                getActivity().finish();
                break;

        }
    }

    private void showwarn(String s) {
        warning.setVisibility(View.VISIBLE);
        warntext.setText(s);
    }

    private void reg() {
        if (TextUtils.isEmpty(pa)){
            //请先获取验证码
            showwarn("请获取验证码");
            return;
        }
        String phone = phoneNum.getText().toString();
        String password=pwd.getText().toString();
        String code_s=code.getText().toString();

//        RegisterParms regParams=new RegisterParms(phone,password,code_s,pa);
        BaseParams baseParams=new BaseParams("user/dothirdlogin");
        baseParams.addParams("regtype",regtype);
        baseParams.addParams("username",phone);
        baseParams.addParams("password",password);
        baseParams.addParams("code",code_s);
        baseParams.addParams("uniqueid",openId);
        baseParams.addParams("otherinfo",otherinfo);
        baseParams.addParams("create_from","2");
        baseParams.addParams("pa",pa);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("thirdlogin bind phone=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        MyAccount myAccount=MyAccount.getInstance();
                        myAccount.setNickname(jsonObject1.getString("nickname"));
                        myAccount.setUsername(jsonObject1.getString("username"));
                        myAccount.setToken(jsonObject1.getString("token"));
                        myAccount.setAvatar(jsonObject1.getString("avatar"));
                        CusToast.showToast(getActivity(),"登录成功",Toast.LENGTH_SHORT);
                        myAccount.isLogin=true;
                        myAccount.saveUserInfo();
                        MyLog.i("登录成功：："+myAccount.toString());
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                        MyLog.i("绑定完成");
                    }else {
                        showwarn(jsonObject.getString("msg"));
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

    private void getCode() {
        String phone= phoneNum.getText().toString();
        BaseParams baseParams=new BaseParams("sms/index");
        baseParams.addParams("phone",phone);
        baseParams.addParams("type","reg");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("GetCode_back:::"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean success=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (success&&code==200){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        pa=jsonObject1.getString("pa");
                        codeChange();
                    }else {
                        showwarn(jsonObject.getString("msg"));
                        getCode.setClickable(true);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getCode.setClickable(true);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void codeChange() {
        ClearWindows.clearInput(getActivity(),phoneNum);
        getCode.setBackgroundResource(R.drawable.border_getcode_gray);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (time>0){
                    try {
                        handler.obtainMessage(0).sendToTarget();
                        Thread.sleep(1000);
                        time--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.obtainMessage(1).sendToTarget();

            }
        }).start();

    }
}
