package com.view.Identification;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class IdentCheckActivity extends BaseActivity {

    String cattype="2";

    private int stage;

    private LinearLayout areaLayout;
    private LinearLayout resLayout;
    private LinearLayout busiLayout;
    private LinearLayout issCatLayout;
    private LinearLayout invesCatLayout;
    private LinearLayout companyLayout;

    private LinearLayout idnum_layout;
    private LinearLayout companyphoto_layout;

    private ImageView back;
    private TextView title;
    private TextView save;

    private RelativeLayout stage_relat;
    private ImageView stage_image;
    private TextView stage_text;



    private TextView companyname_text;
    private ImageView icon_image;
    private TextView company_size;
    private TextView area_text;
    private TextView investcat_text;
    private LinearLayout linear1_os;
    private LinearLayout linear2_os;
    private LinearLayout linear_busicat;
    private LinearLayout linear_isscat;
    private TextView companyinfo_text;
    private TextView username_text;
    private TextView phonenum_text;
    private TextView address_text;
    private TextView idnum_text;
    private ImageView company_photo_image;
    private IdentificationConfig config=IdentificationConfig.getInstance();
    private ArrayList<TextView> resText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cattype=getIntent().getStringExtra("cattype");
        stage=getIntent().getIntExtra("stage",2);

        setContentView(R.layout.activity_identinfo_check);

        areaLayout= (LinearLayout) findViewById(R.id.layout_area);
        resLayout= (LinearLayout) findViewById(R.id.layout_wbrescat);
        busiLayout= (LinearLayout) findViewById(R.id.layout_buscat);
        issCatLayout= (LinearLayout) findViewById(R.id.layout_issscat);
        invesCatLayout= (LinearLayout) findViewById(R.id.layout_invescat);
        companyLayout= (LinearLayout) findViewById(R.id.layout_companyinfo);
        idnum_layout= (LinearLayout) findViewById(R.id.layout_idnum);
        companyphoto_layout= (LinearLayout) findViewById(R.id.layout_companyphoto);

        companyname_text= (TextView) findViewById(R.id.edit_companyname);
        icon_image= (ImageView) findViewById(R.id.icon_iden_sub);
        company_size= (TextView) findViewById(R.id.scale_ident_sub);
        area_text= (TextView) findViewById(R.id.area_text_iden_sub);
        investcat_text= (TextView) findViewById(R.id.invescat_text_iden_sub);
        linear1_os= (LinearLayout) findViewById(R.id.linear1_os);
        linear2_os= (LinearLayout) findViewById(R.id.linear2_os);
        linear_busicat= (LinearLayout) findViewById(R.id.linear_busicat);
        linear_isscat= (LinearLayout) findViewById(R.id.linear_issicat);
        companyinfo_text= (TextView) findViewById(R.id.edit_companyinfo);
        username_text= (TextView) findViewById(R.id.text_username);
        phonenum_text= (TextView) findViewById(R.id.phonenum_text);
        address_text= (TextView) findViewById(R.id.address_edit);
        idnum_text= (TextView) findViewById(R.id.idnum_text);
        company_photo_image= (ImageView) findViewById(R.id.companyphoto_ident);

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        stage_relat= (RelativeLayout) findViewById(R.id.stage_relat);
        stage_image= (ImageView) findViewById(R.id.stage_image);
        stage_text= (TextView) findViewById(R.id.stage_text);

        save.setVisibility(View.INVISIBLE);
        title.setText("查看认证资料");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

        initLayout();


        initData();
    }

    private void initLayout() {
        String catName="";
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                catName="开发者";
                break;
            case "3":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                catName="外包";
                break;
            case "4":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                catName="投资人";

                break;
            case "5":
                areaLayout.setVisibility(View.GONE);
                companyLayout.setVisibility(View.GONE);
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                catName="IP方";

                break;
            case "6":
                resLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                catName="发行方";

                break;
        }

        switch (stage){
            case 0:
                stage_relat.setBackgroundResource(R.drawable.bg_rz_fail);
                stage_image.setImageResource(R.drawable.rz_ing_lu);
                stage_text.setText("您的"+catName+"身份正在审核中！");
                break;
            case 1:
            case 3:
                stage_relat.setBackgroundResource(R.drawable.bg_rz_success);
                stage_image.setImageResource(R.drawable.rz_success_lu);
                stage_text.setText("您的"+catName+"身份已通过认证！");
                break;
        }
    }

    public void initReslayout(List<NameVal> dataList,String data){
        resText=new ArrayList<>();
        for (int i=0;i<linear1_os.getChildCount();i++){
            TextView textView= (TextView) linear1_os.getChildAt(i);
            resText.add(textView);
        }
        for (int i=0;i<linear2_os.getChildCount();i++){
            TextView textView= (TextView) linear2_os.getChildAt(i);
            resText.add(textView);
        }

        for (int i=0;i<resText.size();i++){
            TextView textView=resText.get(i);
            NameVal nameVal=null;
            if (i>=dataList.size()){
                textView.setVisibility(View.GONE);
            }else {
                nameVal=dataList.get(i);
                textView.setText(nameVal.getVal());
                if (data.contains(nameVal.getId())){
                    textView.setSelected(true);
                    textView.setTextColor(Color.WHITE);
                }
            }
        }

    }
    private void initBusSelect() {
        List<NameVal> buisCatList=config.getBuscatList();
        if (buisCatList==null){
            MyLog.i("buisCatList null");
            return;
        }
        for (int i=0;i<linear_busicat.getChildCount();i++){
            if (i>=buisCatList.size()){
                linear_busicat.getChildAt(i).setVisibility(View.INVISIBLE);
            }else {
                TextView textView= (TextView) linear_busicat.getChildAt(i);
                NameVal nameVal=buisCatList.get(i);
                textView.setText(nameVal.getVal());
                if (subinfo.getBusiness_cat().contains(nameVal.getId())){
                    textView.setSelected(true);
                    textView.setTextColor(Color.WHITE);
                }

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
                TextView textView= (TextView) linear_isscat.getChildAt(i);
                NameVal nameVal=issCatList.get(i);
                textView.setText(nameVal.getVal());
                if (subinfo.getCoop_cat().contains(nameVal.getId())){
                    textView.setSelected(true);
                    textView.setTextColor(Color.WHITE);
                }
            }
        }
    }





    private void initData() {
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
                initLinear();
            }
        });
    }


    private IdentSubInfo subinfo;
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

    private void initLinear() {
        ImageLoader imageLoader=ImageLoader.getInstance();
        companyname_text.setText(subinfo.getCompany_name());
        imageLoader.displayImage(Url.prePic+subinfo.getLogo(),icon_image, MyDisplayImageOptions.getBigImageOption());
        company_size.setText(subinfo.getCompany_scale().getVal());
        area_text.setText(subinfo.getArea().getVal());
        companyinfo_text.setText(subinfo.getCompany_info());
        username_text.setText(subinfo.getApply());
        phonenum_text.setText(subinfo.getCompany_phone());
        String addr=subinfo.getProvince().getVal()+subinfo.getCity().getVal()+subinfo.getCountry().getVal()+subinfo.getAddress();
        address_text.setText(addr);
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":

                break;
            case "3":
                initReslayout(config.getOsresList(),subinfo.getOutsource_cat());
                break;
            case "4":
                initReslayout(config.getStageList(),subinfo.getInvest_stage());
                investcat_text.setText(subinfo.getInvest_cat().getVal());
                break;
            case "5":

                break;
            case "6":
                initBusSelect();
                intIssSelect();
                break;
        }

        switch (subinfo.getPersonal()){
            case "1":
                companyphoto_layout.setVisibility(View.GONE);
                idnum_text.setText(subinfo.getIdnum());
                break;
            case "2":
                idnum_layout.setVisibility(View.GONE);
                imageLoader.displayImage(Url.prePic+subinfo.getCompany_photo(),company_photo_image,MyDisplayImageOptions.getBigImageOption());
                break;
        }
    }
}
