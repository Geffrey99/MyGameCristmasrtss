package com.example.mygamecristmasrts;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MejoresPuntuaciones extends AppCompatActivity {

    private miDbHelper dbHelper;
    private TextView textViewListaPuntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_puntuaciones);

        dbHelper = new miDbHelper(this);
        textViewListaPuntuaciones = findViewById(R.id.textViewListaPuntuaciones);
        Button buttonInicio = findViewById(R.id.buttonInicio);

        //mostrarTodasLasPartidas();

        mostrarMejoresPuntuaciones();

        buttonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MejoresPuntuaciones.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarMejoresPuntuaciones() {
        Cursor cursor = dbHelper.getMejoresPuntuaciones();
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                stringBuilder.append("Puntos: ").append(puntos).append(", Fecha: ").append(fecha).append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        textViewListaPuntuaciones.setText(stringBuilder.toString());
    }
}


    //PERFECTTTT ✔️
   /* private void mostrarTodasLasPartidas() {
        Cursor cursor = dbHelper.getTodasLasPartidas();
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                stringBuilder.append("Puntos: ").append(puntos).append(", Fecha: ").append(fecha).append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        textViewListaPuntuaciones.setText(stringBuilder.toString());
    }

    */
