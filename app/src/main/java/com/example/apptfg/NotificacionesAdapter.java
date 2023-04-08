package com.example.apptfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptfg.Notificacion;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private List<Notificacion> listaNotificaciones;
    private SimpleDateFormat formatoFecha;

    public NotificacionesAdapter(List<Notificacion> listaNotificaciones) {
        this.listaNotificaciones = listaNotificaciones;
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = listaNotificaciones.get(position);
        holder.tvAsunto.setText(notificacion.getAsunto());
        holder.tvServicio.setText(notificacion.getServicio());
        holder.tvFecha.setText(formatoFecha.format(notificacion.getFecha()));
    }

    @Override
    public int getItemCount() {
        return listaNotificaciones.size();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {

        TextView tvAsunto;
        TextView tvServicio;
        TextView tvFecha;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAsunto = itemView.findViewById(R.id.id_tv_asunto_notificacion);
            tvServicio = itemView.findViewById(R.id.id_tv_servicio_notificacion);
            tvFecha = itemView.findViewById(R.id.id_tv_fecha_notificacion);
        }
    }
}
