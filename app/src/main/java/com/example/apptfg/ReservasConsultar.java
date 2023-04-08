package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservasConsultar extends AppCompatActivity {
    private RecyclerView rvReservas;
    private ReservasAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_consultar);

        //TextView para mock de reservas
        rvReservas = findViewById(R.id.id_rv_reservas);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));

        List<Reserva> listaReservas = obtenerReservas(); // Aquí debes implementar la lógica para obtener la lista de reservas de actividades
        adapter = new ReservasAdapter(listaReservas);
        rvReservas.setAdapter(adapter);
    }

    //Función para ver los detalles de una reserva
    public void detallesReservaVentana(View view){
        Intent detallesReserva = new Intent(this, ReservaDetalles.class);
        startActivity(detallesReserva);
    }

    private List<Reserva> obtenerReservas() {
        // Aquí debes implementar la lógica para obtener la lista de reservas de actividades.
        // Por ejemplo, podrías crear una lista de reservas ficticia para pruebas:
        List<Reserva> listaReservas = new ArrayList<>();
        listaReservas.add(new Reserva("Pádel", "10:00 - 11:00", new Date()));
        listaReservas.add(new Reserva("Fútbol", "11:00 - 12:00", new Date()));
        listaReservas.add(new Reserva("Basket", "14:00 - 15:00", new Date()));
        return listaReservas;
    }

    //Función para volver a la ventana de actividades
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this, ReservaActividades.class);
        startActivity(anterior);
    }
}