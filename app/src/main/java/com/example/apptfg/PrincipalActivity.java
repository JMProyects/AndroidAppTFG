package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PrincipalActivity extends AppCompatActivity {

    ImageButton pagos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*
        pagos = (ImageButton)findViewById(R.id.imgpagos_id);

        pagos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrincipalActivity.this, Pagos.class);
                startActivity(i);
            }
        });*/
        ImageButton btnVerDatos = findViewById(R.id.btn_ver_datos);
        btnVerDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intent = new Intent(PrincipalActivity.this, VerDatosVecino.class);
            startActivity(intent);
        });

    }

    /*********************************************************
     *Funciones para la navegación entre apartados de la app:*
     *********************************************************/
    //Función para la acceder a la ventana de Pagos
    public void ventanaPagos(View v){
        Intent ventanaPagos = new Intent(this,Pagos.class);
        startActivity(ventanaPagos);
    }

    //Función para la acceder a la ventana de Recibos
    public void ventanaRecibos(View v){
        Intent ventanaRecibos = new Intent(this,Recibos.class);
        startActivity(ventanaRecibos);
    }

    //Función para la acceder a la ventana de ReservaActividades
    public void ventanaActividades(View v){
        Intent ventanaActividades = new Intent(this, ReservaActividades.class);
        startActivity(ventanaActividades);
    }

    //Función para la acceder a la ventana de Notificaciones y más
    public void ventanaNotificaciones(View v){
        Intent ventanaNotificaciones = new Intent(this,Notificaciones.class);
        startActivity(ventanaNotificaciones);
    }


    //Función para cerrar sesión (falta implementar la destrucción de claves de sesión correctamente)
    public void cerrarSesion(View view){
        Intent csesion = new Intent(this,MainActivity.class);
        startActivity(csesion);
    }

}