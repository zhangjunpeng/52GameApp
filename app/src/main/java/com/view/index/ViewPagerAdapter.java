package com.view.index;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews;

    public ViewPagerAdapter(List<ImageView> imageViewList){
        imageViews=imageViewList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=null;
        imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView=imageViews.get(position);
        container.removeView(imageView);
    }

    public ViewPagerAdapter(){

    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
