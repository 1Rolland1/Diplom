package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diplom.adapter.FileAdapter;
import com.example.diplom.model.File;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Documents extends AppCompatActivity {

    ListView list;

    FileAdapter fileAdapter;
    ArrayList<File> files;
    boolean start = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        list = (ListView) findViewById(R.id.files);

        java.io.File dir = new java.io.File(getFilesDir().toString() + "/");
        java.io.File[] listOfFiles = dir.listFiles();


        files = new ArrayList<>();

        if (start){
            start(files, listOfFiles);
        }

        //Указание адаптера для listview
        fileAdapter = new FileAdapter(this, files);

        list.setAdapter(fileAdapter);

    }

    private void start(ArrayList<File> files, java.io.File[] listOfFiles) {
        for(java.io.File obj: listOfFiles){
            //Выводим название файла
            String[] r = obj.toString().split("/");
            String name= r[r.length - 1];
            files.add(new File (name));
        }
        start = false;
    }

    int requestcode = 1;

    public void onActivityResult(int requestcode, int resulCode, Intent data){
        super.onActivityResult(requestcode, resulCode, data);
        Context context = getApplicationContext();
        if (requestcode == this.requestcode && resulCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }
            Uri uri = data.getData();
            byte[] bytes = getBytesFromUri(getApplicationContext(), uri);


            //Выводим название файла
            String[] r = uri.getPath().split("/");
            String name= r[r.length - 1];



            if (files.contains(new File(name, false)) || files.contains(new File(name, true))){
                Toast toast = Toast.makeText(this, "Файл с таким названием\n          уже существует", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                //Пробуем сохранить выбранный файл
                writeFile(bytes, name);

                //Добавляем документ в список
                files.add(new File (name, false));
                fileAdapter.notifyDataSetChanged();
            }

        }
    }



    //Сохранение выбранного файла в локальную папку приложения
    void writeFile(byte[] bytes, String name) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(name, MODE_PRIVATE);
            fos.write(bytes);
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Действие при нажатии на клавишу +
    public void openFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, requestcode);
    }


    //Получение данных bytes из uri для документов
    byte[] getBytesFromUri (Context context, Uri uri) {
        InputStream iStream = null;
        try {
            iStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = iStream.read(buffer)) != -1){
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        }
        catch (Exception ex){

        }
        return null;
    }

    //Клавиша возврата на начальную страницу
    public void toMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}