package com.view.myreport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/6/20.
 */
public class FragmentEvaCon extends Fragment {

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView= (RecyclerView) inflater.inflate(R.layout.recyclerview,container,false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new HomeAdapter());

        MyLog.i("FragmentEvaCon call");

        return recyclerView;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_expertreport, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.content.setText("6666666666");
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView content;
            HorizontalScrollView horizontalScrollView;
            public MyViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.name_item_expertreport);
                content= (TextView) itemView.findViewById(R.id.content_item_expertreport);
                horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.imageContinar_expertreport);
            }
        }
    }



}
