package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void projectDesc(View view){
        Intent intent = new Intent(this, Description.class);

        startActivity(intent);
    }

    public void documents(View view){
        Intent intent = new Intent(this, Documents.class);

        startActivity(intent);
    }
}