package com.example.apptfg

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AcercaDeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_acerca_de)
    }

    //Función para navegar a "Notificaciones y más" del administrador
    fun anteriorVentanaNotisAdm(view: View?) {
        val anterior = Intent(this, NotificacionesAdminActivity::class.java)
        startActivity(anterior)
    }
}