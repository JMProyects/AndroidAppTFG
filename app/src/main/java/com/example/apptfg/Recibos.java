package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Recibos extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);

    }

    //Función para volver a la ventana principal
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }
}