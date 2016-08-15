package com.view.coustomrequire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.FindIssueInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/7/28.
 */
public class FindIssuesFragment extends CoustomBaseFragment {

    private List<TextView> issueregionTexts;
    private List<TextView> inssuecatTexts;
    private List<TextView> issuegameTexts;

    private List<NameVal> issueregions;
    private List<NameVal> inssuecats;
    private List<NameVal> issuegames;


    public static List<NameVal> issueregions_sel;
    public static List<NameVal> inssuecats_sel;
    public static List<NameVal> issuegames_sel;
    public static String fileUrl;
    public static String otherStr;


    private LinearLayout linear_issueregion;
    private LinearLayout linear_inssuegames;
    private LinearLayout linear_inssuecats;

    private ImageView upfile;
    private TextView clicktoup;
    private EditText otherEdit;
    private CustomizedData customizedData=CustomizedData.getInstance();

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    private FindIssueInfo findIssueInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        issueregionTexts=new ArrayList<>();
        inssuecatTexts=new ArrayList<>();
        issuegameTexts=new ArrayList<>();

        issueregions=customizedData.getMap().get("issueregion");
        inssuecats=customizedData.getMap().get("inssuecat");
        issuegames=customizedData.getMap().get("issuegame");

        issueregions_sel=new ArrayList<>();
        inssuecats_sel=new ArrayList<>();
        issuegames_sel=new ArrayList<>();

        if (info!=null){
            findIssueInfo= (FindIssueInfo) info;

            issueregions_sel=findIssueInfo.getRegion();
            inssuecats_sel=findIssueInfo.getIssuecat();
            issuegames_sel=findIssueInfo.getIssuegame();
            fileUrl=itemInfo.getAppendix();
            otherStr=itemInfo.getNote();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_findissues,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear_inssuecats= (LinearLayout) view.findViewById(R.id.linear_inssuecat);
        linear_inssuegames= (LinearLayout) view.findViewById(R.id.linear_inssuegames);
        linear_issueregion= (LinearLayout) view.findViewById(R.id.linear_issueregion);

        otherEdit= (EditText) view.findViewById(R.id.edit_other);

        upfile= (ImageView) view.findViewById(R.id.upfile);
        clicktoup= (TextView) view.findViewById(R.id.clicktoup);
        
        initView();
    }

    private void initView() {
        for (int i=0;i<linear_inssuecats.getChildCount();i++){
            TextView textView= (TextView) linear_inssuecats.getChildAt(i);
            inssuecatTexts.add(textView);
        }
        for (int i=0;i<linear_inssuegames.getChildCount();i++){
            TextView textView= (TextView) linear_inssuegames.getChildAt(i);
            issuegameTexts.add(textView);
        }
        for (int i=0;i<linear_issueregion.getChildCount();i++){
            TextView textView= (TextView) linear_issueregion.getChildAt(i);
            issueregionTexts.add(textView);
        }

        initTextViewSelect(inssuecatTexts, inssuecats, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    inssuecats_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = inssuecats_sel.iterator();
                    while (it.hasNext()) {
                        NameVal nameVal1 = (NameVal) it.next();
                        if (nameVal1.getId().equals(nameVal.getId())) {
                            //移除当前的对象
                            it.remove();
                        }
                    }
                }
            }
        });

        initTextViewSelect(issueregionTexts, issueregions, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    issueregions_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = issueregions_sel.iterator();
                    while (it.hasNext()) {
                        NameVal nameVal1 = (NameVal) it.next();
                        if (nameVal1.getId().equals(nameVal.getId())) {
                            //移除当前的对象
                            it.remove();
                        }
                    }
                }
            }
        });

        initTextViewSelect(issuegameTexts, issuegames, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    issuegames_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = issuegames_sel.iterator();
                    while (it.hasNext()) {
                        NameVal nameVal1 = (NameVal) it.next();
                        if (nameVal1.getId().equals(nameVal.getId())) {
                            //移除当前的对象
                            it.remove();
                        }
                    }
                }
            }
        });

        otherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    otherStr=otherEdit.getText().toString();
                }
            }
        });

        upfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser(FindIssuesFragment.this);
            }
        });

        if (info!=null){
            initSelectedText(issueregionTexts,issueregions_sel);
            initSelectedText(inssuecatTexts,inssuecats_sel);
            initSelectedText(issuegameTexts,issuegames_sel);
            if (fileUrl.contains(".jpg")){
                ImageLoader.getInstance().displayImage(Url.prePic+fileUrl,upfile, MyDisplayImageOptions.getdefaultBannerOptions());
                clicktoup.setVisibility(View.GONE);
            }else {
                clicktoup.setText("上传完成");
            }
            otherEdit.setText(otherStr);
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
                        CusToast.showToast(getActivity(),mess, Toast.LENGTH_SHORT);
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
