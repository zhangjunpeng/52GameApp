package com.view.Identification;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IdentificationActivity extends BaseActivity implements View.OnClickListener {

    private List<TextView> textViewList;
    private List<ImageView> lines;

    private ImageView back;
    private TextView title;
    private TextView save;


    private List<String> titleString;
    private List<IdentiMess> datainfo;
    private List<Fragment> fragmentList;


    private ViewPager viewPager;

    private IdenViewPagerAdapter adapter;

    private String[] titles={"cp","outSource","investor","ip","issue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleString=new ArrayList<>();

        setImmerseLayout(findViewById(R.id.titlebar_idtf));

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        back.setOnClickListener(this);
        title.setText("身份认证");
        save.setVisibility(View.INVISIBLE);

        initData();
        initData2();
        textViewList=new ArrayList<>();
        textViewList.add((TextView) findViewById(R.id.cp_title));
        textViewList.add((TextView) findViewById(R.id.tz_title));
        textViewList.add((TextView) findViewById(R.id.ip_title));
        textViewList.add((TextView) findViewById(R.id.fx_title));
        textViewList.add((TextView) findViewById(R.id.wb_title));

        lines=new ArrayList<>();
        lines.add((ImageView) findViewById(R.id.line1_title));
        lines.add((ImageView) findViewById(R.id.line2_title));
        lines.add((ImageView) findViewById(R.id.line3_title));
        lines.add((ImageView) findViewById(R.id.line4_title));
        lines.add((ImageView) findViewById(R.id.line5_title));

        viewPager= (ViewPager) findViewById(R.id.viewpager_identi);
        fragmentList=new ArrayList<>();
        adapter=new IdenViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        setTitlebar(0);
        for (TextView textView:textViewList){
            textView.setOnClickListener(this);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitlebar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        BaseParams params=new BaseParams("identity/index");
        params.addParams("token", MyAccount.getInstance().getToken());
        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("id result=="+result);
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
                initTitle();
                initViewPagerfragment();
//                setView(0);

            }
        });

    }

    private void initViewPagerfragment() {
        for (int i=0;i<textViewList.size();i++){
            IdentiMess mess=datainfo.get(i);
            Bundle bundle=new Bundle();
//            Iterator<String> itear=map.keySet().iterator();
//
//            while (itear.hasNext()){
//                String key=itear.next();
//                bundle.putString(key,map.get(key));
//            }
            Fragment fragment;
            if (!mess.getChecked().equals("4")){
//                MyLog.i("IdentiSuccFragment");
                fragment=new IdentiSuccFragment();
                bundle.putSerializable("data",mess);


            }else {
//                MyLog.i("IdentifitionFragment");
                MyLog.i("type==="+mess.getId());
                bundle.putString("type",mess.getId());
                fragment=new IdentifitionFragment();
            }
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        adapter.notifyDataSetChanged();
    }

    private void initTitle(){
        for (int i=0;i<textViewList.size();i++){
            TextView title=textViewList.get(i);
            title.setText(titleString.get(i));
        }
    }

    class IdenViewPagerAdapter extends FragmentPagerAdapter{

        public IdenViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONArray identityArr=data.getJSONArray("identityArr");

                JSONObject identityInfo=data.getJSONObject("identityInfo");
                datainfo=new ArrayList<>();

                for (int i=0;i<titles.length;i++){
                    String key=titles[i];
                    MyLog.i("key=="+key);
                    JSONObject info=identityInfo.getJSONObject(key);
//                    Map<String,String> map=new HashMap<>();
                    IdentiMess mess=new IdentiMess();
                    mess.setChecked(info.getString("checked"));
                    JSONArray serverArr=info.getJSONArray("service");
                    String serverString="";
                    for (int j=0;j<serverArr.length();j++){
                        JSONObject server=serverArr.getJSONObject(j);
                        if (j!=0){
                            serverString=serverString+"\n";
                        }
                        int posi=j+1;
                        serverString=serverString+posi+")"+server.getString("identity_info");
                    }
                    mess.setService(serverString);
                    mess.setTip(info.getString("tip"));
                    mess.setNote(info.getString("note"));
                    datainfo.add(mess);
                }


                JSONObject companyInfo=data.getJSONObject("companyInfo");
                IdentificationConfig config=IdentificationConfig.getInstance();
                config.setCompanyName(companyInfo.getString("company_name"));

                JSONObject commonData=jsonObject.getJSONObject("commonData");
                config.setUploadUrl(commonData.getString("uploadUrl"));
                config.setUploadType(commonData.getString("uploadType"));

                titleString=new ArrayList<>();
                for (int i=0;i<identityArr.length();i++){
                    IdentiMess mess=datainfo.get(i);
                    JSONObject identity=identityArr.getJSONObject(i);
                    titleString.add(identity.getString("val"));
                    mess.setId(identity.getString("id"));
                    mess.setVal(identity.getString("val"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void initData2() {
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

            }
        });
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
                for (int i=0;i<areaArr.length();i++){
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

                for (int i=0;i<sizeArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=sizeArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    sizeList.add(nameVal);
                }

                JSONArray osresArr=selectList.getJSONArray("outsorcestyle");
                List<NameVal> osresList=new ArrayList<>();

                for (int i=0;i<osresArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=osresArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    osresList.add(nameVal);
                }

                JSONArray copstageArr=selectList.getJSONArray("coompstage");
                List<NameVal> stageList=new ArrayList<>();

                for (int i=0;i<copstageArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=copstageArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    stageList.add(nameVal);
                }

                JSONArray coopcatArr=selectList.getJSONArray("coopcat");
                List<NameVal> isscatList=new ArrayList<>();

                for (int i=0;i<coopcatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=coopcatArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    isscatList.add(nameVal);
                }

                JSONArray businecatArr=selectList.getJSONArray("businecat");
                List<NameVal> busiscatList=new ArrayList<>();


                for (int i=0;i<businecatArr.length();i++){
                    NameVal nameVal=new NameVal();
                    JSONObject object=businecatArr.getJSONObject(i);
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
                    busiscatList.add(nameVal);
                }

                JSONArray investcatArr=selectList.getJSONArray("investcat");
                List<NameVal> invescatList=new ArrayList<>();


                for (int i=0;i<investcatArr.length();i++){
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

    private void setTitlebar(int m) {
        for (int i=0;i<textViewList.size();i++){
            TextView textView=textViewList.get(i);
            ImageView line=lines.get(i);
            if (i==m){
                textView.setTextColor(Color.rgb(255,156,0));
                line.setVisibility(View.VISIBLE);
            }else {
                textView.setTextColor(Color.rgb(76,76,76));
                line.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cp_title:
                setTitlebar(0);
                viewPager.setCurrentItem(0);

                break;
            case R.id.tz_title:
                setTitlebar(1);
                viewPager.setCurrentItem(1);

                break;
            case R.id.ip_title:
                setTitlebar(2);
                viewPager.setCurrentItem(2);

                break;
            case R.id.fx_title:
                setTitlebar(3);
                viewPager.setCurrentItem(3);

                break;
            case R.id.wb_title:
                setTitlebar(4);
                viewPager.setCurrentItem(4);

                break;
        }
    }
}
