package com.example.apptfg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PagarLuz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_luz);
        ImageButton btnIberdrola = findViewById(R.id.imgservicioluz1_id);
        ImageButton btnEndesa = findViewById(R.id.imgservicioluz2_id);
        ImageButton btnNaturgy = findViewById(R.id.imgservicioluz3_id);
        ImageButton btnHolaluz = findViewById(R.id.imgservicioluz4_id);
        ImageButton btnLucera = findViewById(R.id.imgservicioluz5_id);
        ImageButton btnRepsol = findViewById(R.id.imgservicioluz6_id);

        Button btnVolver = findViewById(R.id.id_btn_Anterior_Pagos);

        btnVolver.setOnClickListener(view ->{
            Intent intent = new Intent(this,Pagos.class);
            startActivity(intent);
        });

        btnIberdrola.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.iberdrola.es/wclifral/login/loginUnicoForm");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnEndesa.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.endesaclientes.com/login.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnNaturgy.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://areaprivada.naturgy.es/ovh-web/Login.gas");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnHolaluz.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://clientes.holaluz.com/es/login/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnLucera.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://clientes.lucera.es/acceso/usuario");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnRepsol.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://areacliente.repsolluzygas.com/login");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

    }
}