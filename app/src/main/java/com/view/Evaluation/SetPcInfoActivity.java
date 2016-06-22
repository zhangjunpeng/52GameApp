package com.view.Evaluation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.test4s.myapp.R;
import com.view.accountsetting.SetAddressFragment;
import com.view.accountsetting.SetJobFragment;

public class SetPcInfoActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Fragment fragment;
    String tag;

    String sex;
    String gameid;
    String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pc_info);
        fragmentManager=getSupportFragmentManager();
        tag=getIntent().getStringExtra("tag");
        gameid=getIntent().getStringExtra("game_id");
        switch (tag){
            case "age":
                age=getIntent().getStringExtra("age");
                fragment=new SetAgeFragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("game_id",gameid);
                bundle2.putString("age",age);
                fragment.setArguments(bundle2);
                break;
            case "sex":
                sex=getIntent().getStringExtra(tag);
                fragment=new SetSexFragment();
                Bundle bundle=new Bundle();
                bundle.putString("game_id",gameid);
                bundle.putString("sex",sex);
                fragment.setArguments(bundle);
                break;
            case "job":
                fragment=new SetJobFragment();
                Bundle bundle3=new Bundle();
                bundle3.putString("tag","pc");
                fragment.setArguments(bundle3);
                break;
            case "area":
                fragment=new SetAddressFragment();
                Bundle bundle4=new Bundle();
                bundle4.putString("tag","pc");
                fragment.setArguments(bundle4);
                break;
            case "phonebrand":
                String phond_id=getIntent().getStringExtra(tag);
                fragment=new SetPhoneBrandFragment();
                Bundle bundle1=new Bundle();
                bundle1.putString("game_id",gameid);
                bundle1.putString("phonebrand_id",phond_id);
                fragment.setArguments(bundle1);
                break;

        }
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        fragmentManager.beginTransaction().replace(R.id.contianer_pcgame,fragment).commit();
    }

}
