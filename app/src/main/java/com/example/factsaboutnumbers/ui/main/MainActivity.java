package com.example.factsaboutnumbers.ui.main;

import android.os.Bundle;

import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.base.BaseActivity;
import com.example.factsaboutnumbers.ui.list.ListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ListFragment()).commit();
    }
}
