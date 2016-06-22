package com.view.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.tools.ClearWindows;
import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/18.
 */
public class AdviceReportFragment extends Fragment {
    private EditText editText;
    private Button submit;
    private SettingActivity settingActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingActivity= (SettingActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_advice_report,null);
        editText= (EditText) view.findViewById(R.id.edit_advice_report);
        submit= (Button) view.findViewById(R.id.submit_advice_report);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advice=editText.getText().toString();
                if (TextUtils.isEmpty(advice)){

                }else {
                    reportAdvice(advice);
                }
            }
        });
        return view;
    }

    private void reportAdvice(String advice) {
        BaseParams baseParams=new BaseParams("setting/advise");
        baseParams.addParams("advise",advice);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("reportadvise back==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        CusToast.showToast(getActivity(),"提交成功，感谢您的反馈。", Toast.LENGTH_SHORT);
                        settingActivity.backToSetting();
                        ClearWindows.clearInput(getActivity(),editText);
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
