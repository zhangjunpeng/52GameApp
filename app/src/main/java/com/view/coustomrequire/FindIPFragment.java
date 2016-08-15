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
import com.view.coustomrequire.info.FindIPInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class FindIPFragment extends CoustomBaseFragment {

    private List<TextView> ipcooptypeTexts;
    private List<TextView> ipcatTexts;
    private List<TextView> ipstyleTexts;

    private List<NameVal> ipcooptypes;
    private List<NameVal> ipcats;
    private List<NameVal> ipstyles;

    public static List<NameVal> ipcoottype_sel;
    public static List<NameVal> ipcat_sel;
    public static List<NameVal> ipstyle_sel;
    public static String other;



    private CustomizedData customizedData=CustomizedData.getInstance();

    private FindIPInfo findIPInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipcooptypes=customizedData.getMap().get("ipcooptype");
        ipcats=customizedData.getMap().get("ipcat");
        ipstyles=customizedData.getMap().get("ipstyle");

        ipcooptypeTexts=new ArrayList<>();
        ipcatTexts=new ArrayList<>();
        ipstyleTexts=new ArrayList<>();

        ipcoottype_sel=new ArrayList<>();
        ipcat_sel=new ArrayList<>();
        ipstyle_sel=new ArrayList<>();

        if (info!=null){
            findIPInfo= (FindIPInfo) info;

            ipcoottype_sel=findIPInfo.getIpcoopcat();
            ipcat_sel=findIPInfo.getIpcat();
            ipstyle_sel=findIPInfo.getIpstyle();
            other=itemInfo.getNote();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_findip,null);
        return view;
    }

    private LinearLayout coopcatlinear;
    private LinearLayout iptypelinear;
    private LinearLayout stylelinear1;
    private LinearLayout stylelinear2;
    private EditText otherEdit;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coopcatlinear= (LinearLayout) view.findViewById(R.id.linear_coopcat);
        iptypelinear= (LinearLayout) view.findViewById(R.id.linear_iptype);
        stylelinear1= (LinearLayout) view.findViewById(R.id.linear1_ipstyle);
        stylelinear2= (LinearLayout) view.findViewById(R.id.linear2_ipstyle);
        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initTextView();
    }

    private void initTextView() {
        for (int i=0;i<coopcatlinear.getChildCount();i++){
            TextView textView= (TextView) coopcatlinear.getChildAt(i);
            ipcooptypeTexts.add(textView);
        }
        for (int i=0;i<iptypelinear.getChildCount();i++){
            TextView textView= (TextView) iptypelinear.getChildAt(i);
            ipcatTexts.add(textView);
        }
        for (int i=0;i<stylelinear1.getChildCount();i++){
            TextView textView= (TextView) stylelinear1.getChildAt(i);
            ipstyleTexts.add(textView);
        }
        for (int i=0;i<stylelinear2.getChildCount();i++){
            TextView textView= (TextView) stylelinear2.getChildAt(i);
            ipstyleTexts.add(textView);
        }

        initTextViewSelect(ipcooptypeTexts, ipcooptypes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ipcoottype_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = ipcoottype_sel.iterator();
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

        initTextViewSelect(ipcatTexts, ipcats, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ipcat_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = ipcat_sel.iterator();
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
        initTextViewSelect(ipstyleTexts, ipstyles, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ipstyle_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = ipstyle_sel.iterator();
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
                    other=otherEdit.getText().toString();
                }
            }
        });

        if (info!=null){
            initSelectedText(ipcatTexts,ipcat_sel);
            initSelectedText(ipcooptypeTexts,ipcoottype_sel);
            initSelectedText(ipstyleTexts,ipstyle_sel);
            otherEdit.setText(other);
        }

    }
}
