package com.view.Evaluation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/3/29.
 */
public class ReportAdviceFragment extends Fragment {

    View view;
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_report_advice,null);
        editText= (EditText) view.findViewById(R.id.edit_report_advice);
        return view;
    }

    public String getEditText(){
        return editText.getText().toString();
    }
}
