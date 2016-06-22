package com.view.s4server;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import com.squareup.picasso.Picasso;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.activity.BaseActivity;
import com.view.myattention.AttentionChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class IPDetailActivity extends BaseActivity implements View.OnClickListener{

    String id;

    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;

    ImageView back;
    TextView title;
    ImageView care_title;
    ImageView share;

    TextView name_title;
    ImageView icon;
    TextView name_ipdetail;
    TextView ipcat;
    TextView ipintro;
    ListView deslistView;
    ListView otheripListView;
    TextView all;
    ImageView care_detail;


    LinearLayout ipshotContinar;

    String ip_name;
    String ip_logo;

    String ip_cat;//类型
    String ip_style;//风格
    String uthority;//授权范围
    String over_time;//上市时间

    String ip_info;

    String video_url;

    List<String> ipshot;

    List<IPDesInfo> ipderivatives;
    List<IPSimpleInfo> otherIp;


    Toolbar toolbar;

    float density;


    RelativeLayout intro_re;
    boolean flag_showall=false;

    boolean focus=false;

    private LinearLayout div_image;
    private LinearLayout div_video;
    private LinearLayout div_des;
    private LinearLayout div_relate;

    private String html="<div id=\"youkuplayer\" style=\"width:996px;height:561px\"></div>\n" +
            "<script type=\"text/javascript\" src=\"http://player.youku.com/jsapi\">\n" +
            "    player = new YKU.Player('youkuplayer',{\n" +
            "        styleid: '0',\n" +
            "        client_id: '22f67ce1674f8f6e',\n" +
            "        vid: 'XMTU4MDI2MDEyNA',\n" +
            "        autoplay: true,\n" +
            "        show_related: true,\n" +
            "        embsig: 'VERSION_TIMESTAMP_SIGNATURE',\n" +
            "        events:{\n" +
            "            onPlayEnd: function(){ /*your code*/ }\n" +
            "        }\n" +
            "    });\n" +
            "    function playVideo(){\n" +
            "        player.playVideo();\n" +
            "    }\n" +
            "</script>";
    private String html2="<iframe height=187 width=332 src=\"http://player.youku.com/embed/XMTU4MDI2MDEyNA==\" frameborder=0 allowfullscreen></iframe>";


    private ImageLoader imageloder=ImageLoader.getInstance();
//    private YoukuBasePlayerManager basePlayerManager;
//    private YoukuPlayer youkuPlayer;
//    private YoukuPlayerView youkuvideo;
//    private JCVideoPlayerStandard videoPlayerStandard;
    private WebView webView;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_ipdetail);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#252525"));


        view=findViewById(R.id.coordinatorlayout_ipdetail);
        view.setVisibility(View.INVISIBLE);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collToolbar_ipDetail);
        appBarLayout= (AppBarLayout) findViewById(R.id.appbar_ipdetail);
        ipshotContinar= (LinearLayout) findViewById(R.id.imageContinar_ipdetail);
        back= (ImageView) findViewById(R.id.back_ipdetail);
        name_title= (TextView) findViewById(R.id.title_ipdetail);
        care_title= (ImageView) findViewById(R.id.care_ipdetatil);
        toolbar= (Toolbar) findViewById(R.id.toolbar_ipDetail);

        icon= (ImageView) findViewById(R.id.roundImage_ipdetail);
        name_ipdetail= (TextView) findViewById(R.id.name_ipdetail);
        ipcat= (TextView) findViewById(R.id.cat_ipdetail);
        ipintro= (TextView) findViewById(R.id.introduction_ipdetail);
        deslistView= (ListView) findViewById(R.id.ipdes_ipdetail);
        otheripListView= (ListView) findViewById(R.id.relatedip_ipdetail);
        intro_re= (RelativeLayout) findViewById(R.id.intro_ipdetail_re);
        all= (TextView) findViewById(R.id.all_ipdetail);
        care_detail= (ImageView) findViewById(R.id.connect_ip);

        div_image= (LinearLayout) findViewById(R.id.div_image_ipdetail);
        div_video= (LinearLayout) findViewById(R.id.div_video_ipdetail);
        div_des= (LinearLayout) findViewById(R.id.div_des_ipdetail);
        div_relate= (LinearLayout) findViewById(R.id.div_relate_ipdetail);

        webView= (WebView) findViewById(R.id.video_ipdetail);
//        WebSettings settings = webView.getSettings();
////WebView启用Javascript脚本执行
//        settings.setJavaScriptEnabled(true);//是否允许javascript脚本
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);//是否允许页面弹窗

//        webView.loadData(html2, "text/html; charset=UTF-8", null);

//        videoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.video_ipdetail);
//        videoPlayerStandard.setUp("http://player.youku.com/player.php/sid/XMTU4MDI2MDEyNA==/v.swf"
//                , "ip演示");
//        videoPlayerStandard.
//        initYouku();
//
//        VODPlayer  player = new VODPlayer(this, true);
//        videoContian.addView(player.getPlayRootLayout());
//
//        VideoLists videos= new VideoLists();
//        Video v1 = new Video();
//        v1.url = "http://player.youku.com/embed/XMTU4MzE1NTYyMA==";
//        v1.quality = "标清";
//        videos.addVideo(v1);
//        player.playVideo(videos);


        findViewById(R.id.back_ipdetail).setOnClickListener(this);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
//        webView.loadData("<iframe height=498 width=510 src=\"http://player.youku.com/embed/XMTQ5MjA1NTgwNA==\" frameborder=0 allowfullscreen></iframe>","html/text",null);
//        webView.loadUrl("http://player.youku.com/embed/XMTQ5MjA1NTgwNA==");
        settings.setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）

        id=getIntent().getStringExtra("id");
        initListener();
        initData(id);


    }

//    private void initYouku() {
//        basePlayerManager = new YoukuBasePlayerManager(this) {
//
//            @Override
//            public void setPadHorizontalLayout() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onInitializationSuccess(YoukuPlayer player) {
//                // TODO Auto-generated method stub
//                // 初始化成功后需要添加该行代码
//                addPlugins();
//
//                // 实例化YoukuPlayer实例
//                youkuPlayer = player;
//
//                // 进行播放
//                goPlay();
//            }
//
//            @Override
//            public void onSmallscreenListener() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onFullscreenListener() {
//                // TODO Auto-generated method stub
//
//            }
//        };
//        basePlayerManager.onCreate();
//
//        // 播放器控件
//        youkuvideo= (YoukuPlayerView) findViewById(R.id.video_ipdetail);
//
//
//        //控制竖屏和全屏时候的布局参数。这两句必填。
//        youkuvideo
//                .setSmallScreenLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//        youkuvideo.initialize(basePlayerManager);
//
//    }
//
//    private void goPlay() {
//        youkuPlayer.playVideo("XMTU4MjU5NDkyNA");
//
//    }

    private void initListener() {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
//                if (verticalOffset<-100*density){
//                    toolbar.setBackgroundColor(Color.argb(15,255,255,255));
//                }else {
//                    toolbar.setBackgroundColor(Color.argb( 0,37,37,37));
//                }
                if (verticalOffset>-200*density){
                    name_title.setVisibility(View.INVISIBLE);
                    care_title.setVisibility(View.INVISIBLE);
                }else {
                    name_title.setVisibility(View.VISIBLE);
                    care_title.setVisibility(View.VISIBLE);
                }
                if (scrollRange + verticalOffset == 0) {

                    isShow = true;
                } else if(isShow) {
                    isShow = false;
                }
            }
        });
        View.OnClickListener myListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyAccount.isLogin){
                    if (focus){
                        focus=false;
                        AttentionChange.removeAttention("5",id, IPDetailActivity.this);
                    }else {
                        focus=true;
                        AttentionChange.addAttention("5",id,IPDetailActivity.this);
                    }
                    changeCare(focus);
                }else {
                    //未登录
                    goLogin(IPDetailActivity.this);
                }

            }
        };
        care_title.setOnClickListener(myListener);
        care_detail.setOnClickListener(myListener);

        otheripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(IPDetailActivity.this,IPDetailActivity.class);
                intent.putExtra("id",otherIp.get(position).getId());
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onLowMemory() { // android系统调用
        super.onLowMemory();
//        basePlayerManager.onLowMemory();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        basePlayerManager.onResume();
        webView.loadUrl(video_url);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        basePlayerManager.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        videoPlayerStandard.releaseAllVideos();
//        toggleWebViewState(true);
        webView.onPause();

//        basePlayerManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        basePlayerManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initData(String id) {
        BaseParams baseParams=new BaseParams("index/ipdetail");
        baseParams.addParams("id",id);
        if (MyAccount.isLogin){
            baseParams.addParams("token",MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("IPDetail==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        parser(jsonObject.getString("data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                setContentView(R.layout.neterror);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parser(String result) {
        ipshot=new ArrayList<>();
        ipderivatives=new ArrayList<>();
        otherIp=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(result);
            Url.prePic=jsonObject.getString("prefixPic");
            JSONObject info=jsonObject.getJSONObject("info");
            ip_name=info.getString("ip_name");
            ip_logo=info.getString("ip_logo");
            ip_cat =info.getString("ip_cat");
            ip_style=info.getString("ip_style");
            uthority=info.getString("uthority");
            over_time=info.getString("over_time");
            ip_info=info.getString("ip_info");
            video_url=info.getString("video_url");
            String fo=info.getString("focus");
            if (fo.equals("false")||fo.equals("0")){
                focus=false;
            }else if(fo.equals("true")||fo.equals("1")){
                focus=true;
            }

            JSONArray ipshotArray=info.getJSONArray("ip_shot");
            if (ipshotArray!=null){
                MyLog.i("ip_shot not null");
                for (int i=0;i<ipshotArray.length();i++){
                    String ipshot_url=ipshotArray.getString(i);
                    ipshot.add(ipshot_url);
                }
            }else {
                MyLog.i("ip_shot null");
            }

            JSONArray derivatives=info.getJSONArray("derivative");
            if (derivatives!=null){
                MyLog.i("derivatives not null");
            for (int i=0;i<derivatives.length();i++){
                JSONObject ip=derivatives.getJSONObject(i);
                IPDesInfo ipDesInfo=new IPDesInfo();
                ipDesInfo.setName(ip.getString("name"));
                ipDesInfo.setYear(ip.getString("year"));
                ipDesInfo.setMonth(ip.getString("month"));
                ipDesInfo.setLogo(ip.getString("logo"));
                ipDesInfo.setInfo(ip.getString("info"));
                ipderivatives.add(ipDesInfo);
            }
            } else {
                MyLog.i("derivatives null");
            }
            JSONArray others=info.getJSONArray("ip_list");
            if (others!=null) {
                MyLog.i("others not null");
                for (int i = 0; i < others.length(); i++) {
                    IPSimpleInfo ipSimpleInfo = new IPSimpleInfo();
                    JSONObject ip = others.getJSONObject(i);
                    ipSimpleInfo.setId(ip.getString("id"));
                    ipSimpleInfo.setIp_name(ip.getString("ip_name"));
                    ipSimpleInfo.setLogo(ip.getString("ip_logo"));
                    ipSimpleInfo.setIp_cat(ip.getString("ip_cat"));
                    ipSimpleInfo.setIp_style(ip.getString("ip_style"));
                    ipSimpleInfo.setUthority(ip.getString("uthority"));
                    otherIp.add(ipSimpleInfo);
                }
            }else {
                MyLog.i("others null");
            }
            MyLog.i("ipdetail解析完成");


            initView();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void changeCare(boolean iscare){
        if (iscare){
            care_title.setImageResource(R.drawable.cared);
            care_detail.setImageResource(R.drawable.attention_has);
        }else {
            care_title.setImageResource(R.drawable.care);
            care_detail.setImageResource(R.drawable.attention);
        }
    }

    private void initView() {

        if (TextUtils.isEmpty(ip_info)){
        }else {

                ipintro.setText(ip_info);
                intro_re.setClickable(false);
                all.setVisibility(View.INVISIBLE);

        }
        MyLog.i("ipshot size=="+ipshot.size());
        if (ipshot.size()==0){
            div_image.setVisibility(View.GONE);
        }else {
            initIpshot();
        }

        if (ipderivatives.size()==0){
            div_des.setVisibility(View.GONE);
        }else {
            deslistView.setAdapter(new MyDesAdapter(this, ipderivatives));
            setListViewHeightBasedOnChildren(deslistView);
        }

        if (otherIp.size()==0){
            div_relate.setVisibility(View.GONE);
        }else {
            otheripListView.setAdapter(new MyIpListAdapter(this, otherIp));
            setListViewHeightBasedOnChildren(otheripListView);
        }
        if (focus){
            care_title.setImageResource(R.drawable.cared);
            care_detail.setImageResource(R.drawable.attention_has);
        }

        if (TextUtils.isEmpty(video_url)||video_url.equals("null")){
            div_video.setVisibility(View.GONE);
        }else {
//            webView.loadUrl(video_url);
            webView.loadUrl(video_url);
        }



        imageloder.displayImage(Url.prePic+ip_logo,icon, MyDisplayImageOptions.getroundImageOptions());

//        Picasso.with(this)
//                .load(Url.prePic+ip_logo)
//                .into(icon);
        name_ipdetail.setText(ip_name);
        name_title.setText(ip_name);
        ipcat.setText("类型 ："+ip_cat+"\n风格 ："+ip_style+"\n授权范围 ："+uthority+"\n上线时间 ："+over_time);

        view.setVisibility(View.VISIBLE);


    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ipdetail:
                finish();
                overridePendingTransition(R.anim.in_form_left,R.anim.out_to_right);

                break;
        }
    }

    class MyDesAdapter extends BaseAdapter{
        List<IPDesInfo> list;
        Context context;

        public MyDesAdapter(Context context, List<IPDesInfo> list){
            this.context=context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_ipderivative,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_ipdes);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_ipdes);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_ipdes);
                viewHolder.voertime= (TextView) convertView.findViewById(R.id.overtime_ipdes);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            IPDesInfo ipDesInfo=list.get(position);

            Picasso.with(context).load(Url.prePic+ipDesInfo.getLogo())
                    .into(viewHolder.icon);
            viewHolder.name.setText(ipDesInfo.getName());
            viewHolder.intro.setText("简介 ："+ipDesInfo.getInfo());
            viewHolder.voertime.setText("上线时间 ："+ipDesInfo.getYear()+"-"+ipDesInfo.getMonth());
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
            TextView voertime;
        }
    }


    private void initIpshot() {

        if (ipshot==null){
            MyLog.i("ipshot null");
            return;
        }
        MyLog.i("ipshot size=="+ipshot.size());
        for (int i=0;i<ipshot.size();i++){
            ImageView imageView=new ImageView(this);
            int height=getWindow().getAttributes().height;
            int width=getWindow().getAttributes().width;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(332*density),(int)(187*density));

            params.leftMargin= (int) (14*density);
            params.rightMargin= (int) (14*density);
            params.bottomMargin= (int) (6*density);
            imageView.setImageResource(R.drawable.a1);
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ipshotContinar.addView(imageView,params);
            MyLog.i("添加ImageView");
            Picasso.with(this).load(Url.prePic+ipshot.get(i))
                    .placeholder(R.drawable.a2)
                    .into(imageView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.loadUrl("");

    }
}
