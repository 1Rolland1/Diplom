package com.example.diplom.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.diplom.DBHelper;
import com.example.diplom.DBHelper2;
import com.example.diplom.Documents;
import com.example.diplom.MainActivity;
import com.example.diplom.R;
import com.example.diplom.model.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileAdapter extends ArrayAdapter<File> {

    private Context context;
    private ArrayList<File> files;
    String user;





    public FileAdapter(Context context, ArrayList<File> files, String user) {
        super(context, R.layout.file_item, files);
        this.context = context;
        this.files = files;
        this.user = user;
    }

    //@NonNull
    //@Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.file_item, parent, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(this.files.get(position).getTitle());
        DBHelper dbHelper = new DBHelper(context);


        ImageView doc_icon = (ImageView) view.findViewById(R.id.doc_icon);
        //Выбор картинки возле документа в зависимости подписан файл или нет
        if (files.get(position).getSign().equals("true")){
            doc_icon.setImageResource(R.drawable.signfile);
        } else {
            doc_icon.setImageResource(R.drawable.file);
        }


        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Файл " + files.get(position).getTitle() + " удален", Toast.LENGTH_SHORT).show();
                //путь к документу
                String filepath = context.getFilesDir().toString() + "/" + files.get(position).getTitle();
                java.io.File file = new java.io.File(filepath);
                //путь к подписи документа и архиву
                int atIndex = filepath.indexOf(".pdf");
                String fileSignPath = "";
                String archivePath = "";
                if (atIndex != -1) {
                    fileSignPath = filepath.substring(0, atIndex);
                }
                archivePath = fileSignPath + ".zip";
                fileSignPath += ".sig";
                java.io.File fileSign = new java.io.File(fileSignPath);
                java.io.File fileArchive = new java.io.File(archivePath);
                //проверка на наличие файла в папке
                if (file.exists()){
                    file.delete();
                    dbHelper.DeleteOne(files.get(position).getTitle());
                    files.remove(position);
                }
                //проверка на наличие подписи файла в папке
                if (fileSign.exists()){
                    fileSign.delete();
                }
                //проверка на наличие архива в папке
                if (fileArchive.exists()){
                    fileArchive.delete();
                }

                notifyDataSetChanged();
            }
        });

        ImageButton btnDownload = (ImageButton) view.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //путь к файлу
                String filePath = context.getFilesDir().toString() + "/" + files.get(position).getTitle();
                //название файла
                String fileName = files.get(position).getTitle();
                //название архива
                int atIndex = fileName.indexOf(".pdf");
                String archiveName = "";
                if (atIndex != -1) {
                    archiveName = fileName.substring(0, atIndex);
                }
                archiveName += ".zip";
                //путь к архиву
                String archivePath = context.getFilesDir().toString() + "/" + archiveName;

                String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + archiveName;

                java.io.File file = new java.io.File(filePath);
                java.io.File fileArchive = new java.io.File(archivePath);

                try {
                    FileOutputStream fos = new FileOutputStream(destinationPath);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    //проверка на наличие архива или файла в папке
                    if (fileArchive.exists()){
                        FileInputStream fis = new FileInputStream(archivePath);
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        // Закрываем поток
                        fis.close();
                    } else if (file.exists()) {
                        FileInputStream fis = new FileInputStream(filePath);
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        // Закрываем поток
                        fis.close();
                    }

                    // Закрываем поток
                    fos.close();

                    //проверка на наличие архива или файла в папке
                    if (fileArchive.exists()){
                        Toast.makeText(context, "Файл " + archiveName + " успешно скачан", Toast.LENGTH_SHORT).show();
                    } else if (file.exists()) {
                        Toast.makeText(context, "Файл " + fileName + " успешно скачан", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if (fileArchive.exists()){
                        Toast.makeText(context, "Не удалось скачать файл " + archiveName, Toast.LENGTH_SHORT).show();
                    } else if (file.exists()) {
                        Toast.makeText(context, "Не удалось скачать файл " + fileName, Toast.LENGTH_SHORT).show();
                    }
                }

                notifyDataSetChanged();
            }
        });

        ImageButton btnSign = (ImageButton) view.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (files.get(position).getSign().equals("true")){
                    Toast.makeText(context, "Файл " + files.get(position).getTitle() + " уже подписан", Toast.LENGTH_SHORT).show();
                } else {
                    //изменение записей в бд
                    files.get(position).setSign("true");
                    dbHelper.ChangeOne(files.get(position).getTitle());
                    //подписание документа
                    try {
                        makeSign(user, position, files.get(position).getTitle());
                    } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException |
                             InvalidKeyException | SignatureException e) {
                        throw new RuntimeException(e);
                    }
                    //вывод сообщения
                    Toast.makeText(context, "Файл " + files.get(position).getTitle() + " подписан", Toast.LENGTH_SHORT).show();
                    //обновление view
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    //Создание подписи
    public void makeSign(String user, int position, String title) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, InvalidKeyException, SignatureException {
        //название нового файла
        int atIndex = title.indexOf(".pdf");
        String name = "";

        if (atIndex != -1) {
            name = title.substring(0, atIndex);
        }
        DBHelper2 dbHelper2 = new DBHelper2(context);


        //указание алгоритма для подписи
        Signature sr = Signature.getInstance("SHA1WithRSA");
        //инициализация подписи при помощи приватного ключа, принадлежащего конктретному пользователю
        sr.initSign(dbHelper2.GetKeyPair(user).getKaypair().getPrivate());

        //Получение файла в виде массива байт
        try {
            String filepath = context.getFilesDir().toString() + "/";
            String fullFilepath = filepath + files.get(position).getTitle();
            java.io.File file = new java.io.File(fullFilepath);  // Путь к файлу
            byte[] fileBytes = new byte[1024];  // Создание массива байт для хранения данных файла

            FileInputStream fis = new FileInputStream(file); //указываем путь по которому нужно считать файл
            fis.read(fileBytes);  // Чтение данных файла в массив байт
            fis.close();

            // Теперь у вас есть данные файла в виде массива байт (fileBytes)

            //Обновляется подписываемыми данными с помощью метода update
            sr.update(fileBytes);
            //Вызывается метод sign для генерации подписи
            byte[] bytes = sr.sign();
            //сохранение подписи в файл
            writeFile(bytes, name, filepath);

            //создание архива и копирование файлов в него
            createZip(name, filepath);

        } catch (IOException e) {
            // Обработка возможных исключений, связанных с чтением файла
            e.printStackTrace();
        }
    }

    void createZip(String name, String filepath) {

        try {
            // Создаем объект FileOutputStream для записи данных в файл архива
            FileOutputStream fos = new FileOutputStream(filepath + name + ".zip");
            // Создаем объект ZipOutputStream для записи данных в архив
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Открываем входной поток для чтения файла
            FileInputStream fis = new FileInputStream(filepath + name + ".pdf");
            // Создаем объект ZipEntry для добавления файла в архив
            ZipEntry entry = new ZipEntry(name + ".pdf");
            // Добавляем файл в архив
            zos.putNextEntry(entry);

            // Читаем данные из входного потока и записываем их в архив
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            fis.close();

            //помещаем второй файл в архив
            // Открываем входной поток для чтения файла
            FileInputStream fis2 = new FileInputStream(filepath + name + ".sig");
            // Создаем объект ZipEntry для добавления файла в архив
            ZipEntry entry2 = new ZipEntry(name + ".sig");
            // Добавляем файл в архив
            zos.putNextEntry(entry2);

            // Читаем данные из входного потока и записываем их в архив
            while ((bytesRead = fis2.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            fis.close();

            // Закрываем входной поток и архив
            zos.closeEntry();
            zos.close();

            // Архив успешно создан
        } catch (IOException e) {
            // Обработка возможных исключений, связанных с созданием архива
            e.printStackTrace();
        }

    }

    void writeFile(byte[] bytes, String name, String filepath) {

        try {
            // Создаем объект FileOutputStream для записи данных в файл
            FileOutputStream fos = new FileOutputStream(filepath + name + ".sig");
            // Записываем в созданный файл сгенерированную подпись
            fos.write(bytes);
            // Закрываем считыватель
            fos.close();

            // Файл успешно сохранен
        } catch (IOException e) {
            // Обработка возможных исключений, связанных с записью файла
            e.printStackTrace();
        }
    }

}
