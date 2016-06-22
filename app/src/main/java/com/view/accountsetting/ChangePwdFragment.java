package com.view.accountsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
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
import com.test4s.account.PasswordMatch;
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
public class ChangePwdFragment extends BaseFragment implements View.OnClickListener{

    String oldpwd;
    String newpwd;

    View view;

    private ImageView back;
    private TextView title;
    private TextView save;

    private EditText pwd_edit;
    private EditText newpwd_edit;
    private EditText cpwd_edit;
    private Button changeButton;

    private RelativeLayout layout;
    private TextView warning;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_changepwd,null);
        setImmerseLayout(view.findViewById(R.id.title_changepwd));

        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        save= (TextView) view.findViewById(R.id.save_savebar);

        pwd_edit= (EditText) view.findViewById(R.id.oldpwd_changepwd);
        newpwd_edit= (EditText) view.findViewById(R.id.newpwd_changepwd);
        cpwd_edit= (EditText) view.findViewById(R.id.comfirpwd_changepwd);
        changeButton= (Button) view.findViewById(R.id.change_changepwd);

        layout= (RelativeLayout) view.findViewById(R.id.re_warning_changepwd);
        warning= (TextView) view.findViewById(R.id.warning_changepwd);

        title.setText("修改密码");
        save.setVisibility(View.INVISIBLE);

        getFocus(pwd_edit);

        back.setOnClickListener(this);
        changeButton.setOnClickListener(this);
        changeButton.setClickable(false);

        initTextwatcher();

        return view;
    }

    private void initTextwatcher() {
        TextWatcher myWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>=6){
                    changeButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        pwd_edit.addTextChangedListener(myWatcher);
        newpwd_edit.addTextChangedListener(myWatcher);
        cpwd_edit.addTextChangedListener(myWatcher);


    }

    private void changeButton() {
        oldpwd=pwd_edit.getText().toString();
        newpwd=newpwd_edit.getText().toString();
        String c_pwd=cpwd_edit.getText().toString();

        if (oldpwd.length()>=6&&newpwd.length()>=6&&c_pwd.length()>=6){
            if ((!newpwd.equals(c_pwd))){
                showWarning("两次输入密码不一致");

            }else if (newpwd.equals(c_pwd)&&newpwd.length()>=6){
                layout.setVisibility(View.INVISIBLE);
            }
            if (newpwd.equals(c_pwd)){
                changeButton.setBackgroundResource(R.drawable.border_button_orange);
                changeButton.setClickable(true);
            }else {
                changeButton.setBackgroundResource(R.drawable.border_button_gray);
                changeButton.setClickable(false);
            }
        }




    }

    public void changepwd(){
        if (newpwd.equals(oldpwd)){
            showWarning("新旧密码不能相同");
            return;
        }
        if (!PasswordMatch.passwordMatch(newpwd)){
            showWarning("新密码必须由字母和数字组成");
            return;
        }
        BaseParams baseParams=new BaseParams("user/chgpassword");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("password",oldpwd);
        baseParams.addParams("newpassword",newpwd);
        baseParams.addParams("comfirmpassword",newpwd);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("changepwd==="+result);
                try {
                    JSONObject jsonObject1=new JSONObject(result);
                    boolean su=jsonObject1.getBoolean("success");
                    int code=jsonObject1.getInt("code");
                    if (su&&code==200){
                        ClearWindows.clearInput(getActivity(),pwd_edit);
                        ClearWindows.clearInput(getActivity(),newpwd_edit);
                        ClearWindows.clearInput(getActivity(),cpwd_edit);
                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                        CusToast.showToast(getActivity(),"修改成功",Toast.LENGTH_SHORT);

                    }else {
                        String mes=jsonObject1.getString("msg");
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

            }
        });

    }

    private void showWarning(String mes) {
        layout.setVisibility(View.VISIBLE);
        warning.setText(mes);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_savebar:
                ClearWindows.clearInput(getActivity(),pwd_edit);
                ClearWindows.clearInput(getActivity(),newpwd_edit);
                ClearWindows.clearInput(getActivity(),cpwd_edit);

                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
            case R.id.change_changepwd:
                changepwd();
                break;
        }
    }
}
