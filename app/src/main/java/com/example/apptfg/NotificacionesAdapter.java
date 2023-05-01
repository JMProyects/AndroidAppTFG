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

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private final List<Notificacion> listaIncidencias;
    private final SimpleDateFormat formatoFecha;

    public NotificacionesAdapter(List<Notificacion> listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
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
        Notificacion incidencia = listaIncidencias.get(position);
        holder.tvAsunto.setText(incidencia.getAsunto());
        holder.tvMensaje.setText(incidencia.getMensaje());
        holder.tvUsuario.setText(incidencia.getUsuario());
        holder.tvFecha.setText(formatoFecha.format(incidencia.getFecha()));
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {

        TextView tvAsunto;
        TextView tvMensaje;
        TextView tvUsuario;
        TextView tvFecha;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAsunto = itemView.findViewById(R.id.id_tv_asunto_notificacion);
            tvMensaje = itemView.findViewById(R.id.id_tv_mensaje_notificacion);
            tvUsuario = itemView.findViewById(R.id.id_tv_usuario_notificacion);
            tvFecha = itemView.findViewById(R.id.id_tv_fecha_notificacion);
        }
    }
}
