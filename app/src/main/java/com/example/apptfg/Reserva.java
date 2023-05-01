package com.example.apptfg;

public class Reserva {

    //Es como SpringBoot / JPA, hay que poner los nombres de las variables al igual que en la
    //base de datos, si no no aparecen los datos.
    private String id;
    private String actividad;
    private String horario;
    private String fecha_reserva;

    // Constructor vacío requerido para Firestore
    public Reserva() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(String fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }
}
