package com.view.Identification;

import android.app.Dialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.Config;
import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class IdentSubFirstFragment extends Fragment implements View.OnClickListener {

    private String cattype="2";

    private LinearLayout areaLayout;
    private LinearLayout resLayout;
    private LinearLayout busiLayout;
    private LinearLayout issCatLayout;
    private LinearLayout invesCatLayout;
    private LinearLayout companyLayout;

    private Dialog dialog;

    private IdentificationConfig config;

    private IdentParams params;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config=IdentificationConfig.getInstance();
        cattype=getArguments().getString("cattype","2");

        params=IdentParams.getInstance();
        params.setOsresSelected(new ArrayList<NameVal>());
        params.setIsscatSelected(new ArrayList<NameVal>());
        params.setInvesStageSelected(new ArrayList<NameVal>());
        params.setBuscatSelected(new ArrayList<NameVal>());
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
            case "5":

                break;
            case "6":

                break;
        }
    }

    private EditText companyName_edit;
    private ImageView icon_sub;
    private ImageView upimage;
    private TextView clicktoselect;
    private TextView size_text;
    private TextView area_text;
    private TextView invescat_text;
    private TextView title_resselect;
    private LinearLayout linear1_res;
    private LinearLayout linear2_res;
    private LinearLayout linear_buscat;
    private LinearLayout linear_isscat;
    private EditText companyInfo_edit;
    private TextView nextstep;

    private TextView error;

    private ScrollView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= (ScrollView) inflater.inflate(R.layout.fragment_ident_first,container,false);

        return view;
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        MyLog.i("type=="+cattype);
        areaLayout= (LinearLayout) view.findViewById(R.id.layout_area);
        resLayout= (LinearLayout) view.findViewById(R.id.layout_wbrescat);
        busiLayout= (LinearLayout) view.findViewById(R.id.layout_buscat);
        issCatLayout= (LinearLayout) view.findViewById(R.id.layout_issscat);
        invesCatLayout= (LinearLayout) view.findViewById(R.id.layout_invescat);
        companyLayout= (LinearLayout) view.findViewById(R.id.layout_companyinfo);

        companyName_edit= (EditText) view.findViewById(R.id.edit_companyname);
        icon_sub= (ImageView) view.findViewById(R.id.icon_iden_sub);
        upimage= (ImageView) view.findViewById(R.id.upimage_icon);
        clicktoselect= (TextView) view.findViewById(R.id.clicktoup_iden_sub);

        size_text= (TextView) view.findViewById(R.id.scale_ident_sub);
        area_text= (TextView) view.findViewById(R.id.area_text_iden_sub);
        invescat_text= (TextView) view.findViewById(R.id.invescat_text_iden_sub);

        title_resselect= (TextView) view.findViewById(R.id.name_wbrescat_ident_sub);
        linear1_res= (LinearLayout) view.findViewById(R.id.linear1_os);
        linear2_res= (LinearLayout) view.findViewById(R.id.linear2_os);

        linear_buscat= (LinearLayout) view.findViewById(R.id.linear_busicat);
        linear_isscat= (LinearLayout) view.findViewById(R.id.linear_issicat);

        companyInfo_edit= (EditText) view.findViewById(R.id.edit_companyinfo);

        nextstep= (TextView) view.findViewById(R.id.nextstep_first);

        error= (TextView) view.findViewById(R.id.text_error_ident);


        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方
            case "2":
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                break;
            case "3":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                initReslayout(config.getOsresList());

                break;
            case "4":
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                initReslayout(config.getStageList());

                break;
            case "5":
                areaLayout.setVisibility(View.GONE);
                companyLayout.setVisibility(View.GONE);
                resLayout.setVisibility(View.GONE);
                busiLayout.setVisibility(View.GONE);
                issCatLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);
                break;
            case "6":
                resLayout.setVisibility(View.GONE);
                invesCatLayout.setVisibility(View.GONE);

                initBusSelect();
                intIssSelect();
                break;
        }
        size_text.setOnClickListener(this);
        area_text.setOnClickListener(this);
        invescat_text.setOnClickListener(this);
        nextstep.setOnClickListener(this);
        super.onViewCreated(view,savedInstanceState);
    }

    private void initBusSelect() {
        final List<NameVal> buisCatList=config.getBuscatList();
        if (buisCatList==null){
            MyLog.i("buisCatList null");
            return;
        }
        for (int i=0;i<linear_buscat.getChildCount();i++){
            if (i>=buisCatList.size()){
                linear_buscat.getChildAt(i).setVisibility(View.INVISIBLE);
            }else {
                final TextView textView= (TextView) linear_buscat.getChildAt(i);
                final NameVal nameVal=buisCatList.get(i);
                textView.setText(nameVal.getVal());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setSelected(!textView.isSelected());
                        if (textView.isSelected()){
                            textView.setTextColor(Color.WHITE);
                            params.getBuscatSelected().add(nameVal);
                        }else {
                            textView.setTextColor(Color.rgb(124,124,124));
                            params.getBuscatSelected().remove(nameVal);

                        }
                    }
                });
            }
        }
    }
    private void intIssSelect(){
        List<NameVal> issCatList=config.getIsscatList();
        if (issCatList==null){
            return;
        }
        for (int i=0;i<linear_isscat.getChildCount();i++){
            if (i>=issCatList.size()){
                linear_isscat.getChildAt(i).setVisibility(View.INVISIBLE);
            }else {
                final TextView textView= (TextView) linear_isscat.getChildAt(i);
                final NameVal nameVal=issCatList.get(i);
                textView.setText(nameVal.getVal());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setSelected(!textView.isSelected());
                        if (textView.isSelected()){
                            textView.setTextColor(Color.WHITE);
                            params.getIsscatSelected().add(nameVal);
                        }else {
                            textView.setTextColor(Color.rgb(124,124,124));
                            params.getIsscatSelected().remove(nameVal);

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scale_ident_sub:
                showSelectDiaolog(size_text,config.getSizeList(),"size");
                break;
            case R.id.area_text_iden_sub:
                showSelectDiaolog(area_text,config.getAreaList(),"area");

                break;
            case R.id.invescat_text_iden_sub:
                showSelectDiaolog(invescat_text,config.getInvescatList(),"invescat");
                break;
            case R.id.nextstep_first:
                if ( checkParams()){
                    IdentSubSecondFragment fragment=new IdentSubSecondFragment();

                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction transition=fm.beginTransaction();
                    transition.replace(R.id.contianer_ident_sub,fragment).commit();
                    transition.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
                }else {
                    error.setVisibility(View.VISIBLE);
                    view.scrollTo(0,0);
                }

                break;
        }

    }

    private boolean checkParams() {
        params.setComppanyName(companyName_edit.getText().toString());
        params.setCompanyInfo(companyInfo_edit.getText().toString());
        if (TextUtils.isEmpty(params.getComppanyName())||TextUtils.isEmpty(params.getIconUrl())){
            return false;
        }
        switch (cattype){
            //2:开发者 3:外包 4:投资人 5:IP方 6:发行方

            case "2":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }

                break;
            case "3":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (params.getOsresSelected().size()==0){
                    return false;
                }
                break;
            case "4":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (TextUtils.isEmpty(params.getInvescat())||params.getInvesStageSelected().size()==0){
                    return false;
                }
                break;
            case "5":
                if (TextUtils.isEmpty(params.getCompanySize())){
                    return false;
                }
                break;
            case "6":
                if (TextUtils.isEmpty(params.getCompanySize())||TextUtils.isEmpty(params.getArea())||TextUtils.isEmpty(params.getCompanyInfo())){
                    return false;
                }
                if (params.getBuscatSelected().size()==0||params.getIsscatSelected().size()==0){
                    return false;
                }
                break;

        }

        return true;
    }

    private List<TextView> resText;

    public void initReslayout(List<NameVal> dataList){
        resText=new ArrayList<>();
        for (int i=0;i<linear1_res.getChildCount();i++){
            TextView textView= (TextView) linear1_res.getChildAt(i);
            resText.add(textView);
        }
        for (int i=0;i<linear2_res.getChildCount();i++){
            TextView textView= (TextView) linear2_res.getChildAt(i);
            resText.add(textView);
        }

        for (int i=0;i<resText.size();i++){
            final TextView textView=resText.get(i);
            NameVal nameVal=null;
            if (i>=dataList.size()){
                textView.setVisibility(View.GONE);
            }else {
                nameVal=dataList.get(i);
                textView.setText(nameVal.getVal());

            }
            final NameVal finalNameVal = nameVal;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setSelected(!textView.isSelected());

                    if (textView.isSelected()){
                        textView.setTextColor(Color.WHITE);
                        switch (cattype){
                            case "3":
                                params.getOsresSelected().add(finalNameVal);
                                break;
                            case "4":
                                params.getInvesStageSelected().add(finalNameVal);
                                break;
                        }
                    }else {
                        textView.setTextColor(Color.rgb(124,124,124));
                        switch (cattype){
                            case "3":
                                params.getOsresSelected().remove(finalNameVal);
                                break;
                            case "4":
                                params.getInvesStageSelected().remove(finalNameVal);
                                break;
                        }
                    }
                }
            });
        }
        
    }


    private void showSelectDiaolog(final TextView textView, final List<NameVal> dataList, final String type){

        if (dataList==null){
            MyLog.i("dataList null");
            return;
        }
        dialog=new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_ident);




        ListView listView= (ListView) dialog.findViewById(R.id.listview_dialog_select);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView==null){
                    convertView=new RelativeLayout(getActivity());
                    RelativeLayout layout= (RelativeLayout) convertView;
                    layout.setGravity(Gravity.CENTER);
                    textView=new TextView(getActivity());
                    textView.setTextSize(15);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.rgb(124,124,124));
                    textView.setBackgroundColor(Color.WHITE);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (52* Config.density));
                    layout.addView(textView,params);
                    convertView.setTag(textView);
                }else {
                    textView= (TextView) convertView.getTag();
                }
                NameVal nameVal=dataList.get(position);
                textView.setText(nameVal.getVal());

                return convertView;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("listView dianji ");
//                initTextview(textView,dataList.get(position));
                NameVal nameVal=dataList.get(position);
                textView.setText(nameVal.getVal());
                switch (type){
                    case "size":
                        params.setCompanySize(nameVal.getId());
                        break;
                    case "area":
                        params.setArea(nameVal.getId());
                        break;
                    case "invescat":
                        params.setInvescat(nameVal.getId());
                        break;
                }
                dialog.dismiss();
//                initNextStep();
            }
        });
        dialog.show();
    }

    private void initNextStep() {
        if (checkParams()){
            nextstep.setClickable(true);
            nextstep.setBackgroundResource(R.drawable.border_button_orange);

        }else {
            nextstep.setBackgroundResource(R.drawable.border_edit_comment);
            nextstep.setClickable(false);
        }
    }
}
