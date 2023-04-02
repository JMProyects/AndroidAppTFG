package com.example.apptfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsuario = findViewById(R.id.idtxtUsuario);
        edtPassword = findViewById(R.id.idtxtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {
            //validarUsuario("http://192.168.1.3/BDappTFG/validar_usuario.php");
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
        });

    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if (!response.isEmpty()){
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "¡Usuario o contraseña incorrecta!", Toast.LENGTH_LONG).show();
            }

        }, error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", edtUsuario.getText().toString());
                parametros.put("clave", edtPassword.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //Función para dar de alta a un nuevo vecino
    public void registrarVecinoNuevo(View view){
        Intent registrar = new Intent(this, RegistrarDatosVecino.class);
        startActivity(registrar);
    }

}