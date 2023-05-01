package com.example.apptfg;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.net.ParseException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderBroadcast extends BroadcastReceiver {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    public void onReceive(Context context, Intent intent) {
        String actividad = intent.getStringExtra("actividad");
        String horario = intent.getStringExtra("horario");
        if (actividad != null && horario != null) {
            fetchAndNotifyUpcomingReservations(context, actividad, horario);
        }
    }

    private void fetchAndNotifyUpcomingReservations(final Context context, String fecha_reserva, String horario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference allReservationsQuery = db.collection("reservas");
        allReservationsQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            long currentTimeInMillis = System.currentTimeMillis();
            long twentyFourHoursInMillis = 24 * 60 * 60 * 1000;
            long endTimeInMillis = currentTimeInMillis + twentyFourHoursInMillis;

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String reservationId = documentSnapshot.getId();
                String activityName = documentSnapshot.getString("actividad");
                String reservationDate = documentSnapshot.getString("fecha_reserva");
                String reservationTime = documentSnapshot.getString("horario");
                String[] timeRange = reservationTime.split(" - ");
                String startDateTimeString = reservationDate + " " + timeRange[0].trim();
                try {
                    Date startDateTime = sdf.parse(startDateTimeString);
                    if (startDateTime != null) {
                        long startDateTimeInMillis = startDateTime.getTime();
                        if (startDateTimeInMillis >= currentTimeInMillis && startDateTimeInMillis <= endTimeInMillis) {
                            showNotification(context, reservationId, activityName, startDateTime);
                        }
                    }
                } catch (ParseException | java.text.ParseException e) {e.printStackTrace();
                }
            }
        }).addOnFailureListener(e -> { });
    }


    private void showNotification(Context context, String reservationId, String activityName, Date startDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
        String startDateTimeString = sdf.format(startDateTime);

        String title = "Recordatorio de reserva";
        String message = "Le recordamos que tiene una reserva de\nActividad: " + activityName + "\nHorario: " + startDateTimeString;

        // Crea un intent para abrir MainActivity
        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingOpenAppIntent = PendingIntent.getActivity(context, 100, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyReservation")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingOpenAppIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(200, builder.build());
    }
}



