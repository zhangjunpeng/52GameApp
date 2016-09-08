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
import com.view.coustomrequire.info.IPFindTeamInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class IPFindTeam extends CoustomBaseFragment {
    private List<TextView> ipsText;
    private List<TextView> ipdevelopcatText;

    private List<NameVal> ips;
    private List<NameVal> ipdevelopcats;

    public static List<NameVal> ip_sel;
    public static List<NameVal> ipdevelopcats_sel;
    public static String otherStr;

    private LinearLayout linear_ipdevelopcat;
    private EditText otherEdit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private IPFindTeamInfo ipFindTeamInfo=new IPFindTeamInfo();
    private LinearLayout linear_ipcoop;
    private float density;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ips=customizedData.getMap().get("ipnames");
        ipdevelopcats=customizedData.getMap().get("ipdevelopcat");

        ipsText=new ArrayList<>();
        ipdevelopcatText=new ArrayList<>();

        ip_sel=new ArrayList<>();
        ipdevelopcats_sel=new ArrayList<>();
        otherStr="";

        getDensity();

        if (info!=null){
            ipFindTeamInfo= (IPFindTeamInfo) info;

            ip_sel=ipFindTeamInfo.getCoopip();
            ipdevelopcats_sel=ipFindTeamInfo.getIpdevelopcat();
            otherStr=itemInfo.getNote();

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ipfindteam,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear_ipcoop= (LinearLayout) view.findViewById(R.id.linear_ipcoop);
        linear_ipdevelopcat= (LinearLayout) view.findViewById(R.id.linear_ipdevelopcat);
        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initView();
    }

    private void initView() {

        for (int i=0;i<linear_ipdevelopcat.getChildCount();i++){
            TextView textView= (TextView) linear_ipdevelopcat.getChildAt(i);
            ipdevelopcatText.add(textView);
        }

        addIPCoop(ips);

        initTextViewSelect(ipdevelopcatText, ipdevelopcats, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    ipdevelopcats_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    //一般我们使用
                    Iterator it = ipdevelopcats_sel.iterator();
                    while(it.hasNext()){
                        NameVal nameVal1 = (NameVal) it.next();
                        if(nameVal1.getId().equals(nameVal.getId())){
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
            initSelectedText(ipdevelopcatText,ipdevelopcats_sel);

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
