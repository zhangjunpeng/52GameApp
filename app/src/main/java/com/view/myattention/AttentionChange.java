package com.view.myattention;

import android.content.Context;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.test4s.account.MyAccount;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/21.
 */
public class AttentionChange {
    public static void addAttention(String identity_cat, String care_id, final Context context){
        BaseParams baseParams=new BaseParams("care/care4s");
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addParams("care_id",care_id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("caretype","care");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        String mes="关注成功";
                        CusToast.showToast(context,mes,Toast.LENGTH_SHORT);

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
    public static void removeAttention(String identity_cat, String care_id, final Context context){
        BaseParams baseParams=new BaseParams("care/care4s");
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addParams("care_id",care_id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("caretype","uncare");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        String mes="取消关注";
                        CusToast.showToast(context,mes,Toast.LENGTH_SHORT);

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
    public static void removeAttention2(String identity_cat, String care_id, Callback.CommonCallback<String> callback){
        BaseParams baseParams=new BaseParams("care/care4s");
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addParams("care_id",care_id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("caretype","uncare");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), callback);
    }
}
