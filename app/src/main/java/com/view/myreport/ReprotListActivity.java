package com.view.myreport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.game.GameDetailActivity;
import com.app.delslidelistview.DeleListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReprotListActivity extends BaseActivity {

    List<GameReportInfo> gameReportInfos;

    ListView listview;
    MyListAdapter myadapter;

    ImageView back;
    TextView title;
    TextView save;
    private ImageLoader imageLoader=ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reprot_list);
        setImmerseLayout(findViewById(R.id.titlebar_reportlist));

        listview= (ListView) findViewById(R.id.listview_reportlist);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("我的报告");
        save.setVisibility(View.INVISIBLE);

        gameReportInfos=new ArrayList<>();
        myadapter=new MyListAdapter(this,gameReportInfos);
        listview.setAdapter(myadapter);

        initData(1+"");
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("test/reportlist");
        baseParams.addParams("p",p);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("My report list=="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        JSONObject data=json.getJSONObject("data");
                        JSONArray reports=data.getJSONArray("reportList");
                        for (int i=0;i<reports.length();i++){
                            JSONObject info=reports.getJSONObject(i);
                            GameReportInfo gamereportInfo=new GameReportInfo();
                            gamereportInfo.setGame_id(info.getString("game_id"));
                            gamereportInfo.setCreate_time(info.getString("create_time"));
                            gamereportInfo.setTest_total_score(info.getString("test_total_score"));
                            gamereportInfo.setGame_img(info.getString("game_img"));
                            gamereportInfo.setGame_name(info.getString("game_name"));
                            gamereportInfo.setGame_grade(info.getString("game_grade"));
                            gamereportInfo.setGame_test_nums(info.getString("game_test_nums"));
                            gamereportInfo.setGame_stage(info.getString("game_stage"));
                            gamereportInfo.setGame_platform(info.getString("game_platform"));
                            gamereportInfo.setGame_type(info.getString("game_type"));
                            gamereportInfo.setStatus(info.getString("status"));
                            gameReportInfos.add(gamereportInfo);
                        }
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
                initListView();
            }
        });

    }
    class MyListAdapter extends BaseSwipeAdapter {

        Context context;
        List<GameReportInfo> gameInfos;

        public MyListAdapter(Context context,List<GameReportInfo> gameInfos){
            this.context=context;
            this.gameInfos=gameInfos;

        }

        @Override
        public int getCount() {
            return gameInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return gameInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.item_evalist_swipe;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {

            View convertView= LayoutInflater.from(context).inflate(R.layout.item_evalua_list_std,null);
            SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(R.id.item_evalist_swipe);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_gameevalu_std);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gameevalu_std);
            viewHolder.bg= (TextView) convertView.findViewById(R.id.cancel_care_evalu_std);
            viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gameevalu_std);
            viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating_gameevalu_std);
            viewHolder.delete= (ImageView) convertView.findViewById(R.id.delete_item_evalu_std);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
//                    YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.delete_item_evalu_std));
                    MyLog.i("onOpen");

                }

                @Override
                public void onClose(SwipeLayout layout) {
                    super.onClose(layout);
                    MyLog.i("onClose");

                }
            });

            final GameReportInfo gameInfo= (GameReportInfo) gameReportInfos.get(position);
            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTestGame(gameInfo.getGame_id());
                }
            });
            if (position==gameInfos.size()-1){
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.height=1;
                convertView.findViewById(R.id.line_reportlist_std).setLayoutParams(layoutParams);
            }
//            Picasso.with(context)
//                    .load(Url.prePic+gameInfo.getGame_img())
//                    .placeholder(R.drawable.default_icon)
//                    .into(viewHolder.icon);
//            Picasso.with(context)
//                    .load(Url.prePic+gameInfo.getGame_grade())
//                    .into(viewHolder.gamerating);
            imageLoader.displayImage(Url.prePic+gameInfo.getGame_img(),viewHolder.icon, MyDisplayImageOptions.getdefaultImageOptions());
            imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),viewHolder.gamerating,MyDisplayImageOptions.getdefaultImageOptions());

            viewHolder.name.setText(gameInfo.getGame_name());

            viewHolder.info.setText(gameInfo.getGame_platform()+"\n"+gameInfo.getGame_stage()+" / "+gameInfo.getGame_type()+"\n"+timeToDate(gameInfo.getCreate_time()));
            switch (gameInfo.getStatus()){
                case "查看报告":
                    viewHolder.bg.setText(gameInfo.getStatus());
                    viewHolder.bg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(ReprotListActivity.this,GameReportActivity.class);
                            intent.putExtra("game_id",gameInfo.getGame_id());
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });
                    break;
                case "测评中":
                    viewHolder.bg.setText(gameInfo.getStatus());
                    viewHolder.bg.setTextColor(Color.rgb(76,76,76));
                    viewHolder.bg.setBackgroundResource(R.drawable.grayborder_button);
                    viewHolder.bg.setClickable(false);
                    break;
                case "审核失败":
                    viewHolder.bg.setText(gameInfo.getStatus());
                    viewHolder.bg.setTextColor(Color.rgb(76,76,76));
                    viewHolder.bg.setBackgroundResource(R.drawable.grayborder_button);
                    viewHolder.bg.setClickable(false);
                    break;
            }
            return convertView;
        }

        @Override
        public void fillValues(int position, View convertView) {

        }

        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView bg;
            TextView info;
            ImageView gamerating;
            ImageView delete;
        }
    }

    private void initListView() {
        if (gameReportInfos.size()==0){
            listview.setVisibility(View.GONE);
        }else {
            myadapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void deleteTestGame(String gameid){
        BaseParams baseParams=new BaseParams("test/deltestgame");
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("params==="+result);
                onCreate(null);
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

    public String timeToDate(String times){
        long time=Long.parseLong(times+"000");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date=new Date(time);
        return simpleDateFormat.format(date);
    }
}
