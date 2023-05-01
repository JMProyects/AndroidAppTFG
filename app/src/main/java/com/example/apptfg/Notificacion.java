package com.example.apptfg;

import java.util.Date;

public class Notificacion {
    private String asunto;
    private String mensaje;
    private Date fecha;
    private String usuario;

    public Notificacion() {
        // Constructor vacío necesario para Firebase
    }
    public Notificacion(String asunto, String mensaje, Date fecha, String usuario) {
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

