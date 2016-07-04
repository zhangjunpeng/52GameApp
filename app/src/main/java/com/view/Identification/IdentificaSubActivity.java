package com.view.Identification;

import android.app.Dialog;
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
import com.test4s.myapp.Config;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentificaSubActivity extends AppCompatActivity implements View.OnClickListener {


    private List<NameVal> areaList;
    private List<NameVal> companysizeList;
    private List<NameVal> coompstageList;
    private List<NameVal> coopcatList;
    private List<NameVal> businecatList;
    private List<NameVal> investcatList;


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


    //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
    private String type="2";
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_sub);
        initData();

        companyName_edit= (EditText) findViewById(R.id.edit_companyname);
        icon= (ImageView) findViewById(R.id.icon_iden_sub);
        upphoto= (ImageView) findViewById(R.id.upimage_icon);
        chickToUp= (TextView) findViewById(R.id.chicktoup_iden_sub);
        companySize_text= (TextView) findViewById(R.id.scale_ident_sub);
        area_text= (TextView) findViewById(R.id.area_text_iden_sub);
        companyInfo_edit= (EditText) findViewById(R.id.edit_companyinfo);

        personName_edit= (EditText) findViewById(R.id.edit_username);
        phoneNum_edit= (EditText) findViewById(R.id.edit_phone);
        getCode_text= (TextView) findViewById(R.id.getcode_iden_sub);
        phoneCode_edit= (EditText) findViewById(R.id.edit_code);
        pcc_text= (TextView) findViewById(R.id.address_text);
        address_edit= (EditText) findViewById(R.id.address_edit);
        submit= (TextView) findViewById(R.id.submit_iden_sub);

        areaLayout= (LinearLayout) findViewById(R.id.layout_area);
        companyInfoLayout= (LinearLayout) findViewById(R.id.layout_companyinfo);

        type=getIntent().getStringExtra("type");

        if (type.equals("5")){
            areaLayout.setVisibility(View.GONE);
            companyInfoLayout.setVisibility(View.GONE);
            findViewById(R.id.line_needgone1).setVisibility(View.GONE);
            findViewById(R.id.line_needgone2).setVisibility(View.GONE);

        }else if (!type.equals("2")){
            FragmentManager fm=getSupportFragmentManager();
            Fragment fragment = null;
            Bundle bundle=new Bundle();
            switch (type){
                case "3":
                    fragment=new OSIdenFragment();
                    break;
                case "4":
                    fragment=new InvIdenFragment();
                    break;
                case "6":
                    fragment=new IssIdenFragment();

                    break;
            }
            fm.beginTransaction().replace(R.id.contianer_identisub,fragment).commit();
        }

        companySize_text.setOnClickListener(this);
        area_text.setOnClickListener(this);
//        EventBus.getDefault().register(this);
    }
    private void initData() {
        BaseParams params=new BaseParams("identity/identityselect");
        params.addParams("type","all");
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

            }
        });
    }

    private void listdataParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject selectList=data.getJSONObject("selectList");
                areaList=new ArrayList<>();
                JSONArray areaArr=selectList.getJSONArray("area");
                for (int i=0;i<areaArr.length();i++){
//                    Map<String,String> map=new HashMap<>();
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("name"));
//                    map.put("id",object.getString("id"));
//                    map.put("name",object.getString("name"));
                    areaList.add(nameVal);
                }

                companysizeList=new ArrayList<>();
                JSONArray sizeArr=selectList.getJSONArray("companysize");
                for (int i=0;i<sizeArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    companysizeList.add(nameVal);
                }



                coompstageList=new ArrayList<>();
                JSONArray copstageArr=selectList.getJSONArray("coompstage");
                for (int i=0;i<copstageArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    coompstageList.add(nameVal);
                }

                coopcatList=new ArrayList<>();
                JSONArray coopcatArr=selectList.getJSONArray("coopcat");
                for (int i=0;i<coopcatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    coopcatList.add(nameVal);
                }

                businecatList=new ArrayList<>();
                JSONArray businecatArr=selectList.getJSONArray("businecat");
                for (int i=0;i<businecatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    businecatList.add(nameVal);
                }

                investcatList=new ArrayList<>();
                JSONArray investcatArr=selectList.getJSONArray("investcat");
                for (int i=0;i<investcatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=areaArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    investcatList.add(nameVal);
                }

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
                initTextview(textView,dataList.get(position));
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    public void initTextview(TextView textView,NameVal val){
        textView.setTextColor(Color.rgb(124,124,124));
        textView.setText(val.getVal());
        dialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDateSeclect(TextView textView, Map<String, String> map){

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scale_ident_sub:
                showSelectDiaolog(companySize_text,companysizeList);
                break;
            case R.id.area_text_iden_sub:
                showSelectDiaolog(area_text,areaList);
                break;
        }
    }
}
