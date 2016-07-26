package com.view.coustomrequire;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test4s.myapp.R;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {

    private Activity context;
    private int size;
    FancyCoverFlowSampleAdapter(Activity context,int size){
        this.context=context;
        this.size=size;
    }

    @Override
    public View getCoverFlowItem(int position, View reusableView, ViewGroup parent) {
        CircleImageView view=null;
        if (reusableView==null){
            view=new CircleImageView(context);
        }else {
            view= (CircleImageView) reusableView;
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        view.setLayoutParams(new FancyCoverFlow.LayoutParams(300,300));

        view.setImageResource(R.drawable.require_icon);
        return view;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return R.drawable.require_icon;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
