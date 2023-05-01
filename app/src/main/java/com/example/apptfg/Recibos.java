package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recibos extends AppCompatActivity {
    private FirebaseFirestore db;
    RecyclerView rvRecibos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);
        rvRecibos = findViewById(R.id.id_rv_recibos);
        rvRecibos.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        obtenerRecibos();
    }

    private void obtenerRecibos() {
        db.collection("recibos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Recibo> listaRecibos = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String servicio = document.getString("servicio");
                    Date fecha = document.getDate("fecha_pago");
                    String importe = document.getString("importe");
                    String numeroTarjeta = document.getString("numero_tarjeta");

                    Recibo recibo = new Recibo(servicio, fecha, importe, numeroTarjeta);
                    listaRecibos.add(recibo);
                }
                RecibosAdapter adapter = new RecibosAdapter(listaRecibos);
                rvRecibos.setAdapter(adapter);
            } else {
                Log.d("RecibosActivity", "Error obteniendo documentos: ", task.getException());
            }
        });
    }

    //Función para volver a la ventana principal
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, PrincipalActivity.class);
        startActivity(anterior);
    }
}