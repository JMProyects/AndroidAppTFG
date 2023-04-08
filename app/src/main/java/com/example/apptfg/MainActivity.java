package com.example.apptfg;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText edtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView greetingTextView = findViewById(R.id.greetingTextView);
        EditText passwordEditText = findViewById(R.id.idtxtPassword);
        Drawable visibilityOff = getResources().getDrawable(R.drawable.ic_visibility_off);
        Drawable visibilityOn = getResources().getDrawable(R.drawable.ic_visibility);
        Drawable lockIcon = getResources().getDrawable(R.drawable.ic_lock);
        LinearLayout linearLayout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);
        edtUsuario = findViewById(R.id.idtxtUsuario);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtRegistrarse = findViewById(R.id.id_lblregistrarse);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.init_anim);
        linearLayout.startAnimation(anim);
        // Obtén la hora actual
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Establece el texto de saludo según la hora del día
        if (hour >= 6 && hour < 13) {
            greetingTextView.setText("¡Buenos días!");
        } else if (hour >= 13 && hour < 19) {
            greetingTextView.setText("¡Buenas tardes!");
        } else {
            greetingTextView.setText("¡Buenas noches!");
        }


        String texto = "¿No tiene cuenta? Regístrese.";
        SpannableString spannableString = new SpannableString(texto);
        int inicio = texto.indexOf("Regístrese.");
        int fin = inicio + "Regístrese.".length();
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, inicio, fin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegistrarse.setText(spannableString);

        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, visibilityOn, null);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        passwordEditText.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (passwordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, visibilityOff, null);

                    } else {
                        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, visibilityOn, null);
                    }
                    return true;
                }
            }
            return false;
        });

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
                //parametros.put("clave", edtPassword.getText().toString());
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