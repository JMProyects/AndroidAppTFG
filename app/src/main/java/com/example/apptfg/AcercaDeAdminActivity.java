package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeAdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_acerca_de);
    }

    //Función para navegar a "Notificaciones y más" del administrador
    public void anteriorVentanaNotisAdm(View view) {
        Intent anterior = new Intent(this, NotificacionesAdminActivity.class);
        startActivity(anterior);
    }
}
