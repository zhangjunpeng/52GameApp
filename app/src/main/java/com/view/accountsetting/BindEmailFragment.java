package com.view.accountsetting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.ClearWindows;
import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/2/22.
 */
public class BindEmailFragment extends BaseFragment implements View.OnClickListener{

    UserInfo userInfo;

    View view;

    EditText email_editText;
    EditText code_editText;
    TextView getCode;
    Button bindEmail;
    TextView tips;
    TextView warning;
    RelativeLayout layout;


    String email;
    String code;
    String pa;


    private TextView title;
    private ImageView back;
    private TextView save;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userInfo=MyAccount.getInstance().getUserInfo();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(userInfo.getEmail())){
            view=inflater.inflate(R.layout.fragment_emailbinded,null);
            setImmerseLayout(view.findViewById(R.id.title_emailbinded));
            TextView email= (TextView) view.findViewById(R.id.email_emailbind);
            email.setText(userInfo.getEmail());
        }else {
            view=inflater.inflate(R.layout.fragment_bindemail,null);
            setImmerseLayout(view.findViewById(R.id.title_bindemail));
            initView(view);
        }
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        save= (TextView) view.findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);

        title.setText("绑定邮箱");



        back.setOnClickListener(this);

        return view;
    }

    private void initView(View view) {
        email_editText= (EditText) view.findViewById(R.id.email_bindemail);
        code_editText= (EditText) view.findViewById(R.id.code_bindemail);
        getCode= (TextView) view.findViewById(R.id.getcode_bindemail);
        bindEmail= (Button) view.findViewById(R.id.bind_bindemail);
        tips= (TextView) view.findViewById(R.id.tips_bindemail);
        warning= (TextView) view.findViewById(R.id.warning_bindemail);
        layout= (RelativeLayout) view.findViewById(R.id.re_warning_bindemail);


        getFocus(email_editText);
        getCode.setOnClickListener(this);
        bindEmail.setOnClickListener(this);
        email_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emails=s.toString();
                if (emails.length()>3&&emails.contains("@")){
                    changeBindButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    changeBindButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void getEmailCode(){
        email=email_editText.getText().toString();
        if (TextUtils.isEmpty(email)||!email.contains("@")){
            showWarning("Email格式错误");
        }

        ClearWindows.clearInput(getActivity(),email_editText);
        final BaseParams baseParams=new BaseParams("api/sendemail");
        baseParams.addParams("email",email);
        baseParams.addSign();
        getCode.setClickable(false);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("sendEmail=="+result);
                try {
                    JSONObject js=new JSONObject(result);
                    boolean su=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (su&&code==200){
                        codeChange();

                        JSONObject js1=js.getJSONObject("data");
                        pa=js1.getString("pa");
                        CusToast.showToast(getActivity(),"发送邮件成功",Toast.LENGTH_SHORT);
                    }else {
                        String mes=js.getString("msg");
                        showWarning(mes);
                        getCode.setClickable(true);

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

    private void showWarning(String mes) {
        layout.setVisibility(View.VISIBLE);
        warning.setText(mes);
    }

    private void bindEamil(){
        code=code_editText.getText().toString();
        if (TextUtils.isEmpty(code)||TextUtils.isEmpty(pa)){
            showWarning("验证码错误");
        }
        BaseParams baseParams=new BaseParams("user/chgemail");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("email",email);
        baseParams.addParams("code",code);
        baseParams.addParams("pa",pa);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("bindEmail==="+result);
                try {
                    JSONObject js=new JSONObject(result);
                    boolean su=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (su&&code==200){
                        CusToast.showToast(getActivity(),"邮箱绑定成功",Toast.LENGTH_SHORT);
                        userInfo.setEmail(email);
                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("tag","init");
                        myAcountSettingFragment.setArguments(bundle);
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                    }else {
                        String mes=js.getString("msg");

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
    int time=59;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    time--;
                    getCode.setText(time+"s");
                    break;
                case 1:
                    getCode.setBackgroundResource(R.drawable.border_getcode_orange);
                    getCode.setText("重新获取");
                    getCode.setClickable(true);
                    time=59;

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void codeChange() {
        ClearWindows.clearInput(getActivity(),email_editText);
        getCode.setClickable(false);
        getCode.setBackgroundResource(R.drawable.border_getcode_gray);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (time>0){
                    try {
                        handler.obtainMessage(0).sendToTarget();
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.obtainMessage(1).sendToTarget();

            }
        }).start();

    }
    private void changeBindButton() {
        String emails=email_editText.getText().toString();
        String codes=code_editText.getText().toString();
        if (!TextUtils.isEmpty(emails)&&codes.length()>3){
            bindEmail.setClickable(true);
            bindEmail.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            bindEmail.setClickable(false);
            bindEmail.setBackgroundResource(R.drawable.border_button_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getcode_bindemail:
                getEmailCode();
                break;
            case R.id.bind_bindemail:
                bindEamil();
                break;
            case R.id.back_savebar:
                if (email_editText!=null&&code_editText!=null) {
                    ClearWindows.clearInput(getActivity(), email_editText);
                    ClearWindows.clearInput(getActivity(), code_editText);
                }

                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }

    }
}
