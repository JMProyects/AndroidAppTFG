package com.example.apptfg;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class ReservaHorarios extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    TextView textViewDate;
    TextView txtActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_horarios);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewDate = findViewById(R.id.id_fecha_reserva);
        textViewDate.setOnClickListener(v -> showDatePickerDialog());
        txtActividad = findViewById(R.id.id_txt_reservas);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("actividadSeleccionada")) {
            String actividad = intent.getStringExtra("actividadSeleccionada");
            txtActividad.setText(actividad);
        }

        //Lista de franjas horarias
        Button btnHorario1 = findViewById(R.id.id_btn_horario1);
        Button btnHorario2 = findViewById(R.id.id_btn_horario2);
        Button btnHorario3 = findViewById(R.id.id_btn_horario3);
        Button btnHorario4 = findViewById(R.id.id_btn_horario4);
        Button btnHorario5 = findViewById(R.id.id_btn_horario5);
        Button btnHorario6 = findViewById(R.id.id_btn_horario6);
        Button btnHorario7 = findViewById(R.id.id_btn_horario7);

        handleButtonClick(btnHorario1, getString(R.string.txt_horario1));
        handleButtonClick(btnHorario2, getString(R.string.txt_horario2));
        handleButtonClick(btnHorario3, getString(R.string.txt_horario3));
        handleButtonClick(btnHorario4, getString(R.string.txt_horario4));
        handleButtonClick(btnHorario5, getString(R.string.txt_horario5));
        handleButtonClick(btnHorario6, getString(R.string.txt_horario6));
        handleButtonClick(btnHorario7, getString(R.string.txt_horario7));

        if (intent != null && intent.hasExtra("actividadSeleccionada")) {
            String actividad = intent.getStringExtra("actividadSeleccionada");
            txtActividad.setText(actividad);
            updateHorarioButtons(textViewDate.getText().toString(), actividad);
        }

    }

    //Función para volver a la ventana principal
    public void anteriorReservasVentana(View view) {
        Intent anterior = new Intent(this, ReservaActividades.class);
        startActivity(anterior);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
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
            updateHorarioButtons(textViewDate.getText().toString(), txtActividad.getText().toString());

        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void handleButtonClick(Button button, String horario) {
        button.setTag(false); // Agrega un atributo booleano isReserved al botón y lo establece en 'false' inicialmente
        button.setOnClickListener(v -> {
            boolean isReserved = (boolean) button.getTag();
            if (isReserved) {
                // Si el horario ya está reservado, no hagas nada
                return;
            }

            TextView textViewDate = findViewById(R.id.id_fecha_reserva);
            String fechaReserva = textViewDate.getText().toString();

            // Verifica si el usuario ha seleccionado una fecha
            if (fechaReserva.isEmpty() || fechaReserva.equals("Fecha")) {
                Toast.makeText(this, "Por favor, seleccione una fecha primero", Toast.LENGTH_SHORT).show();
                return;
            }

            TextView txtActividad = findViewById(R.id.id_txt_reservas);
            String actividad = txtActividad.getText().toString();

            showConfirmationDialog(horario, fechaReserva, actividad, button);
        });
    }


    private void showConfirmationDialog(String horarioSeleccionado, String fechaReserva, String actividad, Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Reserva");
        builder.setMessage("Está a punto de realizar una reserva. \n" + "\nActividad: " + actividad + "." + "\nDía: " + fechaReserva + "." + "\nHorario: " + horarioSeleccionado + ".\n\n ¿Está seguro?").setCancelable(false).setPositiveButton("Confirmar", (dialog, id) -> {
            // Realizar la reserva aquí
            realizarReserva(horarioSeleccionado, fechaReserva, actividad);
            button.setBackgroundColor(Color.parseColor("#FF3957"));
            button.setTag(true); // Establece el atributo isReserved en 'true' para bloquear el botón
            Intent anterior = new Intent(this, ReservaActividades.class);
            startActivity(anterior);
            Toast.makeText(this, "¡Reserva confirmada con éxito!", Toast.LENGTH_LONG).show();
        }).setNegativeButton("Cancelar", (dialog, id) -> dialog.cancel());
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


    private void realizarReserva(String horario, String fechaReserva, String actividad) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Crear un mapa para almacenar los datos en Firestore
            Map<String, Object> reserva = new HashMap<>();
            reserva.put("fecha_reserva", fechaReserva);
            reserva.put("actividad", actividad);
            reserva.put("horario", horario);
            reserva.put("usuario", userEmail);

            // Almacenar la información en Firestore
            db.collection("reservas").add(reserva).addOnSuccessListener(documentReference -> {
                Log.d("ReservaHorarios", "Reserva agregada con éxito");
                String reservaId = documentReference.getId();
                documentReference.update("id", reservaId);
            }).addOnFailureListener(e -> Log.w("ReservaHorarios", "Error al agregar la reserva", e));
        }
    }


    private void updateHorarioButtons(String fecha, String actividad) {
        List<Button> buttons = Arrays.asList(findViewById(R.id.id_btn_horario1), findViewById(R.id.id_btn_horario2), findViewById(R.id.id_btn_horario3), findViewById(R.id.id_btn_horario4), findViewById(R.id.id_btn_horario5), findViewById(R.id.id_btn_horario6), findViewById(R.id.id_btn_horario7));

        // Restablecer el estado y el color de todos los botones antes de actualizarlos
        for (Button button : buttons) {
            button.setBackgroundColor(Color.parseColor("#C7FE80"));
            button.setTag(false);
            button.setEnabled(true);
        }

        // Deshabilita las franjas horarias anteriores a la hora actual si la fecha seleccionada es hoy
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = sdf.parse(fecha);
            if (selectedDate != null && selectedDate.compareTo(todayCalendar.getTime()) == 0) {
                int currentTimeInMinutes = getCurrentTimeInMinutes();
                int[] horarioMinutes = {0, 120, 240, 360, 480, 600, 720};
                for (int i = 0; i < buttons.size(); i++) {
                    if (currentTimeInMinutes > horarioMinutes[i]) {
                        buttons.get(i).setEnabled(false);
                        buttons.get(i).setBackgroundColor(Color.parseColor("#9E9E9E"));
                    }
                }
            }
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        // Consulta Firestore y actualiza la disponibilidad de los botones según las reservas
        db.collection("reservas").whereEqualTo("fecha_reserva", fecha).whereEqualTo("actividad", actividad).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String horario = document.getString("horario");
                    for (Button button : buttons) {
                        if (button.getText().toString().equals(horario)) {
                            button.setBackgroundColor(Color.parseColor("#FF3957"));
                            button.setTag(true);
                        }
                    }
                }
            } else {
                Log.d("ReservaHorarios", "Error al obtener las reservas: ", task.getException());
            }
        });
    }

    private int getCurrentTimeInMinutes() {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        // Resta 8 horas (480 minutos) para calcular el tiempo transcurrido desde las 8:00 a.m.
        return (currentHour * 60 + currentMinute) - 480;
    }

}