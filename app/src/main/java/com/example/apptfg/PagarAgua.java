package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PagarAgua extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_agua);
        Button btnVolver = findViewById(R.id.id_btn_Anterior_Pagos);
        ImageButton btnServicioAgua = findViewById(R.id.imgservicioagua1_id);

        btnVolver.setOnClickListener(view ->{
            Intent intent = new Intent(this,Pagos.class);
            startActivity(intent);
        });


        btnServicioAgua.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.aguasdevalencia.es/VirtualOffice");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

    }

}