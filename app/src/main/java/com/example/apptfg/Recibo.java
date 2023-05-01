package com.example.apptfg;

import java.util.Date;

public class Recibo {
    private String servicio;
    private Date fecha_pago;
    private String importe;
    private String numero_tarjeta;

    public Recibo(){
        //Constructor para FireBase
    }

    public Recibo(String servicio, Date fecha_pago, String importe, String numero_tarjeta) {
        this.servicio = servicio;
        this.fecha_pago = fecha_pago;
        this.importe = importe;
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getMaskedCardNumber() {
        if (numero_tarjeta.length() < 4) {
            return "****";
        } else {
            int cantidadAsteriscos = numero_tarjeta.length() - 4;
            String asteriscos = new String(new char[cantidadAsteriscos]).replace("\0", "*");
            String ultimosDigitos = numero_tarjeta.substring(numero_tarjeta.length() - 4);
            return asteriscos + ultimosDigitos;
        }
    }
}

