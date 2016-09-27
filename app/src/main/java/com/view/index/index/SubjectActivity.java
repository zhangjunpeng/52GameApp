package com.view.index.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.IP;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.view.game.MyGameListAdapter;
import com.view.myattention.AttentionChange;
import com.view.s4server.CPDetailActivity;
import com.view.s4server.IPDetailActivity;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.OutSourceActivity;

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

public class SubjectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private String id;

    List<IndexItemInfo> dataList;
    List<IP> ipList;

    List<GameInfo> gameInfoList;

    private String headerUrl;
    private String name_s;
    private String info_s;

    private ImageView back;
    private TextView title;
    private TextView save;

    private String ident_cat;

    ImageLoader imageLoader=ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        id=getIntent().getStringExtra("id");

        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);
        title.setText("推荐专辑");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        initData();

    }

    private void initData() {
        BaseParams baseParams=new BaseParams("index/album");
        baseParams.addParams("id",id);
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("subject "+result);
                if (id.equals("1")) {
                    jsonparser(result);
                }else {
                    gamepaerser(result);
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

    private void gamepaerser(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            boolean su = jsonObject.getBoolean("success");
            int code = jsonObject.getInt("code");
            if (su && code == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray albumarray = data.getJSONArray("album");
                JSONObject album = albumarray.getJSONObject(0);
                headerUrl = album.getString("logo");
                name_s = "精选专辑";
                info_s = album.getString("intro");

                gameInfoList=new ArrayList<>();
                JSONArray gamearray=album.getJSONArray("content");
                for (int i=0;i<gamearray.length();i++){
                    JSONObject gameObj=gamearray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(gameObj.getString("game_name"));
                    gameInfo.setGame_grade(gameObj.getString("game_grade_img"));
                    gameInfo.setGame_type(gameObj.getString("game_type"));
                    gameInfo.setGame_stage(gameObj.getString("game_stage"));
                    gameInfo.setGame_platform(gameObj.getString("game_platform"));
                    gameInfo.setGame_img(gameObj.getString("game_img"));

                    JSONObject gamedata=gameObj.getJSONObject("s4_name");

                    gameInfo.setGame_id(gamedata.getString("user_id"));

                    gameInfo.setNorms(gameObj.getString("norms"));

                    if (MyAccount.isLogin){
                        gameInfo.setIscare(gamedata.getBoolean("iscare"));
                    }

                    gameInfoList.add(gameInfo);
                }
            }

            recyclerView.setAdapter(new MygGameAdapter(SubjectActivity.this,gameInfoList));
        }catch (Exception e){

        }
    }
    class MygGameAdapter extends RecyclerView.Adapter<MygGameAdapter.MyViewHolder> {

        private List<GameInfo> datalist;
        Activity context;

        ImageLoader imageLoader=ImageLoader.getInstance();
        final int HEAD_TYPE=0;
        final int NORMAL_TYPE=1;

        private View header;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==NORMAL_TYPE){
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                        context).inflate(R.layout.item_gamelist_fragment, parent, false));
                return holder;
            }else {
                header=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_recycler_subject,parent,false);
                return new MyViewHolder(header);
            }

        }

        public MygGameAdapter(Activity context,List<GameInfo> datalist) {
            this.context=context;
            this.datalist=datalist;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0){
                return  HEAD_TYPE;
            }
            return NORMAL_TYPE;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            if (position==0){
                ImageView imageView= (ImageView) header.findViewById(R.id.icon);
                TextView name= (TextView) header.findViewById(R.id.name);
                TextView info= (TextView) header.findViewById(R.id.info);
                name.setText(name_s);
                info.setText(info_s);

                imageLoader.displayImage(Url.prePic+headerUrl,imageView, MyDisplayImageOptions.getdefaultBannerOptions());

            }else {
                final GameInfo gameInfo = datalist.get(position-1);

                holder.name.setText(gameInfo.getGame_name());

                imageLoader.displayImage(Url.prePic + gameInfo.getGame_img(), holder.icon, MyDisplayImageOptions.getroundImageOptions());
                if (TextUtils.isEmpty(gameInfo.getGame_grade())) {
                    holder.grade.setVisibility(View.INVISIBLE);
                } else {
                    imageLoader.displayImage(Url.prePic + gameInfo.getGame_grade(), holder.grade, MyDisplayImageOptions.getIconOptions());
                }

                if ("1".equals(gameInfo.getNorms())) {
                    holder.norms.setVisibility(View.VISIBLE);
                } else{
                    holder.norms.setVisibility(View.INVISIBLE);
                }

                holder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyAccount.isLogin) {
                            if (gameInfo.iscare()) {
                                gameInfo.setIscare(false);
                                AttentionChange.removeAttention("1", gameInfo.getGame_id(), context);
                            } else {
                                gameInfo.setIscare(true);
                                AttentionChange.addAttention("1", gameInfo.getGame_id(), context);
                            }
                            if (gameInfo.iscare()) {
                                holder.care.setText("已关注");
                                holder.care.setSelected(true);
                            } else {
                                holder.care.setText("关注");
                                holder.care.setSelected(false);
                            }
                        }else {
                            Intent intent = new Intent(context, AccountActivity.class);
                            startActivity(intent);
                            context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    }
                });
                if (MyAccount.isLogin) {
                    if (gameInfo.iscare()) {
                        holder.care.setText("已关注");
                        holder.care.setSelected(true);
                    } else {
                        holder.care.setText("关注");
                        holder.care.setSelected(false);
                    }

                }

                String mess = gameInfo.getGame_type() + "/" + gameInfo.getGame_stage() + "\n" + gameInfo.getGame_platform();
                holder.info.setText(mess);

                if (MyAccount.isLogin) {
                    if (gameInfo.iscare()) {
                        holder.care.setText("已关注");
                        holder.care.setSelected(true);
                    } else {
                        holder.care.setText("关注");
                        holder.care.setSelected(false);
                    }
                    holder.care.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (gameInfo.iscare()) {
                                gameInfo.setIscare(false);
                                AttentionChange.removeAttention("1", gameInfo.getGame_id(), context);
                            } else {
                                gameInfo.setIscare(true);
                                AttentionChange.addAttention("1", gameInfo.getGame_id(), context);
                            }
                            if (gameInfo.iscare()) {
                                holder.care.setText("已关注");
                                holder.care.setSelected(true);
                            } else {
                                holder.care.setText("关注");
                                holder.care.setSelected(false);
                            }
                        }
                    });

                } else {
                    holder.care.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AccountActivity.class);
                            startActivity(intent);
                            context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    });

                }


                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goDetail(gameInfo.getGame_id(), gameInfo.getGame_dev());
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return datalist.size()+1;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView name;
            TextView info;
            TextView care;
            View item;
            TextView norms;
            ImageView grade;

            public MyViewHolder(View view) {
                super(view);
                if (view==header){
                    return;
                }
                icon = (ImageView) view.findViewById(R.id.icon);
                name = (TextView) view.findViewById(R.id.name);
                info = (TextView) view.findViewById(R.id.info);
                care = (TextView) view.findViewById(R.id.care);
                norms= (TextView) view.findViewById(R.id.norms);
                grade= (ImageView) view.findViewById(R.id.grade);
                item = view;
            }
        }
    }

    private void goDetail(String gameid,String ident_cat){
        Intent intent= new Intent(this,GameDetailActivity.class);
        intent.putExtra("game_id",gameid);
        intent.putExtra("ident_cat",ident_cat);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

    }
    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONArray albumarray=data.getJSONArray("album");
                JSONObject album=albumarray.getJSONObject(0);
                headerUrl=album.getString("logo");
                name_s="精选专辑";
                info_s=album.getString("intro");

                ident_cat=album.getString("cat");
                if (ident_cat.equals("5")){
                    ipList=new ArrayList<>();
                }else {
                    dataList=new ArrayList<>();
                }

                dataList=new ArrayList<>();
                JSONArray content=album.getJSONArray("content");
                for (int i=0;i<content.length();i++){

                    //2:开发者 4:投资人 5:IP方 6:发行方
                    JSONObject contentObj=content.getJSONObject(i);
                    JSONObject s4=contentObj.getJSONObject("s4_name");

                    if (ident_cat.equals("5")){
                        IP ipsimpleInfo=new IP();
                        ipsimpleInfo.setIp_name(s4.getString("ip_name"));
                        ipsimpleInfo.setIp_logo(s4.getString("ip_logo"));
                        ipsimpleInfo.setId(s4.getString("id"));
                        if (MyAccount.isLogin) {
                            ipsimpleInfo.setIscare(s4.getBoolean("iscare"));
                        }
                        String info_s="类    型："+s4.getString("ip_cat")
                                +"\n风    格："+s4.getString("ip_style")
                                +"\n授权范围："+s4.getString("uthority");
                        ipsimpleInfo.setIntrouduction(info_s);
                        ipList.add(ipsimpleInfo);
                    }else {
                        IndexItemInfo simpleInfo=new IndexItemInfo();
                        simpleInfo.setLogo(s4.getString("logo"));
                        simpleInfo.setIdentity_cat(ident_cat);
                        if (MyAccount.isLogin){
                            simpleInfo.setIscare(s4.getBoolean("iscare"));
                        }
                        String info_s="";
                        switch (simpleInfo.getIdentity_cat()){
                            case "2":
                                //cp
                                simpleInfo.setUser_id(s4.getString("id"));
                                simpleInfo.setCompany_name(s4.getString("name"));

                                info_s="所在区域："+s4.getString("area")
                                        +"\n公司规模："+s4.getString("company_scale");
                                break;
                            case "3":
                                //outsource
                                simpleInfo.setUser_id(s4.getString("user_id"));
                                simpleInfo.setCompany_name(s4.getString("company_name"));

                                info_s="所在区域："+s4.getString("area")
                                        +"\n公司规模："+s4.getString("company_scale")
                                        +"\n类    型："+s4.getString("outsource_name");

                                break;
                            case "4":
                                //investor
                                simpleInfo.setUser_id(s4.getString("user_id"));
                                simpleInfo.setCompany_name(s4.getString("company_name"));

                                info_s="所在区域："+s4.getString("area")
                                        +"\n机构类型："+s4.getString("invest_cat_name")
                                        +"\n投资阶段："+s4.getString("invest_stage_name");

                                break;
                            case "6":
                                //issue
                                simpleInfo.setUser_id(s4.getString("user_id"));
                                simpleInfo.setCompany_name(s4.getString("company_name"));

                                info_s="所在区域："+s4.getString("area")
                                        +"\n业务类型："+s4.getString("busine_cat_name")
                                        +"\n发行方式："+s4.getString("coop_cat_name");

                                break;
                        }
                        simpleInfo.setInfo(info_s);
                        dataList.add(simpleInfo);
                    }

                }
            }
            recyclerView.setAdapter(new HomeAdapter(SubjectActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        final int HEAD_TYPE=0;
        final int NORMAL_TYPE=1;

        private View header;

        public HomeAdapter(Context context){



        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_TYPE:
                    header=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_recycler_subject,parent,false);
                    return new MyViewHolder(header);
                case NORMAL_TYPE:
                    return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iplistfragment,parent,false));
                default:
                    return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iplistfragment,parent,false));

            }

        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (position==0){
                ImageView imageView= (ImageView) header.findViewById(R.id.icon);
                TextView name= (TextView) header.findViewById(R.id.name);
                TextView info= (TextView) header.findViewById(R.id.info);
                name.setText(name_s);
                info.setText(info_s);

                imageLoader.displayImage(Url.prePic+headerUrl,imageView, MyDisplayImageOptions.getdefaultBannerOptions());

            }else {
                String name="";
                String info="";
                String logo="";
                if (ident_cat.equals("5")){
                    final IP ip=ipList.get(position-1);
                    name=ip.getIp_name();
                    info=ip.getIntrouduction();
                    logo=ip.getIp_logo();


                    if (MyAccount.isLogin) {
                        if (ip.iscare()) {
                            holder.care.setText("已关注");
                            holder.care.setSelected(true);
                        } else {
                            holder.care.setText("关注");
                            holder.care.setSelected(false);
                        }
                    }

                    holder.care.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (MyAccount.isLogin) {
                                if (ip.iscare()) {
                                    ip.setIscare(false);
                                    AttentionChange.removeAttention("5", ip.getId(), SubjectActivity.this);
                                } else {
                                    ip.setIscare(true);
                                    AttentionChange.addAttention("5", ip.getId(), SubjectActivity.this);
                                }
                                if (ip.iscare()) {
                                    holder.care.setText("已关注");
                                    holder.care.setSelected(true);
                                } else {
                                    holder.care.setText("关注");
                                    holder.care.setSelected(false);
                                }
                            }else {
                                Intent intent=new Intent(SubjectActivity.this,AccountActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            }
                        }
                    });

                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(SubjectActivity.this, IPDetailActivity.class);
                            intent.putExtra("id",ip.getId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });

                }else {
                    final IndexItemInfo indexItemInfo=dataList.get(position-1);
                    name=indexItemInfo.getCompany_name();
                    info=indexItemInfo.getInfo();
                    logo=indexItemInfo.getLogo();
                    id=indexItemInfo.getUser_id();

                    if (MyAccount.isLogin) {
                        if (indexItemInfo.iscare()) {
                            holder.care.setText("已关注");
                            holder.care.setSelected(true);
                        } else {
                            holder.care.setText("关注");
                            holder.care.setSelected(false);
                        }

                    }else {
                        Intent intent=new Intent(SubjectActivity.this,AccountActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }


                    holder.care.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (MyAccount.isLogin) {
                                if (indexItemInfo.iscare()) {
                                    indexItemInfo.setIscare(false);
                                    AttentionChange.removeAttention(ident_cat, indexItemInfo.getUser_id(), SubjectActivity.this);
                                } else {
                                    indexItemInfo.setIscare(true);
                                    AttentionChange.addAttention(ident_cat, indexItemInfo.getUser_id(), SubjectActivity.this);
                                }
                                if (indexItemInfo.iscare()) {
                                    holder.care.setText("已关注");
                                    holder.care.setSelected(true);
                                } else {
                                    holder.care.setText("关注");
                                    holder.care.setSelected(false);
                                }
                            }else {
                                Intent intent=new Intent(SubjectActivity.this,AccountActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            }
                        }

                    });


                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=null;
                            switch (ident_cat){
                                //2:开发者3:外包 4:投资人 5:IP方 6:发行方

                                case "2":
                                    intent=new Intent(SubjectActivity.this, CPDetailActivity.class);
                                    intent.putExtra("user_id",indexItemInfo.getUser_id());
                                    intent.putExtra("identity_cat","2");
                                    break;
                                case "3":
                                    intent=new Intent(SubjectActivity.this, OutSourceActivity.class);
                                    intent.putExtra("user_id",indexItemInfo.getUser_id());
                                    intent.putExtra("identity_cat","3");
                                    break;
                                case "4":
                                    intent=new Intent(SubjectActivity.this, InvesmentDetialActivity.class);
                                    intent.putExtra("user_id",indexItemInfo.getUser_id());
                                    intent.putExtra("identity_cat","4");
                                    break;
                                case "6":
                                    intent=new Intent(SubjectActivity.this, IssueDetailActivity.class);
                                    intent.putExtra("user_id",indexItemInfo.getUser_id());
                                    intent.putExtra("identity_cat","6");
                                    break;
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });
                }
                holder.name.setText(name);
                holder.intro.setText(info);
                imageLoader.displayImage(Url.prePic+logo,holder.icon,MyDisplayImageOptions.getroundImageOptions());



            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0){
                return  HEAD_TYPE;
            }
            return NORMAL_TYPE;
        }

        @Override
        public int getItemCount() {
            if (ident_cat.equals("5")){
                return ipList.size()+1;
            }else {
                return dataList.size() + 1;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView icon;
            TextView name;
            TextView intro;
            TextView care;
            View view;
            public MyViewHolder(View itemView) {
                super(itemView);
                if (itemView==header){
                    return;
                }
                icon= (ImageView) itemView.findViewById(R.id.imageView_iplist);
                name= (TextView) itemView.findViewById(R.id.name_item_iplist);
                intro= (TextView) itemView.findViewById(R.id.introuduction_item_iplist);
                care= (TextView) itemView.findViewById(R.id.care_item_list);
                view=itemView;
            }
        }

    }

}
