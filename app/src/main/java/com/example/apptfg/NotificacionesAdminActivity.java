package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NotificacionesAdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notificaciones);
    }

    //Función para navegar al "menú principal" del administrador.
    public void anteriorVentanaPrincipalAdm(View view) {
        Intent intent = new Intent(this, MainAdminActivity.class);
        startActivity(intent);
    }

    //Función para navegar al "consultar notificaciones" del administrador.
    public void consultarNotisVentanaAdm(View view) {
        Intent intent = new Intent(this, ConsultarNotisAdminActivity.class);
        startActivity(intent);
    }

    //Función para navegar al "acerca de" del administrador.
    public void acercaDeVentanaAdm(View view) {
        Intent intent = new Intent(this, AcercaDeAdminActivity.class);
        startActivity(intent);
    }

    //Función para navegar al "Enviar Mensaje Comunidad" del administrador.
    public void enviarMensajeAdminVentana(View view) {
        Intent intent = new Intent(this, MensajeAdminActivity.class);
        startActivity(intent);
    }

}
