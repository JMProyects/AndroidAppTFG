package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

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

        // Generar un identificador aleatorio de 5 dígitos
        Random random = new Random();
        int idPago = random.nextInt(100000);

        // Formatear el número con 5 dígitos rellenando con ceros a la izquierda si es necesario
        String idPagoFormateado = String.format("ID: %05d", idPago);

        // Mostrar el identificador en el TextView correspondiente
        TextView textViewIdPago = findViewById(R.id.id_txtpagarcomunidadconfirmado_id);
        textViewIdPago.setText(idPagoFormateado);


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
