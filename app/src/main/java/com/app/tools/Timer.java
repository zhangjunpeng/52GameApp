package com.app.tools;

import android.os.Handler;

/**
 * Created by Administrator on 2016/5/9.
 */
public class Timer extends Thread {
    private Handler handler;
    public Timer(Handler handler){
        this.handler=handler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5 * 1000);
                handler.sendEmptyMessage(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
