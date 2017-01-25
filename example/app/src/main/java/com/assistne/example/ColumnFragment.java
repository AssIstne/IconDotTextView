package com.assistne.example;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.assistne.icondottextview.IconDotTextView;

import static com.assistne.example.R.id.main;


/**
 * A simple {@link Fragment} subclass.
 */
public class ColumnFragment extends Fragment implements View.OnClickListener{

    private IconDotTextView mIconDotTextView0;
    private IconDotTextView mIconDotTextView1;
    private IconDotTextView mIconDotTextView2;
    private IconDotTextView mIconDotTextView3;
    private ViewGroup mViewGroup;

    public ColumnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_column, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewGroup = (ViewGroup) view.findViewById(main);
        mIconDotTextView0 = (IconDotTextView) mViewGroup.getChildAt(0);
        mIconDotTextView1 = (IconDotTextView) mViewGroup.getChildAt(1);
        mIconDotTextView2 = (IconDotTextView) mViewGroup.getChildAt(2);
        mIconDotTextView3 = (IconDotTextView) mViewGroup.getChildAt(3);
        view.findViewById(R.id.button0).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
    }

    public void changeState(View view) {
        if (!mViewGroup.isSelected()) {
            ((Button)view).setText("Unselected");
            mViewGroup.setSelected(true);
        } else {
            ((Button)view).setText("Selected");
            mViewGroup.setSelected(false);
        }
    }

    public void changeText() {
        mIconDotTextView0.setTextSize(14);
        mIconDotTextView1.setText("123");
        mIconDotTextView2.setTextColor(android.R.color.holo_purple);
    }

    public void changeIcon() {
        mIconDotTextView2.setIconSize(60, 30);
        mIconDotTextView3.setIcon(R.drawable.ic_email_black_12dp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                changeState(v);
                break;
            case R.id.button1:
                changeText();
                break;
            case R.id.button2:
                changeIcon();
                break;
        }
    }
}
