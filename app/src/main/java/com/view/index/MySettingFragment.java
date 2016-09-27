package com.view.index;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.tools.UploadUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.account.AccountActivity;
import com.test4s.account.UserInfo;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Evaluation.EvaluationActivity;
import com.view.Identification.IdentificationActivity;
import com.view.coustomrequire.CustomizedListActivity;
import com.view.messagecenter.MessageList;
import com.view.accountsetting.MyAcountSettingActivity;
import com.view.activity.SelectPicActivity;
import com.test4s.myapp.R;
import com.view.myattention.AttentionActivity;
import com.view.myreport.ReprotListActivity;
import com.view.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/12/7.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener{

    private CircleImageView roundedIcon;
    private TextView textView;
    private TextView name2;

    private ImageView reddot;

    private String picPath = null;

    private String requestURL="http://www.4stest.com/upload.php";

    public static final int RequestCode_login=101;
    public static final int RequestCode_setting=102;


    public boolean network=true;

    private ImageLoader imageLoader=ImageLoader.getInstance();

    public static boolean changeIcon=false;

    private LinearLayout require;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mysetting,null);
        roundedIcon= (CircleImageView) view.findViewById(R.id.icon_roundImage);
        roundedIcon.setOnClickListener(this);
        textView= (TextView) view.findViewById(R.id.name_my);
        name2= (TextView) view.findViewById(R.id.name2_my);
        name2.setAlpha((float)0.4);
        view.findViewById(R.id.myAccount_my).setOnClickListener(this);
        view.findViewById(R.id.seting_mysetting).setOnClickListener(this);
        view.findViewById(R.id.messagecenter_my).setOnClickListener(this);
        view.findViewById(R.id.pc_my).setOnClickListener(this);
        view.findViewById(R.id.myattention_mysetting).setOnClickListener(this);
        view.findViewById(R.id.myreport_my).setOnClickListener(this);
        view.findViewById(R.id.idtifition_mysetting).setOnClickListener(this);

        view.findViewById(R.id.customized_my).setOnClickListener(this);
        view.findViewById(R.id.cooperation_my).setOnClickListener(this);

        require= (LinearLayout) view.findViewById(R.id.require_linear);


        updataUserName();
        updataAva();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void change(ChangeEvent event){
        //type 为0变昵称，为1变头像
        switch (event.type){
            case 0:
                textView.setText(event.data);
                break;
            case 1:
                if (event.data.contains("http")) {
                    imageLoader.displayImage(event.data, roundedIcon, MyDisplayImageOptions.getIconOptions());
                }else {
                    imageLoader.displayImage( Url.prePic+event.data, roundedIcon,MyDisplayImageOptions.getIconOptions());
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void changeRequir(Set<String> datalist){
        if (datalist==null){
            require.setVisibility(View.GONE);
        }else {
            if (datalist.size()==0){
                require.setVisibility(View.GONE);
            }else {
                require.setVisibility(View.VISIBLE);
                Object[] useridents=  datalist.toArray();
                for (int i=0;i<5;i++){
                    TextView textView= (TextView) require.getChildAt(i);
                    MyLog.i("require list size=="+datalist.size());

                    if (i>=datalist.size()){
                        textView.setVisibility(View.GONE);
                    }else {
                        textView.setText(useridents[i].toString());
                    }
                }
            }
        }
    }




    private void initData() {
        if (MyAccount.isLogin){
            UserInfo userInfo=MyAccount.getInstance().getUserInfo();
            if (userInfo!=null) {
                MyLog.i("userInfo not null");
                if (!TextUtils.isEmpty(userInfo.getNickname())) {
                    textView.setText(userInfo.getNickname());
                } else {
                    if (!TextUtils.isEmpty(userInfo.getPhone())){
                        String nickname = userInfo.getPhone();
                        String subs = nickname.substring(3, 7);
                        MyLog.i("subs===" + subs);
                        nickname = nickname.replace(subs, "*****");
                        textView.setText(nickname);
                    }else {
                        String nickname = userInfo.getEmail();
                        String subs[] = nickname.split("@");
                        MyLog.i("subs===" + subs[0]);
                        nickname = nickname.replace(subs[0], subs[0].replace(subs[0].substring(3, subs[0].length()), "****"));
                        textView.setText(nickname);
                    }
                }
            }else {
                MyLog.i("userInfo null");

                initUserInfo();

            }

        }else {
            textView.setText(MyAccount.getInstance().getNickname());
        }
//        updataAva();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myAccount_my:
                Intent intent;
                if (MyAccount.isLogin){
                    if (!network){
                        CusToast.showToast(getActivity(),"网络连接失败，请检查网络",Toast.LENGTH_SHORT);

                        return;
                    }else {
                        intent=new Intent(getActivity(), MyAcountSettingActivity.class);
                        startActivityForResult(intent,RequestCode_setting);
                    }

                }else {
                    intent=new Intent(getActivity(), AccountActivity.class);
                    startActivityForResult(intent,RequestCode_login);
                }

                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.icon_roundImage:
//                Dialog dialog=new Dialog(getActivity());
//                dialog.setContentView(R.layout.dialog_mysetting);
//                Intent intent1=new Intent(getActivity(), SelectPicActivity.class);
//                startActivityForResult(intent1, TO_SELECT_PHOTO);

                break;
            case R.id.seting_mysetting:
                Intent intent3 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent3,RequestCode_setting);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case R.id.messagecenter_my:
                if (MyAccount.isLogin) {
                    Intent intent2 = new Intent(getActivity(), MessageList.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("messagecenter");
                }
                break;
            case R.id.pc_my:
                if (MyAccount.isLogin) {
                    Intent intent2 = new Intent(getActivity(), EvaluationActivity.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("pc");

                }
                break;
            case R.id.myattention_mysetting:
                if (MyAccount.isLogin) {
                    Intent intent1 = new Intent(getActivity(), AttentionActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("attention");
                }
                break;
            case R.id.myreport_my:
                if (MyAccount.isLogin) {
                    Intent intent1 = new Intent(getActivity(), ReprotListActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("report");
                }
                break;
            case R.id.idtifition_mysetting:
                if (MyAccount.isLogin) {
                    Intent intent1 = new Intent(getActivity(), IdentificationActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("ident");
                }
                break;

            case R.id.customized_my:
                //我的定制
                if (MyAccount.isLogin) {
                    Intent intent1 = new Intent(getActivity(), CustomizedListActivity.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else {
                    //未登录
                    gologin("customized");
                }
                break;
            case R.id.cooperation_my:
                //我的合作
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.i("index My onActivityResult");
        if (resultCode==Activity.RESULT_OK&&requestCode==RequestCode_login){
            //登录成功返回
            MyLog.i("mysetting~~~~~onActivityResult");
            if (!MyAccount.isLogin){
                CusToast.showToast(getActivity(),"登录失败",Toast.LENGTH_SHORT);

            }else {
                CusToast.showToast(getActivity(),"登录成功",Toast.LENGTH_SHORT);

                updataUserName();
                updataAva();
                initUserInfo();
            }
        }
        if (resultCode==Activity.RESULT_OK&&requestCode==RequestCode_setting){

            MyLog.i("mysetting~~~~~RequestCode_setting");
//            initView();
            updataUserName();
            updataAva();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updataUserName() {
        if (MyAccount.isLogin){
            name2.setVisibility(View.GONE);
            MyAccount myAccount=MyAccount.getInstance();
            MyLog.i("myaccount=="+myAccount.toString());
            if (TextUtils.isEmpty(myAccount.getNickname())){
                String nickname=myAccount.getUsername();
                String subs=nickname.substring(3,7);
                MyLog.i("subs==="+subs);
                nickname=nickname.replace(subs,"*****");
                textView.setText(nickname);
            }else {
                textView.setText(myAccount.getNickname());
            }
//
            Set<String> requireList=MyAccount.getInstance().getUserident();
            if (requireList.size()==0){
                require.setVisibility(View.GONE);
            }else {
                require.setVisibility(View.VISIBLE);
                Object[] useridents=  requireList.toArray();
                for (int i=0;i<5;i++){
                    TextView textView= (TextView) require.getChildAt(i);
                    MyLog.i("require list size=="+requireList.size());
                    if (i>=requireList.size()){
                        textView.setVisibility(View.GONE);
                    }else {
                        textView.setText(useridents[i].toString());
                    }
                }
            }

        }else {
            roundedIcon.setImageResource(R.drawable.default_icon);
            textView.setText("未登录");
            name2.setVisibility(View.VISIBLE);
        }
    }



    private void updataAva() {
        if (MyAccount.isLogin){
            MyAccount myAccount=MyAccount.getInstance();
            if (myAccount.getAvatar().contains("http")) {
                imageLoader.displayImage( myAccount.getAvatar(), roundedIcon, MyDisplayImageOptions.getIconOptions());
            }else {
                imageLoader.displayImage( Url.prePic+myAccount.getAvatar(), roundedIcon,MyDisplayImageOptions.getIconOptions());
            }
            changeIcon=false;
        }else {
            roundedIcon.setImageResource(R.drawable.default_icon);
        }
    }


    private void initUserInfo() {
        BaseParams baseParams=new BaseParams("user/index");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("UserInfo==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        UserInfo userInfo=new UserInfo();
                        JSONObject jsonObject2=jsonObject.getJSONObject("data");
                        JSONObject jsonObject3=jsonObject2.getJSONObject("userInfo");
                        userInfo.setId(jsonObject3.getString("id"));
                        userInfo.setUsername(jsonObject3.getString("username"));
                        userInfo.setNickname(jsonObject3.getString("nickname"));
                        userInfo.setEmail(jsonObject3.getString("email"));
                        userInfo.setPhone(jsonObject3.getString("phone"));
                        userInfo.setAvatar(jsonObject3.getString("avatar"));
                        userInfo.setUser_identity(jsonObject3.getString("user_identity"));
                        userInfo.setGame_like(jsonObject3.getString("game_like"));
                        userInfo.setJob_id(jsonObject3.getString("job_id"));
                        userInfo.setEdu_id(jsonObject3.getString("edu_id"));
                        userInfo.setProvince(jsonObject3.getString("province"));
                        userInfo.setCity(jsonObject3.getString("city"));
                        userInfo.setCounty(jsonObject3.getString("county"));
                        userInfo.setAddr(jsonObject3.getString("addr"));
                        userInfo.setProvince_name(jsonObject3.getString("province_name"));
                        userInfo.setCity_name(jsonObject3.getString("city_name"));
                        userInfo.setCounty_name(jsonObject3.getString("county_name"));
                        userInfo.setEdu_name(jsonObject3.getString("edu_name"));
                        userInfo.setJob_name(jsonObject3.getString("job_name"));
                        userInfo.setQq_sign(jsonObject3.getString("qq_sign"));
                        userInfo.setWeixin_sign(jsonObject3.getString("weixin_sign"));
                        userInfo.setSina_sign(jsonObject3.getString("sina_sign"));

                        String ident=jsonObject3.getString("user_identity");
                        if (!TextUtils.isEmpty(ident)){
                            JSONArray identArray=jsonObject3.getJSONArray("user_identity");
                            Set<String> identList=new TreeSet<String>();
                            for (int i=0;i<identArray.length();i++){
                                identList.add(identArray.getString(i));
                            }
//                            userInfo.setUserIdent(identList);
                            MyAccount.getInstance().setUserident(identList);
                            MyAccount.getInstance().saveUserInfo();
                        }


                        MyAccount.getInstance().setUsername(jsonObject3.getString("username"));
                        MyAccount.getInstance().setNickname(jsonObject3.getString("nickname"));


                        MyAccount.getInstance().setUserInfo(userInfo);
                        network=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i("UserInfo==="+ex.toString());
                network=false;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initData();
                updataAva();
            }
        });
    }

    public void gologin(String tag){
        Intent intent=new Intent(getActivity(), AccountActivity.class);
        intent.putExtra("tag",tag);
        startActivityForResult(intent,RequestCode_login);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
