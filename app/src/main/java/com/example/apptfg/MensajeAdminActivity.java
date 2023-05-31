package com.example.apptfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MensajeAdminActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spinnerUsuarios;
    private String urbanizacion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mensaje);
        Button btnEnviar = findViewById(R.id.id_btn_enviar_incidencia);

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

        spinnerUsuarios = findViewById(R.id.spinner_usuariosAdm);  // Aquí debe ir el ID real del Spinner

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Nota: Reemplaza "usuarios" y "email" con el nombre correcto de la colección y el campo
            db.collection("vecinos").document(user.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        urbanizacion = document.getString("urbanizacion");
                        loadUsuariosData(urbanizacion);
                        try {
                            btnEnviar.setOnClickListener(v -> enviarIncidenciaAdm(urbanizacion));
                        } catch (Exception e) {
                            Log.e("MensajeAdminActivity", "Excepción no controlada", e);
                        }


                       /* btnEnviar.setOnClickListener(v -> {
                            Toast.makeText(MensajeAdminActivity.this, "Botón presionado", Toast.LENGTH_SHORT).show();
                        });*/

                    } else {
                        Log.d("MensajeAdminActivity", "No such document");
                    }
                } else {
                    Log.d("MensajeAdminActivity", "get failed with ", task.getException());
                }
            });
        }

    }

    private void loadUsuariosData(String urbanizacion) {
        List<String> usuariosData = new ArrayList<>();

        db.collection("vecinos").whereEqualTo("urbanizacion", urbanizacion).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String numeroPuerta = document.getString("puerta");
                    usuariosData.add(numeroPuerta);
                }
                ArrayAdapter<String> adapterUsuarios = new ArrayAdapter<>(MensajeAdminActivity.this, android.R.layout.simple_spinner_item, usuariosData);
                adapterUsuarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUsuarios.setAdapter(adapterUsuarios);
            } else {
                Log.d("MensajeAdminActivity", "Error getting documents: ", task.getException());
            }
        });
    }

    public void enviarIncidenciaAdm(String urbanizacionActual) {
        // Obtener los elementos de la vista
        Spinner spinnerIncidencia = findViewById(R.id.spinner_incidenciasAdm);
        EditText editTextTexto = findViewById(R.id.id_txtmensaje);
        Spinner spinnerUsuarios = findViewById(R.id.spinner_usuariosAdm);

        // Obtener los valores seleccionados por el usuario
        String texto = editTextTexto.getText().toString();
        String tipoIncidencia = spinnerIncidencia.getSelectedItem().toString();
        String numeroPuertaSeleccionado = spinnerUsuarios.getSelectedItem().toString();

        // Comprobar que el spinner tiene seleccionado un tipo de incidencia
        if (spinnerIncidencia.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Seleccione un tipo de incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el usuario haya rellenado el editText
        if (texto.trim().isEmpty()) {
            Toast.makeText(this, "Escriba un correo específico para enviar un mensaje", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la referencia a la colección "notificaciones"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificacionesRef = db.collection("notificaciones");

        // Crear un nuevo objeto Map con los campos de la notificacion
        Map<String, Object> notificaciones = new HashMap<>();
        notificaciones.put("fecha", new Timestamp(new Date()));
        notificaciones.put("asunto", tipoIncidencia);
        notificaciones.put("mensaje", texto);
        notificaciones.put("urbanizacion", urbanizacionActual);

        // Añadir el correo del usuario a la notificacion
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            notificaciones.put("usuario", email);
        }

        // Comprobar si el tipo de incidencia es "Paquetería"
        if (tipoIncidencia.equals("Paquetería")) {
            // Si es "Paquetería", solo enviar la notificacion al usuario seleccionado
            db.collection("vecinos").whereEqualTo("puerta", numeroPuertaSeleccionado).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String emailUsuario = document.getString("correo");
                        notificaciones.put("emailUsuario", emailUsuario);
                        notificacionesRef.add(notificaciones);
                        Toast.makeText(this, "Mensaje enviado con éxito", Toast.LENGTH_SHORT).show();
                        spinnerIncidencia.setSelection(0);
                        editTextTexto.setText("");
                        spinnerUsuarios.setSelection(0);
                    }
                } else {
                    // Si ha habido un error, mostrar un mensaje de error
                    Toast.makeText(this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no es "Paquetería", enviar la notificacion a todos los usuarios de la urbanización
            db.collection("vecinos").whereEqualTo("urbanizacion", urbanizacionActual).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    notificacionesRef.add(notificaciones);
                    Toast.makeText(this, "Mensaje enviado con éxito", Toast.LENGTH_SHORT).show();
                    spinnerIncidencia.setSelection(0);
                    editTextTexto.setText("");
                    spinnerUsuarios.setSelection(0);
                }
            });
        }
    }


    //Función para navegar al "Notificaciones y más" del administrador.
    public void anteriorActivityNotificacionesAdm(View view) {
        Intent intent = new Intent(this, NotificacionesAdminActivity.class);
        startActivity(intent);
    }
}
