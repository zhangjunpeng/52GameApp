package com.view.Evaluation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;
import com.view.game.GameListActivity;
import com.app.delslidelistview.DeleListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class EvaluationListFragment extends Fragment {

   View view;

    private ListView deleListView;
    private ArrayList<GameInfo> gamelist;
    private Dialog dialog;
    private float density;
    private int windowWidth;
    private EvaluaAdapter adapter;

    private Button want_down;

    private String sdk;
    private ImageLoader imageLoader=ImageLoader.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gamelist=new ArrayList<>();


        adapter=new EvaluaAdapter(getActivity());
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_evaluationlist, container, false);
        deleListView= (ListView) view.findViewById(R.id.listview_pclist);
        deleListView.setAdapter(adapter);
        want_down= (Button) view.findViewById(R.id.want_download);

        initData("1");
        initListener();

        return view;
    }

    private void initListener() {
       want_down.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(), GameListActivity.class);
               startActivity(intent);
               getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
           }
       });
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("test/gamelist");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("p",p);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("pclist==="+result);
                jsonParser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initView();
            }
        });
    }

    private void initView() {
        adapter.notifyDataSetChanged();
        if (gamelist.size()==0){
            deleListView.setVisibility(View.GONE);
        }
    }

    private void jsonParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                JSONArray gameArray=data.getJSONArray("gameList");
                for (int i=0;i<gameArray.length();i++){
                    JSONObject info=gameArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_id(info.getString("game_id"));
                    gameInfo.setGame_name(info.getString("game_name"));
                    gameInfo.setGame_img(info.getString("game_img"));
                    gameInfo.setGame_grade(info.getString("game_grade"));
                    gameInfo.setGame_platform(info.getString("game_platform"));
                    gameInfo.setCreate_time(info.getString("create_time"));
                    gameInfo.setIs_test(info.getString("is_test"));
                    gameInfo.setGame_type(info.getString("game_type"));
                    gameInfo.setOnline(info.getInt("online"));
                    gameInfo.setEnabled(info.getInt("enabled"));
                    gameInfo.setSdk(info.getString("sdk"));
                    gameInfo.setGame_dev(info.getString("identity_cat"));
                    gamelist.add(gameInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class EvaluaAdapter extends BaseSwipeAdapter{
        private Activity context;


        public EvaluaAdapter(Activity context){
            this.context=context;
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
            viewHolder.pc= (TextView) convertView.findViewById(R.id.cancel_care_evalu_std);
            viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gameevalu_std);
            viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating_gameevalu_std);

            MyLog.i("SwipeLayout=="+swipeLayout.toString());
            //set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

//            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);
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

                @Override
                public void onStartClose(SwipeLayout layout) {
                    super.onStartClose(layout);
                    MyLog.i("onStartClose");

                }

            });
            final GameInfo gameInfo= (GameInfo) gamelist.get(position);

            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    intent.putExtra("ident_cat",gameInfo.getGame_dev());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    intent.putExtra("ident_cat",gameInfo.getGame_dev());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            imageLoader.displayImage(Url.prePic+gameInfo.getGame_img(),viewHolder.icon, MyDisplayImageOptions.getdefaultImageOptions());

            if (TextUtils.isEmpty(gameInfo.getGame_grade())) {
                viewHolder.gamerating.setVisibility(View.GONE);

            }else {
                viewHolder.gamerating.setVisibility(View.VISIBLE);

                imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),viewHolder.gamerating, MyDisplayImageOptions.getdefaultImageOptions());

            }

            viewHolder.info.setText(gameInfo.getGame_platform()+" / "+gameInfo.getGame_type()+"\n"+gameInfo.getCreate_time());
            viewHolder.name.setText(gameInfo.getGame_name());
            if ("0".equals(gameInfo.getIs_test())){
                //未评测
                viewHolder.pc.setText("评测游戏");
                viewHolder.pc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gameInfo.getSdk().equals("0")){
                            warningDialog("该游戏未集成SDK，无法评测");
                        }else if (gameInfo.getSdk().equals("1")){
                            if (gameInfo.getEnabled()==0){
                                showWarningDialog(gameInfo.getOnline()/60);
                            }else {
                                Intent intent=new Intent(getActivity(),StartPCActivity.class);
                                intent.putExtra("game_id",gameInfo.getGame_id());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            }

                        }
                    }
                });

            }else {
                viewHolder.pc.setText("评测详情");
                viewHolder.pc.setTextColor(Color.rgb(76,76,76));
                viewHolder.pc.setBackgroundResource(R.drawable.grayborder_button);
                viewHolder.pc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),PcDetailActivity.class);
                        intent.putExtra("game_id",gameInfo.getGame_id());
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                });
            }
            final int i=position;
            convertView.findViewById(R.id.delete_item_evalu_std).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLog.i("delete position ==="+i);

                    deleteGame(i);
                }
            });


            return convertView;
        }

        @Override
        public void fillValues(int position, View convertView) {

        }

        @Override
        public int getCount() {
            return gamelist.size();
        }

        @Override
        public Object getItem(int position) {
            return gamelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView pc;
            TextView info;
            ImageView gamerating;
            ImageView delete;
        }
    }

    class MyListAdapter extends BaseAdapter{

        Context context;

        public MyListAdapter(Context context){
            this.context=context;


        }

        @Override
        public int getCount() {
            return gamelist.size();
        }

        @Override
        public Object getItem(int position) {
            return gamelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.item_evaluationlist,parent,false);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_gameevalu);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gameevalu);
                viewHolder.pc= (TextView) convertView.findViewById(R.id.cancel_care_evalu);
                viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gameevalu);
                viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating_gameevalu);
                viewHolder.delete= (ImageView) convertView.findViewById(R.id.delete_item_evalu);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final GameInfo gameInfo= (GameInfo) gamelist.get(position);
            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    context.startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, GameDetailActivity.class);
                    intent.putExtra("game_id",gameInfo.getGame_id());
                    context.startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            imageLoader.displayImage(Url.prePic+gameInfo.getGame_img(),viewHolder.icon, MyDisplayImageOptions.getdefaultImageOptions());

            imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),viewHolder.gamerating, MyDisplayImageOptions.getdefaultImageOptions());

            viewHolder.info.setText(gameInfo.getGame_dev()+" / "+gameInfo.getGame_type()+"\n"+gameInfo.getCreate_time());
            if ("0".equals(gameInfo.getIs_test())){
                //未评测
                viewHolder.pc.setText("评测游戏");
                viewHolder.pc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gameInfo.getSdk().equals("0")){
                            warningDialog("该游戏未集成SDK，无法评测");
                        }else if (gameInfo.getSdk().equals("1")){
                            if (gameInfo.getEnabled()==0){
                                showWarningDialog(gameInfo.getOnline()/60);
                            }else {
                                Intent intent=new Intent(getActivity(),StartPCActivity.class);
                                intent.putExtra("game_id",gameInfo.getGame_id());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                            }

                        }

                    }
                });

            }else {
                viewHolder.pc.setText("评测详情");
                viewHolder.pc.setTextColor(Color.rgb(76,76,76));
                viewHolder.pc.setBackgroundResource(R.drawable.grayborder_button);
                viewHolder.pc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),PcDetailActivity.class);
                        intent.putExtra("game_id",gameInfo.getGame_id());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                });
            }
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteGame(position);
                    gamelist.remove(position);
//                    notifyDataSetChanged();
                    deleListView.setAdapter(new EvaluaAdapter(getActivity()));

                }
            });
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView pc;
            TextView info;
            ImageView gamerating;
            ImageView delete;
        }
    }

    private void deleteGame(int position) {
        GameInfo info=null;
        try {
            info=gamelist.get(position);

            gamelist.remove(position);
            deleListView.setAdapter(new EvaluaAdapter(getActivity()));

        }catch (Exception e){
            return;
        }
        MyLog.i("delete game "+info.getGame_name());

        BaseParams baseParams=new BaseParams("test/delgame");
        baseParams.addParams("game_id",info.getGame_id());
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
//                        CusToast.showToast(getActivity(),"删除成功", Toast.LENGTH_SHORT);

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
                if (gamelist.size()==0){
                    deleListView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showWarningDialog(int min){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText("游戏时长60分钟以上开启评测\n您当前游戏时长为"+min+"分钟");
        clear.setText("我知道了");
        channel.setVisibility(View.GONE);
        dialog.show();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void warningDialog(String mess){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting,null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        dialog.setContentView(view,params);
        TextView mes= (TextView) view.findViewById(R.id.message_dialog_setting);
        TextView channel= (TextView) view.findViewById(R.id.channel_dialog_setting);
        TextView clear= (TextView) view.findViewById(R.id.positive_dialog_setting);
        mes.setText(mess);
        clear.setText("我知道了");
        channel.setVisibility(View.GONE);
        dialog.show();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
