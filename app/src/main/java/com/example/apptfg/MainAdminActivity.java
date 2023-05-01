package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainAdminActivity extends AppCompatActivity {
    Button logOutButton;
    TextView emailTextView;
    ImageButton btnVerDatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        logOutButton = findViewById(R.id.btn_cerrarsesion);
        emailTextView = findViewById(R.id.txtUser);
        btnVerDatos = findViewById(R.id.btn_ver_datos);

        loadImage();
        loadUser();

        btnVerDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intentPerfil = new Intent(MainAdminActivity.this, VerDatosAdministrador.class);
            startActivity(intentPerfil);
        });

        logOutButton.setOnClickListener(v -> {
            // Eliminar la clave de sesión del usuario
            FirebaseAuth.getInstance().signOut();

            // Redirigir al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(MainAdminActivity.this, MainActivity.class);
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

    /***********************************************************************
     *Funciones para la navegación entre apartados de la app administrador:*
     ***********************************************************************/
    //Función para la acceder a la ventana de "Consultar Recibos"
    public void ventanaRecibosAdm(View v) {
        Intent ventanaRecibos = new Intent(this, ConsultarRecibosAdminActivity.class);
        startActivity(ventanaRecibos);
    }

    public void ventanaActividadesAdm(View v) {
        Intent ventanaActividades = new Intent(this, ConsultarActividadesAdminActivity.class);
        startActivity(ventanaActividades);
    }

    public void ventanaNotificacionesAdmin(View v) {
        Intent ventanaNotis = new Intent(this, NotificacionesAdminActivity.class);
        startActivity(ventanaNotis);
    }
}
