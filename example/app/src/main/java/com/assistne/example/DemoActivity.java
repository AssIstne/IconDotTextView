package com.assistne.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.assistne.icondottextview.IconDotTextView;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void selectView(View view) {
        IconDotTextView iconDotTextView = (IconDotTextView) findViewById(R.id.t1);
        if (iconDotTextView.isSelected()) {
            iconDotTextView.setSelected(false);
            ((Button)view).setText("Select");
        } else {
            iconDotTextView.setSelected(true);
            ((Button)view).setText("UnSelect");
        }
    }
}
