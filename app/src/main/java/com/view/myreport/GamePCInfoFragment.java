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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GamePCInfoFragment extends Fragment{
    View view;
    PieChart pieChart;
    List<Map<String,String>> piedatalist;
    String gameid;

    List<GamePCInfo> gamePCInfoList;
    int[] textid={R.id.text_gamepc1,R.id.text_gamepc2,R.id.text_gamepc3,R.id.text_gamepc4,R.id.text_gamepc5,R.id.text_gamepc6};
    List<TextView> textViewList;

    LinearLayout stars;

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
        final BaseParams baseParams=new BaseParams("test/gametest");
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
        GamePCInfo gamePCInfo=gamePCInfoList.get(i);
        piedatalist=gamePCInfo.getNum();
        MyLog.i("piedatalist==="+piedatalist.toString());
        MyPieChart.showChart(pieChart,piedatalist,gamePCInfo.getName(),getActivity());
        float score=Float.parseFloat(gamePCInfo.getScore());
        setStars(score);
    }

    private void setStars(float score) {
        for (int i=0;i<stars.getChildCount();i++){
            ImageView image= (ImageView) stars.getChildAt(i);
            int orangestar= (int) score;
            if (i<orangestar){
                image.setImageResource(R.drawable.star_question_1);
            }else if(i==orangestar){
                if (score-orangestar>=0.5){
                    image.setImageResource(R.drawable.question_star_half);
                }else {
                    image.setImageResource(R.drawable.star_question_0);
                }
            }else {
                image.setImageResource(R.drawable.star_question_0);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_game_pc_info,null);
        pieChart= (PieChart) view.findViewById(R.id.piechart_pcgameinfo);
        stars= (LinearLayout) view.findViewById(R.id.score_playerreport);
        initData();
        return view;
    }

    private void initTextView() {
        MyLog.i("size=="+gamePCInfoList.size());
        for (int i=0;i<gamePCInfoList.size();i++){
            TextView textView= (TextView) view.findViewById(textid[i]);
            GamePCInfo gamePCInfo=gamePCInfoList.get(i);
            textView.setText(gamePCInfo.getName());
            textViewList.add(textView);
            final int j=i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextView(j);
                    changePieChart(j);
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
            gamePCInfoList=new ArrayList<>();
            for (int i=0;i<data.length();i++){
                GamePCInfo gamePCInfo=new GamePCInfo();
                JSONObject object=data.getJSONObject(i);
                String na=object.getString("name");
                gamePCInfo.setName(na);
                gamePCInfo.setScore(object.getString("score"));
                JSONArray numarray=object.getJSONArray("num");
                List<Map<String,String>> numlist=new ArrayList<>();
                for (int j=0;j<numarray.length();j++){
                    JSONObject start=numarray.getJSONObject(j);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",start.getString("name"));
                    map.put("num",start.getString("num"));
                    numlist.add(map);
                }
                gamePCInfo.setNum(numlist);
                gamePCInfoList.add(gamePCInfo);
            }
        }
    }

    
}
