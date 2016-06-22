package com.view.s4server;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

/**
 * Created by Administrator on 2016/6/17.
 */
public class FiltrateListActivity extends BaseActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtratelist);

        mRecyclerView = (RecyclerView) findViewById(R.id.listview_filtrate);
//设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//设置adapter
        mRecyclerView.setAdapter(new HomeAdapter());
//设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//添加分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return null;
//        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    FiltrateListActivity.this).inflate(R.layout.item_filtratelist, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText("aaaaa");
        }

//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        }

        @Override
        public int getItemCount() {
            return 50;
        }
        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }

    class ListViewAdapter extends BaseAdapter{

        Context context;
        ListViewAdapter( Context context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView name;
            TextView cont;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_expertreport,null);
            }else {
            }
            name= (TextView) convertView.findViewById(R.id.name_item_expertreport);
            cont= (TextView) convertView.findViewById(R.id.content_item_expertreport);

            name.setVisibility(View.VISIBLE);
            cont.setText("saddsasdasd");

            return convertView;
        }
    }
}
