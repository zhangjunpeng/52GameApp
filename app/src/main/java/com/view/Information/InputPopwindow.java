package com.view.Information;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/3/15.
 */
public class InputPopwindow extends PopupWindow implements TextWatcher {
    public EditText editText;
    private TextView send;
    private TextView channel;
    private View view;
    private Context mcontext;
    public InputPopwindow(Activity context, View.OnClickListener myListener){
        super(context);
        mcontext=context;
        view= LayoutInflater.from(context).inflate(R.layout.popwindow_comment,null);
        editText= (EditText) view.findViewById(R.id.edit_popwindow);
        send= (TextView) view.findViewById(R.id.send_popwindow);
        channel= (TextView) view.findViewById(R.id.channel_popwindow);
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        send.setOnClickListener(myListener);

        editText.addTextChangedListener(this);

        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        MyLog.i("commond text change");
        if (s.length()>0){
            send.setTextColor(Color.rgb(255,156,0));
            send.setClickable(true);
        }else {
            send.setTextColor(Color.rgb(50,50,50));
            send.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
