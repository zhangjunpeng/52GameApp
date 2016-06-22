package com.app.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CusToast {

    public static  void showToast(Context context,String mess,int lenth){
//        Toast toast=Toast.makeText(context,mess,lenth);
        if (context==null){
            return;
        }
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        View view= LayoutInflater.from(context).inflate(R.layout.custoast,null);
        TextView textView= (TextView) view.findViewById(R.id.text_toast);
        textView.setText(mess);
        toast.setView(view);
        toast.setDuration(lenth);
        toast.show();
    }
    public static void testShow(Context context,String mess,int lenth){
        if (MyApplication.DeBug){
            Toast toast=new Toast(context);
            toast.setGravity(Gravity.CENTER,0,0);
            View view= LayoutInflater.from(context).inflate(R.layout.custoast,null);
            TextView textView= (TextView) view.findViewById(R.id.text_toast);
            textView.setText(mess);
            toast.setView(view);
            toast.setDuration(lenth);
            toast.show();
        }
    }
}
