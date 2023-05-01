package com.example.apptfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservasAdminAdapter extends RecyclerView.Adapter<ReservasAdminAdapter.ReservasViewHolder> {
    private final List<ReservaAdmin> reservas;


    public ReservasAdminAdapter(List<ReservaAdmin> reservas) {
        this.reservas = reservas;

    }

    @NonNull
    @Override
    public ReservasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva_admin, parent, false);
        return new ReservasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasViewHolder holder, int position) {
        ReservaAdmin reserva = reservas.get(position);
        holder.actividadTextView.setText(reserva.getActividad());
        holder.fechaTextView.setText(reserva.getFecha_reserva());
        holder.horarioTextView.setText(reserva.getHorario());
        holder.usuarioTextView.setText(reserva.getUsuarioNombre());
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class ReservasViewHolder extends RecyclerView.ViewHolder {
        TextView actividadTextView, fechaTextView, horarioTextView, usuarioTextView;

        public ReservasViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa los elementos de la vista
            actividadTextView = itemView.findViewById(R.id.id_tv_nombre_actividad);
            fechaTextView = itemView.findViewById(R.id.id_tv_fecha_actividad);
            horarioTextView = itemView.findViewById(R.id.id_tv_franja_horaria);
            usuarioTextView = itemView.findViewById(R.id.id_tv_usuario);
        }
    }
}

