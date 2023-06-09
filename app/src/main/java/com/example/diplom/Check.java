package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Check extends AppCompatActivity {
    ArrayList <String> users;

    String selectedUser;
    int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        //Внесение списка пользователей из базы данных
        DBHelper2 dbHelper = new DBHelper2(this);
        users = dbHelper.getAllUsers();


        Spinner spinner = findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, users);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                selectedUser = (String)parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);


    }

    int requestcode = 1;
    public void openFile(View view) {
        Toast.makeText(this, selectedUser, Toast.LENGTH_SHORT).show();
        point++;

        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, requestcode);*/
    }

    public void checkSign(View view) {
        if(point == 0){
            Toast.makeText(this, "Не все условия выполнены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Проверка подписи", Toast.LENGTH_SHORT).show();
        }


        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, requestcode);*/
    }

}