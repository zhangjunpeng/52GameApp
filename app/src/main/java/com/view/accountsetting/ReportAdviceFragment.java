package com.view.accountsetting;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.net.BaseParams;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/2/29.
 */
public class ReportAdviceFragment extends Fragment {
    String advice;

    private void sendAdvice(){
        if (!MyAccount.isLogin){
            Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(advice)){
            Toast.makeText(getActivity(),"请输入最少20个字",Toast.LENGTH_SHORT).show();
            return;
        }
        BaseParams baseParams=new BaseParams("setting/advise");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("advise",advice);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("reportAdvice=="+result);
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
