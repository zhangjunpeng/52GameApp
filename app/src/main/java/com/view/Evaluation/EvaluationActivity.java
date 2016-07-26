package com.view.Evaluation;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

public class EvaluationActivity extends BaseActivity {

    FragmentManager fragmentManager;
    Fragment fragment;

    private ImageView back;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evaluation);
        setImmerseLayout(findViewById(R.id.titlebar_evalua));

        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        title.setText("玩家评测");
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragment=new EvaluationListFragment();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contianer_evalua,fragment).commit();
    }

}
