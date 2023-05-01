package com.example.apptfg;

public class ReservaAdmin extends Reserva {
    private String usuarioNombre;

    public ReservaAdmin() {
        super();
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
}

