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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConsultarRecibosAdminActivity extends AppCompatActivity {
    private RecyclerView rvRecibos;
    private RecibosAdminAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView noRecibosTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consultar_recibos);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consultar Recibos");

        rvRecibos = findViewById(R.id.id_rv_recibos);
        noRecibosTextView = findViewById(R.id.id_txt_no_recibos);
        rvRecibos.setHasFixedSize(true);
        rvRecibos.setLayoutManager(new LinearLayoutManager(this));

        loadAllRecibos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú con las opciones de ordenamiento
        getMenuInflater().inflate(R.menu.menu_order_options_recibos_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja las opciones de ordenamiento
        switch (item.getItemId()) {
            case R.id.order_by_id:
                sortReservationsById();
                return true;
            case R.id.order_by_servicio_name:
                sortReservationsByServicioName();
                return true;
            case R.id.order_by_fecha:
                sortReservationsByFecha();
                return true;
            case R.id.order_by_importe:
                sortReservationsByImporte();
                return true;
            case R.id.order_by_usuario:
                sortReservationsByUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortReservationsByUsuario() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(ReciboAdmin::getUsuarioNombre));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsById() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Recibo::getIdentificador));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByImporte() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Recibo::getImporte));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByFecha() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Recibo::getFecha_pago));
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByServicioName() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Recibo::getServicio));
            adapter.notifyDataSetChanged();
        }
    }

    private void loadAllRecibos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("vecinos").document(currentUser.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");
                        db.collection("recibos")
                                .whereEqualTo("urbanizacion", urbanizacion)   // Solo obtenemos los recibos de la urbanización
                                .orderBy("fecha_pago", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        List<ReciboAdmin> recibo = new ArrayList<>();
                                        for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                            ReciboAdmin recibos = document2.toObject(ReciboAdmin.class);
                                            // Añade el nombre del usuario al objeto recibos
                                            String usuarioNombre = document2.getString("usuario");
                                            recibos.setUsuarioNombre(usuarioNombre);
                                            recibo.add(recibos);
                                        }

                                        adapter = new RecibosAdminAdapter(recibo, ConsultarRecibosAdminActivity.this, ConsultarRecibosAdminActivity.this);
                                        rvRecibos.setAdapter(adapter);

                                        if (recibo.isEmpty()) {
                                            noRecibosTextView.setVisibility(View.VISIBLE);
                                        } else {
                                            noRecibosTextView.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Log.w("ConsultarRecibosAdmin", "Error getting documents.", task2.getException());
                                    }
                                });
                    } else {
                        Log.d("ConsultarRecibosAdmin", "No such document");
                    }
                } else {
                    Log.d("ConsultarRecibosAdmin", "get failed with ", task.getException());
                }
            });
        }
    }


    //Función para volver a "Menú principal" del administrador.
    public void anteriorVentanaMenuPrincipalAdm(View view) {
        Intent registrar = new Intent(this, MainAdminActivity.class);
        startActivity(registrar);
    }
}
