package com.view.coustomrequire;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.test4s.myapp.Config;
import com.test4s.myapp.R;
import com.view.Identification.NameVal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/7/29.
 */
public class CoustomBaseFragment extends Fragment{

    public Object info;
    public ItemInfoCustomList itemInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null){
            itemInfo= (ItemInfoCustomList) bundle.getSerializable("info");
            info=itemInfo.getInfo();
        }
    }

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    public Dialog showSelectDiaolog(final List<NameVal> dataList, AdapterView.OnItemClickListener onItemClickListener){

        if (dataList==null){
            MyLog.i("dataList null");
            return null;
        }
        final Dialog dialog = new Dialog(getActivity());
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


        listView.setOnItemClickListener(onItemClickListener);
        dialog.show();
        return dialog;
    }

    public void initTextViewSelect(List<TextView> textViews, List<NameVal> datalist, View.OnClickListener onClickListener) {
        List<TextView> textViewList=new ArrayList<>();
        for (int i=0;i<textViews.size();i++){
            final TextView textView=textViews.get(i);
            if (i>=datalist.size()){
                textView.setVisibility(View.INVISIBLE);
                textViewList.add(textView);
            }else {
                NameVal nameVal=datalist.get(i);
                textView.setText(nameVal.getVal());
                textView.setOnClickListener(onClickListener);
                textView.setTag(nameVal);
            }
        }
        for (TextView textView:textViewList){
            textViews.remove(textView);
        }
    }
    public void showChooser(Fragment fragment) {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(target,"Select a file");
        try {
            fragment.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    public void initSelectedText(List<TextView> textViewList,List<NameVal> nameValList){
       List<String> idlist=getListFromList(nameValList);
        for (int i=0;i<textViewList.size();i++){
            TextView textView=textViewList.get(i);
            NameVal nameVal= (NameVal) textView.getTag();
            String id=nameVal.getId();
            if (idlist.contains(id)){
                textView.setSelected(true);
                textView.setTextColor(Color.WHITE);
            }
        }
    }


    private List<String> getListFromList(List<NameVal> nameVals){
        List<String> data=new ArrayList<>();
        for (NameVal nameVal:nameVals){
            data.add(nameVal.getId());
        }
        return data;
    }
}
