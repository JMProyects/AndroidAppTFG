package com.example.apptfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecibosAdapter extends RecyclerView.Adapter<RecibosAdapter.ViewHolder> {

    private final List<Recibo> listaRecibos;

    public RecibosAdapter(List<Recibo> listaRecibos) {
        this.listaRecibos = listaRecibos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recibo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recibo recibo = listaRecibos.get(position);
        holder.tvServicio.setText(recibo.getServicio());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = dateFormat.format(recibo.getFecha_pago());
        holder.tvFecha.setText(fechaFormateada);
        holder.tvImporte.setText(recibo.getImporte());
        holder.tvUltimosDigitosTarjeta.setText(recibo.getMaskedCardNumber());
    }

    @Override
    public int getItemCount() {
        return listaRecibos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServicio, tvFecha, tvImporte, tvUltimosDigitosTarjeta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServicio = itemView.findViewById(R.id.id_tv_tipo_recibo);
            tvFecha = itemView.findViewById(R.id.id_tv_fecha_recibo);
            tvImporte = itemView.findViewById(R.id.id_tv_importe_recibo);
            tvUltimosDigitosTarjeta = itemView.findViewById(R.id.id_tv_numeroTarjeta_recibo);
        }
    }
}
