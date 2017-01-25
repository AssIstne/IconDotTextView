package com.assistne.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Row");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ColumnFragment();
                    case 1:
                        return new RowFragment();
                    case 2:
                        return new RowReverseFragment();
                    default:
                        return new ColumnReverseFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTitle("Column");
                        break;
                    case 1:
                        setTitle("Row");
                        break;
                    case 2:
                        setTitle("Row Reverse");
                        break;
                    default:
                        setTitle("Column Reverse");
                }
            }
        });
    }
}
