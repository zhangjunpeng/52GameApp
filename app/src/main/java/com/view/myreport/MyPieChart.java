package com.view.myreport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tools.MyLog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyPieChart {

    private static float density;

    private static int[] color={Color.rgb(250, 105, 0),
                                Color.rgb(244, 134, 49),
                                Color.rgb(224, 228, 204),
                                Color.rgb(105, 210, 231),
                                Color.rgb(166, 219, 215)};

    private static ArrayList<Integer> colors=new ArrayList<>();
    public static void showChart(PieChart pieChart, List<Map<String,String>> list, String name, Activity context) {



        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;

        pieChart.setHoleColorTransparent(true);
//        pieChart.setHoleRadius(35f);  //半径
//        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

//        pieChart.setDescription("测试饼状图");

//         mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
//
        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setDrawSliceText(true);
        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false); // 手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText(name);  //饼状图中间的文字

        PieData pieData=getPieData(list);

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

//        List<String> names=new ArrayList<>();
//        List<Integer> legendColors=new ArrayList<>();
//        for (int m=0;m<list.size();m++){
//            Map<String,String> map=list.get(m);
//            names.add(map.get("name"));
//            legendColors.add(color[m]);
//        }
//        MyLog.i("names==="+names.toString());
        Legend mLegend = pieChart.getLegend();  //设置比例图

//        mLegend.setFormSize(12f);
        mLegend.setEnabled(false);
        // 饼图颜色
//        mLegend.setCustom(legendColors,names);
//        mLegend.setComputedLabels(names);
//        mLegend.setComputedColors(legendColors);
//        mLegend.setExtra(colorlist,list);
//        MyLog.i("比例图颜色==="+legendColors);

//        mLegend.setComputedLabels(list);
//        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  //
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
//        if (names.size()>3){
//            mLegend.setXEntrySpace(10f);
//            mLegend.setYEntrySpace(5f);
//            mLegend.setTextSize(5*density);
//            mLegend.setYOffset(5*density);
//        }else {
//            mLegend.setXEntrySpace(10*density);
//            mLegend.setYEntrySpace(5f);
//            mLegend.setTextSize(10*density);
//            mLegend.setYOffset(10*density);
//        }



        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
        pieChart.invalidate();
    }

    /**
     *
     * @param count 分成几部分
     * @param range
     */
    public static PieData getPieData(List<Map<String,String>> list) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        ArrayList<Integer> piecolor = new ArrayList<Integer>();

        //  饼图数据
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> map=list.get(i);
//            xValues.add(map.get("name"));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
            xValues.add("");
            float val=Float.parseFloat(map.get("num"));
            yValues.add(new Entry(val,i));
        }


        //
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);

        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        piecolor.add(Color.rgb(250, 105, 0));
        piecolor.add(Color.rgb(244, 134, 49));

        piecolor.add(Color.rgb(224, 228, 205));

        piecolor.add(Color.rgb(105, 210, 231));

        piecolor.add(Color.rgb(167, 219, 217));
        piecolor.add(Color.rgb(255, 204, 190));
        piecolor.add(Color.rgb(178, 228, 251));
        piecolor.add(Color.rgb(139, 194, 74));

        piecolor.add(Color.rgb(121,85,73));
        piecolor.add(Color.rgb(254, 193, 6));
        piecolor.add(Color.rgb(98, 123, 145));
        piecolor.add(Color.rgb(0, 151, 136));

        piecolor.add(Color.rgb(158, 158, 158));
        piecolor.add(Color.rgb(114, 114, 114));
        piecolor.add(Color.rgb(202, 16, 168));

        piecolor.add(Color.rgb(20, 47, 189));
        piecolor.add(Color.rgb(62, 89, 14));
        piecolor.add(Color.rgb(147, 189, 128));
        piecolor.add(Color.rgb(200, 156, 185));
        piecolor.add(Color.rgb(124, 106, 140));
        piecolor.add(Color.rgb(200, 16, 14));


//        MyLog.i("饼图颜色==="+piecolor.toString());
        pieDataSet.setColors(piecolor);

//        DisplayMetrics metrics = MyApplication.mcontext.getResources().getDisplayMetrics();
//        float px = 5 * (metrics.densityDpi / 160f);
//        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
}
