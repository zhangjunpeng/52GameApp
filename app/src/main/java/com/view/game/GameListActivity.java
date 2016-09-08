package com.view.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.app.tools.ScreenUtil;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameType;
import com.test4s.gdb.GameTypeDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.activity.ListActivity;
import com.view.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class GameListActivity extends BaseActivity implements View.OnClickListener{


    ImageView back;
    TextView title;
    ImageView search;

    private FiltParamsData filtParamsData=FiltParamsData.getInstance();


//    private TextView showall;

    private Button refreash;

    private String order="time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);


        order=getIntent().getStringExtra("order");


        setImmerseLayout(findViewById(R.id.title_gamelist));

        back= (ImageView) findViewById(R.id.back_titlebar);
        title= (TextView) findViewById(R.id.title_titlebar);
        search= (ImageView) findViewById(R.id.search_titlebar);
        ImageView backimg= (ImageView) findViewById(R.id.backimg_titlebar);

//        showall= (TextView) findViewById(R.id.showall_gamelist);



        title.setText("全部游戏");

        backimg.setImageResource(R.drawable.back);

        refreash= (Button) findViewById(R.id.refeash_gamelist);
//        refreash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ptrFrameLayout.setVisibility(View.VISIBLE);
//                ptrFrameLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ptrFrameLayout.autoRefresh();
//                    }
//                },100);
//            }
//        });

        back.setOnClickListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameListActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


        GameListFragment fragment=new GameListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("order",order);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contianer_gamelist,fragment).commit();


    }



    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
                /*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_titlebar:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);
                break;
        }
    }
    private void initPopList() {
        BaseParams baseParams=new BaseParams("api/selecttype");
        baseParams.addParams("type","all");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("popdata=="+result);
//                jsonparser(result);
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


    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        MyLog.i("Url==="+url);
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
    }


    private List searchTitle(){
        Query query = getGameTypeDao().queryBuilder()
                .build();
        return query.list();
    }

    private GameTypeDao getGameTypeDao() {
        return MyApplication.daoSession.getGameTypeDao();
    }

}
