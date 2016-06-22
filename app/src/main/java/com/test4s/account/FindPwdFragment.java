package com.test4s.account;

import android.app.Dialog;
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

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/2/19.
 */
public class FindPwdFragment extends BaseFragment implements View.OnClickListener{
    private EditText phoneNum;
    private EditText code;
    private EditText pwd;
    private TextView getCode;
    private Button sub;

    private ImageView back;
    private TextView title;
    private TextView save;

    private LinearLayout warning;
    private TextView warntext;

    private Dialog dialog;

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
                    time=60;

                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_findpwd,null);
        phoneNum= (EditText) view.findViewById(R.id.editText_phone_fpw);
        code= (EditText) view.findViewById(R.id.editText_code_fpw);
        pwd= (EditText) view.findViewById(R.id.editText_pwd_fpw);
        getCode= (TextView) view.findViewById(R.id.getcode_fpw);
        sub= (Button) view.findViewById(R.id.button_sub_fpw);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        save= (TextView) view.findViewById(R.id.save_savebar);

        warning= (LinearLayout) view.findViewById(R.id.warning_fpw);
        warntext= (TextView) view.findViewById(R.id.waringtext_fpw);

        save.setVisibility(View.INVISIBLE);
        title.setText("重置密码");
        setImmerseLayout(view.findViewById(R.id.titlebar_fpw));

        initListener();
        sub.setClickable(false);
        return view;
    }

    private void initListener() {
        getCode.setOnClickListener(this);
        sub.setOnClickListener(this);
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
            sub.setClickable(true);
            sub.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            sub.setClickable(false);
            sub.setBackgroundResource(R.drawable.border_button_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getcode_fpw:
                if (phoneNum.getText().toString().length()!=11){
                    showwarn("请输入正确的手机号码");
                    return;
                }
                getCode();
                codeChange();
                break;
            case R.id.button_sub_fpw:
                dialog=showLoadingDialog(getActivity());
                reg();
                break;
            case R.id.back_savebar:
                AccountActivity ac= (AccountActivity) getActivity();
                ac.backlogin();
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

        if (!passwordMatch(password)){
            showwarn("新密码必须由字母和数字组成");
            return;
        }

//        RegisterParms regParams=new RegisterParms(phone,password,code_s,pa);
        BaseParams baseParams=new BaseParams("user/forgetpwd");
        baseParams.addParams("username",phone);
        baseParams.addParams("password",password);
        baseParams.addParams("code",code_s);
        baseParams.addParams("pa",pa);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("forgetpwd返回::::"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        CusToast.showToast(getActivity(),"密码重置成功",Toast.LENGTH_SHORT);
                        AccountActivity accountActivity=(AccountActivity)getActivity();
                        accountActivity.backlogin();
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
                dialog.dismiss();
            }
        });
    }

    private void getCode() {
        String phone= phoneNum.getText().toString();
        BaseParams getCodeParams=new BaseParams("sms/index");
        getCodeParams.addParams("phone",phone);
        getCodeParams.addSign();
        x.http().post(getCodeParams.getRequestParams(), new Callback.CommonCallback<String>() {
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
//                        codeChange();
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

    private void codeChange() {
        ClearWindows.clearInput(getActivity(),phoneNum);
        getCode.setClickable(false);
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

    public boolean passwordMatch(String password){
        String regEx_1="[^a-zA-Z0-9]";

        String regEx_abc="[^a-zA-Z]";
        String regEx_num="[^0-9]";


        boolean result= Pattern.compile(regEx_1).matcher(password).find();
        boolean result_num= Pattern.compile(regEx_abc).matcher(password).find();
        boolean result_abc= Pattern.compile(regEx_num).matcher(password).find();
        return (!result)&&result_abc&&result_num;
    }

}
