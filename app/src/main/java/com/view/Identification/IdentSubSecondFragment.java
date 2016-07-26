package com.view.Identification;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.FileSizeUtil;
import com.app.tools.MD5Test;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.tools.UploadUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.SelectPicActivity;
import com.wheel.ChangeAddressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/5.
 */
public class IdentSubSecondFragment extends Fragment implements View.OnClickListener {

    private String type="person";
    private String cattype="2";
    private int stage;
    ScrollView view;

    private LinearLayout idnum_lauout;
    private LinearLayout companyphoto_layout;


    IdentParams identParams;
    private String username;
    private String phonenum;
    private String code;
    private String provinceid;
    private String cityid;
    private String countryId;
    private String address;
    private String Idnum;
    private String companyPhotoUrl;
    private ImageLoader imageloder;
    private String pa;

    private IdentSubInfo subinfo;
    private ImageLoader imageLoader=ImageLoader.getInstance();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageloder=ImageLoader.getInstance();
        identParams=IdentParams.getInstance();
        type=getArguments().getString("type","company");
        stage=getArguments().getInt("stage",0);
        cattype=getArguments().getString("cattype","2");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ScrollView) inflater.inflate(R.layout.fragment_ident_company, null);
        return view;
    }

    private EditText name_edit;
    private EditText phone_edit;
    private EditText code_edit;
    private TextView getcode_text;

    private TextView selectAddress;
    private EditText address_edit;
    private EditText idnum_edit;

    private ImageView comphoto;
    private LinearLayout up_need;

    private TextView submit;

    private TextView error;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idnum_lauout= (LinearLayout) view.findViewById(R.id.layout_idnum);
        companyphoto_layout= (LinearLayout) view.findViewById(R.id.layout_companyphoto);
        name_edit= (EditText) view.findViewById(R.id.edit_username);
        phone_edit= (EditText) view.findViewById(R.id.edit_phone);
        code_edit= (EditText) view.findViewById(R.id.edit_code);
        getcode_text= (TextView) view.findViewById(R.id.getcode_iden_sub);
        selectAddress= (TextView) view.findViewById(R.id.address_text);
        address_edit= (EditText) view.findViewById(R.id.address_edit);
        idnum_edit= (EditText) view.findViewById(R.id.edit_idnum);
        comphoto= (ImageView) view.findViewById(R.id.companyphoto_ident);
        up_need= (LinearLayout) view.findViewById(R.id.layout_up_need);

        submit= (TextView) view.findViewById(R.id.submit_iden_sub);
        error= (TextView) view.findViewById(R.id.text_error_identsub);


        if (stage==2){
            subinfo=IdentificaSubActivity.subinfo;
            initView();
        }

        MyLog.i("type==="+type);
        switch (type){
            case "person":
                companyphoto_layout.setVisibility(View.GONE);
                break;
            case "company":
                idnum_lauout.setVisibility(View.GONE);
                break;
        }

        submit.setOnClickListener(this);
        selectAddress.setOnClickListener(this);
        comphoto.setOnClickListener(this);
        getcode_text.setOnClickListener(this);
    }

    private void initView() {
        name_edit.setText(subinfo.getApply());
        username=subinfo.getApply();


        phone_edit.setText(subinfo.getCompany_phone());
        phonenum=subinfo.getCompany_phone();

        String address_sel=subinfo.getProvince().getVal()+"-"+subinfo.getCity().getVal()+"-"+subinfo.getCountry().getVal();
        selectAddress.setText(address_sel);
        provinceid=subinfo.getProvince().getId();
        cityid=subinfo.getCity().getId();
        countryId=subinfo.getCountry().getId();

        address=subinfo.getAddress();
        address_edit.setText(address);

        switch (subinfo.getPersonal()){
            case "1":
                idnum_edit.setText(subinfo.getIdnum());
                Idnum=subinfo.getIdnum();

                break;
            case "2":
                imageLoader.displayImage(Url.prePic+subinfo.getCompany_photo(),comphoto,MyDisplayImageOptions.getBigImageOption());
                companyPhotoUrl=subinfo.getCompany_photo();

                break;
        }



    }

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;

    private String picPath = null;

    private String requestURL=IdentificationConfig.getInstance().getUploadUrl();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_iden_sub:
                if (checkparams()){
//                    startIdentifition();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            startIdentifitionHttp();
                        }
                    });
                }
                break;
            case R.id.getcode_iden_sub:
                changeCode();
                break;
            case R.id.address_text:
                ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(
                        getActivity());
                if (stage==2){
                    mChangeAddressDialog.setAddress(subinfo.getProvince(),subinfo.getCity(),subinfo.getCountry());
                }

                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
                            @Override
                            public void onClick(NameVal province, NameVal city, NameVal country) {
                                MyLog.i("province::city::country=="+province.getVal()+"::"+city.getVal()+"::"+country.getVal());

                                selectAddress.setText(province.getVal()+"-"+city.getVal()+"-"+country.getVal());
                                provinceid=province.getId();
                                cityid=city.getId();
                                countryId=country.getId();
                            }
                        });
                break;
            case R.id.companyphoto_ident:
                Intent intent1=new Intent(getActivity(), SelectPicNormalActivity.class);
                startActivityForResult(intent1, TO_SELECT_PHOTO);
                break;
        }
    }

    private boolean checkparams() {
        username=name_edit.getText().toString();
        phonenum=phone_edit.getText().toString();
        code=code_edit.getText().toString();
        address=address_edit.getText().toString();
        Idnum=idnum_edit.getText().toString();

        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(phonenum)||TextUtils.isEmpty(code)||TextUtils.isEmpty(address)){
            showwarn("资料未填写完整");
            return false;
        }

        switch (type){
            case "company":
                if (TextUtils.isEmpty(companyPhotoUrl)){
                    showwarn("图片上传未成功");
                    return false;
                }
                break;
            case "person":
                if (TextUtils.isEmpty(Idnum)){
                    showwarn("身份证号码未填写");
                    return false;
                }
                break;

        }
        if (TextUtils.isEmpty(pa)){
            showwarn("请获取验证码");
            return false;
        }
        return true;
    }

    private void showwarn(String mess) {
        error.setText(mess);
        error.setVisibility(View.VISIBLE);
        view.scrollTo(0,0);
    }

    private void changeCode() {
        phonenum=phone_edit.getText().toString();
        if (phonenum.length()<11){
            showwarn("请输入正确的电话号码");
            return;
        }
        getcode_text.setClickable(false);
        getCode();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (time>0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1111);
                }
                handler.sendEmptyMessage(2222);
            }
        });
    }
    int time=60;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    double size= FileSizeUtil.getFileOrFilesSize(picPath,FileSizeUtil.SIZETYPE_MB);
                    if (size>1){

                    }else {
                        toUploadFile();
                    }
                    break;

                case UPLOAD_INIT_PROCESS:

                    break;
                case UPLOAD_IN_PROCESS:

                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+ UploadUtil.getRequestTime()+"秒";
//                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                    break;
                case 1111:
                    time--;
                    getcode_text.setText(time+"秒");
                    break;
                case 2222:
                    time=60;
                    getcode_text.setText("重新获取");
                    getcode_text.setClickable(true);
                    break;
                case 666:
                    String data= (String) msg.obj;
                    MyLog.i("data==="+data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        boolean su=jsonObject.getBoolean("success");
                        int code=jsonObject.getInt("code");
                        if (su&&code==200){
//                            getActivity().setResult(Activity.RESULT_OK);
//                            getActivity().finish();
                            Intent intent=new Intent(getActivity(),IdentificationActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                            getActivity().finish();
                        }else {
                            String mess=jsonObject.getString("msg");
                            showwarn(mess);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {

//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                icon.setImageBitmap(photo);
//            }

            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            MyLog.i( "最终选择的图片="+picPath);
            if(picPath!=null)
            {
                imageloder.displayImage("file://"+picPath,comphoto, MyDisplayImageOptions.getBigImageOption());
                handler.sendEmptyMessage(TO_UPLOAD_FILE);
                up_need.setVisibility(View.GONE);

            }else{
                CusToast.showToast(getActivity(),"上传的文件路径出错",Toast.LENGTH_SHORT);

            }
        }
//        else if (requestCode==888&&resultCode==Activity.RESULT_OK){
//            Bitmap bmap = data.getParcelableExtra("data");
//            icon.setImageBitmap(bmap);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void toUploadFile() {
//        UploadUtil uploadUtil = UploadUtil.getInstance();;
//        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);


        RequestParams baseParams=new RequestParams(requestURL);

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
        baseParams.addBodyParameter("filedata",new File(picPath),null);
        x.http().post(baseParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("updataImage==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    if (su){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String url=jsonObject1.getString("url");
                        companyPhotoUrl=jsonObject1.getString("picpath");
                        CusToast.showToast(getActivity(),"图片上传成功",Toast.LENGTH_SHORT);

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

    private void startIdentifition(){
        BaseParams params=new BaseParams("identity/saveidentity",true);
//        RequestParams params=new RequestParams("");

//        params.getRequestParams().addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");

        String contentType="application/x-www-form-urlencoded; charset=utf-8";
//        params.getRequestParams().setCharset("utf-8");

        switch (type){
            case "person":
                params.adddParamswithChines("personal","1");
                params.adddParamswithChines("business_id",Idnum);
                break;
            case "company":
                params.adddParamswithChines("personal","2");
                params.adddParamswithChines("business_pic",companyPhotoUrl);
                break;
        }
        params.adddParamswithChines("token",MyAccount.getInstance().getToken());
//        params.getRequestParams().addBodyParameter("company_name",identParams.getComppanyName());
        params.adddParamswithChines("company_name",identParams.getComppanyName());
        params.adddParamswithChines("apply",username);
        params.adddParamswithChines("logo",identParams.getIconUrl());
        params.adddParamswithChines("company_scale",identParams.getCompanySize());
        params.adddParamswithChines("company_phone",phonenum);
        params.adddParamswithChines("code",code);
        params.adddParamswithChines("pa",pa);
        params.adddParamswithChines("province",provinceid);
        params.adddParamswithChines("city_id",cityid);
        params.adddParamswithChines("county",countryId);
        params.adddParamswithChines("addr",address);
        params.adddParamswithChines("area_id",identParams.getArea());
        params.adddParamswithChines("company_intro",identParams.getCompanyInfo());

        params.adddParamswithChines("identity_cat",cattype);

        //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
        switch (cattype){
            case "3":
                String oscats=getParamsFromList(identParams.getOsresSelected());
                params.adddParamswithChines("outsource_cat",oscats);
                break;
            case "4":
                String stages=getParamsFromList(identParams.getInvesStageSelected());
                params.adddParamswithChines("invest_stage",stages);
                params.adddParamswithChines("invest_cat",identParams.getInvescat());
                break;
            case "5":
                break;
            case "6":
                String coops=getParamsFromList(identParams.getIsscatSelected());
                params.adddParamswithChines("coop_cat",coops);
                String busis=getParamsFromList(identParams.getBuscatSelected());
                params.adddParamswithChines("business_cat",busis);
                break;
        }

        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("ident back==="+result);
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
    public void startIdentifitionHttp(){
        try {
            URL postUrl=new URL(BaseParams.urlindex+"identity/saveidentity");
            HttpURLConnection httpURLConnection= (HttpURLConnection) postUrl.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("contentType", "utf-8");
            httpURLConnection.connect();
//            DataOutputStream out = new DataOutputStream(httpURLConnection
//                    .getOutputStream());
            String content=getParams();
// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            MyLog.i("content==="+content);
            OutputStream out=httpURLConnection.getOutputStream();
            out.write(content.getBytes("UTF-8"));

            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String data="";
            String line;

            while ((line = reader.readLine()) != null){
                data=data+line;
            }

            MyLog.i("startident back==="+data);
            handler.obtainMessage(666,data).sendToTarget();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getParams() throws UnsupportedEncodingException {
        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",MyApplication.imei);
        map.put("version",MyApplication.versionName);
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("app","2");
        switch (type){
            case "person":
                map.put("personal","1");
                map.put("business_id",Idnum);
                break;
            case "company":
                map.put("personal","2");
                map.put("business_pic",companyPhotoUrl);
                break;
        }
        map.put("token",MyAccount.getInstance().getToken());
//        params.getRequestParams().addBodyParameter("company_name",identParams.getComppanyName());
        map.put("company_name", identParams.getComppanyName());
        map.put("apply", username);
        map.put("logo",identParams.getIconUrl());
        map.put("company_scale",identParams.getCompanySize());
        map.put("company_phone",phonenum);
        map.put("code",code);
        map.put("pa",pa);
        map.put("province",provinceid);
        map.put("city_id",cityid);
        map.put("county",countryId);
        map.put("addr",address);
        map.put("area_id",identParams.getArea());

        map.put("identity_cat",cattype);

        //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
        switch (cattype){
            case "2":
                map.put("company_intro",identParams.getCompanyInfo());

                break;
            case "3":
                String oscats=getParamsFromList(identParams.getOsresSelected());
                map.put("outsource_cat",oscats);

                map.put("company_intro",identParams.getCompanyInfo());

                break;
            case "4":
                String stages=getParamsFromList(identParams.getInvesStageSelected());
                map.put("invest_stage",stages);
                map.put("invest_cat",identParams.getInvescat());

                map.put("company_intro",identParams.getCompanyInfo());

                break;
            case "5":
                break;
            case "6":
                String coops=getParamsFromList(identParams.getIsscatSelected());
                map.put("coop_cat",coops);
                String busis=getParamsFromList(identParams.getBuscatSelected());
                map.put("business_cat",busis);

                map.put("company_intro",identParams.getCompanyInfo());

                break;
        }

        return Url.getContent(map.entrySet());
    }

    private String getParamsFromList(List<NameVal> nameValList) {
        StringBuffer ids=new StringBuffer("");
        for (int i=0;i<nameValList.size();i++){
            if (i>0){
                ids.append("-");
            }
            ids.append(nameValList.get(i).getId());
        }
        return ids.toString();
    }

    private void getCode() {
        BaseParams baseParams=new BaseParams("sms/index");
        baseParams.addParams("phone",phonenum);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("GetCode_back:::"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean success=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (success&&code==200) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        pa = jsonObject1.getString("pa");
                    }else {
                        String mes=jsonObject.getString("msg");
//                        showwarn(mes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getcode_text.setClickable(true);
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
