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

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.FindTeamInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class FindTeam extends CoustomBaseFragment {

    private List<TextView> teamtypeText;
    private List<TextView> stageText;

    private List<NameVal> teamtypes;
    private List<NameVal> stages;


    public static List<NameVal> teamtype_sel;
    public static List<NameVal> stage_sel;
    public static String otherStr;

    private LinearLayout linear_teamtype;
    private LinearLayout linear1_stage;
    private LinearLayout linear2_stage;
    private EditText otherEdit;

    private CustomizedData customizedData=CustomizedData.getInstance();

    private FindTeamInfo findTeamInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamtypes=customizedData.getMap().get("teamtype");
        stages=customizedData.getMap().get("stage");

        teamtype_sel=new ArrayList<>();
        stage_sel=new ArrayList<>();
        teamtypeText=new ArrayList<>();
        stageText=new ArrayList<>();

        if (info!=null){
            findTeamInfo= (FindTeamInfo) info;

            teamtype_sel=findTeamInfo.getTeamtype();
            stage_sel=findTeamInfo.getStarge();
            otherStr=itemInfo.getNote();

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_findteam,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear_teamtype= (LinearLayout) view.findViewById(R.id.linear_teamtype);
        linear1_stage= (LinearLayout) view.findViewById(R.id.linear1_os);
        linear2_stage= (LinearLayout) view.findViewById(R.id.linear2_os);
        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initView();
    }

    private void initView() {
        for (int i=0;i<linear_teamtype.getChildCount();i++){
            TextView textView= (TextView) linear_teamtype.getChildAt(i);
            teamtypeText.add(textView);
        }
        for (int i=0;i<linear1_stage.getChildCount();i++){
            TextView textView= (TextView) linear1_stage.getChildAt(i);
            stageText.add(textView);
        }
        for (int i=0;i<linear2_stage.getChildCount();i++){
            TextView textView= (TextView) linear2_stage.getChildAt(i);
            stageText.add(textView);
        }


        initTextViewSelect(teamtypeText, teamtypes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    teamtype_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = teamtype_sel.iterator();
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
        initTextViewSelect(stageText, stages, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    stage_sel.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = stage_sel.iterator();
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
                otherStr=otherEdit.getText().toString();
            }
        });

        if (info!=null){
            initSelectedText(teamtypeText,teamtype_sel);
            initSelectedText(stageText,stage_sel);

            otherEdit.setText(otherStr);
        }
    }
}
