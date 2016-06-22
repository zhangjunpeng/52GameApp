package com.app.delslidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.tools.MyLog;

/**
 * Created by Administrator on 2016/3/17.
 */
public class DeleListView extends ListView {

    private int mScreenWidth;	// 屏幕宽度
    private int mDownX;			// 按下点的x值
    private int mDownY;			// 按下点的y值
    private int mDeleteBtnWidth;// 删除按钮的宽度

    private boolean isDeleteShown;	// 删除按钮是否正在显示

    private ViewGroup mPointChild;	// 当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;	// 当前处理的item的LayoutParams
    private int maginleft;
    boolean first=true;

    private GestureDetector gestureDetector;

    public interface ClickListener{
        public void click(String id);

    };

    public DeleListView(Context context) {
        super(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

//        initGesture(context);
    }



    public DeleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
//        initGesture(context);
    }

    public DeleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
//        initGesture(context);
    }

    private void initGesture(Context context) {
        setLongClickable(true);
        gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

//                float x = e2.getX() - e1.getX();
//                float y = e2.getY() - e1.getY();

                MyLog.i("distanceX"+distanceX);

                MyLog.i("distanceY"+distanceY);
//                if (x > 0) {
//                    //右滑
//
//                } else if (x < 0) {
//                    //左滑
//                    MyLog.i("左滑");
//                }
//                maginleft=maginleft;
                if (maginleft>mDeleteBtnWidth){
                    maginleft=mDeleteBtnWidth;
                }else if (maginleft>0){
                    maginleft=0;
                }
                mLayoutParams.leftMargin = -(int) maginleft;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);

                return true;
            }

        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDeleteShown){
            turnToNormal();
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (isDeleteShown){
//            turnToNormal();
//            return true;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MyLog.i("ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.i("ACTION_MOVE");
                if (first){
                    performActionDown(ev);
                    first=false;
                }
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                 MyLog.i("ACTION_UP");
                 performActionUp();
                 first=true;
        }
        return super.onTouchEvent(ev);
    }
    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {

        if (isDeleteShown){
            turnToNormal();
            mPointChild=null;
        }
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        int position=pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition();
        mPointChild = (ViewGroup) getChildAt(position);
        MyLog.i("当前点击 item=="+position);
        // 获取删除按钮的宽度
        if (mPointChild==null){
            return;
        }
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
//        MyLog.i("删除按钮的宽度==="+mDeleteBtnWidth);
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        // 为什么要重新设置layout_width 等于屏幕宽度
        // 因为match_parent时，不管你怎么滑，都不会显示删除按钮
        // why？ 因为match_parent时，ViewGroup就不去布局剩下的view
//        MyLog.i("屏幕宽度的宽度==="+mScreenWidth);
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);

    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        if (mPointChild==null){
            return super.onTouchEvent(ev);
        }
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            // 计算要偏移的距离

            int scroll = (nowX - mDownX);
            // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度

//            if (scroll>0){
                maginleft=maginleft+scroll;
//            }else {
//                maginleft=maginleft-15;
//            }

            if (maginleft<=-mDeleteBtnWidth){
                maginleft=-mDeleteBtnWidth;
            }else if (maginleft>=0){
                maginleft=0;
            }
            // 重新设置leftMargin
            MyLog.i("scroll=="+scroll);
            mLayoutParams.leftMargin = maginleft;
            mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            mDownX=nowX;
            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private void performActionUp() {
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认

        if (mLayoutParams!=null){
            if(-mLayoutParams.leftMargin >= mDeleteBtnWidth / 2) {
                mLayoutParams.leftMargin = -mDeleteBtnWidth;
                isDeleteShown = true;
            }else {
                turnToNormal();
            }
            mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        }


//        MyLog.i("action_up flag=="+flag);

    }

    /**
     * 变为正常状态
     */
    public void turnToNormal() {
        maginleft=0;
        mLayoutParams.leftMargin = maginleft;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShown = false;
    }

    /**
     * 当前是否可点击
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isDeleteShown;
    }

    OnTouchListener myListener=new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
    };
}
