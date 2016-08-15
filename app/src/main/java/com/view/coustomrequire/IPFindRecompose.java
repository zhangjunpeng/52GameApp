package com.view.coustomrequire;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private LinearLayout linear1_ip;
    private LinearLayout linear2_ip;
    private LinearLayout linear1_gametype;
    private LinearLayout linear2_gametype;
    private LinearLayout linear3_gametype;

    private LinearLayout linear_cusgamestages;

    private EditText otherEdit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private IPFindRecomposeInfo ipFindRecomposeInfo=new IPFindRecomposeInfo();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipsText=new ArrayList<>();
        gametypeText=new ArrayList<>();
        cusgamestageText=new ArrayList<>();

        ip_sel=new ArrayList<>();
        gametypes_sel=new ArrayList<>();
        cusgamestages_sel=new ArrayList<>();

        ips=customizedData.getMap().get("ipnames");
        gametypes=customizedData.getMap().get("gametype");
        cusgamestages=customizedData.getMap().get("cusgamestage");

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
        linear1_ip= (LinearLayout) view.findViewById(R.id.linear1_os);
        linear2_ip= (LinearLayout) view.findViewById(R.id.linear2_os);

        linear1_gametype= (LinearLayout) view.findViewById(R.id.linear1_gametype);
        linear2_gametype= (LinearLayout) view.findViewById(R.id.linear2_gametype);
        linear3_gametype= (LinearLayout) view.findViewById(R.id.linear3_gametype);

        linear_cusgamestages= (LinearLayout) view.findViewById(R.id.linear_cusgamestages);

        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initView();
    }

    private void initView() {
        for (int i=0;i<linear1_ip.getChildCount();i++){
            TextView textView= (TextView) linear1_ip.getChildAt(i);
            ipsText.add(textView);
        }
        if (ips.size()<=4){
            linear2_ip.setVisibility(View.GONE);
        }else {
            for (int i=0;i<linear2_ip.getChildCount();i++){
                TextView textView= (TextView) linear2_ip.getChildAt(i);
                ipsText.add(textView);
            }
        }

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

        initTextViewSelect(ipsText, ips, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ip_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
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
}
