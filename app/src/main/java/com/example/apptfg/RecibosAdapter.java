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

public class RecibosAdapter extends RecyclerView.Adapter<RecibosAdapter.ReciboViewHolder> {

    private List<Recibo> listaRecibos;
    private SimpleDateFormat formatoFecha;

    public RecibosAdapter(List<Recibo> listaRecibos) {
        this.listaRecibos = listaRecibos;
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public ReciboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recibo, parent, false);
        return new ReciboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReciboViewHolder holder, int position) {
        Recibo recibo = listaRecibos.get(position);
        holder.tvTipo.setText(recibo.getTipo());
        holder.tvEstado.setText(recibo.getEstado());
        holder.tvFecha.setText(formatoFecha.format(recibo.getFecha()));
    }

    @Override
    public int getItemCount() {
        return listaRecibos.size();
    }

    public static class ReciboViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipo;
        TextView tvEstado;
        TextView tvFecha;

        public ReciboViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.id_tv_tipo_recibo);
            tvEstado = itemView.findViewById(R.id.id_tv_estado_recibo);
            tvFecha = itemView.findViewById(R.id.id_tv_fecha_recibo);
        }
    }
}

