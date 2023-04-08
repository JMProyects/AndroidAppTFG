package com.example.apptfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaActividadViewHolder> {

    private List<Reserva> listaReservas;
    private SimpleDateFormat formatoFecha;


    public ReservasAdapter(List<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    }

    @NonNull
    @Override
    public ReservaActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaActividadViewHolder holder, int position) {
        Reserva reserva = listaReservas.get(position);
        holder.tvNombreActividad.setText(reserva.getNombreActividad());
        holder.tvFranjaHoraria.setText(reserva.getFranjaHoraria());
        holder.tvFechaActividad.setText(formatoFecha.format(reserva.getFecha()));
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static class ReservaActividadViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreActividad;
        TextView tvFranjaHoraria;
        TextView tvFechaActividad;

        public ReservaActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreActividad = itemView.findViewById(R.id.id_tv_nombre_actividad);
            tvFranjaHoraria = itemView.findViewById(R.id.id_tv_franja_horaria);
            tvFechaActividad = itemView.findViewById(R.id.id_tv_fecha_actividad);
        }
    }
}

