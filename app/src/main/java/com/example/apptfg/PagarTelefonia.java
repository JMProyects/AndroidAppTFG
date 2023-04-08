package com.example.apptfg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PagarTelefonia extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_telefonia);
        ImageButton btnVodafone = findViewById(R.id.imgserviciotelefonia1_id);
        ImageButton btnMovistar = findViewById(R.id.imgserviciotelefonia2_id);
        ImageButton btnOrange = findViewById(R.id.imgserviciotelefonia3_id);
        ImageButton btnJazztel = findViewById(R.id.imgserviciotelefonia4_id);
        ImageButton btnSimyo = findViewById(R.id.imgserviciotelefonia5_id);
        ImageButton btnPtv = findViewById(R.id.imgserviciotelefonia6_id);

        Button btnVolver = findViewById(R.id.id_btn_Anterior_Pagos);

        btnVolver.setOnClickListener(view ->{
            Intent intent = new Intent(this,Pagos.class);
            startActivity(intent);
        });

        btnVodafone.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://m.vodafone.es/mves/login");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnMovistar.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.movistar.es/Privada/DesafioUnico/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnOrange.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://areaclientes.orange.es");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnJazztel.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://areaclientes.jazztel.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnSimyo.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.simyo.es/simyo/login");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        btnPtv.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://areacliente.ptvtelecom.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}
