package com.example.apptfg;

import java.util.Date;

public class Recibo {
    private String tipo;
    private String estado;
    private Date fecha;

    public Recibo(String tipo, String estado, Date fecha) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

