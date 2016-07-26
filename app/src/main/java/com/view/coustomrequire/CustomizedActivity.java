package com.view.coustomrequire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test4s.myapp.R;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;

public class CustomizedActivity extends AppCompatActivity {


    private FancyCoverFlow fancyCoverFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized);
        fancyCoverFlow= (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);


        initFancyCoverFlow();
    }

    private void initFancyCoverFlow() {
        fancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter(this,3));
        fancyCoverFlow.setUnselectedAlpha(1.0f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.3f);
        fancyCoverFlow.setSpacing(50);
        fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setScaleDownGravity(0.2f);
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);


    }
}
