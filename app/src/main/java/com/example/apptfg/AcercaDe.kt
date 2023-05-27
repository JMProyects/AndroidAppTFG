package com.example.apptfg

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AcercaDe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)
    }

    //Función para volver a la ventana anterior
    fun anteriorVentana(view: View?) {
        val anterior = Intent(this, Notificaciones::class.java)
        startActivity(anterior)
    }
}