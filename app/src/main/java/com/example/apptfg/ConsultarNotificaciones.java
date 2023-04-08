package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultarNotificaciones extends AppCompatActivity {
    private RecyclerView rvNotificaciones;
    private NotificacionesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_notificaciones);

        rvNotificaciones = findViewById(R.id.id_rv_notificaciones);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(this));

        List<Notificacion> listaNotificaciones = obtenerNotificaciones(); // Aquí debes implementar la lógica para obtener la lista de notificaciones
        adapter = new NotificacionesAdapter(listaNotificaciones);
        rvNotificaciones.setAdapter(adapter);
    }

    private List<Notificacion> obtenerNotificaciones() {
        // Aquí debes implementar la lógica para obtener la lista de notificaciones.
        // Por ejemplo, podrías crear una lista de notificaciones ficticia para pruebas:
        List<Notificacion> listaNotificaciones = new ArrayList<>();
        listaNotificaciones.add(new Notificacion("Pagos", "Telefonía", new Date()));
        listaNotificaciones.add(new Notificacion("Reservas", "Pádel", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Agua", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Luz", new Date()));
        listaNotificaciones.add(new Notificacion("Reservas", "Fútbol", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Comunidad", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Telefonía", new Date()));
        listaNotificaciones.add(new Notificacion("Reservas", "Pádel", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Agua", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Luz", new Date()));
        listaNotificaciones.add(new Notificacion("Reservas", "Fútbol", new Date()));
        listaNotificaciones.add(new Notificacion("Pagos", "Comunidad", new Date()));
        return listaNotificaciones;
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Notificaciones.class);
        startActivity(anterior);
    }
}