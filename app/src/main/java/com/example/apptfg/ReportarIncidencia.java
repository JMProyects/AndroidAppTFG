package com.example.apptfg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class ReportarIncidencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_incidencia);
        TextView buttonDatePicker = findViewById(R.id.id_fecha_incidencia);
        Spinner spinner = findViewById(R.id.spinner_incidencias);

        List<String> items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tipo_incidencia)));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(
                    int position, View convertView,
                    @NonNull ViewGroup parent) {

                // Get the item view
                View view = super.getDropDownView(
                        position, convertView, parent);
                TextView textView = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                }
                else { textView.setTextColor(Color.BLACK); }
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> textViewDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
    }


    //Función para volver a la ventana anterior
    public void enviarIncidencia(View view){
        Intent cIncidencia = new Intent(this,Notificaciones.class);
        startActivity(cIncidencia);
    }

    //Función para volver a la ventana anterior
    public void anteriorVentana(View view){
        Intent anterior = new Intent(this,Notificaciones.class);
        startActivity(anterior);
    }

}