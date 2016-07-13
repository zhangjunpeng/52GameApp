package com.view.Identification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.app.tools.UploadUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.myapp.Config;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.activity.SelectPicActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/7/5.
 */
public class IdentSubFirstFragment extends Fragment implements View.OnClickListener {

    private String cattype="2";
    private String type="company";
    private int stage;


    private LinearLayout areaLayout;
    private LinearLayout resLayout;
    private LinearLayout busiLayout;
    private LinearLayout issCatLayout;
    private LinearLayout invesCatLayout;
    private LinearLayout companyLayout;

    private Dialog dialog;

    private IdentificationConfig config;

    private IdentParams params;


    private ImageLoader imageloder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化参数
        imageloder=ImageLoader.getInstance();
        config=IdentificationConfig.getInstance();
        cattype=getArguments().getString("cattype","2");
        type=getArguments().getString("type","company");
        stage=getArguments().getInt("stage",0);
        params=IdentParams.getInstance();
        params.setOsresSelected(new ArrayList<NameVal>());
        params.setIsscatSelected(new ArrayList<NameVal>());
        params.setInvesStageSelected(new ArrayList<NameVal>());
        params.setBuscatSelected(new ArrayList<NameVal>());
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
            case "5":

                break;
            case "6":

                break;
        }
    }

    private EditText companyName_edit;
    private ImageView icon_sub;
    private ImageView upimage;
    private TextView clicktoselect;
    private TextView size_text;
    private TextView area_text;
    private TextView invescat_text;
    private TextView title_resselect;
    private LinearLayout linear1_res;
    private LinearLayout linear2_res;
    private LinearLayout linear_buscat;
    private LinearLayout linear_isscat;
    private EditText companyInfo_edit;
    private TextView nextstep;

    private RelativeLayout relativeLayout;

    private TextView error;

    private ScrollView view;

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

    private IdentificationConfig identificationConfig=IdentificationConfig.getInstance();

    private String requestURL=identificationConfig.getUploadUrl();


    public IdentSubInfo subinfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= (ScrollView) inflater.inflate(R.layout.fragment_ident_first,container,false);

        return view;
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        MyLog.i("type=="+cattype);
        areaLayout= (LinearLayout) view.findViewById(R.id.layout_area);
        resLayout= (LinearLayout) view.findViewById(R.id.layout_wbrescat);
        busiLayout= (LinearLayout) view.findViewById(R.id.layout_buscat);
        issCatLayout= (LinearLayout) view.findViewById(R.id.layout_issscat);
        invesCatLayout= (LinearLayout) view.findViewById(R.id.layout_invescat);
        companyLayout= (LinearLayout) view.findViewById(R.id.layout_companyinfo);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.stage_relat);

        companyName_edit= (EditText) view.findViewById(R.id.edit_companyname);
        icon_sub= (ImageView) view.findViewById(R.id.icon_iden_sub);
        upimage= (ImageView) view.findViewById(R.id.upimage_icon);
        clicktoselect= (TextView) view.findViewById(R.id.clicktoup_iden_sub);

        size_text= (TextView) view.findViewById(R.id.scale_ident_sub);
        area_text= (TextView) view.findViewById(R.id.area_text_iden_sub);
        invescat_text= (TextView) view.findViewById(R.id.invescat_text_iden_sub);

        title_resselect= (TextView) view.findViewById(R.id.name_wbrescat_ident_sub);
        linear1_res= (LinearLayout) view.findViewById(R.id.linear1_os);
        linear2_res= (LinearLayout) view.findViewById(R.id.linear2_os);

        linear_buscat= (LinearLayout) view.findViewById(R.id.linear_busicat);
        linear_isscat= (LinearLayout) view.findViewById(R.id.linear_issicat);

        companyInfo_edit= (EditText) view.findViewById(R.id.edit_companyinfo);

        nextstep= (TextView) view.findViewById(R.id.nextstep_first);

        error= (TextView) view.findViewById(R.id.text_error_ident);


        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                break;
            case "3":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                initReslayout(config.getOsresList());

                break;
            case "4":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                initReslayout(config.getStageList());

                break;
            case "5":
                areaLayout.setVisibility(View.GONE);
                companyLayout.setVisibility(View.GONE);
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                break;
            case "6":
                resLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                initBusSelect();
                intIssSelect();
                break;
        }

        if (!TextUtils.isEmpty(identificationConfig.getCompanyName())){
            companyName_edit.setText(identificationConfig.getCompanyName());
            companyName_edit.setEnabled(false);
        }
        if (stage==2){
            relativeLayout.setVisibility(View.VISIBLE);
            subinfo=IdentificaSubActivity.subinfo;
            initView();
            switch (subinfo.getPersonal()){
                case "1":
                    type="person";
                    break;
                case "2":
                    type="company";
                    break;
            }

        }


        size_text.setOnClickListener(this);
        area_text.setOnClickListener(this);
        invescat_text.setOnClickListener(this);
        nextstep.setOnClickListener(this);




        icon_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(), SelectPicNormalActivity.class);
                startActivityForResult(intent1, TO_SELECT_PHOTO);
            }
        });

        super.onViewCreated(view,savedInstanceState);
    }

    private void initView() {
        ImageLoader imageLoader=ImageLoader.getInstance();
        companyName_edit.setText(subinfo.getCompany_name());
        imageLoader.displayImage(Url.prePic+subinfo.getLogo(),icon_sub, MyDisplayImageOptions.getBigImageOption());
        size_text.setText(subinfo.getCompany_scale());
        area_text.setText(subinfo.getArea());
        companyInfo_edit.setText(subinfo.getCompany_info());

        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":

                break;
            case "3":
                initTextViews(config.getOsresList(),subinfo.getOutsource_cat());
                break;
            case "4":
                initTextViews(config.getStageList(),subinfo.getInvest_stage());
                invescat_text.setText(subinfo.getInvest_cat());
                break;
            case "5":

                break;
            case "6":
                initBusSelect();
                intIssSelect();
                break;
        }


    }

    private void initTextViews(List<NameVal> osresList, String outsource_cat) {
        for (int i=0;i<osresList.size();i++){
            NameVal nameVal=osresList.get(i);
            TextView textview=resText.get(i);
            if (outsource_cat.contains(nameVal.getId())){
                textview.setSelected(true);
                textview.setTextColor(Color.WHITE);
            }
        }
    }

    private void initBusSelect() {
        final List<NameVal> buisCatList=config.getBuscatList();
        if (buisCatList==null){
            MyLog.i("buisCatList null");
            return;
        }
        for (int i=0;i<linear_buscat.getChildCount();i++){
            if (i>=buisCatList.size()){
                linear_buscat.getChildAt(i).setVisibility(View.INVISIBLE);
            }else {
                final TextView textView= (TextView) linear_buscat.getChildAt(i);
                final NameVal nameVal=buisCatList.get(i);
                textView.setText(nameVal.getVal());

                if (stage==2){
                    if (subinfo.getBusiness_cat().contains(nameVal.getId())){
                        textView.setSelected(true);
                        textView.setTextColor(Color.WHITE);
                    }
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setSelected(!textView.isSelected());
                        if (textView.isSelected()){
                            textView.setTextColor(Color.WHITE);
                            params.getBuscatSelected().add(nameVal);
                        }else {
                            textView.setTextColor(Color.rgb(124,124,124));
                            params.getBuscatSelected().remove(nameVal);

                        }
                    }
                });
            }
        }
    }
    private void intIssSelect(){
        List<NameVal> issCatList=config.getIsscatList();
        if (issCatList==null){
            return;
        }
        for (int i=0;i<linear_isscat.getChildCount();i++){
            if (i>=issCatList.size()){
                linear_isscat.getChildAt(i).setVisibility(View.INVISIBLE);
            }else {
                final TextView textView= (TextView) linear_isscat.getChildAt(i);
                final NameVal nameVal=issCatList.get(i);
                textView.setText(nameVal.getVal());
                if (stage==2){
                    if (subinfo.getBusiness_cat().contains(nameVal.getId())){
                        textView.setSelected(true);
                        textView.setTextColor(Color.WHITE);
                    }
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setSelected(!textView.isSelected());
                        if (textView.isSelected()){
                            textView.setTextColor(Color.WHITE);
                            params.getIsscatSelected().add(nameVal);
                        }else {
                            textView.setTextColor(Color.rgb(124,124,124));
                            params.getIsscatSelected().remove(nameVal);

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scale_ident_sub:
                showSelectDiaolog(size_text,config.getSizeList(),"size");
                break;
            case R.id.area_text_iden_sub:
                showSelectDiaolog(area_text,config.getAreaList(),"area");

                break;
            case R.id.invescat_text_iden_sub:
                showSelectDiaolog(invescat_text,config.getInvescatList(),"invescat");
                break;
            case R.id.nextstep_first:
                if ( checkParams()){
                    IdentSubSecondFragment fragment=new IdentSubSecondFragment();

                    Bundle bundle=new Bundle();
                    bundle.putString("cattype", cattype);

                    bundle.putString("type",type);
                    bundle.putInt("stage",stage);
                    fragment.setArguments(bundle);

                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction transition=fm.beginTransaction();
                    transition.replace(R.id.contianer_ident_sub,fragment).commit();
                    transition.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
                }else {
                    error.setVisibility(View.VISIBLE);
                    view.scrollTo(0,0);
                }

                break;
        }

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;

                case UPLOAD_INIT_PROCESS:

                    break;
                case UPLOAD_IN_PROCESS:

                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+ UploadUtil.getRequestTime()+"秒";
//                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        MyLog.i("MyAccountSeting");
        if(resultCode== Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
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
                imageloder.displayImage("file://"+picPath,icon_sub, MyDisplayImageOptions.getBigImageOption());
                handler.sendEmptyMessage(TO_UPLOAD_FILE);
                upimage.setVisibility(View.GONE);
                clicktoselect.setVisibility(View.GONE);

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

    private boolean checkParams() {
        params.setComppanyName(companyName_edit.getText().toString());
        params.setCompanyInfo(companyInfo_edit.getText().toString());
        if (TextUtils.isEmpty(params.getComppanyName())||TextUtils.isEmpty(params.getIconUrl())){
            return false;
        }
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方

            case "2":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }

                break;
            case "3":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (params.getOsresSelected().size()==0){
                    return false;
                }
                break;
            case "4":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (TextUtils.isEmpty(params.getInvescat())||params.getInvesStageSelected().size()==0){
                    return false;
                }
                break;
            case "5":
                if (TextUtils.isEmpty(params.getCompanySize())){
                    return false;
                }
                break;
            case "6":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (params.getBuscatSelected().size()==0||params.getIsscatSelected().size()==0){
                    return false;
                }
                break;

        }

        return true;
    }

    private List<TextView> resText;

    public void initReslayout(List<NameVal> dataList){
        resText=new ArrayList<>();
        for (int i=0;i<linear1_res.getChildCount();i++){
            TextView textView= (TextView) linear1_res.getChildAt(i);
            resText.add(textView);
        }
        for (int i=0;i<linear2_res.getChildCount();i++){
            TextView textView= (TextView) linear2_res.getChildAt(i);
            resText.add(textView);
        }

        for (int i=0;i<resText.size();i++){
            final TextView textView=resText.get(i);
            NameVal nameVal=null;
            if (i>=dataList.size()){
                textView.setVisibility(View.GONE);
            }else {
                nameVal=dataList.get(i);
                textView.setText(nameVal.getVal());

            }
            final NameVal finalNameVal = nameVal;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setSelected(!textView.isSelected());

                    if (textView.isSelected()){
                        textView.setTextColor(Color.WHITE);
                        switch (cattype){
                            case "3":
                                params.getOsresSelected().add(finalNameVal);
                                break;
                            case "4":
                                params.getInvesStageSelected().add(finalNameVal);
                                break;
                        }
                    }else {
                        textView.setTextColor(Color.rgb(124,124,124));
                        switch (cattype){
                            case "3":
                                params.getOsresSelected().remove(finalNameVal);
                                break;
                            case "4":
                                params.getInvesStageSelected().remove(finalNameVal);
                                break;
                        }
                    }
                }
            });
        }
        
    }


    private void showSelectDiaolog(final TextView textView, final List<NameVal> dataList, final String type){

        if (dataList==null){
            MyLog.i("dataList null");
            return;
        }
        dialog=new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_ident);




        ListView listView= (ListView) dialog.findViewById(R.id.listview_dialog_select);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView==null){
                    convertView=new RelativeLayout(getActivity());
                    RelativeLayout layout= (RelativeLayout) convertView;
                    layout.setGravity(Gravity.CENTER);
                    textView=new TextView(getActivity());
                    textView.setTextSize(15);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.rgb(124,124,124));
                    textView.setBackgroundColor(Color.WHITE);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (52* Config.density));
                    layout.addView(textView,params);
                    convertView.setTag(textView);
                }else {
                    textView= (TextView) convertView.getTag();
                }
                NameVal nameVal=dataList.get(position);
                textView.setText(nameVal.getVal());

                return convertView;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("listView dianji ");
//                initTextview(textView,dataList.get(position));
                NameVal nameVal=dataList.get(position);
                textView.setText(nameVal.getVal());
                switch (type){
                    case "size":
                        params.setCompanySize(nameVal.getId());
                        break;
                    case "area":
                        params.setArea(nameVal.getId());
                        break;
                    case "invescat":
                        params.setInvescat(nameVal.getId());
                        break;
                }
                dialog.dismiss();
//                initNextStep();
            }
        });
        dialog.show();
    }

    private void initNextStep() {
        if (checkParams()){
            nextstep.setClickable(true);
            nextstep.setBackgroundResource(R.drawable.border_button_orange);

        }else {
            nextstep.setBackgroundResource(R.drawable.border_edit_comment);
            nextstep.setClickable(false);
        }
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
                        String iconurl=jsonObject1.getString("picpath");
                        params.setIconUrl(iconurl);
                        CusToast.showToast(getActivity(),"图片上传成功", Toast.LENGTH_SHORT);

                    }else {
                        CusToast.showToast(getActivity(),"图片上传失败，请重新选择", Toast.LENGTH_SHORT);

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
