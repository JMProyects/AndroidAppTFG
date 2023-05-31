package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
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

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consultar Reservas");

        noReservasTextView = findViewById(R.id.id_txt_no_reservas);
        rvReservas = findViewById(R.id.id_rv_reservas);
        rvReservas.setHasFixedSize(true);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));

        loadAllReservations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú con las opciones de ordenamiento
        getMenuInflater().inflate(R.menu.menu_order_options_reservas_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja las opciones de ordenamiento
        switch (item.getItemId()) {
            case R.id.order_by_activity_name:
                sortReservationsByActivityName();
                return true;
            case R.id.order_by_schedule:
                sortReservationsBySchedule();
                return true;
            case R.id.order_by_date:
                sortReservationsByDate();
                return true;
            case R.id.order_by_user_name:
                sortReservationsByUserName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortReservationsByActivityName() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Reserva::getActividad));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsBySchedule() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Reserva::getHorario));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByDate() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Reserva::getFecha_reserva));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByUserName() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(ReservaAdmin::getUsuarioNombre));
            adapter.notifyDataSetChanged();
        }
    }


    private void loadAllReservations() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db.collection("vecinos").document(currentUser.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");
                        db.collection("reservas").whereEqualTo("urbanizacion", urbanizacion)   // Solo obtenemos las reservas de la urbanización
                                .get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        List<ReservaAdmin> reservas = new ArrayList<>();
                                        List<Task<DocumentSnapshot>> userTasks = new ArrayList<>();
                                        for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                            ReservaAdmin reserva = document2.toObject(ReservaAdmin.class);
                                            reserva.setId(document2.getId());
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
                                            adapter = new ReservasAdminAdapter(reservas, ConsultarActividadesAdminActivity.this, ConsultarActividadesAdminActivity.this);
                                            rvReservas.setAdapter(adapter);
                                        });

                                        if (reservas.isEmpty()) {
                                            noReservasTextView.setVisibility(View.VISIBLE);
                                        } else {
                                            noReservasTextView.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Log.w("ConsultarActividadesAdmin", "Error getting documents.", task2.getException());
                                    }
                                });
                    } else {
                        Log.d("ConsultarActividadesAdmin", "No such document");
                    }
                } else {
                    Log.d("ConsultarActividadesAdmin", "get failed with ", task.getException());
                }
            });
        }
    }

    //Función para volver a la ventana anterior
    public void anteriorVentanaActividadesAdm(View view) {
        Intent anterior = new Intent(this, MainAdminActivity.class);
        startActivity(anterior);
    }
}
