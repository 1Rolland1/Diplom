package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    Context context;
    // Получаем значение переменной name из Intent
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        context = this;
        name = getIntent().getStringExtra("name");
    }

    public void check (View view){
        Intent intent = new Intent(this, Check.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void documents(View view){
        Intent intent = new Intent(this, Documents.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

}