package com.app.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/3/2.
 */
public class ClearWindows {

    public static  void clearInput(Context context,View view){
        view.clearFocus();
        InputMethodManager imm =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
