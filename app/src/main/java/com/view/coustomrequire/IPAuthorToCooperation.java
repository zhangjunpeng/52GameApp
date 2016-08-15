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
import com.view.coustomrequire.info.IPFindCooperationInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class IPAuthorToCooperation extends CoustomBaseFragment {

    private List<TextView> ipsText;
    private List<TextView> ipauthtypeText;

    private List<NameVal> ips;
    private List<NameVal> ipauthtypes;

    public static List<NameVal> ip_sel;
    public static List<NameVal> ipauthtypes_sel;
    public static String otherStr;

    private LinearLayout linear1_ip;
    private LinearLayout linear2_ip;
    private LinearLayout linear_ipauthtype;
    private EditText otherEdit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private IPFindCooperationInfo ipFindCooperationInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ips=customizedData.getMap().get("ipnames");
        ipauthtypes=customizedData.getMap().get("ipauthtype");

        ipsText=new ArrayList<>();
        ipauthtypeText=new ArrayList<>();

        ip_sel=new ArrayList<>();
        ipauthtypes_sel=new ArrayList<>();

        if (info!=null){
            ipFindCooperationInfo= (IPFindCooperationInfo) info;
            ip_sel=ipFindCooperationInfo.getCoopip();
            ipauthtypes_sel=ipFindCooperationInfo.getIputhority();

            otherStr=itemInfo.getNote();

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_authortocoop,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear1_ip= (LinearLayout) view.findViewById(R.id.linear1_os);
        linear2_ip= (LinearLayout) view.findViewById(R.id.linear2_os);
        linear_ipauthtype= (LinearLayout) view.findViewById(R.id.linear_ipauth);
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

        for (int i=0;i<linear_ipauthtype.getChildCount();i++){
            TextView textView= (TextView) linear_ipauthtype.getChildAt(i);
            ipauthtypeText.add(textView);
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
        initTextViewSelect(ipauthtypeText, ipauthtypes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ipauthtypes_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = ipauthtypes_sel.iterator();
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
            initSelectedText(ipauthtypeText,ipauthtypes_sel);

            otherEdit.setText(otherStr);
        }
    }
}
