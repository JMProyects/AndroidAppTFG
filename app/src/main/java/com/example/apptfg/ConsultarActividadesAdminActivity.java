package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultarActividadesAdminActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView noReservasTextView;
    private RecyclerView rvReservas;
    private ReservasAdminAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consultar_actividades);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        noReservasTextView = findViewById(R.id.id_txt_no_reservas);
        rvReservas = findViewById(R.id.id_rv_reservas);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));

        loadAllReservations();
    }

    private void loadAllReservations() {
        db.collection("reservas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ReservaAdmin> reservas = new ArrayList<>();
                List<Task<DocumentSnapshot>> userTasks = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    ReservaAdmin reserva = document.toObject(ReservaAdmin.class);
                    reserva.setId(document.getId());
                    reservas.add(reserva);

                    // Agrega una tarea para obtener el documento del usuario
                    userTasks.add(db.collection("reservas").document(reserva.getId()).get());
                }

                // Espera a que todas las tareas de usuario se completen
                Tasks.whenAllSuccess(userTasks).addOnSuccessListener(userDocuments -> {
                    for (int i = 0; i < userDocuments.size(); i++) {
                        DocumentSnapshot userDoc = (DocumentSnapshot) userDocuments.get(i);
                        reservas.get(i).setUsuarioNombre(userDoc.getString("usuario"));
                    }

                    // Configura el adaptador
                    adapter = new ReservasAdminAdapter(reservas);
                    rvReservas.setAdapter(adapter);
                });

                if (reservas.isEmpty()) {
                    noReservasTextView.setVisibility(View.VISIBLE);
                } else {
                    noReservasTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    //Función para volver a la ventana anterior
    public void anteriorVentanaActividadesAdm(View view){
        Intent anterior = new Intent(this, MainAdminActivity.class);
        startActivity(anterior);
    }
}
