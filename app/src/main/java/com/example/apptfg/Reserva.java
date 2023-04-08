package com.example.apptfg;

import java.util.Date;

public class Reserva {
    private String nombreActividad;
    private String franjaHoraria;
    private Date fecha;

    public Reserva(String nombreActividad, String franjaHoraria, Date fecha) {
        this.nombreActividad = nombreActividad;
        this.franjaHoraria = franjaHoraria;
        this.fecha = fecha;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
