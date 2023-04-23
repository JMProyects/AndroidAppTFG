package com.example.apptfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class PrincipalActivity extends AppCompatActivity {
    Button logOutButton;
    TextView emailTextView;
    ImageButton btnVerDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOutButton = findViewById(R.id.btn_cerrarsesion);
        emailTextView = findViewById(R.id.txtUser);

        ImageButton pagos = findViewById(R.id.imgpagosagua_id);
        btnVerDatos = findViewById(R.id.btn_ver_datos);
        loadImage();
        SharedPreferences sharedPreferences = getSharedPreferences("misPreferencias", MODE_PRIVATE);
        String nombreUsuario1 = sharedPreferences.getString("nombreUsuario", "");
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String nombreUsuario2 = preferences.getString("nombre_usuario", "Usuario");

        if (!nombreUsuario1.isEmpty()) {
            emailTextView.setText(nombreUsuario1);
        } else {
            emailTextView.setText(nombreUsuario2);
        }

        pagos.setOnClickListener(v -> {
            Intent i = new Intent(PrincipalActivity.this, Pagos.class);
            startActivity(i);
        });

        btnVerDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intentPerfil = new Intent(PrincipalActivity.this, VerDatosVecino.class);
            startActivity(intentPerfil);
        });

        logOutButton.setOnClickListener(v -> {
            // Eliminar la clave de sesión del usuario
            FirebaseAuth.getInstance().signOut();

            // Eliminar todas las SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("imagen_perfil", MODE_PRIVATE).edit();
            SharedPreferences.Editor editor2 = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
            SharedPreferences.Editor editor3 = getSharedPreferences("misPreferencias", MODE_PRIVATE).edit();

            editor.clear();
            editor.apply();
            editor2.clear();
            editor2.apply();
            editor3.clear();
            editor3.apply();

            // Redirigir al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadImage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String imageUrl = document.getString("profile_image_url");
                    if (imageUrl != null) {
                        // Carga la imagen de perfil en el ImageButton
                        Glide.with(this).load(imageUrl).into(btnVerDatos);
                    }
                }
            } else {
                Log.e("Firestore", "Error al obtener el documento", task.getException());
            }
        });
    }

    /*********************************************************
     *Funciones para la navegación entre apartados de la app:*
     *********************************************************/
    //Función para la acceder a la ventana de Pagos
    public void ventanaPagos(View v) {
        Intent ventanaPagos = new Intent(this, Pagos.class);
        startActivity(ventanaPagos);
    }

    //Función para la acceder a la ventana de Recibos
    public void ventanaRecibos(View v) {
        Intent ventanaRecibos = new Intent(this, Recibos.class);
        startActivity(ventanaRecibos);
    }

    //Función para la acceder a la ventana de ReservaActividades
    public void ventanaActividades(View v) {
        Intent ventanaActividades = new Intent(this, ReservaActividades.class);
        startActivity(ventanaActividades);
    }

    //Función para la acceder a la ventana de Notificaciones y más
    public void ventanaNotificaciones(View v) {
        Intent ventanaNotificaciones = new Intent(this, Notificaciones.class);
        startActivity(ventanaNotificaciones);
    }
}