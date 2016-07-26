package com.app.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.view.Identification.NameVal;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class FiltPopWindow extends PopupWindow {
    private View conentView;
    private List<NameVal> dataList;
    private NameVal val;
    private Activity context;
    private ListView listView;

    public FiltPopWindow(Activity context, List<NameVal> datalist, NameVal val){
        conentView = LayoutInflater.from(context).inflate(R.layout.popwindow_filtlist,null);
        this.dataList=datalist;
        this.val=val;
        this.context=context;
        initView();
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        ColorDrawable cd=new ColorDrawable(Color.rgb(241,245,248));
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(cd);
        this.setAnimationStyle(R.style.AnimationPreview);
        this.setOnDismissListener(new poponDismissListener());
    }

    private void initView() {
        listView= (ListView) conentView.findViewById(R.id.list_filtlist_pop);
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
                ImageView line;
                if (convertView==null){
                    convertView=LayoutInflater.from(context).inflate(R.layout.item_list_filtpop,parent,false);
                }
                textView= (TextView) convertView.findViewById(R.id.texview);
                line= (ImageView) convertView.findViewById(R.id.line);
                NameVal nameVal=dataList.get(position);
                textView.setText(nameVal.getVal());
                if (val!=null){
                    if (nameVal.getVal().equals(val.getVal())){
                        textView.setTextColor(Color.rgb(255,156,0));
                        line.setBackgroundColor(Color.rgb(255,156,0));
                    }
                }
                return convertView;
            }

        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
            backgroundAlpha(0.5f);
        } else {
            this.dismiss();
        }
    }

    public void setOnclickListener(AdapterView.OnItemClickListener listener){
        listView.setOnItemClickListener(listener);
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

}
