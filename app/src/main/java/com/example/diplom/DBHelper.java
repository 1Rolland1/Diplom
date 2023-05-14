package com.example.diplom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diplom.model.File;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "filesDb";
    public static final String TABLE_FILES = "files";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SIGN = "sign";// true or false



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FILES + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text," + KEY_SIGN + " text" + ")");

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

    public void AddOne(File data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TITLE, data.getTitle());
        cv.put(KEY_SIGN, data.getSign());
        db.insert(TABLE_FILES, null, cv);

        db.close();
    }

    public ArrayList<File> GetAll(){
        ArrayList<File> docs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_FILES, null,
                null, null, null,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                int id_t = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                int id_s = cursor.getColumnIndex(DBHelper.KEY_SIGN);

                File doc = new File(cursor.getString(id_t), cursor.getString(id_s));
                docs.add(doc);
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return docs;
    }

    public void ChangeOne(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TITLE, title);
        cv.put(KEY_SIGN, "true");

        db.update(TABLE_FILES, cv, KEY_TITLE + " = ?", new String[] {title});
        db.close();
    }

    public void DeleteOne(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILES, KEY_TITLE + " = ?", new String[] {title});
        db.close();
    }
}
