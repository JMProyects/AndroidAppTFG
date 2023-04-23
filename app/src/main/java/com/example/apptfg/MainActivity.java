package com.example.apptfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    Button btnLogin;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        TextView greetingTextView = findViewById(R.id.greetingTextView);
        passwordEditText = findViewById(R.id.idtxtPassword);
        emailEditText = findViewById(R.id.idtxtUsuario);
        Drawable visibilityOff = getResources().getDrawable(R.drawable.ic_visibility_off);
        Drawable visibilityOn = getResources().getDrawable(R.drawable.ic_visibility);
        Drawable lockIcon = getResources().getDrawable(R.drawable.ic_lock);
        LinearLayout linearLayout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);
        btnLogin = findViewById(R.id.btnLogin);
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
        btnLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //validarUsuario(email, password);
            iniciarSesion(email, password);
        });

    }

    private void iniciarSesion(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Si alguno de los campos está vacío, mostrar un Toast y salir de la función
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    // Inicio de sesión exitoso, continúa con la siguiente actividad
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("vecinos").document(user.getEmail()).get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            DocumentSnapshot document = task2.getResult();
                            if (document != null && document.exists()) {
                                String nombreUsuario = document.getString("usuario");
                                SharedPreferences sharedPreferences = getSharedPreferences("misPreferencias", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nombreUsuario", nombreUsuario);
                                editor.apply();
                                Intent principal = new Intent(MainActivity.this, PrincipalActivity.class);
                                startActivity(principal);
                            } else {
                                Toast.makeText(MainActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario
                    Toast.makeText(MainActivity.this, "Las credenciales introducidas son incorrectas", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("El usuario y/o contraseña introducida no existe o es incorrecto");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome() {
        Intent homeIntent = new Intent(this, PrincipalActivity.class);

        startActivity(homeIntent);
    }


    //Función para dar de alta a un nuevo vecino
    public void registrarVecinoNuevo(View view) {
        Intent registrar = new Intent(this, RegistrarDatosVecino.class);
        startActivity(registrar);
    }

}