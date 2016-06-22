package com.test4s.myapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/2.
 */
public class BaseFragment extends Fragment {

    public static Fragment selectedFragment;
    public ArrayList<View> footviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSelectedFragment(this);
        footviews=new ArrayList<>();
    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(getActivity().getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public void setSelectedFragment(Fragment fragment){
        selectedFragment=fragment;
    }

    public void getFocus(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public Dialog showLoadingDialog(Context context){
        Dialog dialog=new Dialog(context,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();
        ImageView imageView= (ImageView) dialog.findViewById(R.id.image_loadingdialog);
        AnimationDrawable ad = (AnimationDrawable)imageView.getBackground();

        ad.start();
        return dialog;
    }
    public View getTextView(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.nomore,null);
//        TextView textView=new TextView(context);
//        textView.setGravity(Gravity.CENTER);
//        textView.setText("已无更多");
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.height=90;
//        textView.setLayoutParams(params);
//        textView.setTextSize(15);
        view.setLayoutParams(params);
        return view;
    }

    public void addFootView(ListView listView,View view){
        footviews.add(view);
        listView.addFooterView(view);
    }
    public void removeAllFootView(ListView listView){
        for (View view:footviews){
            listView.removeFooterView(view);
        }
       footviews.clear();
    }

}
