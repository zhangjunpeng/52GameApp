package com.view.accountsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
public class SetNickFragment extends BaseFragment implements View.OnClickListener{

    private EditText nickname;
    View view;
    String nickname_s;


    private TextView save;
    private TextView title;
    private ImageView back;

    UserInfo userInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_setnick,null);

        setImmerseLayout(view.findViewById(R.id.title_setnick));

        nickname= (EditText) view.findViewById(R.id.nick_setnick);
        save= (TextView) view.findViewById(R.id.save_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);

        title.setText("昵 称");

        userInfo=MyAccount.getInstance().getUserInfo();
        if (!userInfo.getNickname().equals("null")){
            nickname.setText(userInfo.getNickname());
        }

        getFocus(nickname);

        save.setOnClickListener(this);
        back.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_savebar:


                nickname_s=nickname.getText().toString();
                if (TextUtils.isEmpty(nickname_s)){
                    CusToast.showToast(getActivity(),"昵称不能为空",Toast.LENGTH_SHORT);

                    return;
                }
                MyAccount myAccount=MyAccount.getInstance();
                BaseParams baseParams=new BaseParams("user/chgnickname");
                baseParams.addParams("token",myAccount.getToken());
                baseParams.addParams("nickname",nickname_s);
                baseParams.addSign();
                x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.i("changeNick==="+result);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            boolean issucc=jsonObject.getBoolean("success");
                            int code=jsonObject.getInt("code");
                            if (issucc&&code==200){
                                //强制隐藏android输入框
                                ClearWindows.clearInput(getActivity(),nickname);

                                CusToast.showToast(getActivity(),"修改成功",Toast.LENGTH_SHORT);

                                MyAccount.getInstance().getUserInfo().setNickname(nickname_s);
                                MyAccount.getInstance().setNickname(nickname_s);
                                MyAccount.getInstance().saveUserInfo();

                                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();


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

                break;

            case R.id.back_savebar:
                ClearWindows.clearInput(getActivity(),nickname);
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }

    }

}
