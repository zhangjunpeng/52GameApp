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

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.view.Identification.NameVal;
import com.view.coustomrequire.info.FindGameInfo;
import com.view.game.FiltParamsData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class FindGameFragment extends CoustomBaseFragment {

    CustomizedData customizedData=CustomizedData.getInstance();

    private List<NameVal> gamegrade;
    private List<NameVal> gametype;
    private List<NameVal> gamestage;
    private List<NameVal> issueregion;
    private List<NameVal> inssuecat;

    private LinearLayout linear1_gamegrade;
    private LinearLayout linear2_gamegrade;
    private LinearLayout linear1_gametype;
    private LinearLayout linear2_gametype;
    private LinearLayout linear3_gametype;
    private LinearLayout linear_gamestage;
    private LinearLayout linear_issueregion;
    private LinearLayout linear_inssuecat;
    private EditText otherEdit;


    private List<TextView> gradeTexts;
    private List<TextView> typeTexts;
    private List<TextView> stageTexts;
    private List<TextView> regionTexts;
    private List<TextView> catTexts;


    public static List<NameVal> gamegrade_select;
    public static List<NameVal> gametype_select;
    public static List<NameVal> gamestage_select;
    public static List<NameVal> region_select;
    public static List<NameVal> issuecat_select;
    public static String otherStr;


    private FindGameInfo findGameInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (customizedData.getMap()==null){
            MyLog.i("map null");
        }else if (customizedData.getMap().size()==0){
            MyLog.i("map size 0");
        }else {
            MyLog.i("map size "+customizedData.getMap().size());
        }
        gamegrade=customizedData.getMap().get("gamegrade");
        gametype=customizedData.getMap().get("gametype");
        gamestage=customizedData.getMap().get("gamestage");
        issueregion=customizedData.getMap().get("issueregion");
        inssuecat=customizedData.getMap().get("inssuecat");


        gradeTexts=new ArrayList<>();
        typeTexts=new ArrayList<>();
        stageTexts=new ArrayList<>();
        regionTexts=new ArrayList<>();
        catTexts=new ArrayList<>();

        gamegrade_select=new ArrayList<>();
        gametype_select=new ArrayList<>();
        gamestage_select=new ArrayList<>();
        region_select=new ArrayList<>();
        issuecat_select=new ArrayList<>();
        otherStr="";

        if (info!=null) {
            findGameInfo = (FindGameInfo) info;
            gamegrade_select=findGameInfo.getGamegrade();
            gametype_select=findGameInfo.getGametype();
            gamestage_select=findGameInfo.getGamestage();
            region_select=findGameInfo.getRegion();
            issuecat_select=findGameInfo.getIssuecat();
            otherStr=itemInfo.getNote();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_findgame,null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear1_gamegrade= (LinearLayout) view.findViewById(R.id.linear1_gamegrade);
        linear2_gamegrade= (LinearLayout) view.findViewById(R.id.linear2_gamegrade);
        linear1_gametype= (LinearLayout) view.findViewById(R.id.linear1_type);
        linear2_gametype= (LinearLayout) view.findViewById(R.id.linear2_type);
        linear3_gametype= (LinearLayout) view.findViewById(R.id.linear3_type);
        linear_gamestage= (LinearLayout) view.findViewById(R.id.linear_gamestage);
        linear_issueregion= (LinearLayout) view.findViewById(R.id.linear_issueregion);
        linear_inssuecat= (LinearLayout) view.findViewById(R.id.linear_inssuecat);
        otherEdit= (EditText) view.findViewById(R.id.edit_other);
        initView();
    }

    private void initView() {
        for (int i=0;i<linear1_gamegrade.getChildCount();i++){
            TextView textView= (TextView) linear1_gamegrade.getChildAt(i);
            gradeTexts.add(textView);
        }
        for (int i=0;i<linear2_gamegrade.getChildCount();i++){
            TextView textView= (TextView) linear2_gamegrade.getChildAt(i);
            gradeTexts.add(textView);
        }
        for (int i=0;i<linear1_gametype.getChildCount();i++){
            TextView textView= (TextView) linear1_gametype.getChildAt(i);
            typeTexts.add(textView);
        }
        for (int i=0;i<linear2_gametype.getChildCount();i++){
            TextView textView= (TextView) linear2_gametype.getChildAt(i);
            typeTexts.add(textView);
        }
        for (int i=0;i<linear3_gametype.getChildCount();i++){
            TextView textView= (TextView) linear3_gametype.getChildAt(i);
            typeTexts.add(textView);
        }

        for (int i=0;i<linear_gamestage.getChildCount();i++){
            TextView textView= (TextView) linear_gamestage.getChildAt(i);
            stageTexts.add(textView);
        }
        for (int i=0;i<linear_issueregion.getChildCount();i++){
            TextView textView= (TextView) linear_issueregion.getChildAt(i);
            regionTexts.add(textView);
        }
        for (int i=0;i<linear_inssuecat.getChildCount();i++){
            TextView textView= (TextView) linear_inssuecat.getChildAt(i);
            catTexts.add(textView);
        }
        if (gamegrade==null){
            MyLog.i("gamegrade null");
            return;
        }
        initTextViewSelect(gradeTexts, gamegrade, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());

                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    gamegrade_select.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = gamegrade_select.iterator();
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
        initTextViewSelect(typeTexts, gametype, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());
                NameVal nameVal= (NameVal) textView.getTag();
                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    gametype_select.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = gametype_select.iterator();
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
        initTextViewSelect(stageTexts, gamestage, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());
                NameVal nameVal= (NameVal) textView.getTag();
                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    gamestage_select.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = gamestage_select.iterator();
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
        initTextViewSelect(regionTexts, issueregion, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());
                NameVal nameVal= (NameVal) textView.getTag();
                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    region_select.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = region_select.iterator();
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
        initTextViewSelect(catTexts, inssuecat, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) v;
                textView.setSelected(!textView.isSelected());
                NameVal nameVal= (NameVal) textView.getTag();

                if (textView.isSelected()){
                    textView.setTextColor(Color.WHITE);
                    issuecat_select.add(nameVal);
                }else {
                    textView.setTextColor(Color.rgb(124, 124, 124));
                    Iterator it = issuecat_select.iterator();
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

        if (info!=null){
            initSelectedText(gradeTexts,gamegrade_select);
            initSelectedText(typeTexts,gametype_select);
            initSelectedText(stageTexts,gamestage_select);
            initSelectedText(regionTexts,region_select);
            initSelectedText(catTexts,issuecat_select);
            otherEdit.setText(otherStr);
        }


        otherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    otherStr=otherEdit.getText().toString();
                }
            }
        });


    }


}
