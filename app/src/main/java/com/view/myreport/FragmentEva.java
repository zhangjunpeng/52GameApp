package com.view.myreport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
 * Created by Administrator on 2016/6/21.
 */
public class FragmentEva extends Fragment {

    private RecyclerView recyclerView;

    private HomeAdapter adapter;

    private List<ItemEva> itemEvas;

    private List<Map<String,String>> ratio_data;
    private List<Map<String,String>> rader_data;

    private RadarChart chart;

    private PieChart pieChart;
    private LinearLayout legend_linear;
    private float density;
    private String gameid;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new HomeAdapter();
        itemEvas=new ArrayList<>();

        gameid=getArguments().getString("gameid","");


        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView view= (NestedScrollView) inflater.inflate(R.layout.fragment_eva,null);

        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_fragment_eva);

//        recyclerView.setLayoutManager(new SyLinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        chart= (RadarChart) view.findViewById(R.id.radar_eva_fagment);
        pieChart= (PieChart) view.findViewById(R.id.ratio_eva_fagment);

        legend_linear= (LinearLayout) view.findViewById(R.id.legendlistcontianer_eva_fragment);

        pieChart.setDescription("");

        initData();

        initChart();

        return view;
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("test/ratereport");
        baseParams.addParams("game_id",gameid);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("result=="+result);
                jsonparser(result);
                MyLog.i("解析完成");
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
                setRader();
                MyPieChart.showChart(pieChart,ratio_data,"",getActivity());
                changeLegend();
            }
        });
    }

    private void setRader() {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i=0;i<rader_data.size();i++){
            Map<String,String> map=rader_data.get(i);
            xVals.add(map.get("name"));
            float val=Float.parseFloat(map.get("val"));
            yVals.add(new Entry(val,i));
        }
        RadarDataSet set1 = new RadarDataSet(yVals, "");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(xVals, sets);
//        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(true);
        chart.setData(data);

        chart.invalidate();
    }

    private void initChart() {
        chart.setDescription("");
        chart.setWebLineWidth(1.5f);
        chart.setWebLineWidthInner(0.75f);
        chart.setWebAlpha(100);


        Legend legend=chart.getLegend();
        legend.setEnabled(false);

//        chart.animateXY(
//                1400, 1400,
//                Easing.EasingOption.EaseInOutQuad,
//                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        xAxis.setYOffset(5);

        YAxis yAxis = chart.getYAxis();
         // Y坐标值字体样式
        // yAxis.setTypeface(tf);
        // Y坐标值标签个数
         yAxis.setLabelCount(6, true);
        // Y坐标值字体大小
//        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);
        // 是否显示y值在图表上
        yAxis.setDrawLabels(true);

        yAxis.setAxisMaxValue(10);

        yAxis.setAxisMinValue(0f);

    }

    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONObject rate_info=data.getJSONObject("rate_info");
                JSONArray jsonArray=rate_info.getJSONArray("main");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject evaObj=jsonArray.getJSONObject(i);
                    ItemEva itemEva=new ItemEva();
                    itemEva.setName(evaObj.getString("name"));
                    itemEva.setContent(evaObj.getString("content"));
                    List<ItemEvaTable> itemEvaTables=new ArrayList<>();
                    JSONArray array=evaObj.getJSONArray("item");
                    for (int j=0;j<array.length();j++){
                        JSONObject itemObjtable=array.getJSONObject(j);
                        ItemEvaTable itemEvaTable=new ItemEvaTable();
                        itemEvaTable.setTitle(itemObjtable.getString("title"));
                        itemEvaTable.setRate(itemObjtable.getString("rate"));
                        itemEvaTable.setScore(itemObjtable.getString("score"));
                        itemEvaTables.add(itemEvaTable);
                    }
                    itemEva.setItemEvaTables(itemEvaTables);
                    itemEvas.add(itemEva);
                }
                JSONObject ratio=rate_info.getJSONObject("ratio");
                JSONArray ratioarray=ratio.getJSONArray("num");
                ratio_data=new ArrayList<>();
               for (int i=0;i<ratioarray.length();i++){
                   Map<String,String> map=new HashMap<>();
                   JSONObject object=ratioarray.getJSONObject(i);
                   map.put("name",object.getString("name"));
                   map.put("num",object.getString("num"));
                   ratio_data.add(map);
               }
                JSONArray raderarray=rate_info.getJSONArray("radar");
                rader_data=new ArrayList<>();
                for (int i=0;i<raderarray.length();i++){
                    Map<String,String> map=new HashMap<>();
                    JSONObject object=raderarray.getJSONObject(i);
                    map.put("name",object.getString("name"));
                    map.put("val",object.getString("val"));
                    rader_data.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_expert_pj,parent,false));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ItemEva itemEva=itemEvas.get(position);
            holder.name.setText(itemEva.getName());
            for (int i=0;i<itemEva.getItemEvaTables().size();i++){
                TableRow tableRow= (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.tablerow_item_eva,null);

                ItemEvaTable itemEvaTable=itemEva.getItemEvaTables().get(i);
                TextView title= (TextView) tableRow.findViewById(R.id.title_row_item_expert);
                TextView rate= (TextView) tableRow.findViewById(R.id.rate_row_item_expert);
                TextView score= (TextView) tableRow.findViewById(R.id.score_row_item_expert);
                title.setText(itemEvaTable.getTitle());
                rate.setText(itemEvaTable.getRate());
                score.setText(itemEvaTable.getScore());
                TableLayout.LayoutParams params=new TableLayout.LayoutParams();
                holder.tableLayout.addView(tableRow,params);
            }
            if (TextUtils.isEmpty(itemEva.getContent())){
                holder.linear_content.setVisibility(View.GONE);
            }else {
                holder.content.setText(itemEva.getContent());
            }

        }

        @Override
        public int getItemCount() {
            return itemEvas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView name;
            TableLayout tableLayout;
            LinearLayout linear_content;
            TextView content;

            public MyViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.name_tablename_table_item_expert);
                tableLayout= (TableLayout) itemView.findViewById(R.id.table_eva_fragment);
                linear_content= (LinearLayout) itemView.findViewById(R.id.linear_dppc_item_expert);
                content= (TextView) itemView.findViewById(R.id.content__item_expert);
            }
        }
    }
    private void changeLegend() {
        MyLog.i("开始添加legend");
//        legend_linear.removeAllViews();
        int[] colors= pieChart.getData().getColors();

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width=0;
        params.weight=1;
        LinearLayout linear_con = null;
        for (int i=0;i<ratio_data.size();i++){
            Map<String,String> map=ratio_data.get(i);
//            MyLog.i("name=="+map.get("name"));
            if (i%4==0){
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
                view.setLayoutParams(params);

                linear_con.addView(view);

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
                view.setLayoutParams(params);

                linear_con.addView(view);
//                linear_con.addView(view,params);


            }

        }
    }
}
