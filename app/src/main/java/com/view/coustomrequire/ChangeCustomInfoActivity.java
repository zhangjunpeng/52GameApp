package com.view.coustomrequire;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.Event.ToastEvent;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;

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
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

public class ChangeCustomInfoActivity extends AppCompatActivity {

    private ItemInfoCustomList itemInfo;

    private FragmentManager fm;

    private String name;
    private String phonenum;
    private String code;
    private String pa;

    private EditText usernameEdit;
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getcode;
    private TextView submit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private Map<String,List<NameVal>> cusmap;

    private ImageView back;
    private TextView title;
    private ImageView save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customized);

        Bundle bundle=getIntent().getBundleExtra("data");
        if (bundle!=null){
            itemInfo= (ItemInfoCustomList) bundle.getSerializable("info");
        }
        fm=getSupportFragmentManager();


        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (ImageView) findViewById(R.id.help_savebar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChangeCustomInfoActivity.this,ServiceMoneyInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        title.setText("需求定制");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

        findViewById(R.id.head_customized).setVisibility(View.GONE);

        usernameEdit= (EditText) findViewById(R.id.edit_username);
        phoneEdit= (EditText) findViewById(R.id.edit_phone);
        codeEdit= (EditText) findViewById(R.id.edit_code);
        getcode= (TextView) findViewById(R.id.getcode);
        submit= (TextView) findViewById(R.id.submit);

        submit.setText("更改");

        if (customizedData.getMap()==null){
            initPopList();
        }else {
            initFragment();
        }

        if(itemInfo.getChecked().equals("1")||itemInfo.getChecked().equals("2")){
            submit.setVisibility(View.GONE);
        }


        initView();
    }

    private void initView() {
        name=itemInfo.getApply();
        phonenum=itemInfo.getPhone();

        usernameEdit.setText(name);
        phoneEdit.setEnabled(false);
        phoneEdit.setText(itemInfo.getPhone());

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
                        CusToast.showToast(ChangeCustomInfoActivity.this,"请输入联系人信息",Toast.LENGTH_SHORT);
                        return;
                    }
                    if (phonenum.length()!=11){
                        CusToast.showToast(ChangeCustomInfoActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT);
                        return;
                    }
                    startSubmit();

                }else {
                    CusToast.showToast(ChangeCustomInfoActivity.this,"资料不完整",Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void startSubmit() {
        NameVal service_cat=itemInfo.getServive_cat();

        BaseParams baseParams=new BaseParams("customize/savecustomize");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("identity_cat",itemInfo.getIdentity_cat());
        baseParams.addParams("service_cat",service_cat.getId());
        baseParams.addParams("id",itemInfo.getId());
        baseParams.addParams("apply",name);
        baseParams.addParams("phone",phonenum);

        if (!TextUtils.isEmpty(pa)){
            baseParams.addParams("pa",pa);
            baseParams.addParams("code",code);
        }

        switch (itemInfo.getIdentity_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (service_cat.getId().equals("4")){
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
                }else if (service_cat.getId().equals("5")){
                    baseParams.addParams("ip_coop_type",getStringFromList(FindIPFragment.ipcoottype_sel));
                    baseParams.addParams("ip_cat",getStringFromList(FindIPFragment.ipcat_sel));
                    baseParams.addParams("ip_style",getStringFromList(FindIPFragment.ipstyle_sel));
                    if (!TextUtils.isEmpty(FindIPFragment.other)){
                        baseParams.addParams("note",FindIPFragment.other);
                    }

                }else if (service_cat.getId().equals("6")){
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
                if (service_cat.getId().equals("1")){
                    baseParams.addParams("team_type",getStringFromList(FindTeam.teamtype_sel));
                    baseParams.addParams("starge",getStringFromList(FindTeam.stage_sel));
                    if (!TextUtils.isEmpty(FindTeam.otherStr)){
                        baseParams.addParams("note",FindTeam.otherStr);
                    }

                }else if(service_cat.getId().equals("2")){
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
                if (service_cat.getId().equals("1")){
                    baseParams.addParams("coop_ip",getStringFromList(IPAuthorToCooperation.ip_sel));
                    baseParams.addParams("ip_uthority",getStringFromList(IPAuthorToCooperation.ipauthtypes_sel));
                    if (!TextUtils.isEmpty(IPAuthorToCooperation.otherStr)){
                        baseParams.addParams("note",IPAuthorToCooperation.otherStr);
                    }
                }else if(service_cat.getId().equals("2")){
                    baseParams.addParams("coop_ip",getStringFromList(IPUnite.ip_sel));
                    baseParams.addParams("ip_coop_cat",getStringFromList(IPUnite.ipcoopcats_sel));
                    if (!TextUtils.isEmpty(IPUnite.otherStr)){
                        baseParams.addParams("note",IPUnite.otherStr);
                    }
                }else if(service_cat.getId().equals("3")){
                    baseParams.addParams("coop_ip",getStringFromList(IPFindTeam.ip_sel));
                    baseParams.addParams("ip_develop_cat",getStringFromList(IPFindTeam.ipdevelopcats_sel));
                    if (!TextUtils.isEmpty(IPFindTeam.otherStr)){
                        baseParams.addParams("note",IPFindTeam.otherStr);
                    }
                }else if(service_cat.getId().equals("4")){
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
                if (service_cat.getId().equals("2")){
                    baseParams.addParams("game_grade",getStringFromList(FindGameFragment.gamegrade_select));
                    baseParams.addParams("game_type",getStringFromList(FindGameFragment.gametype_select));
                    baseParams.addParams("game_stage",getStringFromList(FindGameFragment.gamestage_select));
                    baseParams.addParams("region",getStringFromList(FindGameFragment.region_select));
                    baseParams.addParams("issue_cat",getStringFromList(FindGameFragment.issuecat_select));
                    if (!TextUtils.isEmpty(FindGameFragment.otherStr)){
                        baseParams.addParams("note",FindGameFragment.otherStr);
                    }
                }else if(service_cat.getId().equals("4")){
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
                }else if(service_cat.getId().equals("5")){
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
                        Intent intent=new Intent(ChangeCustomInfoActivity.this,CustomizedListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                        finish();
                        ToastEvent toastEvent=new ToastEvent();
                        toastEvent.setId("0");
                        toastEvent.setMessage("需求定制提交成功");
                        EventBus.getDefault().post(toastEvent);
                    }else {
                        String mess=jsonObject.getString("msg");
                        CusToast.showToast(ChangeCustomInfoActivity.this,mess,Toast.LENGTH_SHORT);
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

    private boolean checkParams() {
        NameVal service_cat= itemInfo.getServive_cat();
//        MyLog.i("find =="+nameVal.getVal());

        switch (itemInfo.getIdentity_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (service_cat.getId().equals("4")){
                    if (TextUtils.isEmpty(FindInvestFragment.stageStr)
                            ||TextUtils.isEmpty(FindInvestFragment.maxshare)
                            ||TextUtils.isEmpty(FindInvestFragment.minshare)
                            ||TextUtils.isEmpty(FindInvestFragment.neednum)){
                        return false;
                    }
                }else if (service_cat.getId().equals("5")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIPFragment()).commit();
                    if (FindIPFragment.ipstyle_sel.size()==0
                            ||FindIPFragment.ipcat_sel.size()==0
                            ||FindIPFragment.ipcoottype_sel.size()==0){
                        return false;
                    }
                }else if (service_cat.getId().equals("6")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindIssuesFragment()).commit();
                    if (FindIssuesFragment.issuegames_sel.size()==0
                            ||FindIssuesFragment.issueregions_sel.size()==0
                            ||FindIssuesFragment.inssuecats_sel.size()==0){
                        return false;
                    }
                }
                break;
            case "4":
                // "1": "找团队","2": "找IP"
                if (service_cat.getId().equals("1")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindTeam()).commit();
                    if (FindTeam.stage_sel.size()==0
                            ||FindTeam.teamtype_sel.size()==0){
                        return false;
                    }

                }else if(service_cat.getId().equals("2")){
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
                if (service_cat.getId().equals("1")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPAuthorToCooperation()).commit();
                    if (IPAuthorToCooperation.ipauthtypes_sel.size()==0
                            ||IPAuthorToCooperation.ip_sel.size()==0){
                        return false;
                    }
                }else if(service_cat.getId().equals("2")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPUnite()).commit();
                    if (IPUnite.ipcoopcats_sel.size()==0
                            ||IPUnite.ip_sel.size()==0){
                        return false;
                    }
                }else if(service_cat.getId().equals("3")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new IPFindTeam()).commit();
                    if (IPFindTeam.ip_sel.size()==0
                            ||IPFindTeam.ipdevelopcats_sel.size()==0){
                        return false;
                    }
                }else if(service_cat.getId().equals("4")){
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
                if (service_cat.getId().equals("2")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindGameFragment()).commit();
                    if (FindGameFragment.issuecat_select.size()==0
                            ||FindGameFragment.region_select.size()==0
                            ||FindGameFragment.gamegrade_select.size()==0
                            ||FindGameFragment.gamestage_select.size()==0
                            ||FindGameFragment.gametype_select.size()==0){
                        return false;
                    }
                }else if(service_cat.getId().equals("4")){
//                    fm.beginTransaction().replace(R.id.contianer_coustomized,new FindInvestFragment()).commit();
                    if (TextUtils.isEmpty(FindInvestFragment.stageStr)
                            ||TextUtils.isEmpty(FindInvestFragment.maxshare)
                            ||TextUtils.isEmpty(FindInvestFragment.minshare)
                            ||TextUtils.isEmpty(FindInvestFragment.neednum)){
                        return false;
                    }
                }else if(service_cat.getId().equals("5")){
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
        if (!phone_new.equals(itemInfo.getPhone())){
            if (TextUtils.isEmpty(pa)||TextUtils.isEmpty(codeEdit.getText().toString())){
                return false;
            }
        }


        return true;
    }

    private void changeCode() {
        phonenum=phoneEdit.getText().toString();
        if (phonenum.length()<11){
//            showwarn("请输入正确的电话号码");
            CusToast.showToast(this,"请输入正确的电话号码", Toast.LENGTH_SHORT);
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
                initFragment();
            }
        });
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
                        CusToast.showToast(ChangeCustomInfoActivity.this,mes, Toast.LENGTH_SHORT);
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

    private void initFragment() {
        NameVal service_cat=itemInfo.getServive_cat();
        Fragment fragment=null;
        switch (itemInfo.getIdentity_cat()){
            //2:开发者 4:投资人 5:IP方 6:发行方
            case "2":
                // "4": "找投资","5": "找IP","6": "找发行"
                if (service_cat.getId().equals("4")){
                    fragment=new FindInvestFragment();
                }else if (service_cat.getId().equals("5")){
                    fragment=new FindIPFragment();
                }else if (service_cat.getId().equals("6")){
                    fragment=new FindIssuesFragment();
                }
                break;
            case "4":
                // "1": "找团队","2": "找IP"
                if (service_cat.getId().equals("1")){
                    fragment=new FindTeam();

                }else if(service_cat.getId().equals("2")){
                    fragment=new FindIPFragment();

                }
                break;
            case "5":
                //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                if (service_cat.getId().equals("1")){
                    fragment=new IPAuthorToCooperation();
                }else if(service_cat.getId().equals("2")){
                    fragment=new IPUnite();
                }else if(service_cat.getId().equals("3")){
                    fragment=new IPFindTeam();
                }else if(service_cat.getId().equals("4")){
                    fragment=new IPFindRecompose();
                }
                break;
            case "6":
                //   "2": "找游戏","4": "找投资","5": "找IP"
                if (service_cat.getId().equals("2")){
                    fragment=new FindGameFragment();
                }else if(service_cat.getId().equals("4")){
                    fragment=new FindInvestFragment();
                }else if(service_cat.getId().equals("5")){
                    fragment=new FindIPFragment();
                }
                break;

        }
        Bundle bundle=new Bundle();
        bundle.putSerializable("info",itemInfo);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.contianer_coustomized,fragment).commit();

    }
}
