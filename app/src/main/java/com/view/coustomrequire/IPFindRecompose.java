package com.view.coustomrequire;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.IPFindRecomposeInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class IPFindRecompose extends CoustomBaseFragment{

    private List<TextView> ipsText;
    private List<TextView> gametypeText;
    private List<TextView> cusgamestageText;

    private List<NameVal> ips;
    private List<NameVal> gametypes;
    private List<NameVal> cusgamestages;

    public static List<NameVal> ip_sel;
    public static List<NameVal> gametypes_sel;
    public static List<NameVal> cusgamestages_sel;

    public static String otherStr;

    private LinearLayout linear1_gametype;
    private LinearLayout linear2_gametype;
    private LinearLayout linear3_gametype;

    private LinearLayout linear_cusgamestages;

    private EditText otherEdit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private IPFindRecomposeInfo ipFindRecomposeInfo=new IPFindRecomposeInfo();
    private LinearLayout linear_ipcoop;
    private float density;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipsText=new ArrayList<>();
        gametypeText=new ArrayList<>();
        cusgamestageText=new ArrayList<>();

        ip_sel=new ArrayList<>();
        gametypes_sel=new ArrayList<>();
        cusgamestages_sel=new ArrayList<>();
        otherStr="";

        ips=customizedData.getMap().get("ipnames");
        gametypes=customizedData.getMap().get("gametype");
        cusgamestages=customizedData.getMap().get("cusgamestage");

        getDensity();

        if (info!=null){
            ipFindRecomposeInfo= (IPFindRecomposeInfo) info;

            ip_sel=ipFindRecomposeInfo.getCoopip();
            gametypes_sel=ipFindRecomposeInfo.getGametype();
            cusgamestages_sel=ipFindRecomposeInfo.getGamestage();
            otherStr=itemInfo.getNote();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ipfindrecompose,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linear_ipcoop= (LinearLayout) view.findViewById(R.id.linear_ipcoop);

        linear1_gametype= (LinearLayout) view.findViewById(R.id.linear1_gametype);
        linear2_gametype= (LinearLayout) view.findViewById(R.id.linear2_gametype);
        linear3_gametype= (LinearLayout) view.findViewById(R.id.linear3_gametype);

        linear_cusgamestages= (LinearLayout) view.findViewById(R.id.linear_cusgamestages);

        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initView();
    }

    private void initView() {

        for (int i=0;i<linear1_gametype.getChildCount();i++){
            TextView textView= (TextView) linear1_gametype.getChildAt(i);
            gametypeText.add(textView);
        }
        for (int i=0;i<linear2_gametype.getChildCount();i++){
            TextView textView= (TextView) linear2_gametype.getChildAt(i);
            gametypeText.add(textView);
        }
        for (int i=0;i<linear3_gametype.getChildCount();i++){
            TextView textView= (TextView) linear3_gametype.getChildAt(i);
            gametypeText.add(textView);
        }

        for (int i=0;i<linear_cusgamestages.getChildCount();i++){
            TextView textView= (TextView) linear_cusgamestages.getChildAt(i);
            cusgamestageText.add(textView);
        }

        addIPCoop(ips);

        initTextViewSelect(gametypeText, gametypes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    gametypes_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = gametypes_sel.iterator();
                    while (it.hasNext()) {
                        NameVal nameVal1 = (NameVal) it.next();
                        if (nameVal1.getId().equals(nameVal.getId())) {
                            //移除当前的对象
                            it.remove();
                        }
                    }
                }
            }
        });

        initTextViewSelect(cusgamestageText, cusgamestages, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    cusgamestages_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = cusgamestages_sel.iterator();
                    while (it.hasNext()) {
                        NameVal nameVal1 = (NameVal) it.next();
                        if (nameVal1.getId().equals(nameVal.getId())) {
                            //移除当前的对象
                            it.remove();
                        }
                    }
                }
            }
        });

        otherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    otherStr=otherEdit.getText().toString();
                }
            }
        });

        if (info!=null){
            initSelectedText(ipsText,ip_sel);
            initSelectedText(gametypeText,gametypes_sel);
            initSelectedText(cusgamestageText,cusgamestages_sel);

            otherEdit.setText(otherStr);
        }
    }

    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void addIPCoop(List<NameVal> ips) {
        if(ips.size()==0){
            TextView noip=new TextView(getActivity());
            noip.setText("没有IP，请先上传IP");
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin= (int) (17*density);
            layoutParams.topMargin= (int) (15*density);
            layoutParams.bottomMargin= (int) (15*density);
            noip.setTextColor(Color.RED);
            linear_ipcoop.addView(noip,layoutParams);
            return;
        }

        int lines=ips.size()/4;
        int yip=ips.size()%4;

        for (int i=0;i<lines;i++){
            LinearLayout view= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_linear_ipcoop,linear_ipcoop,false);
            for (int j=0;j<4;j++){
                int position=i*4+j;
                TextView textView= (TextView) view.getChildAt(j);
                NameVal nameVal=ips.get(position);
                textView.setTag(nameVal);
                textView.setText(nameVal.getVal());
                ipsText.add(textView);
            }
            linear_ipcoop.addView(view);
        }
        LinearLayout view1= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_linear_ipcoop,linear_ipcoop,false);
        for (int i=0;i<4;i++){
            TextView textView= (TextView) view1.getChildAt(i);
            int position=lines*4+i;
            if (i>=yip){
                textView.setVisibility(View.INVISIBLE);
            }else {
                NameVal nameVal=ips.get(position);
                textView.setTag(nameVal);
                textView.setText(nameVal.getVal());
                ipsText.add(textView);
            }
        }
        linear_ipcoop.addView(view1);


        for (TextView textView:ipsText){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView _textView= (TextView) v;
                    _textView.setSelected(!_textView.isSelected());

                    NameVal nameVal= (NameVal) _textView.getTag();

                    if (_textView.isSelected()){
                        _textView.setTextColor(Color.WHITE);
                        ip_sel.add(nameVal);
                    }else {
                        _textView.setTextColor(Color.rgb(124, 124, 124));
                        Iterator it = ip_sel.iterator();
                        while (it.hasNext()) {
                            NameVal nameVal1 = (NameVal) it.next();
                            if (nameVal1.getId().equals(nameVal.getId())) {
                                //移除当前的对象
                                it.remove();
                            }
                        }
                    }
                }
            });

        }
    }
}
