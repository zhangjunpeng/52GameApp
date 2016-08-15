package com.view.coustomrequire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;
import  com.view.coustomrequire.SwipeLayout;

import com.test4s.myapp.R;
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

public class MyAdapter extends RecyclerView.Adapter implements ItemHelpter.Callback{
    private Activity context;
    private LayoutInflater mInflater;
    private RecyclerView mRecycler;
    private List<ItemInfoCustomList> datalist;

    public MyAdapter(Activity context, List<ItemInfoCustomList> datalist) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.datalist=datalist;
    }

    @Override
    public SwipeLayout getSwipLayout(float x, float y) {
        return (SwipeLayout) mRecycler.findChildViewUnder(x,y);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler = recyclerView;
//        recyclerView.addOnItemTouchListener(new ItemHelpter(context,this));
        recyclerView.addOnItemTouchListener(new ItemHelpter(context,this));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_customized_list,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        final MyViewHolder holder= (MyViewHolder) viewholder;
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

        ViewTreeObserver vto = holder.relative.getViewTreeObserver();
        final RelativeLayout relativeLayout=holder.relative;
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height= relativeLayout.getHeight();
                MyLog.i("relative height=="+height);
                RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams) holder.delete.getLayoutParams();
                layoutParams1.topMargin=height/2;
                holder.delete.setLayoutParams(layoutParams1);

                RelativeLayout.LayoutParams layoutParams2= (RelativeLayout.LayoutParams) holder.line.getLayoutParams();
                layoutParams2.height=height;
                layoutParams2.topMargin=45;
                layoutParams2.bottomMargin=45;
                holder.line.setLayoutParams(layoutParams2);

            }
        });


        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChangeCustomInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",itemInfoCustomList);
                intent.putExtra("data",bundle);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomize(itemInfoCustomList);
            }
        });
    }

    private void deleteCustomize(final ItemInfoCustomList itemInfoCustomList) {
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
                        CusToast.showToast(context,"删除成功",Toast.LENGTH_SHORT);
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
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView name;
        TextView info;
        TextView stage;
        ImageView line;
        RelativeLayout relative;
        ImageView delete;


        public MyViewHolder(View view) {
            super(view);
            circleImageView = (CircleImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            info = (TextView) view.findViewById(R.id.info);
            stage = (TextView) view.findViewById(R.id.stage);
            line= (ImageView) view.findViewById(R.id.line);
            relative= (RelativeLayout) view.findViewById(R.id.relative);
            delete= (ImageView) view.findViewById(R.id.delete);
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

}