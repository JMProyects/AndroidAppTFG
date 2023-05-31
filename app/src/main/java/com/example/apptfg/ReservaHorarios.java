package com.example.apptfg;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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
        createNotificationChannel();
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

            // Llama a la función showConfirmationDialog
            showConfirmationDialog(horario, fechaReserva, actividad, button);
        });
    }

    private void showConfirmationDialog(String horarioSeleccionado, String fechaReserva, String actividad, Button button) {
        checkOverlappingReservations(horarioSeleccionado, fechaReserva, actividad, (overlappingReservation, existingReservation, hasExistingActivityReservation) -> {
            if (overlappingReservation) {
                String existingReservationActivity = existingReservation.getString("actividad");
                Toast.makeText(this, "Ya tienes una reserva para la actividad: " + existingReservationActivity + " en este horario.", Toast.LENGTH_SHORT).show();
            } else if (hasExistingActivityReservation) {
                Toast.makeText(this, "Solo puedes realizar una reserva de la actividad por dia.", Toast.LENGTH_SHORT).show();
            } else {
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
        });
    }

    private void realizarReserva(String horario, String fechaReserva, String actividad) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");

                        Map<String, Object> reserva = new HashMap<>();
                        reserva.put("fecha_reserva", fechaReserva);
                        reserva.put("actividad", actividad);
                        reserva.put("horario", horario);
                        reserva.put("usuario", userEmail);
                        reserva.put("urbanizacion", urbanizacion);

                        db.collection("reservas").add(reserva).addOnSuccessListener(documentReference -> {
                            String reservaId = documentReference.getId();
                            documentReference.update("id", reservaId);
                            setReminderAlarm(fechaReserva, horario, actividad);
                        }).addOnFailureListener(e -> Log.w("ReservaHorarios", "Error al agregar la reserva", e));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }

    private void updateHorarioButtons(String fecha, String actividad) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");
                        actualizaBotonesConReservasUrbanizacion(fecha, actividad, urbanizacion);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }

    private void actualizaBotonesConReservasUrbanizacion(String fecha, String actividad, String urbanizacion) {
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
                int[] horarioInicioMinutes = {480, 600, 720, 840, 960, 1080, 1200}; // Horarios de inicio de cada franja horaria (en minutos desde las 0:00 horas)
                int[] horarioFinMinutes = {600, 720, 840, 960, 1080, 1200, 1320}; // Horarios de finalización de cada franja horaria (en minutos desde las 0:00 horas)
                for (int i = 0; i < buttons.size(); i++) {
                    if (currentTimeInMinutes >= horarioInicioMinutes[i] && currentTimeInMinutes < horarioFinMinutes[i]) {
                        // La franja actual aún no ha terminado, así que no hagas nada
                    } else if (currentTimeInMinutes >= horarioFinMinutes[i]) {
                        buttons.get(i).setEnabled(false);
                        buttons.get(i).setBackgroundColor(Color.parseColor("#9E9E9E"));
                    }
                }
            }
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        // Consulta Firestore y actualiza la disponibilidad de los botones según las reservas
        db.collection("reservas").whereEqualTo("fecha_reserva", fecha).whereEqualTo("actividad", actividad).whereEqualTo("urbanizacion", urbanizacion).get().addOnCompleteListener(task -> {
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

    private void checkOverlappingReservations(String horario, String fechaReserva, String actividad, OnOverlappingReservationCheckCompleteListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String urbanizacion = document.getString("urbanizacion");

                        db.collection("reservas").whereEqualTo("fecha_reserva", fechaReserva).whereEqualTo("urbanizacion", urbanizacion).get().addOnCompleteListener(reservationTask -> {
                            if (reservationTask.isSuccessful() && !reservationTask.getResult().isEmpty()) {
                                boolean overlappingReservation = false;
                                boolean alreadyReservedActivity = false;
                                DocumentSnapshot existingReservation = null;
                                for (QueryDocumentSnapshot reservationDocument : reservationTask.getResult()) {
                                    String existingHorario = reservationDocument.getString("horario");
                                    String existingActividad = reservationDocument.getString("actividad");
                                    String existingUser = reservationDocument.getString("usuario");
                                    if (userEmail.equals(existingUser) && areHorariosOverlapping(horario, existingHorario)) {
                                        overlappingReservation = true;
                                        existingReservation = reservationDocument;
                                        break;
                                    }
                                    if (userEmail.equals(existingUser) && existingActividad.equals(actividad)) {
                                        alreadyReservedActivity = true;
                                    }
                                }
                                if (!overlappingReservation && alreadyReservedActivity) {
                                    listener.onComplete(false, null, true);
                                } else {
                                    listener.onComplete(overlappingReservation, existingReservation, false);
                                }
                            } else {
                                boolean overlappingReservation = false;
                                boolean alreadyReservedActivity = false;
                                DocumentSnapshot existingReservation = null;
                                listener.onComplete(overlappingReservation, existingReservation, alreadyReservedActivity);
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }


    public interface OnOverlappingReservationCheckCompleteListener {
        void onComplete(boolean overlappingReservation, DocumentSnapshot existingReservation, boolean hasExistingActivityReservation);
    }

    private boolean areHorariosOverlapping(String horario1, String horario2) {
        String[] horario1Parts = horario1.split(" - ");
        String[] horario2Parts = horario2.split(" - ");

        String horario1Start = horario1Parts[0];
        String horario1End = horario1Parts[1];
        String horario2Start = horario2Parts[0];
        String horario2End = horario2Parts[1];

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date horario1StartAsDate = sdf.parse(horario1Start);
            Date horario1EndAsDate = sdf.parse(horario1End);
            Date horario2StartAsDate = sdf.parse(horario2Start);
            Date horario2EndAsDate = sdf.parse(horario2End);

            if (horario1StartAsDate == null || horario1EndAsDate == null || horario2StartAsDate == null || horario2EndAsDate == null) {
                return false;
            }

            if (horario1StartAsDate.before(horario2StartAsDate) && horario1EndAsDate.after(horario2StartAsDate)) {
                return true;
            } else if (horario2StartAsDate.before(horario1StartAsDate) && horario2EndAsDate.after(horario1StartAsDate)) {
                return true;
            } else if (horario1StartAsDate.equals(horario2StartAsDate) || horario1EndAsDate.equals(horario2EndAsDate)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    private int getCurrentTimeInMinutes() {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        //Calcula la franja en hora de finalización
        return currentHour * 60 + currentMinute;
    }

    private void setReminderAlarm(String fechaReserva, String horario, String actividad) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date reservaDate = sdf.parse(fechaReserva);
            Date reservaTime = timeSdf.parse(horario.split(" - ")[0]);
            if (reservaDate != null && reservaTime != null) {
                Calendar reminderCalendar = Calendar.getInstance();
                reminderCalendar.setTime(reservaDate);
                reminderCalendar.set(Calendar.HOUR_OF_DAY, reservaTime.getHours());
                reminderCalendar.set(Calendar.MINUTE, reservaTime.getMinutes());
                Calendar now = Calendar.getInstance();

                // Calcula la diferencia entre la hora de la reserva menos 24 horas y la hora actual
                long diffMillis = (reminderCalendar.getTimeInMillis() - TimeUnit.HOURS.toMillis(24)) - now.getTimeInMillis();
                Log.d("DEBUG", "Diferencia en milisegundos: " + diffMillis);

                // Establece la alarma para que se active 24 horas antes de la reserva
                reminderCalendar.add(Calendar.HOUR_OF_DAY, -24);
                Log.d("DEBUG", "Alarma establecida para: " + reminderCalendar.getTime());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ReminderBroadcast.class);
                intent.putExtra("actividad", actividad);
                intent.putExtra("horario", horario);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_IMMUTABLE);
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);
                }
            }
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        CharSequence name = "ReservationReminder";
        String description = "Channel for reservation reminder notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("notifyReservation", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}