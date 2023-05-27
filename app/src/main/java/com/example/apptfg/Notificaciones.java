package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Notificaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
    }

    //Función para ver las notificaciones
    public void verNotificacionesVentana(View view){
        Intent vNotis = new Intent(this,ConsultarNotificaciones.class);
        startActivity(vNotis);
    }

    //Función para reportar una incidencia
    public void reportarIncidenciaVentana(View view){
        Intent rIncidencia = new Intent(this,ReportarIncidencia.class);
        startActivity(rIncidencia);
    }

    //Función para ver el apartado de "acerca de"
    public void acercaDeVentana(View view){
        Intent acercaDe = new Intent(this, AcercaDe.class);
        startActivity(acercaDe);
    }

    //Función para volver a la ventana principal
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }
}