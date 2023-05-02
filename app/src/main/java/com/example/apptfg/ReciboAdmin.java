package com.example.apptfg;

public class ReciboAdmin extends Recibo {
    private String usuario;

    public ReciboAdmin() {
        super();
    }

    public String getUsuarioNombre() {
        return usuario;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuario = usuarioNombre;
    }
}
