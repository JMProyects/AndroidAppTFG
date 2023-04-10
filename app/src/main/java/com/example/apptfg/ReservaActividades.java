package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ReservaActividades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_actividades);
    }

    //Función para volver a la ventana principal
    public void anteriorPrincipalVentana(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }

    //Función para consultar las reservas de las instalaciones.
    public void consultarReservasVentana(View view){
        Intent consultar = new Intent(this, ReservasConsultar.class);
        startActivity(consultar);
    }

    //Función para ver los horarios de reserva para las pistas
    // y sus horarios de apertura y cierre.
    public void verHorariosReservaVentana(View view) {
        Intent intent = new Intent(ReservaActividades.this, ReservaHorarios.class);
        String actividad = "";
        switch (view.getId()) {
            case R.id.id_img_actividad_futbol:
                actividad = getString(R.string.txt_futbol);
                break;
            case R.id.id_img_actividad_basket:
                actividad = getString(R.string.txt_basket);
                break;
            case R.id.id_img_actividad_padel:
                actividad = getString(R.string.txt_padel);
                break;
            case R.id.id_img_actividad_tenis:
                actividad = getString(R.string.txt_tenis);
                break;
        }
        intent.putExtra("actividadSeleccionada", actividad);
        startActivity(intent);
    }





}