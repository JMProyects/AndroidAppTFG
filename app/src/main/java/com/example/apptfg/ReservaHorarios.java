package com.example.apptfg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReservaHorarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_horarios);
        TextView buttonDatePicker = findViewById(R.id.id_fecha_reserva);
        buttonDatePicker.setOnClickListener(v -> showDatePickerDialog());
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
                (view, year1, month1, dayOfMonth) -> textViewDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
    }

}