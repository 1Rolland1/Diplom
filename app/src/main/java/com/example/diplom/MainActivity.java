package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
    }

    public void projectDesc(View view){
        Intent intent = new Intent(this, Description.class);
        startActivity(intent);
    }

    public void sign(View view){
    //проверка разрешения на запись в хранилище
        if (checkPermissions()){
            //в случае успеха переход на страницу авторизации
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

    }

    //проверка разрешения на запись в хранилище
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //вызов окна запроса разрешения
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return false;
        }
    }

    //окно запроса разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            //если разрешение дано, переход на страницу авторизации
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
            } else {
                //в случае отказа переход на страницу выполнен не будет и выведется сообщение об ошибке
                Toast.makeText(context, "Пожалуйста, дайта разрешение на запись в хранилище, чтобы войти", Toast.LENGTH_SHORT).show();
            }
        }
    }

}