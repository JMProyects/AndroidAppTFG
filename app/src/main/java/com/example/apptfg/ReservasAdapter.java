package com.example.apptfg;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservasViewHolder> {
    private final List<Reserva> reservas;
    private final ReservasConsultar reservasConsultar; // Añade una referencia a la actividad
    private final Context context; // Mantén la referencia al contexto

    public ReservasAdapter(List<Reserva> reservas, ReservasConsultar reservasConsultar, Context context) {
        this.reservas = reservas;
        this.reservasConsultar = reservasConsultar; // Inicializa la referencia
        this.context = context; // Inicializa la referencia al contexto
    }

    @NonNull
    @Override
    public ReservasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        // Configura los elementos de la vista con los datos de la reserva
        holder.actividadTextView.setText(reserva.getActividad());
        holder.fechaTextView.setText(reserva.getFecha_reserva());
        holder.horarioTextView.setText(reserva.getHorario());
        holder.cancelReservationButton.setOnClickListener(v -> cancelReservation(reserva, position));

    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class ReservasViewHolder extends RecyclerView.ViewHolder {
        TextView actividadTextView, fechaTextView, horarioTextView;
        ImageButton cancelReservationButton;

        public ReservasViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa los elementos de la vista
            actividadTextView = itemView.findViewById(R.id.id_tv_nombre_actividad);
            fechaTextView = itemView.findViewById(R.id.id_tv_fecha_actividad);
            horarioTextView = itemView.findViewById(R.id.id_tv_franja_horaria);
            cancelReservationButton = itemView.findViewById(R.id.cancel_reservation_button);
        }
    }

    private void cancelReservation(Reserva reserva, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancelar reserva");
        builder.setMessage("¿Estás seguro de que deseas cancelar esta reserva?");

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Elimina la reserva de Firestore
            db.collection("reservas").document(reserva.getId()).delete().addOnSuccessListener(aVoid -> {
                // Actualiza la lista de reservas y notifica al adaptador
                reservas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reservas.size());

                // Actualiza el mensaje "No hay reservas realizadas" en ReservasConsultar
                reservasConsultar.updateNoReservasMessage(reservas.size());

                // Muestra un Toast con el mensaje de éxito
                Toast.makeText(context, "¡Reserva cancelada con éxito!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Maneja el error en caso de que la eliminación falle
                Log.e("ReservasAdapter", "Error al eliminar la reserva", e);
            });
        });

        builder.setNegativeButton("Atrás", (dialog, which) -> {
            // Cierra el diálogo si el usuario selecciona "No"
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Obtener los botones del AlertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

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


