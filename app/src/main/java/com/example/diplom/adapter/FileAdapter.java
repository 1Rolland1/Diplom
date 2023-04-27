package com.example.diplom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diplom.R;
import com.example.diplom.model.File;

import java.util.ArrayList;

public class FileAdapter extends ArrayAdapter<File> {

    private Context context;
    private ArrayList<File> files;


    public FileAdapter(Context context, ArrayList<File> files) {
        super(context, R.layout.file_item, files);
        this.context = context;
        this.files = files;
    }

    //@NonNull
    //@Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.file_item, parent, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(this.files.get(position).getName());


        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filepath = context.getFilesDir().toString() + "/" + files.get(position).getName();
                Toast.makeText(context, "Файл " + files.get(position).getName() + " удален", Toast.LENGTH_SHORT).show();
                java.io.File file = new java.io.File(filepath);
                if (file.exists()){
                    file.delete();
                }
                files.remove(position);
                notifyDataSetChanged();
            }
        });

        ImageButton btnSign = (ImageButton) view.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (files.get(position).isSign()){
                    Toast.makeText(context, "Файл " + files.get(position).getName() + " уже подписан", Toast.LENGTH_SHORT).show();
                } else {
                    



                    files.get(position).setSign(true);
                    Toast.makeText(context, "Файл " + files.get(position).getName() + " подписан", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
