package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recibos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);
        RecyclerView rvRecibos = findViewById(R.id.id_rv_recibos);
        rvRecibos.setLayoutManager(new LinearLayoutManager(this));

        List<Recibo> listaRecibos = obtenerRecibos(); // Aquí debes implementar la lógica para obtener la lista de recibos
        RecibosAdapter adapter = new RecibosAdapter(listaRecibos);
        rvRecibos.setAdapter(adapter);
    }

    private List<Recibo> obtenerRecibos() {
        // Aquí debes implementar la lógica para obtener la lista de recibos.
        // Por ejemplo, podrías crear una lista de recibos ficticia para pruebas:
        List<Recibo> listaRecibos = new ArrayList<>();
        listaRecibos.add(new Recibo("Vodafone", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Endesa", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Comunidad", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Aguas de Valencia", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Iberdrola", "Pagado", new Date()));
        listaRecibos.add(new Recibo("PTV", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Holaluz", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Movistar", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Comunidad", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Repsol", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Iberdrola", "Pagado", new Date()));
        listaRecibos.add(new Recibo("Simyo", "Pagado", new Date()));
        return listaRecibos;
    }
    //Función para volver a la ventana principal
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }
}