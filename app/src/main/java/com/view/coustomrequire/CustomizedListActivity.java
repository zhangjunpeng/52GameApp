package com.view.coustomrequire;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.daimajia.swipe.*;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;
import com.view.activity.BaseActivity;
import com.view.coustomrequire.info.FindGameInfo;
import com.view.coustomrequire.info.FindIPInfo;
import com.view.coustomrequire.info.FindInvestInfo;
import com.view.coustomrequire.info.FindIssueInfo;
import com.view.coustomrequire.info.FindTeamInfo;
import com.view.coustomrequire.info.IPFindCooperationInfo;
import com.view.coustomrequire.info.IPFindRecomposeInfo;
import com.view.coustomrequire.info.IPFindTeamInfo;
import com.view.coustomrequire.info.IPFindUniteInfo;
import com.view.messagecenter.MessageActivity;
import com.view.messagecenter.MessageInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomizedListActivity extends BaseActivity {

    private ListView recyclerView;

    private MySipeAdapter adapter;

    private List<ItemInfoCustomList> dataList=new ArrayList<>();

    private ImageView back;
    private TextView title;
    private TextView save;

    int p=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized_list);
        setImmerseLayout(findViewById(R.id.title_customized));

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);
        title.setText("需求定制");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });
        initData(p+"");

        adapter=new MySipeAdapter(this,dataList,recyclerView);



        recyclerView= (ListView) findViewById(R.id.recyleview_custom);

        recyclerView.setAdapter(adapter);
//        //设置布局管理器
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //设置adapter
//        recyclerView.setAdapter(adapter);
//        //设置Item增加、移除动画
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        recyclerView.addOnScrollListener(new MyScrollListener());


    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("customize/index");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("p",p);
        baseParams.addSign();

        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
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
                MyLog.i("datalist size=="+dataList.size());
                adapter.notifyDataSetChanged();
                if (dataList.size()==0){
//                    setContentView(R.layout.layout_nocustomized);
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.go_coutomized).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(CustomizedListActivity.this, CustomizedActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            finish();
                        }
                    });

                }

            }
        });
    }

    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONArray data=jsonObject.getJSONArray("data");
                for (int i=0;i<data.length();i++){
                    ItemInfoCustomList itemInfo=new ItemInfoCustomList();
                    JSONObject dataitem=data.getJSONObject(i);
                    itemInfo.setId(dataitem.getString("id"));
                    itemInfo.setUser_id(dataitem.getString("user_id"));
                    itemInfo.setIdentity_cat(dataitem.getString("identity_cat"));
                    NameVal service_cat=new NameVal();
                    String id=dataitem.getJSONObject("service_cat").getString("id");
                    service_cat.setId(id);
                    service_cat.setVal(dataitem.getJSONObject("service_cat").getString("val"));
                    itemInfo.setServive_cat(service_cat);
                    itemInfo.setApply(dataitem.getString("apply"));
                    itemInfo.setPhone(dataitem.getString("phone"));

                    itemInfo.setCustom_id(dataitem.getString("custom_id"));
                    itemInfo.setChecked(dataitem.getString("checked"));
                    itemInfo.setAppendix(dataitem.getString("appendix"));
                    itemInfo.setNote(dataitem.getString("note"));
                    itemInfo.setReson(dataitem.getString("reason"));
                    itemInfo.setIdentity_cat_name(dataitem.getString("identity_cat_name"));
                    itemInfo.setChecked_name(dataitem.getString("checked_name"));

                    JSONObject info=dataitem.getJSONObject("info");
                    switch (itemInfo.getIdentity_cat()){
                        //2:开发者 4:投资人 5:IP方 6:发行方
                        case "2":
                            // "4": "找投资","5": "找IP","6": "找发行"
                            if (id.equals("4")){
                                FindInvestInfo investInfo=new FindInvestInfo();
                                investInfo.setMoney(info.getString("money"));
                                investInfo.setStarge(info.getJSONArray("starge").getJSONObject(0).getString("id"));
                                investInfo.setStarge_name(info.getString("starge_name"));
                                JSONArray array=info.getJSONArray("stock");
                                investInfo.setMinstock(array.getString(0));
                                investInfo.setMaxstock(array.getString(1));
                                itemInfo.setInfo(investInfo);
                            }else if (id.equals("5")){
                                FindIPInfo findIPInfo=new FindIPInfo();
                                JSONArray coopcatarray=info.getJSONArray("ip_coop_type");
                                List<NameVal> coopcats=new ArrayList<>();
                                for (int j=0;j<coopcatarray.length();j++){
                                    JSONObject object=coopcatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopcats.add(nameVal);
                                }
                                findIPInfo.setIpcoopcat(coopcats);

                                JSONArray catarray=info.getJSONArray("ip_cat");
                                List<NameVal> cats=new ArrayList<>();
                                for (int j=0;j<catarray.length();j++){
                                    JSONObject object=catarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    cats.add(nameVal);
                                }
                                findIPInfo.setIpcat(cats);

                                JSONArray stylearray=info.getJSONArray("ip_style");
                                List<NameVal> styles=new ArrayList<>();
                                for (int j=0;j<stylearray.length();j++){
                                    JSONObject object=stylearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    styles.add(nameVal);
                                }
                                findIPInfo.setIpstyle(styles);

                                itemInfo.setInfo(findIPInfo);
                            }else if (id.equals("6")){

                                FindIssueInfo findIssueInfo=new FindIssueInfo();

                                JSONArray regionarray=info.getJSONArray("region");
                                List<NameVal> regions=new ArrayList<>();
                                for (int j=0;j<regionarray.length();j++){
                                    JSONObject object=regionarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    regions.add(nameVal);
                                }
                                findIssueInfo.setRegion(regions);

                                JSONArray issuecatarray=info.getJSONArray("issue_cat");
                                List<NameVal> issuecats=new ArrayList<>();
                                for (int j=0;j<issuecatarray.length();j++){
                                    JSONObject object=issuecatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    issuecats.add(nameVal);
                                }
                                findIssueInfo.setIssuecat(issuecats);

                                JSONArray issuegamearray=info.getJSONArray("issue_game");
                                List<NameVal> issuegames=new ArrayList<>();
                                for (int j=0;j<issuegamearray.length();j++){
                                    JSONObject object=issuegamearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    issuegames.add(nameVal);
                                }
                                findIssueInfo.setIssuegame(issuegames);

                                itemInfo.setInfo(findIssueInfo);
                            }
                            break;
                        case "4":
                            // "1": "找团队","2": "找IP"
                            if (id.equals("1")){
                                FindTeamInfo findTeamInfo=new FindTeamInfo();

                                JSONArray teamtypearray=info.getJSONArray("team_type");
                                List<NameVal> teamtypes=new ArrayList<>();
                                for (int j=0;j<teamtypearray.length();j++){
                                    JSONObject object=teamtypearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    teamtypes.add(nameVal);
                                }
                                findTeamInfo.setTeamtype(teamtypes);

                                JSONArray stargearray=info.getJSONArray("starge");
                                List<NameVal> starges=new ArrayList<>();
                                for (int j=0;j<stargearray.length();j++){
                                    JSONObject object=stargearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    starges.add(nameVal);
                                }
                                findTeamInfo.setStarge(starges);

                                itemInfo.setInfo(findTeamInfo);
                            }else if(id.equals("2")){
                                FindIPInfo findIPInfo=new FindIPInfo();
                                JSONArray coopcatarray=info.getJSONArray("ip_coop_type");
                                List<NameVal> coopcats=new ArrayList<>();
                                for (int j=0;j<coopcatarray.length();j++){
                                    JSONObject object=coopcatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopcats.add(nameVal);
                                }
                                findIPInfo.setIpcoopcat(coopcats);

                                JSONArray catarray=info.getJSONArray("ip_cat");
                                List<NameVal> cats=new ArrayList<>();
                                for (int j=0;j<catarray.length();j++){
                                    JSONObject object=catarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    cats.add(nameVal);
                                }
                                findIPInfo.setIpcat(cats);

                                JSONArray stylearray=info.getJSONArray("ip_style");
                                List<NameVal> styles=new ArrayList<>();
                                for (int j=0;j<stylearray.length();j++){
                                    JSONObject object=stylearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    styles.add(nameVal);
                                }
                                findIPInfo.setIpstyle(styles);

                                itemInfo.setInfo(findIPInfo);
                            }
                            break;
                        case "5":
                            //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                            if (id.equals("1")){
                                IPFindCooperationInfo ipFindCooperationInfo=new IPFindCooperationInfo();

                                JSONArray iputhorityarray=info.getJSONArray("ip_uthority");
                                List<NameVal> iputhoritys=new ArrayList<>();
                                for (int j=0;j<iputhorityarray.length();j++){
                                    JSONObject object=iputhorityarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    iputhoritys.add(nameVal);
                                }
                                ipFindCooperationInfo.setIputhority(iputhoritys);

                                JSONArray coopIparray=info.getJSONArray("coop_ip");
                                List<NameVal> coopips=new ArrayList<>();
                                for (int j=0;j<coopIparray.length();j++){
                                    JSONObject object=coopIparray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopips.add(nameVal);
                                }
                                ipFindCooperationInfo.setCoopip(coopips);

                                itemInfo.setInfo(ipFindCooperationInfo);

                            }else if(id.equals("2")){
                                IPFindUniteInfo ipFindUniteInfo=new IPFindUniteInfo();

                                JSONArray ipCoopCatarray=info.getJSONArray("ip_coop_cat");
                                List<NameVal> ipcoopcats=new ArrayList<>();
                                for (int j=0;j<ipCoopCatarray.length();j++){
                                    JSONObject object=ipCoopCatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    ipcoopcats.add(nameVal);
                                }
                                ipFindUniteInfo.setIpcoopcat(ipcoopcats);

                                JSONArray coopIparray=info.getJSONArray("coop_ip");
                                List<NameVal> coopips=new ArrayList<>();
                                for (int j=0;j<coopIparray.length();j++){
                                    JSONObject object=coopIparray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopips.add(nameVal);
                                }
                                ipFindUniteInfo.setCoopip(coopips);

                                itemInfo.setInfo(ipFindUniteInfo);

                            }else if(id.equals("3")){
                                IPFindTeamInfo ipFindTeamInfo=new IPFindTeamInfo();

                                JSONArray ipDevelopCatarray=info.getJSONArray("ip_develop_cat");
                                List<NameVal> ipdevelopcats=new ArrayList<>();
                                for (int j=0;j<ipDevelopCatarray.length();j++){
                                    JSONObject object=ipDevelopCatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    ipdevelopcats.add(nameVal);
                                }
                                ipFindTeamInfo.setIpdevelopcat(ipdevelopcats);

                                JSONArray coopIparray=info.getJSONArray("coop_ip");
                                List<NameVal> coopips=new ArrayList<>();
                                for (int j=0;j<coopIparray.length();j++){
                                    JSONObject object=coopIparray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopips.add(nameVal);
                                }
                                ipFindTeamInfo.setCoopip(coopips);

                                itemInfo.setInfo(ipFindTeamInfo);
                            }else if(id.equals("4")){

                                IPFindRecomposeInfo ipFindRecomposeInfo=new IPFindRecomposeInfo();

                                JSONArray gameTypearray=info.getJSONArray("game_type");
                                List<NameVal> gametypes=new ArrayList<>();
                                for (int j=0;j<gameTypearray.length();j++){
                                    JSONObject object=gameTypearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    gametypes.add(nameVal);
                                }
                                ipFindRecomposeInfo.setGametype(gametypes);

                                JSONArray gameStagearray=info.getJSONArray("game_stage");
                                List<NameVal> gamestages=new ArrayList<>();
                                for (int j=0;j<gameStagearray.length();j++){
                                    JSONObject object=gameStagearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    gamestages.add(nameVal);
                                }
                                ipFindRecomposeInfo.setGamestage(gamestages);

                                JSONArray coopIparray=info.getJSONArray("coop_ip");
                                List<NameVal> coopips=new ArrayList<>();
                                for (int j=0;j<coopIparray.length();j++){
                                    JSONObject object=coopIparray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopips.add(nameVal);
                                }
                                ipFindRecomposeInfo.setCoopip(coopips);

                                itemInfo.setInfo(ipFindRecomposeInfo);
                            }
                            break;
                        case "6":
                            //   "2": "找游戏","4": "找投资","5": "找IP"
                            if (id.equals("2")){
                                FindGameInfo findGameInfo=new FindGameInfo();

                                JSONArray regionarray=info.getJSONArray("region");
                                List<NameVal> regions=new ArrayList<>();
                                for (int j=0;j<regionarray.length();j++){
                                    JSONObject object=regionarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    regions.add(nameVal);
                                }
                                findGameInfo.setRegion(regions);

                                JSONArray issuecatarray=info.getJSONArray("issue_cat");
                                List<NameVal> issuecats=new ArrayList<>();
                                for (int j=0;j<issuecatarray.length();j++){
                                    JSONObject object=issuecatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    issuecats.add(nameVal);
                                }
                                findGameInfo.setIssuecat(issuecats);

                                JSONArray gamegradearray=info.getJSONArray("game_grade");
                                List<NameVal> gamegrades=new ArrayList<>();
                                for (int j=0;j<gamegradearray.length();j++){
                                    JSONObject object=gamegradearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    gamegrades.add(nameVal);
                                }
                                findGameInfo.setGamegrade(gamegrades);

                                JSONArray gametypearray=info.getJSONArray("game_type");
                                List<NameVal> gametypes=new ArrayList<>();
                                for (int j=0;j<gametypearray.length();j++){
                                    JSONObject object=gametypearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    gametypes.add(nameVal);
                                }
                                findGameInfo.setGametype(gametypes);

                                JSONArray gamestagearray=info.getJSONArray("game_stage");
                                List<NameVal> gamestages=new ArrayList<>();
                                for (int j=0;j<gamestagearray.length();j++){
                                    JSONObject object=gamestagearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    gamestages.add(nameVal);
                                }
                                findGameInfo.setGamestage(gamestages);

                                itemInfo.setInfo(findGameInfo);

                            }else if(id.equals("4")){
                                FindInvestInfo investInfo=new FindInvestInfo();
                                investInfo.setMoney(info.getString("money"));
                                investInfo.setStarge(info.getJSONArray("starge").getJSONObject(0).getString("id"));
                                investInfo.setStarge_name(info.getString("starge_name"));

                                JSONArray array=info.getJSONArray("stock");
                                investInfo.setMinstock(array.getString(0));
                                investInfo.setMaxstock(array.getString(1));

                                itemInfo.setInfo(investInfo);
                            }else if(id.equals("5")){
                                FindIPInfo findIPInfo=new FindIPInfo();
                                JSONArray coopcatarray=info.getJSONArray("ip_coop_type");
                                List<NameVal> coopcats=new ArrayList<>();
                                for (int j=0;j<coopcatarray.length();j++){
                                    JSONObject object=coopcatarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    coopcats.add(nameVal);
                                }
                                findIPInfo.setIpcoopcat(coopcats);

                                JSONArray catarray=info.getJSONArray("ip_cat");
                                List<NameVal> cats=new ArrayList<>();
                                for (int j=0;j<catarray.length();j++){
                                    JSONObject object=catarray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    cats.add(nameVal);
                                }
                                findIPInfo.setIpcat(cats);

                                JSONArray stylearray=info.getJSONArray("ip_style");
                                List<NameVal> styles=new ArrayList<>();
                                for (int j=0;j<stylearray.length();j++){
                                    JSONObject object=stylearray.getJSONObject(j);
                                    NameVal nameVal=new NameVal();
                                    nameVal.setId(object.getString("id"));
                                    nameVal.setVal(object.getString("val"));
                                    styles.add(nameVal);
                                }
                                findIPInfo.setIpstyle(styles);

                                itemInfo.setInfo(findIPInfo);
                            }
                            break;
                    }
                    dataList.add(itemInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
