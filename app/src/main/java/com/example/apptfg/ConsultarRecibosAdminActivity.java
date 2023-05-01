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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultarRecibosAdminActivity extends AppCompatActivity {
    private RecyclerView rvRecibos;
    private RecibosAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView noRecibosTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consultar_recibos);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvRecibos = findViewById(R.id.id_rv_recibos);
        noRecibosTextView = findViewById(R.id.id_txt_no_recibos);
        rvRecibos.setHasFixedSize(true);
        rvRecibos.setLayoutManager(new LinearLayoutManager(this));

        loadAllRecibos();
    }

    private void loadAllRecibos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("recibos").orderBy("fecha_pago", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Recibo> recibo = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recibo recibos = document.toObject(Recibo.class);
                        recibo.add(recibos);
                    }

                    adapter = new RecibosAdapter(recibo);
                    rvRecibos.setAdapter(adapter);

                    if (recibo.isEmpty()) {
                        noRecibosTextView.setVisibility(View.VISIBLE);
                    } else {
                        noRecibosTextView.setVisibility(View.GONE);
                    }
                } else {
                    Log.w("ConsultarRecibosAdmin", "Error getting documents.", task.getException());
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
