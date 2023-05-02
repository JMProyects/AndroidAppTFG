package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReservasConsultar extends AppCompatActivity {
    private RecyclerView rvReservas;
    private ReservasAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TextView noReservasTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_consultar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis reservas");

        rvReservas = findViewById(R.id.id_rv_reservas);
        noReservasTextView = findViewById(R.id.id_txt_no_reservas);
        rvReservas.setHasFixedSize(true);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));
        loadUserReservations();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú con las opciones de ordenamiento
        getMenuInflater().inflate(R.menu.menu_order_options_reservas, menu);
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


    private void loadUserReservations() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            db.collection("reservas").whereEqualTo("usuario", userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Reserva> reservas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Reserva reserva = document.toObject(Reserva.class);
                        reservas.add(reserva);
                    }

                    adapter = new ReservasAdapter(reservas, ReservasConsultar.this, ReservasConsultar.this);
                    rvReservas.setAdapter(adapter);
                    updateNoReservasMessage(reservas.size()); // Actualiza el mensaje
                } else {
                    Log.w("ReservasConsultar", "Error al obtener las reservas", task.getException());
                }
            });
        }
    }


    // Método para mostrar u ocultar el mensaje "No hay reservas realizadas."
    public void updateNoReservasMessage(int numReservas) {
        if (numReservas == 0) {
            noReservasTextView.setVisibility(View.VISIBLE);
        } else {
            noReservasTextView.setVisibility(View.GONE);
        }
    }

    //Función para volver a la ventana de actividades
    public void anteriorVentanaActividades(View view) {
        Intent anterior = new Intent(this, ReservaActividades.class);
        startActivity(anterior);
    }
}
