package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Pagos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
    }

    //Función para pagar el recibo de agua.
    public void pagarAguaVentana(View view){
        Intent pAgua = new Intent(this,PrincipalActivity.class);
        startActivity(pAgua);
    }

    //Función para pagar el recibo de luz.
    public void pagarLuzVentana(View view){
        Intent pLuz = new Intent(this, PagarLuz.class);
        startActivity(pLuz);
    }

    //Función para pagar el recibo de comunidad.
    public void pagarComunidadVentana(View view){
        Intent pComunidad = new Intent(this,PagarComunidad.class);
        startActivity(pComunidad);
    }

    //Función para volver a la ventana principal
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }
}