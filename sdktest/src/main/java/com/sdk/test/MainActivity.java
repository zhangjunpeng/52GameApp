package com.sdk.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.tencent.tauth.Tencent;

public class MainActivity extends AppCompatActivity {
    Tencent mtencent;
    BaseUiListener baseUiListener=new BaseUiListener();
    String mes="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtencent=Tencent.createInstance("1104927639",getApplicationContext());




        findViewById(R.id.login_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtencent.login(MainActivity.this,"zz",baseUiListener);
            }
        });
        final Button button= (Button) findViewById(R.id.share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                button.setText(mes);

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                mes=mes+GameHttpConnection.doGet();
                Log.i("ZJP",mes);


            }
        }).start();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,baseUiListener);
    }


}
