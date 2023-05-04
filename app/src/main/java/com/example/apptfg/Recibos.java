package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Recibos extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    RecyclerView rvRecibos;
    private RecibosAdapter adapter;
    private TextView noRecibosTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis Recibos");

        rvRecibos = findViewById(R.id.id_rv_recibos);
        rvRecibos.setLayoutManager(new LinearLayoutManager(this));
        noRecibosTextView = findViewById(R.id.id_txt_no_recibos);
        loadUserRecibos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú con las opciones de ordenamiento
        getMenuInflater().inflate(R.menu.menu_order_options_recibos, menu);
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
            default:
                return super.onOptionsItemSelected(item);
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
            adapter.sortBy((recibo1, recibo2) -> {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(recibo1.getFecha_pago());
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(recibo2.getFecha_pago());

                int compareYears = Integer.compare(calendar2.get(Calendar.YEAR), calendar1.get(Calendar.YEAR));
                if (compareYears != 0) {
                    return compareYears;
                }

                int compareMonths = Integer.compare(calendar2.get(Calendar.MONTH), calendar1.get(Calendar.MONTH));
                if (compareMonths != 0) {
                    return compareMonths;
                }

                return Integer.compare(calendar2.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            });
            adapter.notifyDataSetChanged();
        }
    }

    private void sortReservationsByServicioName() {
        if (adapter != null) {
            adapter.sortBy(Comparator.comparing(Recibo::getServicio));
            adapter.notifyDataSetChanged();
        }
    }

    private void obtenerRecibos() {
        db.collection("recibos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Recibo> listaRecibos = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String identificador = document.getString("identificador");
                    String servicio = document.getString("servicio");
                    Date fecha = document.getDate("fecha_pago");
                    String importe = document.getString("importe");
                    String numeroTarjeta = document.getString("numero_tarjeta");

                    Recibo recibo = new Recibo(identificador, servicio, fecha, importe, numeroTarjeta);
                    listaRecibos.add(recibo);
                }
                adapter = new RecibosAdapter(listaRecibos);
                rvRecibos.setAdapter(adapter);
            } else {
                Log.d("RecibosActivity", "Error obteniendo documentos: ", task.getException());
            }
        });
    }

    private void loadUserRecibos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            db.collection("recibos").whereEqualTo("usuario", userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Recibo> recibos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recibo recibo = document.toObject(Recibo.class);
                        recibos.add(recibo);
                    }

                    adapter = new RecibosAdapter(recibos);
                    rvRecibos.setAdapter(adapter);
                    updateNoReservasMessage(recibos.size()); // Actualiza el mensaje
                } else {
                    Log.w("ReservasConsultar", "Error al obtener las reservas", task.getException());
                }
            });
        }
    }


    // Método para mostrar u ocultar el mensaje "No hay reservas realizadas."
    public void updateNoReservasMessage(int numReservas) {
        if (numReservas == 0) {
            noRecibosTextView.setVisibility(View.VISIBLE);
        } else {
            noRecibosTextView.setVisibility(View.GONE);
        }
    }

    //Función para volver a la ventana principal
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, PrincipalActivity.class);
        startActivity(anterior);
    }
}