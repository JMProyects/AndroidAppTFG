package com.example.apptfg;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReservaHorarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_horarios);
        TextView buttonDatePicker = findViewById(R.id.id_fecha_reserva);
        buttonDatePicker.setOnClickListener(v -> showDatePickerDialog());
        TextView txtActividad = findViewById(R.id.id_txt_reservas);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("actividadSeleccionada")) {
            String actividad = intent.getStringExtra("actividadSeleccionada");
            txtActividad.setText(actividad);
        }

        //Lista de franjas horarias
        Button buttonHorario1 = findViewById(R.id.id_btn_horario1);
        buttonHorario1.setOnClickListener(v -> showConfirmationDialog(buttonHorario1.getText().toString()));

        Button buttonHorario2 = findViewById(R.id.id_btn_horario2);
        buttonHorario2.setOnClickListener(v -> showConfirmationDialog(buttonHorario2.getText().toString()));

        Button buttonHorario3 = findViewById(R.id.id_btn_horario3);
        buttonHorario3.setOnClickListener(v -> showConfirmationDialog(buttonHorario3.getText().toString()));

        Button buttonHorario4 = findViewById(R.id.id_btn_horario4);
        buttonHorario4.setOnClickListener(v -> showConfirmationDialog(buttonHorario4.getText().toString()));

        Button buttonHorario5 = findViewById(R.id.id_btn_horario5);
        buttonHorario5.setOnClickListener(v -> showConfirmationDialog(buttonHorario5.getText().toString()));

        Button buttonHorario6 = findViewById(R.id.id_btn_horario6);
        buttonHorario6.setOnClickListener(v -> showConfirmationDialog(buttonHorario6.getText().toString()));

        Button buttonHorario7 = findViewById(R.id.id_btn_horario7);
        buttonHorario7.setOnClickListener(v -> showConfirmationDialog(buttonHorario7.getText().toString()));

    }

    //Función para volver a la ventana principal
    public void anteriorReservasVentana(View view){
        Intent anterior = new Intent(this,ReservaActividades.class);
        startActivity(anterior);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final TextView textViewDate = findViewById(R.id.id_fecha_reserva);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);
                    Calendar todayCalendar = Calendar.getInstance();
                    todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    todayCalendar.set(Calendar.MINUTE, 0);
                    todayCalendar.set(Calendar.SECOND, 0);
                    todayCalendar.set(Calendar.MILLISECOND, 0);

                    if (selectedCalendar.before(todayCalendar)) {
                        // La fecha seleccionada es anterior a la fecha de hoy.
                        Toast.makeText(this, "Seleccione una fecha válida", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    textViewDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showConfirmationDialog(String horarioSeleccionado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Reserva");
        builder.setMessage("Está a punto de realizar una reserva de " + horarioSeleccionado + ".\n ¿Está seguro?")
                .setCancelable(false)
                .setPositiveButton("Confirmar", (dialog, id) -> {

                    Intent anterior = new Intent(this,ReservaActividades.class);
                    startActivity(anterior);
                    // Agregue aquí la lógica para reservar el horario seleccionado.
                    Toast.makeText(this, "Reserva confirmada para " + horarioSeleccionado + ".", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

        // Obtener los botones del AlertDialog
        Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

        // Establecer el estilo de fuente en negrita
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        positiveButton.setTypeface(boldTypeface);
        negativeButton.setTypeface(boldTypeface);

        // Establecer el color hexadecimal del texto en los botones
        int greenColor = Color.parseColor("#66BB00");
        int redColor = Color.parseColor("#FF0000");
        positiveButton.setTextColor(greenColor);
        negativeButton.setTextColor(redColor);
    }
}