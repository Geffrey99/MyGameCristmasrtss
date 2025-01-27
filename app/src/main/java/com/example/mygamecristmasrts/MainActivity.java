package com.example.mygamecristmasrts;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

//Ok okokokokok

public class MainActivity extends AppCompatActivity {
    private RelativeLayout layoutContainer;
    private TextView tvPuntos;
    private Button btnCrear;
    private Button btnVerPuntuaciones;

    private ConstraintLayout layoutBotones;

    private int puntos = 0;
   // private int vidas = 5;
    private Handler handler = new Handler(); // manejador para programar tareas como la creacion y elimminacion de personajes de forma repetitiva


    private miDbHelper dbHelper;
    private boolean gameOverShown = false; //// Variable para asegurarme de que mostrarGameOver() solo se llame una vez
    //okkkk

    private int personajeNoAtrapados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Asocia el diseño xml con esta activit

        // Referencias de las vistas
        layoutContainer = findViewById(R.id.layoutContainer);
        tvPuntos = findViewById(R.id.tvPuntos);
        btnCrear = findViewById(R.id.buttonIniciar);
        btnVerPuntuaciones = findViewById(R.id.buttonVerPuntuaciones);

        layoutBotones = findViewById(R.id.constraintLayout);
        dbHelper = new miDbHelper(this);


        mostrarFondoInicial();


        // Acción al hacer clic en el botón de iniciar
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarJuego();
            }
        });

        // Acción al hacer clic en el botón de ver puntuaciones
        btnVerPuntuaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MejoresPuntuaciones.class);
                startActivity(intent);
            }
        });
    }


    private void iniciarJuego() {
        btnCrear.setVisibility(View.GONE); // Ocultar el botón de iniciar
       layoutBotones.setVisibility(View.GONE);

        tvPuntos.setText("Empieza a ganar : " + puntos);
        gameOverShown = false; // Reiniciar la variable cuando se inicia un nuevo juego
        // Eliminar personaje del fondo
        layoutContainer.removeAllViews();


        personajeNoAtrapados =0;

        // Crear personajes cada x tiempo
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameOverShown) {
                    crearPersonaje();
                    handler.postDelayed(this, 1000); // Crear un personaje cada X segundos
                }
            }
        }, 1000);
    }
    //Configura una tarea repetitiva que crea personajes cada X" segundos siempre que haya vidas restantes.

    private void crearPersonaje() {
        final ImageView personaje = new ImageView(this);
        personaje.setImageResource(R.drawable.personaje_despues);
        personaje.setLayoutParams(new RelativeLayout.LayoutParams(250, 250));

        layoutContainer.post(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int maxX = layoutContainer.getWidth() - 200; //Restamos 200 para evitar que el personaje se salga del contenedor.
                int maxY = layoutContainer.getHeight() - 200;

                if (maxX > 0 && maxY > 0) {
                    personaje.setX(random.nextInt(maxX));
                    personaje.setY(random.nextInt(maxY));
                } else {
                    personaje.setX(0);
                    personaje.setY(0);
                }

                layoutContainer.addView(personaje);

                final Runnable eliminarPersonaje = new Runnable() {
                    @Override
                    public void run() {
                        if (layoutContainer.indexOfChild(personaje) != -1) {
                            layoutContainer.removeView(personaje);

                 personajeNoAtrapados++;
                 if (personajeNoAtrapados >= 3) {
                     mostrarGameOver();
                 }

                  //          vidas--;
                    //        if (vidas <= 0) {
                               // mostrarGameOver();
                      //      }
                        }
                    }
                };

                handler.postDelayed(eliminarPersonaje, 4000);

                personaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.removeCallbacks(eliminarPersonaje);
                        layoutContainer.removeView(personaje);
                        puntos += 10;
                        personajeNoAtrapados = 0;
                        tvPuntos.setText("Puntos: " + puntos);
                    }
                });
            }
        });
    }

    private void mostrarGameOver() {
       // if (vidas <= 0) {
         if (!gameOverShown) {
             gameOverShown = true;  //ha sido llamado entonces
            tvPuntos.setText("GAME OVER--Puntos Obtenidos: " + puntos);

            // Guardar la puntuación en la base de datos
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dbHelper.addPuntuacion(puntos, fechaActual);

            btnCrear.setText("RESET");
            btnCrear.setVisibility(View.VISIBLE);
            layoutBotones.setVisibility(View.VISIBLE);
            mostrarFondoInicial();
            btnCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reiniciarJuego();
                }
            });
        }
    }


    private void reiniciarJuego() {
        puntos = 0;
      //  vidas = 5;
        layoutContainer.removeAllViews();
        iniciarJuego();
    }
    //Reinicia las variables, limpia los personajes y reinicia el juego.




//##############################################################################
    //@@para darle animacioooon al juego--------------------------
    private void mostrarFondoInicial() {
        final ImageView personajeFondo = new ImageView(this);
        personajeFondo.setImageResource(R.drawable.personaje_despues);

        // Configurar tamaño y posición
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(900, 900);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        personajeFondo.setLayoutParams(params);

        // Añadir sombra
        personajeFondo.setElevation(40);

        // Añadir el personaje al layout
        layoutContainer.addView(personajeFondo);

        // Obtener el ancho del contenedor para calcular los límites
        layoutContainer.post(() -> {
            float startX = 0; // Punto de inicio (izquierda)
            float endX = layoutContainer.getWidth() - personajeFondo.getWidth(); // Punto de fin (derecha)

            if (endX > 0) { // Verificar que hay espacio suficiente para animar
                // Crear animación de movimiento horizontal
                ObjectAnimator animator = ObjectAnimator.ofFloat(personajeFondo, "translationX", startX, endX);
                animator.setDuration(2000); // Duración de ida (en milisegundos)
                animator.setInterpolator(new LinearInterpolator()); // Movimiento suave
                animator.setRepeatMode(ValueAnimator.REVERSE); // Volver al inicio
                animator.setRepeatCount(ValueAnimator.INFINITE); // Repetir infinitamente
                animator.start();
            }
        });
    }

}
