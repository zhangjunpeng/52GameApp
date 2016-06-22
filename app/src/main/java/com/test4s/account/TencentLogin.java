package com.test4s.account;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.app.tools.MyLog;
import com.tencent.connect.*;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TencentLogin {
    private static TencentLogin intance;
    private Fragment mFragment;
    private Activity mActivity;
    public Tencent mtencent;

    private Context context;

    public String token;
    private IUiListener listener;
    private TencentLogin(Activity activity){
        mActivity=activity;
        context=activity;
        mtencent=Tencent.createInstance("1105244367",activity.getApplicationContext());
    }
    private TencentLogin(Fragment fragment){
        mFragment=fragment;
        context=fragment.getActivity();
        mtencent=Tencent.createInstance("1105244367",fragment.getActivity().getApplicationContext());
    }
    public static TencentLogin getIntance(Activity activity){
        if (intance==null){
            intance=new TencentLogin(activity);
        }
        return intance;
    }
    public static TencentLogin getIntance(Fragment fragment){
        if (intance==null){
            intance=new TencentLogin(fragment);
        }
        return intance;
    }

    public void login(){
        MyLog.i("qq登录3");

        if (!mtencent.isSessionValid())
        {
            if (mActivity==null){
                MyLog.i("qq登录mFragment");

                MyLog.i("qq login back::=="+ mtencent.login(mFragment, "all", listener));
            }else if (mFragment==null){
                MyLog.i("qq登录mActivity");

                MyLog.i("qq login back::=="+ mtencent.login(mActivity, "all", listener));
            }
        }else {
            MyLog.i("checkLogin");

            mtencent.checkLogin(listener);
        }
    }


    public void getUserInfo(Context context,IUiListener listener){
        com.tencent.connect.UserInfo userInfo=new UserInfo(context,mtencent.getQQToken());
        userInfo.getUserInfo(listener);
    }
    public void loginOut(){
        if (mtencent!=null){
            MyLog.i("mtencent loginOut");
          mtencent.logout(context);
        }
    }
}
