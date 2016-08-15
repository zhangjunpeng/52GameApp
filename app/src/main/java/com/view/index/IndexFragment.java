package com.view.index;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;

import com.app.view.VerticalSwitchTextView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.gdb.DaoSession;
import com.test4s.gdb.IP;
import com.test4s.gdb.IPDao;
import com.test4s.gdb.IndexAdvert;
import com.test4s.gdb.IndexAdvertDao;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.gdb.IndexItemInfoDao;
import com.test4s.gdb.Order;
import com.test4s.gdb.OrderDao;
import com.test4s.myapp.MyApplication;
import com.test4s.net.BaseParams;
import com.view.activity.ListActivity;
import com.view.index.adapter.LayoutAdapter;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.Query;

/**
 * Created by Administrator on 2015/12/7.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{

    List<LinearLayout> content;

    LinearLayout continer;

    private RecyclerViewPager viewPager;

    LinearLayout whiteDots;

    static  Integer currentItem=0;

    Thread thread;

    float density;

    private static boolean first=true;
    private Map<String, List> map;
    private List<Order> orders;
    private List<IndexAdvert> indexAdvertses;
    private List<ImageView> imageViewList;
    private LayoutAdapter adapter;

    private List<FloatadInfo> floatadInfos;
    private List<AlbumadInfo> albumadInfos;

    private DaoSession daoSession;
    private Activity context;
    private ImageLoader imageLoader=ImageLoader.getInstance();

    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initView();
                    break;
            }
        }
    };


    private VerticalSwitchTextView switchTextView;

  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        if (thread==null){
//            thread=new Timer(handler);
//            thread.start();
//        }
        getDensity();
        daoSession= MyApplication.daoSession;

        context=getActivity();
        map=new HashMap<>();
        orders=new ArrayList<>();
        indexAdvertses=new ArrayList<>();
        imageViewList=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,null);


        viewPager= (RecyclerViewPager) view.findViewById(R.id.viewpager);
        whiteDots= (LinearLayout) view.findViewById(R.id.whitedot_linear);
        continer= (LinearLayout) view.findViewById(R.id.contianer_index);

        content=new ArrayList<>();

        view.findViewById(R.id.fx_fg_index).setOnClickListener(this);
        view.findViewById(R.id.ip_fg_index).setOnClickListener(this);
        view.findViewById(R.id.tz_fg_index).setOnClickListener(this);
        view.findViewById(R.id.wb_fg_index).setOnClickListener(this);

        switchTextView= (VerticalSwitchTextView) view.findViewById(R.id.switchtextview);
//        indexJsonParser=IndexJsonParser.getInstance();




        initListener();
        initData();

//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                getDataFromDB();
//                mhandler.sendEmptyMessage(0);
//            }
//        });

        return  view;
    }

    private void initListener() {

        viewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                setDot(newPosition);

            }
        });

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                MyLog.i("viewpager position=="+position);
////                MyLog.i("imageview count=="+imageViewList.size());
////                MyLog.i("viewpager count=="+viewPager.getChildCount());
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
    }
    public void getDensity(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    private void initData() {
        BaseParams params=new BaseParams("index/index.html");

        params.addSign();
        params.getRequestParams().setCacheMaxAge(1000*60*30);

        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            private String result=null;
            private boolean success=true;

            @Override
            public void onSuccess(String result) {
                MyLog.i("网络访问");
                this.result=result;
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                success=false;
//                getDataFromDB();
//                initViewPagerfromDB();
                CusToast.showToast(getActivity(),"网络出现问题", Toast.LENGTH_SHORT);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("~~~~~~~~index"+result);
//                indexJsonParser.jsonParser(result);
//                indexAdvertses=indexJsonParser.indexAdvertses;
                if (success){
                    MyLog.i("网络获取数据");
                    deletAll();
                    jsonParser(result);
                    initView();
                    initViewPager();
                }else {

                }

            }
        });
    }

    private void initViewPagerfromDB() {
        indexAdvertses=seerchIndexAdvert();
        initViewPager();
    }


    private void initView() {
        MyLog.i("initView1");
        MyLog.i("map Size=="+map.size());
        content.clear();
        continer.removeAllViews();


        for (int i=0;i<map.size();i++){
            Order order=orders.get(i);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (order.getMethod_name().equals("ip")){
                ArrayList<IP> ipSimpleInfos= (ArrayList<IP>) map.get(order.getMethod_name());
                continer.addView(getIPItemView2(ipSimpleInfos,i),layoutParams);
            }else {
                ArrayList<IndexItemInfo> indexItemInfos= (ArrayList<IndexItemInfo>) map.get(order.getMethod_name());
                continer.addView( getItemView1(indexItemInfos,i),layoutParams);

            }
        }



        MyLog.i("initView");
        initVerTextView();

    }

    private void initVerTextView() {
        ArrayList<String> list=new ArrayList<>();
        for (FloatadInfo floatadInfo:floatadInfos){
            String mess="【"+floatadInfo.getCat()+"】"+" "+floatadInfo.getName();
            list.add(mess);
        }

        switchTextView.setTextContent(list);
        MyLog.i("switchTextView setTextContent");

        switchTextView.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {

            }

            @Override
            public void onItemClick(int index) {

            }
        });

    }

//    private LinearLayout getLinearInScroll2(final ArrayList<IndexItemInfo> indexSimpleinfos, final String methodname) {
//        LinearLayout linear=new LinearLayout(context);
//        for (int i=0;i<indexSimpleinfos.size();i++){
//            View convertView= LayoutInflater.from(context).inflate(R.layout.item_horizaontal_index,null);
//            ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
//            TextView textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
//            IndexItemInfo cp=indexSimpleinfos.get(i);
//            String imageUrl=Url.prePic+cp.getLogo();
//            String name=cp.getCompany_name();
//            imageLoader.displayImage(imageUrl,imageView,MyDisplayImageOptions.getroundImageOptions());
//            textView.setText(name);
//            linear.addView(convertView);
//            final int j=i;
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=null;
//                    switch (methodname){
//                        case "cp":
//                            intent=new Intent(getActivity(), CPDetailActivity.class);
//                            break;
//                        case "issue":
//                            intent=new Intent(getActivity(), IssueDetailActivity.class);
//                            break;
//                        case "outsource":
//                            intent=new Intent(getActivity(), OutSourceActivity.class);
//
//                            break;
//                        case "investor":
//                            intent=new Intent(getActivity(), InvesmentDetialActivity.class);
//                            break;
//                    }
//                    IndexItemInfo ipSimpleInfo=indexSimpleinfos.get(j);
//                    intent.putExtra("user_id",ipSimpleInfo.getUser_id());
//                    intent.putExtra("identity_cat",ipSimpleInfo.getIdentity_cat());
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//                }
//            });
//        }
//        return linear;
//    }

//    private LinearLayout getLinearInScroll(final ArrayList<IP> ipSimpleInfos) {
//        LinearLayout linear=new LinearLayout(context);
//        linear.setOrientation(LinearLayout.HORIZONTAL);
//
//
//        for (int i=0;i<ipSimpleInfos.size();i++){
//            View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.item_horizaontal_index,null);
//            ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
//            TextView textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
//            IP ip=ipSimpleInfos.get(i);
//            String imageUrl= Url.prePic+ip.getIp_logo();
//            String name=ip.getIp_name();
////            Picasso.with(getActivity())
////                    .load(imageUrl)
////                    .into(imageView);
//            imageLoader.displayImage(imageUrl,imageView,MyDisplayImageOptions.getroundImageOptions());
//            textView.setText(name);
//
//            linear.addView(convertView);
//
//            final int j=i;
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(getActivity(),IPDetailActivity.class);
//                    IP ipSimpleInfo=ipSimpleInfos.get(j);
//                    intent.putExtra("id",ipSimpleInfo.getId());
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//
//                }
//            });
//        }
//
//        return linear;
//
//    }

    class ViewHolder{
        HorizontalScrollView listView;
        TextView tj;
        TextView more;
    }


    private View getItemView1(final ArrayList<IndexItemInfo> indexSimpleinfos, int position){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_index_list,null);

        TextView tj= (TextView) view.findViewById(R.id.text);
        TextView more= (TextView) view.findViewById(R.id.more);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler_index_item);
        ImageView imageView= (ImageView) view.findViewById(R.id.image);
        TextView image_title= (TextView) view.findViewById(R.id.image_title);

        if (position>=albumadInfos.size()){
            view.findViewById(R.id.relative).setVisibility(View.GONE);
        }else {
            AlbumadInfo albumadInfo=albumadInfos.get(position);
            imageLoader.displayImage(Url.prePic+albumadInfo.getLogo(),imageView,MyDisplayImageOptions.getroundImageOptions());
            image_title.setText(albumadInfo.getIntro());
        }

        final Order order=orders.get(position);
        tj.setText(order.getName());

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListActivity.class);
                switch (order.getMethod_name()){
                    case "ip":
                        intent.putExtra("tag",ListActivity.IP_TAG);
                        break;
                    case "cp":
                        intent.putExtra("tag",ListActivity.CP_TAG);
                        break;
                    case "issue":
                        intent.putExtra("tag",ListActivity.Issue_TAG);
                        break;
                    case "outsource":
                        intent.putExtra("tag",ListActivity.OutSource_TAG);
                        break;
                    case "investor":
                        intent.putExtra("tag",ListActivity.Invesment_TAG);
                        break;
                }
                intent.putExtra("remment",true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new HomeAdapter(indexSimpleinfos,"1"));




        return view;
    }
    private View getIPItemView2(final ArrayList<IP> ips,int position){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_index_list,null);

        TextView tj= (TextView) view.findViewById(R.id.text);
        TextView more= (TextView) view.findViewById(R.id.more);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler_index_item);
        ImageView imageView= (ImageView) view.findViewById(R.id.image);
        TextView image_title= (TextView) view.findViewById(R.id.image_title);

        if (position>=albumadInfos.size()){
            view.findViewById(R.id.relative).setVisibility(View.GONE);
        }else {
            AlbumadInfo albumadInfo=albumadInfos.get(position);
            imageLoader.displayImage(Url.prePic+albumadInfo.getLogo(),imageView,MyDisplayImageOptions.getroundImageOptions());
            image_title.setText(albumadInfo.getIntro());
        }


        tj.setText("推荐IP");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new HomeAdapter(ips));


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.IP_TAG);
                intent.putExtra("remment",true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });


        return view;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<IndexItemInfo> datalist;
        private List<IP> iplist;
        private String cat;

        ImageLoader imageLoader=ImageLoader.getInstance();


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_index_recycler, parent, false));
            return holder;
        }

        public HomeAdapter(List<IndexItemInfo> datalist,String cat) {
            this.datalist=datalist;
            this.cat=cat;
        }
        public HomeAdapter(List<IP> iplist) {
            this.iplist=iplist;
            cat="ip";
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            if (cat.equals("ip")){
                IP ipSimpleInfo=iplist.get(position);
                imageLoader.displayImage(Url.prePic+ipSimpleInfo.getIp_logo(),holder.icon,MyDisplayImageOptions.getroundImageOptions());
                holder.name.setText(ipSimpleInfo.getIp_name());
                holder.info.setText(ipSimpleInfo.getIntrouduction());

            }else {
                IndexItemInfo indexItemInfo=datalist.get(position);
                imageLoader.displayImage(Url.prePic+indexItemInfo.getLogo(),holder.icon,MyDisplayImageOptions.getroundImageOptions());
                holder.name.setText(indexItemInfo.getCompany_name());
                holder.info.setText(indexItemInfo.getInfo());
            }

            holder.care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyAccount.isLogin){

                    }else {
                        Intent intent=new Intent(getActivity(), AccountActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView name;
            TextView info;
            TextView care;
            View item;


            public MyViewHolder(View view) {
                super(view);
                icon = (ImageView) view.findViewById(R.id.imageView_iplist);
                name = (TextView) view.findViewById(R.id.name_item_iplist);
                info = (TextView) view.findViewById(R.id.introuduction_item_iplist);
                care = (TextView) view.findViewById(R.id.care_item_list);
                item = view;
            }
        }
    }


    private void initViewPager() {

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        viewPager.setLayoutManager(layout);
        viewPager.setAdapter(new LayoutAdapter(getActivity(), viewPager));
        viewPager.setHasFixedSize(true);
        viewPager.setLongClickable(true);

        MyLog.i("initViewPager");


        viewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = viewPager.getChildCount();
                int width = viewPager.getChildAt(0).getWidth();
                int padding = (viewPager.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });


        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (viewPager.getChildCount() < 3) {
                    if (viewPager.getChildAt(1) != null) {
                        if (viewPager.getCurrentPosition() == 0) {
                            View v1 = viewPager.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = viewPager.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (viewPager.getChildAt(0) != null) {
                        View v0 = viewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (viewPager.getChildAt(2) != null) {
                        View v2 = viewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });

        imageViewList.clear();
        whiteDots.removeAllViews();


        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(332*density),(int) (167*density));
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(9*density),(int)(9*density));

        for (int i=0;i<indexAdvertses.size();i++){
            int j=i;
            if (i>=indexAdvertses.size()){
                j=i%indexAdvertses.size();
            }

            ImageView imageView=new ImageView(context);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
            MyLog.i("addimageView");
            if (i>0){
                params1.leftMargin=(int)(12.66*density);
            }
            ImageView dot=new ImageView(context);
            dot.setImageResource(R.drawable.whitedotselected);
            dot.setLayoutParams(params1);
            whiteDots.addView(dot);
            MyLog.i("imageUrl==="+Url.prePic+indexAdvertses.get(j).getAdvert_pic());


            imageLoader.displayImage(Url.prePic+indexAdvertses.get(j).getAdvert_pic(),imageView, MyDisplayImageOptions.getdefaultBannerOptions());
//            Picasso.with(getActivity())
//                    .load()
//                    .into(imageView);
            final IndexAdvert indexAdvert=indexAdvertses.get(j);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url=indexAdvert.getAdvert_url();
//                    Intent intent=new Intent(context, InfomaionDetailActivity.class);
//                    intent.putExtra("url",url);
//                    startActivity(intent);
//                    context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//                }
//            });
        }
        setDot(0);
        MyLog.i("setAdapter");

        adapter=new LayoutAdapter(getActivity(),viewPager,indexAdvertses);
        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }

    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (viewPager!=null&&indexAdvertses!=null) {
                        currentItem = viewPager.getCurrentPosition();
                        currentItem++;
                        if (currentItem == indexAdvertses.size()) {
                            currentItem = 0;
                        }
//                        MyLog.i("currentItem=="+currentItem+"===time==="+new Date().getTime());
                        viewPager.scrollToPosition(currentItem);

//                        thread.start();

                    }
                    break;
            }
        }
    };

    private void setDot(int position) {
        for (int i=0;i<whiteDots.getChildCount();i++){
            if (i==position){
                whiteDots.getChildAt(i).setSelected(true);
            }else {
                whiteDots.getChildAt(i).setSelected(false);
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ip_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
//                intent=new Intent(getActivity(), ExpertReportActivity.class);

                intent.putExtra("tag",ListActivity.IP_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.wb_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.OutSource_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.tz_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Invesment_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.fx_fg_index:
                intent=new Intent(getActivity(), ListActivity.class);
                intent.putExtra("tag",ListActivity.Issue_TAG);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;

        }
    }

    private void getDataFromDB(){
        orders.clear();
        orders=searchOrder();
        if (orders!=null&&orders.size()!=0){
            MyLog.i("从数据库获取数据");
            map.clear();
            for (int i=0;i<orders.size();i++){
                Order order=orders.get(i);
                if (order.getMethod_name().equals("ip")){
                    ArrayList<IP> ipArrayList= (ArrayList<IP>) searchIP();
                    map.put(order.getMethod_name(),ipArrayList);
                }else {
                    ArrayList<IndexItemInfo> indexItemInfos= (ArrayList<IndexItemInfo>) searchIndexItem(order.getMethod_name());
                    map.put(order.getMethod_name(),indexItemInfos);
                }
            }
            MyLog.i("order size=="+orders.size());
//            initViewPagerfromDB();

        }
    }


    public void jsonParser(String res){
        map.clear();
        orders.clear();
        indexAdvertses.clear();
        try {
            JSONObject jsonObect=new JSONObject(res);
            boolean su=jsonObect.getBoolean("success");
            int code=jsonObect.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObect.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                Url.saveUrl(Url.prePic);
                JSONArray reList=data.getJSONArray("reList");
                for (int i=0;i<reList.length();i++){
                    JSONObject info=reList.getJSONObject(i);
                    Order order=new Order();
                    String method_name=info.getString("method_name");
                    String name=info.getString("name");
                    order.setMethod_name(method_name);
                    order.setName(name);
                    orders.add(order);
                    addOrder(order);
                    JSONArray array=info.getJSONArray(method_name+"list");
                    if (method_name.equals("ip")){
                        ArrayList<IP> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IP ipsimpleInfo=new IP();
                            ipsimpleInfo.setIp_name(item.getString("ip_name"));
                            ipsimpleInfo.setIp_logo(item.getString("ip_logo"));
                            ipsimpleInfo.setId(item.getString("id"));
                            String info_s="类    型："+item.getString("ip_cat")
                                            +"\n风    格："+item.getString("ip_style")
                                            +"\n授权范围："+item.getString("uthority");
                            ipsimpleInfo.setIntrouduction(info_s);
                            list.add(ipsimpleInfo);
                            addIP(ipsimpleInfo);
                        }
                        map.put(method_name,list);
                    }else {
                        ArrayList<IndexItemInfo> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IndexItemInfo simpleInfo=new IndexItemInfo();
                            simpleInfo.setUser_id(item.getString("user_id"));
                            simpleInfo.setLogo(item.getString("logo"));
                            simpleInfo.setIdentity_cat(item.getString("identity_cat"));
                            simpleInfo.setCompany_name(item.getString("company_name"));
                            String info_s="";
                            switch (simpleInfo.getIdentity_cat()){
                                case "2":
                                    //cp
                                    info_s="所在区域："+item.getString("area_name")
                                            +"\n公司规模："+item.getString("company_scale_name");
                                    break;
                                case "3":
                                    //outsource
                                    info_s="所在区域："+item.getString("area_name")
                                            +"\n公司规模："+item.getString("company_scale_name")
                                            +"\n类    型："+item.getString("outsource_name");

                                    break;
                                case "4":
                                    //investor
                                    info_s="所在区域："+item.getString("area_name")
                                            +"\n机构类型："+item.getString("invest_cat_name")
                                            +"\n投资阶段："+item.getString("invest_stage_name");

                                    break;
                                case "6":
                                    //issue
                                    info_s="所在区域："+item.getString("area_name")
                                            +"\n业务类型："+item.getString("busine_cat_name")
                                            +"\n发行方式："+item.getString("coop_cat_name");

                                    break;
                            }
                            simpleInfo.setInfo(info_s);
                            simpleInfo.setMethod_name(method_name);
                            list.add(simpleInfo);
                            addIndexItem(simpleInfo);
                        }

                        map.put(method_name,list);
                    }
                }
                JSONArray adverts=data.getJSONArray("adverts");
                for (int i=0;i<adverts.length();i++){
                    IndexAdvert advert=new IndexAdvert();
                    JSONObject jadvert=adverts.getJSONObject(i);
                    advert.setUser_id(jadvert.getString("id"));
                    advert.setAdvert_name(jadvert.getString("advert_name"));
                    advert.setAdvert_pic(jadvert.getString("advert_pic"));
                    advert.setAdvert_url(jadvert.getString("advert_url"));
                    indexAdvertses.add(advert);
                    addIndexAdvert(advert);
                }


                JSONArray floatadarray=data.getJSONArray("floatad");
                floatadInfos=new ArrayList<>();
                for (int i=0;i<floatadarray.length();i++){
                    FloatadInfo floatadInfo=new FloatadInfo();
                    JSONObject floatObject=floatadarray.getJSONObject(i);
                    floatadInfo.setCat(floatObject.getString("cat"));
                    floatadInfo.setIdent_cat(floatObject.getString("identity_cat"));
                    floatadInfo.setName(floatObject.getString("name"));
                    floatadInfos.add(floatadInfo);
                }

                JSONArray albumadArray=data.getJSONArray("albumad");
                albumadInfos=new ArrayList<>();
                for (int i=0;i<albumadArray.length();i++){
                    JSONObject albumajson=albumadArray.getJSONObject(i);
                    AlbumadInfo albumadInfo=new AlbumadInfo();
                    albumadInfo.setLogo(albumajson.getString("logo"));
                    albumadInfo.setIntro(albumajson.getString("intro"));
                    albumadInfo.setContent(albumajson.getString("content"));
                    albumadInfos.add(albumadInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        first=true;
        adapter.notifyDataSetChanged();
    }

    private IndexItemInfoDao getIndexItemInfoDao(){
        return daoSession.getIndexItemInfoDao();
    }
    private IPDao getIPDao(){
        return daoSession.getIPDao();
    }
    private IndexAdvertDao getIndexAdverDao(){
        return daoSession.getIndexAdvertDao();
    }
    private OrderDao getOrderDao(){
        return daoSession.getOrderDao();
    }

    private void addIndexItem(IndexItemInfo info){
        getIndexItemInfoDao().insert(info);
    }
    private void addIndexAdvert(IndexAdvert advert){
        getIndexAdverDao().insert(advert);
    }
    private void addIP(IP ip){
        getIPDao().insert(ip);
    }
    private void addOrder(Order order){
        getOrderDao().insert(order);
    }

    private List searchIndexItem(String method_name){
        Query query = getIndexItemInfoDao().queryBuilder()
                .where(IndexItemInfoDao.Properties.Method_name.eq(method_name))
                .build();
        return query.list();
    }
    private List searchIP(){
        Query query = getIPDao().queryBuilder()
                .build();
        return query.list();
    }
    private List searchOrder(){
        Query query = getOrderDao().queryBuilder()
                .build();
        return query.list();
    }
    private List seerchIndexAdvert(){
        Query query = getIndexAdverDao().queryBuilder()
                .build();
        return query.list();
    }

    private void deletAll(){
        getIndexAdverDao().deleteAll();
        getOrderDao().deleteAll();
        getIndexItemInfoDao().deleteAll();
        getIPDao().deleteAll();
    }




}
