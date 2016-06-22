package com.view.myreport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class PlayerInfoFragment extends Fragment{
    View view;
    PieChart pieChart;
    List<Map<String,String>> piedatalist;
    Map<String,List<Map<String,String>>> playerInfo;
    List<String> tips;
    String gameid;

    private LinearLayout legend_linear;
    private LinearLayout legend_linear_contianer;
    private LinearLayout legend_contianer;

    int[] textid={R.id.text_playerpc1,R.id.text_playerpc2,R.id.text_playerpc3,R.id.text_playerpc4,R.id.text_playerpc5,R.id.text_playerpc6};
    List<TextView> textViewList;
    private float density;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameid=getArguments().getString("game_id","");
        textViewList=new ArrayList<>();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;

    }
    private void initData() {
        final BaseParams baseParams=new BaseParams("test/playertest");
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                MyLog.i("player report==="+result);
                res=result;
                try {
                    jsonparser(result);
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
                MyLog.i("解析完成");
                initTextView();
                changePieChart(0);

            }
        });
    }

    private void changePieChart(int i) {
        piedatalist=playerInfo.get(tips.get(i));
        MyLog.i("piedatalist==="+piedatalist.toString());
        MyPieChart.showChart(pieChart,piedatalist,tips.get(i),getActivity());
        changeLegend();
    }

    private void changeLegend() {
        MyLog.i("开始添加legend");
        legend_linear.removeAllViews();
        int[] colors= pieChart.getData().getColors();

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight=1;
        LinearLayout linear_con = null;
        for (int i=0;i<piedatalist.size();i++){
            Map<String,String> map=piedatalist.get(i);
            MyLog.i("name=="+map.get("name"));
            if (i%3==0){
                LinearLayout.LayoutParams linearParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearParams.topMargin= (int) (5*density);
                linear_con=new LinearLayout(getActivity());
                linear_con.setOrientation(LinearLayout.HORIZONTAL);
                linear_con.setGravity(Gravity.TOP);

                View view=LayoutInflater.from(getActivity()).inflate(R.layout.lengend,null);

                ImageView image= (ImageView) view.findViewById(R.id.image_lengend);
                TextView text= (TextView) view.findViewById(R.id.name_legend);

                image.setBackgroundColor(colors[i]);
                text.setText(map.get("name"));
                text.setTextColor(Color.rgb(180,180,180));

                linear_con.addView(view,params);

                legend_linear.addView(linear_con,linearParams);

            }else {
                MyLog.i("添加legend");
                LinearLayout linear=new LinearLayout(getActivity());
                linear.setId(i);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linear.setGravity(Gravity.TOP);

                View view=LayoutInflater.from(getActivity()).inflate(R.layout.lengend,null);

                ImageView image= (ImageView) view.findViewById(R.id.image_lengend);
                TextView text= (TextView) view.findViewById(R.id.name_legend);

                image.setBackgroundColor(colors[i]);
                text.setText(map.get("name"));
                text.setTextColor(Color.rgb(180,180,180));

//                linear.setGravity(Gravity.CENTER_HORIZONTAL);
//                linear.setLayoutParams(params);
                image.setBackgroundColor(colors[i]);
                text.setText(map.get("name"));
                text.setTextColor(Color.rgb(180,180,180));
                linear_con.addView(view,params);


            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_player_pc_info,null);
        pieChart= (PieChart) view.findViewById(R.id.piechart_pcplayerinfo);

        legend_linear= (LinearLayout) view.findViewById(R.id.legend_report);
        legend_linear_contianer= (LinearLayout) view.findViewById(R.id.legend_linear_contianer);
        legend_contianer= (LinearLayout) view.findViewById(R.id.legend_contianer1);
//        initpieChart();
        initData();
        return view;
    }

    private void initpieChart() {

        pieChart.setHoleColorTransparent(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(false); // 手动旋转
        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
    }

    private void initTextView() {
        for (int i=0;i<tips.size();i++){
            TextView textView= (TextView) view.findViewById(textid[i]);
            textView.setText(tips.get(i));
            textViewList.add(textView);
            final int j=i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextView(j);
                    changePieChart(j);
//                    pieChart.invalidate();
                }
            });
        }
        changeTextView(0);
    }

    private void changeTextView(int position) {
        for (int i=0;i<textViewList.size();i++){
            TextView textView=textViewList.get(i);
            if (i==position){
                textView.setSelected(true);
                textView.setTextColor(Color.WHITE);
            }else {
                textView.setSelected(false);
                textView.setTextColor(Color.rgb(76,76,76));
            }
        }

    }

    private void jsonparser(String res) throws JSONException {
        JSONObject json = new JSONObject(res);
        boolean su = json.getBoolean("success");
        int code = json.getInt("code");
        if (su && code == 200) {
            JSONArray data = json.getJSONArray("data");

            tips=new ArrayList<>();
            playerInfo=new HashMap<>();
            for (int i=0;i<data.length();i++){
                JSONObject object=data.getJSONObject(i);
                String na=object.getString("name");
                tips.add(na);
                JSONArray array=object.getJSONArray("num");
                List<Map<String,String>> numlist=new ArrayList<>();
                for (int j=0;j<array.length();j++){
                    JSONObject object1=array.getJSONObject(j);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object1.getString("name"));
                    map.put("num",object1.getString("num"));
                    numlist.add(map);
                }
                playerInfo.put(na,numlist);
            }
        }
    }
}
