package com.view.coustomrequire;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.FindGameInfo;
import com.view.coustomrequire.info.FindIPInfo;
import com.view.coustomrequire.info.FindInvestInfo;
import com.view.coustomrequire.info.FindIssueInfo;
import com.view.coustomrequire.info.FindTeamInfo;
import com.view.coustomrequire.info.IPFindCooperationInfo;
import com.view.coustomrequire.info.IPFindRecomposeInfo;
import com.view.coustomrequire.info.IPFindTeamInfo;
import com.view.coustomrequire.info.IPFindUniteInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomizedListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private MyAdapter adapter;

    private List<ItemInfoCustomList> dataList=new ArrayList<>();

    private ImageView back;
    private TextView title;
    private TextView save;

    int p=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized_list);

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

        adapter=new MyAdapter(this,dataList);


        recyclerView= (RecyclerView) findViewById(R.id.recyleview_custom);

        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter
        recyclerView.setAdapter(adapter);
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new MyScrollListener());


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
                adapter.notifyDataSetChanged();
                if(dataList.size()==0){
                    setContentView(R.layout.nodata);
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
                                investInfo.setStarge(info.getString("starge"));
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
                                investInfo.setStarge(info.getString("starge"));
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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    CustomizedListActivity.this).inflate(R.layout.item_customized_list,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final ItemInfoCustomList itemInfoCustomList= dataList.get(position);
            NameVal nameVal=itemInfoCustomList.getServive_cat();
            String namestr="需求：";
            final StringBuffer infoStr=new StringBuffer();

            switch (itemInfoCustomList.getIdentity_cat()){
                //2:开发者 4:投资人 5:IP方 6:发行方
                case "2":
                    // "4": "找投资","5": "找IP","6": "找发行"
                    holder.circleImageView.setImageResource(R.drawable.cp_icon);
                    if (nameVal.getId().equals("4")){
                        namestr=namestr+"找投资";
                        FindInvestInfo findInvestInfo= (FindInvestInfo) itemInfoCustomList.getInfo();
                        infoStr.append("融资阶段："+findInvestInfo.getStarge()
                                +"\n融资金额："+findInvestInfo.getMoney()
                                +"\n出让股份："+findInvestInfo.getMinstock()+"-"+findInvestInfo.getMaxstock());
                    }else if (nameVal.getId().equals("5")){
                        namestr=namestr+"找IP";
                        FindIPInfo findIPInfo= (FindIPInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作方式："+getStringFormList(findIPInfo.getIpcoopcat())
                                +"\nIP类型："+getStringFormList(findIPInfo.getIpcat())
                                +"\nIP风格："+getStringFormList(findIPInfo.getIpstyle()));
                    }else if (nameVal.getId().equals("6")){
                        namestr=namestr+"找发行";
                        FindIssueInfo findIssueInfo= (FindIssueInfo) itemInfoCustomList.getInfo();
                        infoStr.append("发行范围："+getStringFormList(findIssueInfo.getRegion())
                                +"\n发行方式："+getStringFormList(findIssueInfo.getIssuecat())
                                +"\n发行游戏："+getStringFormList(findIssueInfo.getIssuegame()));
                    }
                    break;
                case "4":
                    // "1": "找团队","2": "找IP"
                    holder.circleImageView.setImageResource(R.drawable.invest_icon);

                    if (nameVal.getId().equals("1")){
                        namestr=namestr+"找团队";
                        FindTeamInfo findTeamInfo= (FindTeamInfo) itemInfoCustomList.getInfo();
                        infoStr.append("团队类型："+getStringFormList(findTeamInfo.getTeamtype())
                                +"\n投资阶段："+getStringFormList(findTeamInfo.getStarge()));
                    }else if(nameVal.getId().equals("2")){
                        namestr=namestr+"找IP";
                        FindIPInfo findIPInfo= (FindIPInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作方式："+getStringFormList(findIPInfo.getIpcoopcat())
                                +"\nIP类型："+getStringFormList(findIPInfo.getIpcat())
                                +"\nIP风格："+getStringFormList(findIPInfo.getIpstyle()));
                    }
                    break;
                case "5":
                    //"1": "授权合作","2": "IP联合孵化","3": "找团队开发","4": "找产品改编"
                    holder.circleImageView.setImageResource(R.drawable.ip_icon);

                    if (nameVal.getId().equals("1")){
                        namestr=namestr+"授权合作";
                        IPFindCooperationInfo ipFindCooperationInfo= (IPFindCooperationInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作IP："+getStringFormList(ipFindCooperationInfo.getCoopip())
                                +"\n授权类型："+getStringFormList(ipFindCooperationInfo.getIputhority()));
                    }else if(nameVal.getId().equals("2")){
                        namestr=namestr+"IP联合孵化";
                        IPFindUniteInfo ipFindUniteInfo= (IPFindUniteInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作IP："+getStringFormList(ipFindUniteInfo.getCoopip())
                                +"\n合作类型："+getStringFormList(ipFindUniteInfo.getIpcoopcat()));
                    }else if(nameVal.getId().equals("3")){
                        namestr=namestr+"找团队开发";
                        IPFindTeamInfo ipFindTeamInfo= (IPFindTeamInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作IP："+getStringFormList(ipFindTeamInfo.getCoopip())
                                +"\n合作方式："+getStringFormList(ipFindTeamInfo.getIpdevelopcat()));
                    }else if(nameVal.getId().equals("4")){
                        namestr=namestr+"找产品改编";
                        IPFindRecomposeInfo ipFindRecomposeInfo= (IPFindRecomposeInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作IP："+getStringFormList(ipFindRecomposeInfo.getCoopip())
                                +"\n游戏类型："+getStringFormList(ipFindRecomposeInfo.getGametype())
                                +"\n游戏阶段："+getStringFormList(ipFindRecomposeInfo.getGamestage())
                        );
                    }
                    break;
                case "6":
                    //   "2": "找游戏","4": "找投资","5": "找IP"
                    holder.circleImageView.setImageResource(R.drawable.issue_icon);

                    if (nameVal.getId().equals("2")){
                        namestr=namestr+"找游戏";
                        FindGameInfo findGameInfo= (FindGameInfo) itemInfoCustomList.getInfo();

                        infoStr.append("游戏评级："+getStringFormList(findGameInfo.getGamegrade())
                                +"\n游戏类型："+getStringFormList(findGameInfo.getGametype())
                                +"\n版本阶段："+getStringFormList(findGameInfo.getGamestage())
                                +"\n发行范围："+getStringFormList(findGameInfo.getRegion())
                                +"\n发行方式："+getStringFormList(findGameInfo.getIssuecat())
                        );
                    }else if(nameVal.getId().equals("4")){
                        namestr=namestr+"找投资";
                        FindInvestInfo findInvestInfo= (FindInvestInfo) itemInfoCustomList.getInfo();
                        infoStr.append("融资阶段："+findInvestInfo.getStarge()
                                +"\n融资金额："+findInvestInfo.getMoney()
                                +"\n出让股份："+findInvestInfo.getMinstock()+"-"+findInvestInfo.getMaxstock());

                    }else if(nameVal.getId().equals("5")){
                        namestr=namestr+"找IP";
                        FindIPInfo findIPInfo= (FindIPInfo) itemInfoCustomList.getInfo();
                        infoStr.append("合作方式："+getStringFormList(findIPInfo.getIpcoopcat())
                                +"\nIP类型："+getStringFormList(findIPInfo.getIpcat())
                                +"\nIP风格："+getStringFormList(findIPInfo.getIpstyle()));
                    }
                    break;

            }
            holder.name.setText(namestr);
            holder.info.setText(infoStr);
            switch (itemInfoCustomList.getChecked()){
                case "0":
                case "1":
                    holder.stage.setTextColor(Color.rgb(255,156,0));
                    break;
                case "2":
                    holder.stage.setTextColor(Color.rgb(124,124,124));

                    break;
            }
            holder.stage.setText(itemInfoCustomList.getChecked_name());


//            final int[] height = {0};


//            ViewTreeObserver vto2 = holder.relative.getViewTreeObserver();
//            final View view=holder.relative;
//            vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    height[0] =view.getHeight();
//                }
//            });
//
//            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) holder.line.getLayoutParams();
//            layoutParams.height=height[0];

//            layoutParams.height
//            holder.line.setLayoutParams(layoutParams);

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(CustomizedListActivity.this,ChangeCustomInfoActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("info",itemInfoCustomList);
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    finish();
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView circleImageView;
            TextView name;
            TextView info;
            TextView stage;
            ImageView line;
            RelativeLayout relative;

            View item;

            public MyViewHolder(View view) {
                super(view);
                circleImageView = (CircleImageView) view.findViewById(R.id.icon);
                name = (TextView) view.findViewById(R.id.name);
                info = (TextView) view.findViewById(R.id.info);
                stage = (TextView) view.findViewById(R.id.stage);
                line= (ImageView) view.findViewById(R.id.line);
                relative= (RelativeLayout) view.findViewById(R.id.relative);
                item=view;
            }
        }
    }

    public String getStringFormList(List<NameVal> nameValList){
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=0;i<nameValList.size();i++){
            if (i!=0){
                stringBuffer.append("、");
            }
            stringBuffer.append(nameValList.get(i).getVal());
        }
        return stringBuffer.toString();
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    class MyScrollListener extends RecyclerView.OnScrollListener {



        private LAYOUT_MANAGER_TYPE layoutManagerType;

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;



        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
//            LogUtils.i("onScrollStateChanged", "visibleItemCount" + visibleItemCount);
//            LogUtils.i("onScrollStateChanged", "lastVisibleItemPosition" + lastVisibleItemPosition);
//            LogUtils.i("onScrollStateChanged", "totalItemCount" + totalItemCount);
            if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
//                loadingData.onLoadMore();
                MyLog.i("RecyclerView 滚动到底部");
                p++;
                initData(p+"");
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }


    }
}
