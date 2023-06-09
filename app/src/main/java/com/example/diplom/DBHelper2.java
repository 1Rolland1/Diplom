package com.example.diplom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diplom.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.util.ArrayList;

public class DBHelper2 extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDb";
    public static final String TABLE_FILES = "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_SIGN = "key_pair_data";// true or false



    public DBHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FILES + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text," + KEY_SIGN + " BLOB" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FILES);

        onCreate(db);

    }


    public void DeleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILES, null, null);

        db.close();
    }

    public boolean CheckName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {KEY_TITLE};
        String selection = KEY_TITLE + "=?";
        String[] selectionArgs = {name};

        Cursor cursor = db.query(TABLE_FILES, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {KEY_TITLE};
        Cursor cursor = db.query(TABLE_FILES, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int userIndex = cursor.getColumnIndex(KEY_TITLE);
                String username = cursor.getString(userIndex);
                userList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }


    public void AddOne(User data) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TITLE, data.getName());
        // Сериализация KeyPair в массив байтов
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(data.getKaypair());
        byte[] keyPairBytes = bos.toByteArray();
        cv.put(KEY_SIGN, keyPairBytes);
        db.insert(TABLE_FILES, null, cv);

        db.close();
    }

    public User GetKeyPair(String name) throws IOException, ClassNotFoundException {
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] keyPairBytes = null;

        // Выполнение запроса SELECT
        String selectQuery = "SELECT " + KEY_SIGN + " FROM " + TABLE_FILES + " WHERE " + KEY_TITLE + " = ?";
        String[] selectionArgs = { name }; // значение первичного ключа
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            // Извлечение массива байтов из курсора

            // Получение индекса столбца keypair
            int keypairIndex = cursor.getColumnIndex(KEY_SIGN);

            // Получение значения keypair из курсора
            keyPairBytes = cursor.getBlob(keypairIndex);
        }
        // Десериализация массива байтов в объект KeyPair
        ByteArrayInputStream bis = new ByteArrayInputStream(keyPairBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        KeyPair keyPair = (KeyPair) ois.readObject();

        User user = new User(name, keyPair);


        cursor.close();
        db.close();

        return user;
    }
}
