package com.view.coustomrequire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.daimajia.swipe.*;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MySipeAdapter extends BaseSwipeAdapter {

    private Activity context;
    private LayoutInflater mInflater;
    private List<ItemInfoCustomList> datalist;
    private ListView mlistView;


    public MySipeAdapter(Activity context, List<ItemInfoCustomList> datalist, ListView listView){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.datalist=datalist;
        mlistView=listView;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_coustomized_item;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        final SwipeLayout view= (SwipeLayout) LayoutInflater.from(
                context).inflate(R.layout.item_customized_list,parent,false);
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.circleImageView = (CircleImageView) view.findViewById(R.id.icon);
        viewHolder.name = (TextView) view.findViewById(R.id.name);
        viewHolder.info = (TextView) view.findViewById(R.id.info);
        viewHolder.stage = (TextView) view.findViewById(R.id.stage);
        viewHolder.line= (ImageView) view.findViewById(R.id.line);
        viewHolder.relative= (RelativeLayout) view.findViewById(R.id.relative);
        viewHolder.delete= (ImageView) view.findViewById(R.id.delete);

        view.addSwipeListener(new SimpleSwipeListener(){
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCustomize(position);
//                view.close();
                view.toggle();
            }
        });

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {

        MyLog.i("fillValues");
        ViewHolder holder= (ViewHolder) convertView.getTag();
        final ItemInfoCustomList itemInfoCustomList= datalist.get(position);
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
                    infoStr.append("融资阶段："+findInvestInfo.getStarge_name()
                            +"\n融资金额："+findInvestInfo.getMoney()+"万元"
                            +"\n出让股份："+findInvestInfo.getMinstock()+"%-"+findInvestInfo.getMaxstock()+"%");
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
                    infoStr.append("融资阶段："+findInvestInfo.getStarge_name()
                            +"\n融资金额："+findInvestInfo.getMoney()+"万元"
                            +"\n出让股份："+findInvestInfo.getMinstock()+"%-"+findInvestInfo.getMaxstock()+"%");

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


        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChangeCustomInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",itemInfoCustomList);
                intent.putExtra("data",bundle);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                context.finish();
            }
        });

    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.getView(position, null, mlistView);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{

        CircleImageView circleImageView;
        TextView name;
        TextView info;
        TextView stage;
        ImageView line;
        RelativeLayout relative;
        ImageView delete;
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

    private void deleteCustomize(int position) {
        final ItemInfoCustomList itemInfoCustomList= datalist.get(position);

        BaseParams baseParams=new BaseParams("customize/delcustomize");
        baseParams.addParams("id",itemInfoCustomList.getId());
        if (MyAccount.isLogin) {
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("delete back=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean success=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (success&&code==200){
                        CusToast.showToast(context,"删除成功", Toast.LENGTH_SHORT);
                        datalist.remove(itemInfoCustomList);
                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
