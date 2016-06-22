package com.test4s.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.app.tools.ImageDownloadHelper;
import com.app.tools.MyLog;
import com.test4s.myapp.MyApplication;
import com.test4s.net.RegisterParms;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MyAccount {
    public static boolean isLogin=false;
    private String username;
    private String nickname;
    private String token;
    private String avatar;

    private UserInfo userInfo;



    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private static MyAccount instance;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    final static String SP_NAME="4stest";
    SharedPreferences sharedPreferences;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private MyAccount(){
        sharedPreferences= MyApplication.mcontext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        nickname=sharedPreferences.getString("nickname","");
        token=sharedPreferences.getString("token","");
        avatar=sharedPreferences.getString("avatar","");


    }
    public static MyAccount getInstance(){
        if (instance==null){
            instance=new MyAccount();
        }
        return instance;
    }

    public void saveUserInfo(){
        sharedPreferences=MyApplication.mcontext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("nickname",nickname);
        editor.putString("token",token);
        editor.putString("avatar",avatar);

        editor.commit();
    }


    public byte[] bitmap2Bytes(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100,bos);
        return bos.toByteArray();
    }

    public void loginOut(){
        instance=null;
        isLogin=false;
        sharedPreferences=MyApplication.mcontext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username","");
        editor.putString("nickname","");
        editor.putString("token","");
        editor.putString("avatar","");
        editor.commit();
    }

    @Override
    public String toString() {
        return "[username:"+username+"  nickname:"+nickname+"   token:"+token+"  avatar:"+avatar+"]";
    }
}
