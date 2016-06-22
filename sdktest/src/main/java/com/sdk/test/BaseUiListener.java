package com.sdk.test;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/14.
 */
public class BaseUiListener implements IUiListener {
    final static String TAG="SDKTEST";

    @Override
    public void onComplete(Object o) {
        JSONObject jsonObject= (JSONObject) o;
        Log.i(TAG,"SDK回调");
        Log.i(TAG,jsonObject.toString());
    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
