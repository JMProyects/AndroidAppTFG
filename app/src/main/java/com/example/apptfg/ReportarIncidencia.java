package com.example.apptfg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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

        // Spinner on item selected listener
        /*
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view,
                            int position, long id) {

                        // Get the spinner selected item text
                        String selectedItemText = (String) parent
                                .getItemAtPosition(position);

                        // If user change the default selection
                        // First item is disable and
                        // it is used for hint
                        if(position > 0){
                            // Notify the selected item text
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    "Tipo de incidencia : "
                                            + selectedItemText,
                                    Toast.LENGTH_SHORT);
                            //deprecated
                            //view = toast.getView();

                            //To change the Background of Toast (deprecated)
                            //view.setBackground(getDrawable(R.drawable.toast3));
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            //text.setGravity(Gravity.CENTER);

                            //Shadow of the Of the Text Color
                            text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                            text.setTextColor(Color.BLACK);
                            text.setTextSize(Integer.valueOf(getResources().getString(R.string.prueba)));
                            toast.show();
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });*/

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

        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Convertir la fecha seleccionada por el usuario en un objeto Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaSeleccionada;
        try {
            fechaSeleccionada = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error en la fecha seleccionada", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que la fecha introducida es la actual o anterior
        if (fechaSeleccionada.after(fechaActual)) {
            Toast.makeText(this, "La fecha de la incidencia no puede ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el spinner tiene seleccionado un tipo de incidencia
        if (spinnerIncidencia.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe seleccionar un tipo de incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar que el usuario haya rellenado el editText
        if (texto.trim().isEmpty()) {
            Toast.makeText(this, "Debe escribir una descripción de la incidencia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si se han superado todas las comprobaciones, se muestra un mensaje de éxito y se limpian los campos
        Toast.makeText(this, "Incidencia enviada con éxito", Toast.LENGTH_SHORT).show();
        textViewFecha.setText(R.string.txt_fecha_incidencia);
        spinnerIncidencia.setSelection(0);
        editTextTexto.setText("");
    }


    //Función para volver a la ventana anterior
    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Notificaciones.class);
        startActivity(anterior);
    }

}