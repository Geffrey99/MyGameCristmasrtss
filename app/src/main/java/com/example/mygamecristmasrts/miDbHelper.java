package com.example.mygamecristmasrts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;
public class miDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "puntuaciones.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "puntuaciones";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PUNTOS = "puntos";
    private static final String COLUMN_FECHA = "fecha";

    public miDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PUNTOS + " INTEGER, " +
                COLUMN_FECHA + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPuntuacion(int puntos, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PUNTOS, puntos);
        values.put(COLUMN_FECHA, fecha);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getMejoresPuntuaciones() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_PUNTOS + " DESC", "5");
    }
    public Cursor getTodasLasPartidas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_FECHA + " DESC");
    }

}
