package com.view.coustomrequire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.FindInvestInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/7/27.
 */
public class FindInvestFragment extends CoustomBaseFragment {


    CustomizedData customizedData=CustomizedData.getInstance();


    private List<NameVal> financingstage;

    private TextView stageText;
    private EditText needNumEdit;
    private EditText minshareEdit;
    private EditText maxshareEdit;
    private ImageView upfile;
    private LinearLayout needIV;
    private EditText otherEdit;

    public static String neednum;
    public static String minshare;
    public static String maxshare;
    public static String other;

    public static String stageStr;
    public static String fileUrl;

    private Dialog dialog;

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    private TextView clicktoup;

    private FindInvestInfo findInvestInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financingstage=customizedData.getMap().get("stage");
        if (info!=null){
            findInvestInfo= (FindInvestInfo) info;
            neednum=findInvestInfo.getMoney();
            minshare=findInvestInfo.getMinstock();
            maxshare=findInvestInfo.getMaxstock();
            stageStr=findInvestInfo.getStarge();
            fileUrl=itemInfo.getAppendix();
            other=itemInfo.getNote();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_findinvest,null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stageText= (TextView) view.findViewById(R.id.stage);
        needNumEdit= (EditText) view.findViewById(R.id.needNum);
        minshareEdit= (EditText) view.findViewById(R.id.minshare);
        maxshareEdit= (EditText) view.findViewById(R.id.maxshare);
        upfile= (ImageView) view.findViewById(R.id.upfile);
        needIV= (LinearLayout) view.findViewById(R.id.needIV);
        otherEdit= (EditText) view.findViewById(R.id.other);
        clicktoup= (TextView) view.findViewById(R.id.clicktoup);

        initListener();
    }

    private void initListener() {
        needNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    neednum=needNumEdit.getText().toString();
                }
            }
        });
        minshareEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    minshare=minshareEdit.getText().toString();
                }
            }
        });
        maxshareEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    maxshare=maxshareEdit.getText().toString();
                }
            }
        });
        otherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    other=otherEdit.getText().toString();
                }
            }
        });
        stageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=showSelectDiaolog(financingstage, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        NameVal nameVal=financingstage.get(position);
                        stageStr=nameVal.getId();
                        stageText.setText(nameVal.getVal());
                    }
                });
            }
        });
        upfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser(FindInvestFragment.this);
            }
        });

        if (info!=null){
            needNumEdit.setText(neednum);
            minshareEdit.setText(minshare);
            maxshareEdit.setText(maxshare);
            if (fileUrl.contains(".jpg")){
                ImageLoader.getInstance().displayImage(Url.prePic+fileUrl,upfile, MyDisplayImageOptions.getdefaultBannerOptions());
                clicktoup.setVisibility(View.GONE);
            }else {
                clicktoup.setText("上传完成");
            }
            otherEdit.setText(other);
            for (NameVal nameVal:financingstage){
                if (nameVal.getId().equals(stageStr)){
                    stageText.setText(nameVal.getVal());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
//                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            String path = FileUtils.getPath(getActivity(), uri);
//                            Toast.makeText(FileChooserExampleActivity.this,
//                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                            updataFile(path);
                            MyLog.i("file path=="+path);
                        } catch (Exception e) {
//                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updataFile(String path) {

        upfile.setBackgroundResource(R.drawable.loading);
        AnimationDrawable drable= (AnimationDrawable) upfile.getBackground();
        drable.start();
        String uploadurl= Url.IconUploadUrlPrefix+Url.CustomizedFileUploadUrl;

        MyLog.i("begein uploadfile =="+uploadurl);
        RequestParams baseParams=new RequestParams(uploadurl);

        baseParams.setMultipart(true);
        baseParams.addBodyParameter("imei", MyApplication.imei);
        baseParams.addBodyParameter("version",MyApplication.versionName);
        baseParams.addBodyParameter("package_name",MyApplication.packageName);
        baseParams.addBodyParameter("channel_id","1");
        baseParams.addBodyParameter("form","app");
        baseParams.addBodyParameter("token", MyAccount.getInstance().getToken());

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",MyApplication.imei);
        map.put("version",MyApplication.versionName);
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("form","app");
        map.put("token",MyAccount.getInstance().getToken());
        baseParams.addBodyParameter("sign", Url.getSign(map.entrySet()));
        baseParams.addBodyParameter("filedata",new File(path),null);
        x.http().post(baseParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("updata file==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    if (su){
                        JSONObject data=jsonObject.getJSONObject("data");
                        fileUrl=data.getString("picpath");
                        if (fileUrl.contains(".jpg")){
                            ImageLoader.getInstance().displayImage(Url.prePic+fileUrl,upfile, MyDisplayImageOptions.getdefaultBannerOptions());
                            clicktoup.setVisibility(View.GONE);
                        }else {
                            clicktoup.setText("上传完成");
                        }
                    }else {
                        String mess=jsonObject.getString("msg");
                        CusToast.showToast(getActivity(),mess,Toast.LENGTH_SHORT);
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
                upfile.setBackgroundColor(Color.rgb(238,238,238));
            }
        });

    }
}
