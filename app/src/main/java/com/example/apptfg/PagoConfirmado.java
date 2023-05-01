package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PagoConfirmado extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_confirmado);

        TextView btnHistorialRecibos = findViewById(R.id.id_txtpagarcomunidadconfirmado4);
        Animation tickAnimation = AnimationUtils.loadAnimation(this, R.anim.tick_anim);
        ImageView tickImage = findViewById(R.id.id_tick_animation);

        btnHistorialRecibos.setOnClickListener(view ->{
            Intent anterior = new Intent(this, Recibos.class);
            startActivity(anterior);
        });

        // Recibir el identificador de pago desde PagarComunidad
        String idPagoFormateado = getIntent().getStringExtra("identificador");

        // Mostrar el identificador en el TextView correspondiente
        TextView textViewIdPago = findViewById(R.id.id_txtpagarcomunidadconfirmado_id);
        textViewIdPago.setText("ID: " + idPagoFormateado);

        tickAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                tickImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        tickImage.startAnimation(tickAnimation);
    }

    public void principalVentana (View v){
        Intent anterior = new Intent(this, PrincipalActivity.class);
        startActivity(anterior);
    }
}

