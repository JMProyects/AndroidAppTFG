package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ConsultarNotisAdminActivity extends AppCompatActivity {
    private RecyclerView rvNotificaciones;
    private NotificacionesAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView noNotificacionesTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consultar_notis);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvNotificaciones = findViewById(R.id.id_rv_notificaciones);
        noNotificacionesTextView = findViewById(R.id.id_txt_no_notificaciones);
        rvNotificaciones.setHasFixedSize(true);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(this));

        loadAllNotifications();
    }

    private void loadAllNotifications() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Obtener el correo electrónico del usuario actual
            String userEmail = currentUser.getEmail();

            // Obtener la urbanización del administrador
            db.collection("vecinos").document(userEmail).get().addOnCompleteListener(adminTask -> {
                if (adminTask.isSuccessful()) {
                    DocumentSnapshot adminDocument = adminTask.getResult();
                    String adminUrbanizacion = adminDocument.getString("urbanizacion");

                    // Filtrar las incidencias por la urbanización del administrador
                    db.collection("incidencias").whereEqualTo("urbanizacion", adminUrbanizacion).orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Notificacion> incidencias = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Notificacion incidencia = document.toObject(Notificacion.class);
                                incidencias.add(incidencia);
                            }

                            adapter = new NotificacionesAdapter(incidencias);
                            rvNotificaciones.setAdapter(adapter);

                            if (incidencias.isEmpty()) {
                                noNotificacionesTextView.setVisibility(View.VISIBLE);
                            } else {
                                noNotificacionesTextView.setVisibility(View.GONE);
                            }
                        } else {
                            Log.w("ConsultarNotisAdmin", "Error getting documents.", task.getException());
                        }
                    });
                } else {
                    Log.w("ConsultarNotisAdmin", "Error getting admin document.", adminTask.getException());
                }
            });
        }
    }


    //Función para volver a "Notificaciones y más" del administrador.
    public void anteriorVentanaNotificacionesAdm(View view) {
        Intent registrar = new Intent(this, NotificacionesAdminActivity.class);
        startActivity(registrar);
    }
}
