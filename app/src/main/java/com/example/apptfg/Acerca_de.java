package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Acerca_de extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this, Notificaciones.class);
        startActivity(anterior);
    }
}