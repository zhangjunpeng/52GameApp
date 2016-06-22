package com.view.myreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/6/21.
 */
public class FragmentEva extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView view= (NestedScrollView) inflater.inflate(R.layout.item_expert_pj,null);

        return view;
    }
}
