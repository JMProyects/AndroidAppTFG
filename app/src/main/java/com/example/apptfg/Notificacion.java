package com.example.apptfg;

import java.util.Date;

public class Notificacion {
    private String asunto;
    private String servicio;
    private Date fecha;

    public Notificacion(String asunto, String servicio, Date fecha) {
        this.asunto = asunto;
        this.servicio = servicio;
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

