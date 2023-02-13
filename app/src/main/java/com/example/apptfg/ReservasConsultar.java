package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReservasConsultar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_consultar);

        //TextView para mock de reservas
        TextView reserva1 = findViewById(R.id.id_reserva1);
        TextView reserva2 = findViewById(R.id.id_reserva2);
        TextView reserva3 = findViewById(R.id.id_reserva3);

        reserva1.setOnClickListener(this::detallesReservaVentana);

        reserva2.setOnClickListener(this::detallesReservaVentana);

        reserva3.setOnClickListener(this::detallesReservaVentana);
    }

    //Función para ver los detalles de una reserva
    public void detallesReservaVentana(View view){
        Intent detallesReserva = new Intent(this, ReservaDetalles.class);
        startActivity(detallesReserva);
    }

    //Función para volver a la ventana de actividades
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this, ReservaActividades.class);
        startActivity(anterior);
    }
}