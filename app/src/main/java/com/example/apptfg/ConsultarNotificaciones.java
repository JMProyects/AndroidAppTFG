package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultarNotificaciones extends AppCompatActivity {
    private RecyclerView rvNotificaciones;
    private NotificacionesAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView noNotificacionesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_notificaciones);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        noNotificacionesTextView = findViewById(R.id.id_txt_no_notificaciones);
        rvNotificaciones = findViewById(R.id.id_rv_notificaciones);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(this));

        loadAllNotifications();
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Notificaciones.class);
        startActivity(anterior);
    }

    private void loadAllNotifications() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();

            db.collection("vecinos").document(currentUser.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");
                        db.collection("notificaciones").whereEqualTo("urbanizacion", urbanizacion)   // Solo obtenemos las notificaciones de la urbanización
                                .orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        List<Notificacion> notificaciones = new ArrayList<>();
                                        for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                            Notificacion notificacion = document2.toObject(Notificacion.class);
                                            String emailUsuario = notificacion.getEmailUsuario();
                                            if (emailUsuario == null || emailUsuario.isEmpty() || emailUsuario.equals(currentUserEmail)) {
                                                notificaciones.add(notificacion);
                                            }
                                        }

                                        adapter = new NotificacionesAdapter(notificaciones);
                                        rvNotificaciones.setAdapter(adapter);

                                        if (notificaciones.isEmpty()) {
                                            noNotificacionesTextView.setVisibility(View.VISIBLE);
                                        } else {
                                            noNotificacionesTextView.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Log.w("ConsultarNotificaciones", "Error getting documents.", task2.getException());
                                    }
                                });
                    } else {
                        Log.d("ConsultarNotificaciones", "No such document");
                    }
                } else {
                    Log.d("ConsultarNotificaciones", "get failed with ", task.getException());
                }
            });
        }
    }

}
