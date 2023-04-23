package com.example.apptfg;

import android.app.DatePickerDialog;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportarIncidencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_incidencia);
        TextView buttonDatePicker = findViewById(R.id.id_fecha_incidencia);
        Spinner spinner = findViewById(R.id.spinner_incidencias);

        List<String> items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tipo_incidencia)));

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

        buttonDatePicker.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final TextView textViewDate = findViewById(R.id.id_fecha_incidencia);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> textViewDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
    }

    /*
    COMPROBACIONES DEL FORMULARIO:
    Aquí se comprueba si la fecha, el tipo de incidencia y el texto proporcionado por el vecino
    son válidos antes de enviar el formulario:
     */
    public void enviarIncidencia(View view) {
        // Obtener los elementos de la vista
        TextView textViewFecha = findViewById(R.id.id_fecha_incidencia);
        Spinner spinnerIncidencia = findViewById(R.id.spinner_incidencias);
        EditText editTextTexto = findViewById(R.id.id_txtincidencia);

        // Obtener los valores seleccionados por el usuario
        String fechaString = textViewFecha.getText().toString();
        String texto = editTextTexto.getText().toString();
        String tipoIncidencia = spinnerIncidencia.getSelectedItem().toString();

        // Obtener la fecha seleccionada por el usuario
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaSeleccionada;
        try {
            fechaSeleccionada = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si la fecha seleccionada es la actual, conservar la hora actual
        if (isSameDate(fechaSeleccionada, new Date())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaSeleccionada);
            Calendar now = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, now.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND));
            fechaSeleccionada = cal.getTime();
        }

        // Comprobar que la fecha introducida es la actual o anterior
        if (fechaSeleccionada.after(new Date())) {
            Toast.makeText(this, "La fecha de la incidencia no puede ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el spinner tiene seleccionado un tipo de incidencia
        if (spinnerIncidencia.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Seleccione un tipo de incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el usuario haya rellenado el editText
        if (texto.trim().isEmpty()) {
            Toast.makeText(this, "Escriba un mensaje acerca de la incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la referencia a la colección "incidencias"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference incidenciasRef = db.collection("incidencias");

        // Crear un nuevo objeto Map con los campos de la incidencia
        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("fecha", fechaSeleccionada);
        incidencia.put("tipo", tipoIncidencia);
        incidencia.put("mensaje", texto);

        // Añadir el correo del usuario a la incidencia
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            incidencia.put("usuario", email);
        }

        // Añadir la incidencia a la colección "incidencias"
        incidenciasRef.add(incidencia).addOnSuccessListener(documentReference -> {
            // Si se ha añadido correctamente, mostrar un mensaje de éxito y limpiar los campos
            Toast.makeText(this, "Incidencia enviada con éxito", Toast.LENGTH_SHORT).show();
            textViewFecha.setText(R.string.txt_fecha_incidencia);
            spinnerIncidencia.setSelection(0);
            editTextTexto.setText("");
        }).addOnFailureListener(e -> {
            // Si ha habido un error, mostrar un mensaje de error
            Toast.makeText(this, "Error al enviar la incidencia", Toast.LENGTH_SHORT).show();
            Log.e("ReportarIncidencia", "Error al enviar la incidencia", e);
        });
    }

    private boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Notificaciones.class);
        startActivity(anterior);
    }
}