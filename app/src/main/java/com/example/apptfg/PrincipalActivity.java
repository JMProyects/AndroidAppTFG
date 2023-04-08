package com.example.apptfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageButton pagos = findViewById(R.id.imgpagosagua_id);
        ImageButton btnVerDatos = findViewById(R.id.btn_ver_datos); // Reemplaza esto con el ID de tu ImageButton


        // Cargar la imagen desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("imagen_perfil", MODE_PRIVATE);
        String imageUriString = prefs.getString("imagen_perfil", null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            btnVerDatos.setImageURI(imageUri);
        }

        pagos.setOnClickListener(v -> {
            Intent i = new Intent(PrincipalActivity.this, Pagos.class);
            startActivity(i);
        });

        btnVerDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intentPerfil = new Intent(PrincipalActivity.this, VerDatosVecino.class);
            startActivity(intentPerfil);
        });

    }

    /*********************************************************
     *Funciones para la navegación entre apartados de la app:*
     *********************************************************/
    //Función para la acceder a la ventana de Pagos
    public void ventanaPagos(View v) {
        Intent ventanaPagos = new Intent(this, Pagos.class);
        startActivity(ventanaPagos);
    }

    //Función para la acceder a la ventana de Recibos
    public void ventanaRecibos(View v) {
        Intent ventanaRecibos = new Intent(this, Recibos.class);
        startActivity(ventanaRecibos);
    }

    //Función para la acceder a la ventana de ReservaActividades
    public void ventanaActividades(View v) {
        Intent ventanaActividades = new Intent(this, ReservaActividades.class);
        startActivity(ventanaActividades);
    }

    //Función para la acceder a la ventana de Notificaciones y más
    public void ventanaNotificaciones(View v) {
        Intent ventanaNotificaciones = new Intent(this, Notificaciones.class);
        startActivity(ventanaNotificaciones);
    }


    //Función para cerrar sesión (falta implementar la destrucción de claves de sesión correctamente)
    public void cerrarSesion(View view) {
        Intent csesion = new Intent(this, MainActivity.class);
        startActivity(csesion);
    }

}