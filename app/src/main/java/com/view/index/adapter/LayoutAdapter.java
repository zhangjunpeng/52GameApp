/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.view.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.gdb.IndexAdvert;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import java.util.ArrayList;
import java.util.List;


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {
    private static final int DEFAULT_ITEM_COUNT = 100;

    private  Context mContext;
    private  RecyclerView mRecyclerView;
    private List<Integer> mItems;
    private int mCurrentItemId = 0;
    private List<IndexAdvert> indexadverts;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
//        public final TextView title;

        public ImageView imageView;
        public SimpleViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(id.title);
            imageView = (ImageView) view.findViewById(R.id.imageview);
        }
    }

    public LayoutAdapter(Context context, RecyclerView recyclerView) {
    }

    public LayoutAdapter(Context context, RecyclerView recyclerView, List<IndexAdvert> indexAdvertList) {
        mContext = context;
        mItems = new ArrayList<>(indexAdvertList.size());
        indexadverts=indexAdvertList;
        for (int i = 0; i < indexAdvertList.size(); i++) {
            addItem(i);
        }

        mRecyclerView = recyclerView;
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_index_advert, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
//        holder.title.setText(mItems.get(position).toString());
        ImageLoader imageloder=ImageLoader.getInstance();
        IndexAdvert indexadvert=indexadverts.get(position);
        imageloder.displayImage(Url.prePic+indexadvert.getAdvert_pic(),holder.imageView, MyDisplayImageOptions.getdefaultImageOptions());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
