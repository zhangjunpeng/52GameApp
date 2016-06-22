package com.view.Information;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.concurrent.Executors;

public class InfomaionDetailActivity extends BaseActivity implements View.OnClickListener {


    private EditText editText;
    private ImageView sendmes;
    private WebView webview;

    public static String prefixUrl;
    private String url;
    private String id;
    private LinearLayout continar;
    private View click_pop ;
    private View comment_pop;
    boolean flag=false;

    private ImageView back;

    private InputPopwindow inputPopwindow;
    private TextView send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_infomaion_detail);
        setImmerseLayout(findViewById(R.id.titlebar_infodetail));

        webview= (WebView) findViewById(R.id.webView_infodetail);
        continar= (LinearLayout) findViewById(R.id.contianer_infodetail);
        back= (ImageView) findViewById(R.id.back_infodetail);


        click_pop=LayoutInflater.from(this).inflate(R.layout.popwindow_click,null);
        comment_pop=LayoutInflater.from(this).inflate(R.layout.popwindow_comment,null);
        continar.addView(click_pop);
        id=getIntent().getStringExtra("id");
        url=getIntent().getStringExtra("url");

        send= (TextView) comment_pop.findViewById(R.id.send_popwindow);
        editText= (EditText) comment_pop.findViewById(R.id.edit_popwindow);
        send.setClickable(false);

        findViewById(R.id.back_infodetail).setOnClickListener(this);
        findViewById(R.id.share_infodetail).setOnClickListener(this);

        WebSettings settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);

//        webView.loadData("<iframe height=498 width=510 src=\"http://player.youku.com/embed/XMTQ5MjA1NTgwNA==\" frameborder=0 allowfullscreen></iframe>","html/text",null);
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (TextUtils.isEmpty(id)){
            continar.setVisibility(View.GONE);
            webview.loadUrl(url);
        }else {
            webview.loadUrl(prefixUrl+url);
            initData();
            initListener();

        }


    }

    private void initListener() {
        findViewById(R.id.back_infodetail).setOnClickListener(this);
        findViewById(R.id.share_infodetail).setOnClickListener(this);

        continar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyAccount.isLogin){
                    continar.removeAllViews();
                    inputPopwindow.showAtLocation(InfomaionDetailActivity.this.findViewById(R.id.contianer_infodetail),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    //  getFocus(inputPopwindow.editText);
                    popupInputMethodWindow();
                }else {
//                    CusToast.showToast(InfomaionDetailActivity.this,"没有登录，不能评论",Toast.LENGTH_SHORT);
                    goLogin(InfomaionDetailActivity.this);
                }
            }
        });
        inputPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                continar.addView(click_pop);
            }
        });
        //监听触屏事件
        inputPopwindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void initData() {

        //弹出窗口
        inputPopwindow=new InputPopwindow(InfomaionDetailActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendconment();
            }
        });
        inputPopwindow.setFocusable(true);
        inputPopwindow.setOutsideTouchable(false);
        inputPopwindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        inputPopwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置

    }
    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public void sendconment(){
        BaseParams baseParams=new BaseParams("news/addcomments");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("id",id);
        MyLog.i("id==="+id);
        baseParams.addParams("comment_content",inputPopwindow.editText.getText().toString());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("result"+result);
//                Toast.makeText(InfomaionDetailActivity.this,"")
                inputPopwindow.dismiss();
                webview.loadUrl(prefixUrl+url);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_infodetail:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }

    }

    private void showPopwindow() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(R.layout.popwindow_comment);

        builder.show();
    }
    private void popupInputMethodWindow() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }
}
