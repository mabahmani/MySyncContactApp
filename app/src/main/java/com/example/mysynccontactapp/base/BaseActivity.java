package com.example.mysynccontactapp.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysynccontactapp.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
