package com.assistne.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toRow(View view) {
        startActivity(new Intent(this, RowActivity.class));
    }

    public void toRowReverse(View view) {
        startActivity(new Intent(this, RowReverseActivity.class));
    }

    public void toColumn(View view) {
        startActivity(new Intent(this, ColumnActivity.class));
    }

    public void toColumnReverse(View view) {
        startActivity(new Intent(this, ColumnReverseActivity.class));
    }
}
