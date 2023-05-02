package com.example.apptfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecibosAdminAdapter extends RecyclerView.Adapter<RecibosAdminAdapter.ReciboViewHolder> {

    private final List<ReciboAdmin> recibos;

    public RecibosAdminAdapter(List<ReciboAdmin> recibos, ConsultarRecibosAdminActivity recibosConsultar, Context context) {
        this.recibos = recibos;
        // Añade una referencia a la actividad
        // Mantén la referencia al contexto
    }

    @NonNull
    @Override
    public ReciboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recibo_admin, parent, false);
        return new ReciboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReciboViewHolder holder, int position) {
        ReciboAdmin recibo = recibos.get(position);
        holder.tvServicio.setText(recibo.getServicio());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = dateFormat.format(recibo.getFecha_pago());
        holder.tvFecha.setText(fechaFormateada);
        holder.tvImporte.setText(recibo.getImporte());
        holder.tvUltimosDigitosTarjeta.setText(recibo.getMaskedCardNumber());
        holder.usuarioTextView.setText((recibo.getUsuarioNombre()));
    }

    @Override
    public int getItemCount() {
        return recibos.size();
    }

    public static class ReciboViewHolder extends RecyclerView.ViewHolder {
        TextView tvServicio, tvFecha, tvImporte, tvUltimosDigitosTarjeta, usuarioTextView;

        public ReciboViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa los elementos de la vista
            tvServicio = itemView.findViewById(R.id.id_tv_tipo_recibo);
            tvFecha = itemView.findViewById(R.id.id_tv_fecha_recibo);
            tvImporte = itemView.findViewById(R.id.id_tv_importe_recibo);
            tvUltimosDigitosTarjeta = itemView.findViewById(R.id.id_tv_numeroTarjeta_recibo);
            usuarioTextView = itemView.findViewById(R.id.id_tv_usuario);

        }
    }

    public void sortBy(Comparator<ReciboAdmin> comparator) {
        recibos.sort(comparator);
    }
}
