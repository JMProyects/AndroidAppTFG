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
        loadUser();

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

            // Redirigir al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                        if (!imageUrl.equals("https://firebasestorage.googleapis.com/v0/b/home-cloud-bd.appspot.com/o/profile_images%2Ficon.png?alt=media&token=722fc61f-b3d2-4ca2-bf8d-b584be99608d")) {
                            // Carga la imagen de perfil en el ImageButton
                            Glide.with(this).load(imageUrl).into(btnVerDatos);
                        } else {
                            // Carga la imagen predeterminada en el ImageButton
                            Glide.with(this).load(R.drawable.icon).into(btnVerDatos);
                        }
                    }
                }
            } else {
                Log.e("Firestore", "Error al obtener el documento", task.getException());
            }
        });
    }


    private void loadUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String usuario = document.getString("usuario");
                    emailTextView.setText(usuario);
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