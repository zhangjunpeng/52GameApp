package com.view.Identification;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.Config;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentificaSubActivity extends BaseActivity implements View.OnClickListener {




    private EditText companyName_edit;
    private ImageView icon;
    private ImageView upphoto;
    private TextView chickToUp;
    private TextView companySize_text;
    private TextView area_text;
    private EditText companyInfo_edit;
    private EditText personName_edit;
    private EditText phoneNum_edit;
    private EditText phoneCode_edit;
    private TextView getCode_text;
    private TextView pcc_text;
    private EditText address_edit;
    private TextView submit;

    private LinearLayout areaLayout;
    private LinearLayout companyInfoLayout;


    private String type="company";
    private Dialog dialog;


    private ImageView back;
    private TextView title;
    private TextView save;

    //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
    private String cattype="2";
    private int stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_sub);
        setImmerseLayout(findViewById(R.id.titlebar_iden_sub));

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);

        initData();
//        addfragment();
        cattype=getIntent().getStringExtra("cattype");
        type=getIntent().getStringExtra("type");

        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":
                title.setText("认证开发者");
                break;
            case "3":
                title.setText("认证外包");

                break;
            case "4":
                title.setText("认证投资人");

                break;
            case "5":
                title.setText("认证IP方");

                break;
            case "6":
                title.setText("认证发行方");

                break;
        }

        try {
            stage=getIntent().getIntExtra("stage",0);
        }catch (Exception e){

        }
        if (stage==2){
            initData2();
        }


//        companyName_edit= (EditText) findViewById(R.id.edit_companyname);
//        icon= (ImageView) findViewById(R.id.icon_iden_sub);
//        upphoto= (ImageView) findViewById(R.id.upimage_icon);
//        chickToUp= (TextView) findViewById(R.id.chicktoup_iden_sub);
//        companySize_text= (TextView) findViewById(R.id.scale_ident_sub);
//        area_text= (TextView) findViewById(R.id.area_text_iden_sub);
//        companyInfo_edit= (EditText) findViewById(R.id.edit_companyinfo);
//
//        personName_edit= (EditText) findViewById(R.id.edit_username);
//        phoneNum_edit= (EditText) findViewById(R.id.edit_phone);
//        getCode_text= (TextView) findViewById(R.id.getcode_iden_sub);
//        phoneCode_edit= (EditText) findViewById(R.id.edit_code);
//        pcc_text= (TextView) findViewById(R.id.address_text);
//        address_edit= (EditText) findViewById(R.id.address_edit);
//        submit= (TextView) findViewById(R.id.submit_iden_sub);
//
//        areaLayout= (LinearLayout) findViewById(R.id.layout_area);
//        companyInfoLayout= (LinearLayout) findViewById(R.id.layout_companyinfo);
//
//        type=getIntent().getStringExtra("type");
//
//        if (type.equals("5")){
//            areaLayout.setVisibility(View.GONE);
//            companyInfoLayout.setVisibility(View.GONE);
//            findViewById(R.id.line_needgone1).setVisibility(View.GONE);
//            findViewById(R.id.line_needgone2).setVisibility(View.GONE);
//
//        }else if (!type.equals("2")){
//            FragmentManager fm=getSupportFragmentManager();
//            Fragment fragment = null;
//            Bundle bundle=new Bundle();
//            switch (type){
//                case "3":
//                    fragment=new OSIdenFragment();
//                    break;
//                case "4":
//                    fragment=new InvIdenFragment();
//                    break;
//                case "6":
//                    fragment=new IssIdenFragment();
//
//                    break;
//            }
//            fm.beginTransaction().replace(R.id.contianer_identisub,fragment).commit();
//        }
//
//        companySize_text.setOnClickListener(this);
//        area_text.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });
    }
    private void initData() {
        BaseParams params=new BaseParams("identity/identityselect");
        params.addParams("type","all");
        params.addParams("token", MyAccount.getInstance().getToken());
        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("列表 result==="+result);
                listdataParser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("cattype=="+cattype);
                addfragment();
            }
        });
    }

    private void addfragment() {
        if (stage==2){
            if (subinfo==null){
                MyLog.i("sub info null");
                return;
            }
        }

        IdentSubFirstFragment fragment=new IdentSubFirstFragment();
        Bundle bundle=new Bundle();
        bundle.putString("cattype",cattype);
        bundle.putString("type",type);
        bundle.putInt("stage",stage);
        if (stage==2){
            bundle.putString("note",getIntent().getStringExtra("note"));
        }
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contianer_ident_sub,fragment).commit();
    }

    private void listdataParser(String result) {
        IdentificationConfig config=IdentificationConfig.getInstance();

        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){

                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject selectList=data.getJSONObject("selectList");
                JSONArray areaArr=selectList.getJSONArray("area");
                List<NameVal> areaList=new ArrayList<>();
                for (int i=1;i<areaArr.length();i++){
//                    Map<String,String> map=new HashMap<>();
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("name"));
//                    map.put("id",object.getString("id"));
//                    map.put("name",object.getString("name"));
                    if (config==null){
                        MyLog.i("config null");
                    }
                    areaList.add(nameVal);
                }


                JSONArray sizeArr=selectList.getJSONArray("companysize");
                List<NameVal> sizeList=new ArrayList<>();

                for (int i=1;i<sizeArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=sizeArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    sizeList.add(nameVal);
                }

                JSONArray osresArr=selectList.getJSONArray("outsorcestyle");
                List<NameVal> osresList=new ArrayList<>();

                for (int i=1;i<osresArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=osresArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    osresList.add(nameVal);
                }

                JSONArray copstageArr=selectList.getJSONArray("coompstage");
                List<NameVal> stageList=new ArrayList<>();

                for (int i=1;i<copstageArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=copstageArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    stageList.add(nameVal);
                }

                JSONArray coopcatArr=selectList.getJSONArray("coopcat");
                List<NameVal> isscatList=new ArrayList<>();

                for (int i=1;i<coopcatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=coopcatArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    isscatList.add(nameVal);
                }

                JSONArray businecatArr=selectList.getJSONArray("businecat");
                List<NameVal> busiscatList=new ArrayList<>();


                for (int i=1;i<businecatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=businecatArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    busiscatList.add(nameVal);
                }

                JSONArray investcatArr=selectList.getJSONArray("investcat");
                List<NameVal> invescatList=new ArrayList<>();


                for (int i=1;i<investcatArr.length();i++){
                    if (i==0){
                        continue;
                    }
                    NameVal nameVal=new NameVal();
                    JSONObject object=investcatArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    invescatList.add(nameVal);
                }
                config.setAreaList(areaList);
                config.setStageList(stageList);
                config.setSizeList(sizeList);
                config.setOsresList(osresList);
                config.setIsscatList(isscatList);
                config.setInvescatList(invescatList);
                config.setBuscatList(busiscatList);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData2() {
        BaseParams baseParams=new BaseParams("identity/viewidentity");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("identity_cat",cattype);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("checkinfo back=="+result);
                parser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                addfragment();
            }
        });
    }


    public static IdentSubInfo subinfo;
    private void parser(String result) {
        try {

            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){

                subinfo=new IdentSubInfo();


                JSONObject data=jsonObject.getJSONObject("data");
                subinfo.setPersonal(data.getString("personal"));
                subinfo.setChecked(data.getString("checked"));
                NameVal roleName=new NameVal();
                JSONObject roleVal=data.getJSONObject("roleName");
                roleName.setId(roleVal.getString("id"));
                roleName.setVal(roleVal.getString("val"));
                subinfo.setRoleName(roleName);
                JSONObject info=data.getJSONObject("info");
                subinfo.setCompany_name(info.getString("company_name"));
                subinfo.setApply(info.getString("apply"));
                subinfo.setLogo(info.getString("logo"));
//                company_scale=info.getString("company_scale");
                NameVal companysize=new NameVal();
                companysize.setId(info.getJSONObject("size").getString("company_scale"));
                companysize.setVal(info.getJSONObject("size").getString("company_scale_name"));

                subinfo.setCompany_scale(companysize);

                subinfo.setCompany_phone(info.getString("company_phone"));
                subinfo.setAddress(info.getString("addr"));
                subinfo.setCompany_info(info.getString("company_intro"));
                subinfo.setIdnum(info.getString("business_id"));
                subinfo.setCompany_photo(info.getString("business_pic"));

                subinfo.setCoop_cat(info.getString("coop_cat"));

                NameVal invescat=new NameVal();
                invescat.setId(info.getString("invest_cat"));
                invescat.setVal(info.getJSONObject("invest").getString("invest_cat_name"));
                subinfo.setInvest_cat(invescat);

                subinfo.setInvest_stage(info.getString("invest_stage"));
                subinfo.setBusiness_cat(info.getString("business_cat"));
                subinfo.setOutsource_cat(info.getString("outsource_cat"));

                NameVal area=new NameVal();
                area.setId(info.getString("area_id"));
                area.setVal(info.getJSONObject("region").getString("area_name"));
                subinfo.setArea(area);

                NameVal province=new NameVal();
                province.setId(info.getString("province"));
                province.setVal(info.getString("province_name"));
                subinfo.setProvince(province);

                NameVal city=new NameVal();
                city.setId(info.getString("city"));
                city.setVal(info.getString("city_name"));
                subinfo.setCity(city);

                NameVal country=new NameVal();
                country.setId(info.getString("county"));
                country.setVal(info.getString("county_name"));
                subinfo.setCountry(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showSelectDiaolog(final TextView textView, final List<NameVal> dataList){

        dialog=new Dialog(this);
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
                    convertView=new RelativeLayout(IdentificaSubActivity.this);
                    RelativeLayout layout= (RelativeLayout) convertView;
                    layout.setGravity(Gravity.CENTER);
                    textView=new TextView(IdentificaSubActivity.this);
                    textView.setTextSize(15);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.rgb(124,124,124));
                    textView.setBackgroundColor(Color.WHITE);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (52*Config.density));
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
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,IdentificationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
        finish();
    }
}
