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
 * Created by Administrator on 2016/2/26.
 */
public class BindNewPhoneFragment extends BaseFragment implements View.OnClickListener{

    View view;

    UserInfo userInfo;


    EditText phone_editText;
    EditText code_editText;
    TextView getcode;
    Button bindnew;

    String pa;

    private TextView title;
    private ImageView back;
    private TextView save;

    private TextView tips;

    private RelativeLayout layout;
    private TextView warning;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userInfo=MyAccount.getInstance().getUserInfo();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_bindnewphone,null);
        setImmerseLayout(view.findViewById(R.id.title_bindnewphone));

        //title
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        save= (TextView) view.findViewById(R.id.save_savebar);

        tips= (TextView) view.findViewById(R.id.tips_bindnewphone);

        phone_editText= (EditText) view.findViewById(R.id.phone_bindnewphone);
        code_editText= (EditText) view.findViewById(R.id.code_bindnewphone);
        getcode= (TextView) view.findViewById(R.id.getcode_bindnewphone);
        bindnew= (Button) view.findViewById(R.id.bind_bindnewphone);

        layout= (RelativeLayout) view.findViewById(R.id.re_warning_bindnewphone);
        warning= (TextView) view.findViewById(R.id.warning_bindnewphone);

        if (userInfo.getPhone().equals("null")){
            title.setText("绑定手机号");
        }else {
            title.setText("更换手机号");
            tips.setText("更换手机号后，您可以使用新手机号登录");
        }
        save.setVisibility(View.INVISIBLE);


        back.setOnClickListener(this);
        getcode.setOnClickListener(this);
        bindnew.setOnClickListener(this);

        bindnew.setClickable(false);

        initTextChangeListener();
        return view;
    }

    private void initTextChangeListener() {
        phone_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==11){
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
                if (s.length()==4){
                    changeBindButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void changeBindButton() {
        String phones=phone_editText.getText().toString();
        String codes=code_editText.getText().toString();
        if (phones.length()==11&&codes.length()==4){
            bindnew.setClickable(true);
            bindnew.setBackgroundResource(R.drawable.border_button_orange);
        }else {
            bindnew.setClickable(false);
            bindnew.setBackgroundResource(R.drawable.border_button_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getcode_bindnewphone:
                receiveCode();
                break;
            case R.id.bind_bindnewphone:
                bindPhone();
                break;
            case R.id.back_savebar:
                ClearWindows.clearInput(getActivity(),phone_editText);
                ClearWindows.clearInput(getActivity(),code_editText);

                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }
    }

    private void bindPhone() {
        String phoneNum=phone_editText.getText().toString();
        String code=code_editText.getText().toString();
        if (TextUtils.isEmpty(phoneNum)||phoneNum.length()!=11){
            showWarning("手机号码格式错误");
            return;
        }
        if (TextUtils.isEmpty(pa)){
            showWarning("验证码错误");
            return;
        }
        BaseParams baseParams=new BaseParams("user/chgphone");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("phone",phoneNum);
        baseParams.addParams("code",code);
        baseParams.addParams("pa",pa);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("bindphone===="+result);
                try {
                    JSONObject js=new JSONObject(result);
                    boolean su=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (su&&code==200){
                        Toast.makeText(getActivity(),"手机号绑定成功",Toast.LENGTH_SHORT).show();
                        userInfo.setPhone(phone_editText.getText().toString());
                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                    }else{
                        String msg=js.getString("msg");
                        showWarning(msg);
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

    private void receiveCode() {
        String phonenum=phone_editText.getText().toString();
        if (TextUtils.isEmpty(phonenum)||phonenum.length()!=11){
            showWarning("手机号码格式错误");
            return;
        }
        BaseParams baseParams=new BaseParams("sms/index");
        baseParams.addParams("phone",phonenum);
        baseParams.addParams("type","bind");
        baseParams.addSign();
        getcode.setClickable(false);
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("sms===="+result);
                try {
                    JSONObject js=new JSONObject(result);
                    boolean su=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (su && code == 200) {
                        JSONObject data=js.getJSONObject("data");
                        pa=data.getString("pa");
                        codeChange();
                    }else {
                        String mes=js.getString("msg");
                        showWarning(mes);
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
                getcode.setClickable(true);

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
                    getcode.setText(time+"s");
                    break;
                case 1:
                    getcode.setBackgroundResource(R.drawable.border_getcode_orange);
                    getcode.setText("重新获取");
                    getcode.setClickable(true);
                    time=59;

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void codeChange() {
        ClearWindows.clearInput(getActivity(),phone_editText);
        getcode.setClickable(false);
        getcode.setBackgroundResource(R.drawable.border_getcode_gray);
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
    private void showWarning(String mes) {
        layout.setVisibility(View.VISIBLE);
        warning.setText(mes);
    }


}
