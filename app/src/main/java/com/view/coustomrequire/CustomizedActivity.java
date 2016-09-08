package com.view.coustomrequire;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.Event.ToastEvent;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.Identification.IdentificationActivity;
import com.view.Identification.NameVal;
import com.view.game.FiltParamsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import de.greenrobot.event.EventBus;

public class CustomizedActivity extends AppCompatActivity {


    private FancyCoverFlow fancyCoverFlow;

    private Map<Integer,RequirementInfo> map;

    private TextView identcat_text;
    private LinearLayout findLinear;

    private List<TextView> textViewList;

    private int ident_select;
    private int position_select;

    private FragmentManager fm;

    private CustomizedData customizedData=CustomizedData.getInstance();
    private Map<String,List<NameVal>> cusmap;

    private String name;
    private String phonenum;
    private String code;
    private String pa;

    private EditText usernameEdit;
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getcode;
    private TextView submit;

    private ImageView back;
    private TextView title;
    private ImageView save;
    private Dialog dialog;
    private float density;
    private int windowWidth;

    private boolean changePhone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();

        initView();


//        findViewById(R.id.check_moneyinfo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(CustomizedActivity.this,ServiceMoneyInfoActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//            }
//        });



    }

    private void initView() {

        setContentView(R.layout.activity_customized);


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (ImageView) findViewById(R.id.help_savebar);

        title.setText("需求定制");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

        fm=getSupportFragmentManager();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CustomizedActivity.this,ServiceMoneyInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


        usernameEdit= (EditText) findViewById(R.id.edit_username);
        phoneEdit= (EditText) findViewById(R.id.edit_phone);
        codeEdit= (EditText) findViewById(R.id.edit_code);
        getcode= (TextView) findViewById(R.id.getcode);
        submit= (TextView) findViewById(R.id.submit);

        fancyCoverFlow= (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        identcat_text= (TextView) findViewById(R.id.identcat_text);
        findLinear= (LinearLayout) findViewById(R.id.linear_findservice);
        textViewList=new ArrayList<>();
        for (int i=0;i<findLinear.getChildCount();i++){
            TextView textView= (TextView) findLinear.getChildAt(i);
            textViewList.add(textView);
        }

//        initFancyCoverFlow();
        initListener();
    }

    private void initListener() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setFocusable(true);
                submit.setFocusableInTouchMode(true);
                submit.requestFocus();
                submit.requestFocusFromTouch();
                if (checkParams()){
                    name=usernameEdit.getText().toString();
                    phonenum=phoneEdit.getText().toString();
                    code=codeEdit.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        CusToast.showToast(CustomizedActivity.this,"请输入联系人信息",Toast.LENGTH_SHORT);
                        return;
                    }
                    if (phonenum.length()!=11){
                        CusToast.showToast(CustomizedActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT);
                        return;
                    }
                    startSubmit();
                }else {
                    CusToast.showToast(CustomizedActivity.this,"信息填写不完整",Toast.LENGTH_SHORT);
                }
            }
        });

    }
    private String getStringFromList(List<NameVal> nameVals){
        StringBuffer str=new StringBuffer("");
        for (int i=0;i<nameVals.size();i++){
            if (i!=0){
                str.append(",");
            }
            NameVal nameVal=nameVals.get(i);
            str.append(nameVal.getId());
        }
        return str.toString();
    }

    private void startSubmit() {

        RequirementInfo requirementInfo=map.get(ident_select);
        NameVal nameVal= requirementInfo.getFindservice().get(position_select);

        BaseParams baseParams=new BaseParams("customize/savecustomize");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("identity_cat",requirementInfo.getIdent_cat());
        baseParams.addParams("service_cat",nameVal.getId());
        baseParams.addParams("apply",name);
        baseParams.addParams("phone",phonenum);

        if (!TextUtils.isEmpty(pa)){
            baseParams.addParams("pa",pa);
            baseParams.addParams("code",code);
        }

        switch (requirementInfo.getIdent_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (nameVal.getId().equals("4")){
                    baseParams.addParams("starge",FindInvestFragment.stageStr);
                    baseParams.addParams("money",FindInvestFragment.neednum);
                    baseParams.addParams("stock_start",FindInvestFragment.minshare);
                    baseParams.addParams("stock_end",FindInvestFragment.maxshare);
                    if (!TextUtils.isEmpty(FindInvestFragment.fileUrl)){
                        baseParams.addParams("appendix",FindInvestFragment.fileUrl);
                    }
                    if (!TextUtils.isEmpty(FindInvestFragment.other)){
                        baseParams.addParams("note",FindInvestFragment.other);
                    }
                }else if (nameVal.getId().equals("5")){
                    baseParams.addParams("ip_coop_type",getStringFromList(FindIPFragment.ipcoottype_sel));
                    baseParams.addParams("ip_cat",getStringFromList(FindIPFragment.ipcat_sel));
                    baseParams.addParams("ip_style",getStringFromList(FindIPFragment.ipstyle_sel));
                    if (!TextUtils.isEmpty(FindIPFragment.other)){
                        baseParams.addParams("note",FindIPFragment.other);
                    }

                }else if (nameVal.getId().equals("6")){
                    baseParams.addParams("region",getStringFromList(FindIssuesFragment.issueregions_sel));
                    baseParams.addParams("issue_game",getStringFromList(FindIssuesFragment.issuegames_sel));
                    baseParams.addParams("issue_cat",getStringFromList(FindIssuesFragment.inssuecats_sel));
                    if (!TextUtils.isEmpty(FindIssuesFragment.fileUrl)){
                        baseParams.addParams("appendix",FindIssuesFragment.fileUrl);
                    }
                    if (!TextUtils.isEmpty(FindIssuesFragment.otherStr)){
                        baseParams.addParams("note",FindIssuesFragment.otherStr);
                    }
                }
                break;
            case "4":
                // "1": "找团队","2": "找IP"
                if (nameVal.getId().equals("1")){
                    baseParams.addParams("team_type",getStringFromList(FindTeam.teamtype_sel));
                    baseParams.addParams("starge",getStringFromList(FindTeam.stage_sel));
                    if (!TextUtils.isEmpty(FindTeam.otherStr)){
                        baseParams.addParams("note",FindTeam.otherStr);
                    }

                }else if(nameVal.getId().equals("2")){
                    baseParams.addParams("ip_coop_type",getStringFromList(FindIPFragment.ipcoottype_sel));
                    baseParams.addParams("ip_cat",getStringFromList(FindIPFragment.ipcat_sel));
                    baseParams.addParams("ip_style",getStringFromList(FindIPFragment.ipstyle_sel));
                    if (!TextUtils.isEmpty(FindIPFragment.other)){
                        baseParams.addParams("note",FindIPFragment.other);
                    }
                }
                break;
            case "5":
                //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                if (nameVal.getId().equals("1")){
                    baseParams.addParams("coop_ip",getStringFromList(IPAuthorToCooperation.ip_sel));
                    baseParams.addParams("ip_uthority",getStringFromList(IPAuthorToCooperation.ipauthtypes_sel));
                    if (!TextUtils.isEmpty(IPAuthorToCooperation.otherStr)){
                        baseParams.addParams("note",IPAuthorToCooperation.otherStr);
                    }
                }else if(nameVal.getId().equals("2")){
                    baseParams.addParams("coop_ip",getStringFromList(IPUnite.ip_sel));
                    baseParams.addParams("ip_coop_cat",getStringFromList(IPUnite.ipcoopcats_sel));
                    if (!TextUtils.isEmpty(IPUnite.otherStr)){
                        baseParams.addParams("note",IPUnite.otherStr);
                    }
                }else if(nameVal.getId().equals("3")){
                    baseParams.addParams("coop_ip",getStringFromList(IPFindTeam.ip_sel));
                    baseParams.addParams("ip_develop_cat",getStringFromList(IPFindTeam.ipdevelopcats_sel));
                    if (!TextUtils.isEmpty(IPFindTeam.otherStr)){
                        baseParams.addParams("note",IPFindTeam.otherStr);
                    }
                }else if(nameVal.getId().equals("4")){
                    baseParams.addParams("coop_ip",getStringFromList(IPFindRecompose.ip_sel));
                    baseParams.addParams("game_type",getStringFromList(IPFindRecompose.gametypes_sel));
                    baseParams.addParams("game_stage",getStringFromList(IPFindRecompose.cusgamestages_sel));

                    if (!TextUtils.isEmpty(IPFindRecompose.otherStr)){
                        baseParams.addParams("note",IPFindRecompose.otherStr);
                    }
                }
                break;
            case "6":
                //   "2": "找游戏","4": "找投资","5": "找IP"
                if (nameVal.getId().equals("2")){
                    baseParams.addParams("game_grade",getStringFromList(FindGameFragment.gamegrade_select));
                    baseParams.addParams("game_type",getStringFromList(FindGameFragment.gametype_select));
                    baseParams.addParams("game_stage",getStringFromList(FindGameFragment.gamestage_select));
                    baseParams.addParams("region",getStringFromList(FindGameFragment.region_select));
                    baseParams.addParams("issue_cat",getStringFromList(FindGameFragment.issuecat_select));
                    if (!TextUtils.isEmpty(FindGameFragment.otherStr)){
                        baseParams.addParams("note",FindGameFragment.otherStr);
                    }
                }else if(nameVal.getId().equals("4")){
                    baseParams.addParams("starge",FindInvestFragment.stageStr);
                    baseParams.addParams("money",FindInvestFragment.neednum);
                    baseParams.addParams("stock_start",FindInvestFragment.minshare);
                    baseParams.addParams("stock_end",FindInvestFragment.maxshare);
                    if (!TextUtils.isEmpty(FindInvestFragment.fileUrl)){
                        baseParams.addParams("appendix",FindInvestFragment.fileUrl);
                    }

                    if (!TextUtils.isEmpty(FindInvestFragment.other)){
                        baseParams.addParams("note",FindInvestFragment.other);
                    }
                }else if(nameVal.getId().equals("5")){
                    baseParams.addParams("ip_coop_type",getStringFromList(FindIPFragment.ipcoottype_sel));
                    baseParams.addParams("ip_cat",getStringFromList(FindIPFragment.ipcat_sel));
                    baseParams.addParams("ip_style",getStringFromList(FindIPFragment.ipstyle_sel));
                    if (!TextUtils.isEmpty(FindIPFragment.other)){
                        baseParams.addParams("note",FindIPFragment.other);
                    }
                }
                break;

        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("需求定制 back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        Intent intent=new Intent(CustomizedActivity.this,CustomizedListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        finish();
                        ToastEvent toastEvent=new ToastEvent();
                        toastEvent.setId("0");
                        toastEvent.setMessage("需求定制提交成功");
                        EventBus.getDefault().post(toastEvent);
                    }else {
                        String mess=jsonObject.getString("msg");
                        CusToast.showToast(CustomizedActivity.this,mess,Toast.LENGTH_SHORT);
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

    private boolean checkParams() {
        RequirementInfo requirementInfo=map.get(ident_select);
        NameVal nameVal= requirementInfo.getFindservice().get(position_select);
        MyLog.i("find =="+nameVal.getVal());

        switch (requirementInfo.getIdent_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (nameVal.getId().equals("4")){
                    if (TextUtils.isEmpty(FindInvestFragment.stageStr)
                            ||TextUtils.isEmpty(FindInvestFragment.maxshare)
                            ||TextUtils.isEmpty(FindInvestFragment.minshare)
                            ||TextUtils.isEmpty(FindInvestFragment.neednum)){
                        CusToast.showToast(CustomizedActivity.this,"资料不完整",Toast.LENGTH_SHORT);
                        return false;
                    }
                }else if (nameVal.getId().equals("5")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                    if (FindIPFragment.ipstyle_sel.size()==0
                            ||FindIPFragment.ipcat_sel.size()==0
                            ||FindIPFragment.ipcoottype_sel.size()==0){
                        CusToast.showToast(CustomizedActivity.this,"资料不完整",Toast.LENGTH_SHORT);
                        return false;
                    }
                }else if (nameVal.getId().equals("6")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIssuesFragment()).commit();
                    if (FindIssuesFragment.issuegames_sel.size()==0
                            ||FindIssuesFragment.issueregions_sel.size()==0
                            ||FindIssuesFragment.inssuecats_sel.size()==0
                            ){
                        return false;
                    }
                }
                break;
            case "4":
                // "1": "找团队","2": "找IP"
                if (nameVal.getId().equals("1")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindTeam()).commit();
                    if (FindTeam.stage_sel.size()==0
                            ||FindTeam.teamtype_sel.size()==0){
                        return false;
                    }

                }else if(nameVal.getId().equals("2")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                    if (FindIPFragment.ipstyle_sel.size()==0
                            ||FindIPFragment.ipcat_sel.size()==0
                            ||FindIPFragment.ipcoottype_sel.size()==0){
                        return false;
                    }
                }
                break;
            case "5":
                //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                if (nameVal.getId().equals("1")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPAuthorToCooperation()).commit();
                    if (IPAuthorToCooperation.ipauthtypes_sel.size()==0
                            ||IPAuthorToCooperation.ip_sel.size()==0){
                        return false;
                    }
                }else if(nameVal.getId().equals("2")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPUnite()).commit();
                    if (IPUnite.ipcoopcats_sel.size()==0
                            ||IPUnite.ip_sel.size()==0){
                        return false;
                    }
                }else if(nameVal.getId().equals("3")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPFindTeam()).commit();
                    if (IPFindTeam.ip_sel.size()==0
                            ||IPFindTeam.ipdevelopcats_sel.size()==0){
                        return false;
                    }
                }else if(nameVal.getId().equals("4")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPFindRecompose()).commit();
                    if (IPFindRecompose.ip_sel.size()==0
                            ||IPFindRecompose.cusgamestages_sel.size()==0
                            ||IPFindRecompose.gametypes_sel.size()==0){
                        return false;
                    }
                }
                break;
            case "6":
                //   "2": "找游戏","4": "找投资","5": "找IP"
                if (nameVal.getId().equals("2")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindGameFragment()).commit();
                    if (FindGameFragment.issuecat_select.size()==0
                            ||FindGameFragment.region_select.size()==0
                            ||FindGameFragment.gamegrade_select.size()==0
                            ||FindGameFragment.gamestage_select.size()==0
                            ||FindGameFragment.gametype_select.size()==0){
                        return false;
                    }
                }else if(nameVal.getId().equals("4")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindInvestFragment()).commit();
                    if (TextUtils.isEmpty(FindInvestFragment.stageStr)
                            ||TextUtils.isEmpty(FindInvestFragment.maxshare)
                            ||TextUtils.isEmpty(FindInvestFragment.minshare)
                            ||TextUtils.isEmpty(FindInvestFragment.neednum)){
                        return false;
                    }
                }else if(nameVal.getId().equals("5")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                    if (FindIPFragment.ipstyle_sel.size()==0
                            ||FindIPFragment.ipcat_sel.size()==0
                            ||FindIPFragment.ipcoottype_sel.size()==0){
                        return false;
                    }
                }
                break;

        }
        String phone_new=phoneEdit.getText().toString();
        if (!phone_new.equals(requirementInfo.getCompany_phone())){
            if (TextUtils.isEmpty(pa)||TextUtils.isEmpty(codeEdit.getText().toString())){
                return false;
            }
        }




        return true;
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("customize/customize");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("requirement back=="+result);
                jsonparser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (map.size()==0){
                    setContentView(R.layout.layout_noident);
                    findViewById(R.id.go_ident).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(CustomizedActivity.this,IdentificationActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });

                    back= (ImageView) findViewById(R.id.back_savebar);
                    title= (TextView) findViewById(R.id.textView_titlebar_save);
                    save= (ImageView) findViewById(R.id.help_savebar);

                    title.setText("需求定制");

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(CustomizedActivity.this,ServiceMoneyInfoActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });
                }else {
                    initPopList();

                    initView();
                }

            }
        });
    }
    private void initPopList() {
        BaseParams baseParams=new BaseParams("api/selecttype");
        baseParams.addParams("type","all");
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("popdata=="+result);
                listjsonparser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initFancyCoverFlow();

            }
        });
    }

    private void jsonparser(String result) {
        //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONArray userIdentity=data.getJSONArray("userIdentity");
                map=new TreeMap<>();
                for (int i=0;i<userIdentity.length();i++){
                    JSONObject ident=userIdentity.getJSONObject(i);
                    RequirementInfo info=new RequirementInfo();

                    String ident_cat=ident.getString("identity_cat");
                    info.setIdent_cat(ident_cat);
                    switch (ident_cat){
                        case "2":
                            info.setIdent_name("开发者");
                            break;
                        case "3":
                            info.setIdent_name("外包");

                            break;
                        case "4":
                            info.setIdent_name("投资人");

                            break;
                        case "5":
                            info.setIdent_name("IP方");

                            break;
                        case "6":
                            info.setIdent_name("发行方");

                            break;
                    }
                    JSONObject findservices=ident.getJSONObject("find_services");
                    Iterator<String> iterator=findservices.keys();
                    List<NameVal> nameVals=new ArrayList<>();
                    while (iterator.hasNext()){
                        String key=iterator.next();
                        String value=findservices.getString(key);
                        NameVal nameVal=new NameVal();
                        nameVal.setId(key);
                        nameVal.setVal(value);
                        nameVals.add(nameVal);
                    }
                    info.setFindservice(nameVals);
                    info.setApply(ident.getString("apply"));
                    info.setCompany_phone(ident.getString("company_phone"));
                    map.put(i,info);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void listjsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                cusmap=new HashMap<>();
                JSONObject selectList=data.getJSONObject("selectList");
                Iterator<String> iterator= selectList.keys();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    JSONArray array=selectList.getJSONArray(key);
                    List<NameVal> nameValList=new ArrayList<>();
                    for (int i=0;i<array.length();i++){

                        JSONObject nameJson=array.getJSONObject(i);
                        NameVal val=new NameVal();
                        val.setId(nameJson.getString("id"));
                        if (nameJson.getString("id").equals("0")) {
                            continue;

                        }else {
                            if (nameJson.has("name")) {
                                val.setVal(nameJson.getString("name"));
                            } else if (nameJson.has("val")) {
                                val.setVal(nameJson.getString("val"));
                            }else if (nameJson.has("game_name")){
                                val.setVal(nameJson.getString("game_name"));
                            }else if (nameJson.has("ip_name")){
                                val.setVal(nameJson.getString("ip_name"));
                            }
                        }
                        nameValList.add(val);
                    }
                    MyLog.i("key==="+key);
                    cusmap.put(key,nameValList);
                }
                customizedData.setMap(cusmap);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initFancyCoverFlow() {
        if (map==null){
            MyLog.i("map null");
            return;
        }
        if (cusmap==null){
            MyLog.i("cusmap null");
            return;
        }
        if (map.size()==0){
            showDialog("请先认证身份");
        }
        fancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter(this,map.size()));
        fancyCoverFlow.setUnselectedAlpha(1.0f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.3f);
        fancyCoverFlow.setSpacing(50);
        fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setScaleDownGravity(0.2f);
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);


        fancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("fancyCoverFlow selected "+position);
                ident_select=position;

                chanegView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void chanegView(int position) {
        RequirementInfo requirementInfo=map.get(position);
        identcat_text.setText(requirementInfo.getIdent_name());

        textViewList=new ArrayList<>();
        for (int i=0;i<findLinear.getChildCount();i++){
            TextView textView= (TextView) findLinear.getChildAt(i);
            textViewList.add(textView);
        }
        if (requirementInfo.getFindservice().size()==3){
            TextView textView1=textViewList.get(1);
            textView1.setVisibility(View.VISIBLE);
            TextView textView=textViewList.get(2);
            textView.setVisibility(View.GONE);
            textViewList.remove(textView);
        }else if (requirementInfo.getFindservice().size()==4){
            TextView textView=textViewList.get(2);
            textView.setVisibility(View.VISIBLE);
            TextView textView1=textViewList.get(1);
            textView1.setVisibility(View.VISIBLE);
        }else if(requirementInfo.getFindservice().size()==2){
            TextView textView=textViewList.get(2);
            textView.setVisibility(View.GONE);

            TextView textView1=textViewList.get(1);
            textView1.setVisibility(View.GONE);

            textViewList.remove(textView);
            textViewList.remove(textView1);
        }
        setTextViewSelect(0);
        changeFragment(0);
        List<NameVal> nameValList=requirementInfo.getFindservice();
        for (int i=0;i<nameValList.size();i++){
            TextView textView=textViewList.get(i);
            textView.setText(nameValList.get(i).getVal());
            final int posi=i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTextViewSelect(posi);
                    changeFragment(posi);
                }
            });
        }

        usernameEdit.setText(requirementInfo.getApply());
        phoneEdit.setEnabled(false);
        phoneEdit.setText(requirementInfo.getCompany_phone());
        getcode.setText("更换");
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEdit.setVisibility(View.VISIBLE);
                phoneEdit.setEnabled(true);
                getcode.setText("验证码");
                getcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeCode();
                    }
                });
            }
        });
    }

    private void changeFragment(int posi) {
        MyLog.i("ident select=="+ident_select);
        position_select=posi;
        RequirementInfo requirementInfo=map.get(ident_select);
        NameVal nameVal= requirementInfo.getFindservice().get(posi);
        MyLog.i("find =="+nameVal.getVal());

        switch (requirementInfo.getIdent_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (nameVal.getId().equals("4")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindInvestFragment()).commit();
                }else if (nameVal.getId().equals("5")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                }else if (nameVal.getId().equals("6")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIssuesFragment()).commit();
                }
                break;
            case "4":
                // "1": "找团队","2": "找IP"
                if (nameVal.getId().equals("1")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindTeam()).commit();

                }else if(nameVal.getId().equals("2")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();

                }
                break;
            case "5":
                //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                if (nameVal.getId().equals("1")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPAuthorToCooperation()).commit();
                }else if(nameVal.getId().equals("2")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPUnite()).commit();
                }else if(nameVal.getId().equals("3")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPFindTeam()).commit();
                }else if(nameVal.getId().equals("4")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPFindRecompose()).commit();
                }
                break;
            case "6":
                //   "2": "找游戏","4": "找投资","5": "找IP"
                if (nameVal.getId().equals("2")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindGameFragment()).commit();
                }else if(nameVal.getId().equals("4")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindInvestFragment()).commit();
                }else if(nameVal.getId().equals("5")){
                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                }
                break;

        }

    }

    private void setTextViewSelect(int posi) {
        for (int i=0;i<textViewList.size();i++){
            TextView textView=textViewList.get(i);
            textView.setSelected(false);
            textView.setTextColor(Color.rgb(255,156,0));
        }
        TextView textView=textViewList.get(posi);
        textView.setSelected(true);
        textView.setTextColor(Color.WHITE);
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
                        CusToast.showToast(CustomizedActivity.this,mes, Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getcode.setClickable(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void changeCode() {
        phonenum=phoneEdit.getText().toString();
        if (phonenum.length()<11){
//            showwarn("请输入正确的电话号码");
            CusToast.showToast(this,"请输入正确的电话号码",Toast.LENGTH_SHORT);
            return;
        }
        getcode.setClickable(false);
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
                    handler.sendEmptyMessage(0);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }
    int time=60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    time--;
                    getcode.setText(time+"秒");
                    break;
                case 1:
                    time=60;
                    getcode.setText("重新获取");
                    getcode.setClickable(true);
                    break;
            }
        }
    };

    public void showDialog(String message){
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText(message);
        clear.setText("我知道了");

//        clear.setTextColor(Color.rgb(255,157,0));
        dialog.show();
        channel.setVisibility(View.GONE);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CustomizedActivity.this.finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });
    }
}
