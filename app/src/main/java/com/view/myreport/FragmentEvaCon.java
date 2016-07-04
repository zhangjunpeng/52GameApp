package com.view.myreport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class FragmentEvaCon extends Fragment {

    RecyclerView recyclerView;

    private List<ExpertEvaConInfo> infos;

    HomeAdapter adapter;
    ImageLoader imageLoader;
    private float density;

    String gameid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infos=new ArrayList<>();
        adapter=new HomeAdapter();
        imageLoader=ImageLoader.getInstance();

        gameid=getArguments().getString("gameid","");


        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView= (RecyclerView) inflater.inflate(R.layout.recyclerview,container,false);

        initData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        MyLog.i("FragmentEvaCon call");

        return recyclerView;
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("test/splreport");
        baseParams.addParams("game_id",gameid);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("report =="+result);
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
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void jsonParser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject splreport=data.getJSONObject("splreport");
                int version=splreport.getInt("version");

                if (version==2){
                    JSONObject rate=splreport.getJSONObject("rate");

                    EventBus.getDefault().post(new GameScoreEvent(rate.getString("game_grade"),rate.getString("score")));

                    JSONArray jsonArray=splreport.getJSONArray("main");
                    for (int i=0;i<jsonArray.length();i++){
                        ExpertEvaConInfo info=new ExpertEvaConInfo();
                        JSONObject object=jsonArray.getJSONObject(i);
                        if (object.has("type")){
                            String type= object.getString("type");
                            ArrayList<String> urls=new ArrayList<>();

                            try {
                                JSONArray array=object.getJSONArray("data");
                                for (int j=0;j<array.length();j++){
                                    String url=array.getString(j);
                                    urls.add(url);
                                }
                            }catch (JSONException e){
                            }
                            info.setGameshot(urls);

                            String name=object.getString("name");
                            info.setName(name);
                            info.setType(type);
                        }else {
                            String name=object.getString("name");
                            String mess=object.getString("data");
                            info.setData(mess);
                            info.setName(name);
                        }

                        infos.add(info);
                    }

                }else if (version==1){

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_expertreport, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.content.setText("6666666666");
            ExpertEvaConInfo info=infos.get(position);
            holder.name.setText(info.getName());
            if (TextUtils.isEmpty(info.getType())){
                holder.content.setVisibility(View.VISIBLE);
                holder.content.setText(info.getData());
                holder.horizontalScrollView.setVisibility(View.GONE);
            }else {
                holder.content.setVisibility(View.GONE);
                holder.horizontalScrollView.setVisibility(View.VISIBLE);
                holder.horizontalScrollView.setHorizontalScrollBarEnabled(false);
                addImage(holder.horizontalScrollView,info.getGameshot(),info.getType());
            }
        }

        @Override
        public int getItemCount() {
            return infos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView content;
            HorizontalScrollView horizontalScrollView;
            public MyViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.name_item_expertreport);
                content= (TextView) itemView.findViewById(R.id.content_item_expertreport);
                horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.imageContinar_expertreport);
            }
        }
    }

    private void addImage(HorizontalScrollView horizontalScrollView, ArrayList<String> gameshot,String type) {
        LinearLayout linearLayout=new LinearLayout(getActivity());
        if (type.equals("hori")){
            for (int i=0;i<gameshot.size();i++){
                MyLog.i("imageUrl=="+Url.prePic+gameshot.get(i));

//                ImageView imageView= (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.image_hori,null);
                ImageView imageView=new ImageView(getActivity());

                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(263*density), (int) (148*density));

                if (i==0){
                    params.leftMargin= (int) (15*density);
                }else {
                    params.leftMargin= 0;

                }


                imageLoader.displayImage(Url.prePic+gameshot.get(i),imageView, MyDisplayImageOptions.getshotOptions());
                linearLayout.addView(imageView,params);
            }
        }else {
            for (int i=0;i<gameshot.size();i++){
                MyLog.i("imageUrl=="+Url.prePic+gameshot.get(i));

//                ImageView imageView= (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.image_ver,null);
                ImageView imageView=new ImageView(getActivity());


                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(160*density), (int) (238*density));
                if (i==0){
                    params.leftMargin= (int) (15*density);
                }else {
                    params.leftMargin= (int) (6.66*density);

                }

                imageLoader.displayImage(Url.prePic+gameshot.get(i),imageView, MyDisplayImageOptions.getshotOptions());
                linearLayout.addView(imageView,params);
            }
        }
        if (horizontalScrollView.getChildCount()!=0){
            horizontalScrollView.removeAllViews();
        }
        horizontalScrollView.addView(linearLayout);
    }


}
