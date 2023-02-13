package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrarDatosVecino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_vecino);
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,MainActivity.class);
        startActivity(anterior);
    }
}