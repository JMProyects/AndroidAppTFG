package com.example.apptfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MensajeAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mensaje);
        Spinner spinner = findViewById(R.id.spinner_incidenciasAdm);
        List<String> items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tipo_incidenciaAdm)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the item view
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    /*
    COMPROBACIONES DEL FORMULARIO:
    Aquí se comprueba si la fecha, el tipo de incidencia y el texto proporcionado por el vecino
    son válidos antes de enviar el formulario:
     */
    public void enviarIncidencia(View view) {
        // Obtener los elementos de la vista
        Spinner spinnerIncidencia = findViewById(R.id.spinner_incidenciasAdm);
        EditText editTextTexto = findViewById(R.id.id_txtmensaje);
        EditText editTextEmailUsuario = findViewById(R.id.id_txt_email_usuario);
        String emailUsuario = editTextEmailUsuario.getText().toString();

        // Obtener los valores seleccionados por el usuario
        String texto = editTextTexto.getText().toString();
        String tipoIncidencia = spinnerIncidencia.getSelectedItem().toString();

        // Comprobar que el spinner tiene seleccionado un tipo de incidencia
        if (spinnerIncidencia.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Seleccione un tipo de incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el usuario haya rellenado el editText
        if (texto.trim().isEmpty()) {
            Toast.makeText(this, "Escriba un mensaje para la comunidad", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la referencia a la colección "incidencias"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference incidenciasRef = db.collection("notificaciones");

        // Crear un nuevo objeto Map con los campos de la incidencia
        Map<String, Object> notificaciones = new HashMap<>();
        notificaciones.put("fecha", new Timestamp(new Date()));
        notificaciones.put("asunto", tipoIncidencia);
        notificaciones.put("mensaje", texto);

        // Añadir el correo del usuario a la incidencia
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            notificaciones.put("usuario", email);
        }
        if (!emailUsuario.isEmpty()) {
            notificaciones.put("emailUsuario", emailUsuario);
        }

        // Añadir la incidencia a la colección "incidencias"
        incidenciasRef.add(notificaciones).addOnSuccessListener(documentReference -> {
            // Si se ha añadido correctamente, mostrar un mensaje de éxito y limpiar los campos
            Toast.makeText(this, "Mensaje enviado con éxito", Toast.LENGTH_SHORT).show();
            spinnerIncidencia.setSelection(0);
            editTextTexto.setText("");
            editTextEmailUsuario.setText("");
        }).addOnFailureListener(e -> {
            // Si ha habido un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
            Log.e("ReportarIncidencia", "Error al enviar el mensaje", e);
        });
    }

    //Función para navegar al "Notificaciones y más" del administrador.
    public void anteriorActivityNotificacionesAdm(View view) {
        Intent intent = new Intent(this, NotificacionesAdminActivity.class);
        startActivity(intent);
    }
}
