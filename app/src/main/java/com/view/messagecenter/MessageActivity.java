package com.view.messagecenter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class MessageActivity extends BaseActivity implements View.OnClickListener {

    String id;
    String timestring;
    String contentstring;
    private TextView time;
    private TextView content;

    private ImageView back;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        time= (TextView) findViewById(R.id.time_mes);
        content= (TextView) findViewById(R.id.message_mes);
        id=getIntent().getStringExtra("id");
        timestring=getIntent().getStringExtra("time");
        setImmerseLayout(findViewById(R.id.titlebar_mess));

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        title.setText("消息中心");
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(this);

        initData(id);

    }

    private void initData(String id) {
        BaseParams baseParams=new BaseParams("user/msgdetail");
        baseParams.addParams("id",id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("read msg back==="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    JSONObject data=json.getJSONObject("data");
                    contentstring=data.getString("content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initView();
            }
        });
    }

    private void initView() {
        time.setText(timestring);
        content.setText(contentstring);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_savebar:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }
    }
}
