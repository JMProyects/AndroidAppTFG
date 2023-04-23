package com.example.apptfg;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class VerDatosVecino extends AppCompatActivity {

    private ActivityResultLauncher<String> galleryLauncher;
    ImageButton btnImagen;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_vecino);

        //Recoger los datos del usuario actual logeado:
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            getUserData();
        }

        Button btnModificarDatos = findViewById(R.id.id_btn_modificar_datos);
        Button id_btn_volver_registro = findViewById(R.id.id_btn_volver_registro);
        btnImagen = findViewById(R.id.id_btn_imagen_perfil);
        setupGalleryLauncher();

        // Cargar la imagen desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("imagen_perfil", MODE_PRIVATE);
        String imageUriString = prefs.getString("imagen_perfil", null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            btnImagen.setImageURI(imageUri);
        }

        btnImagen.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerDatosVecino.this);
            builder.setTitle("Selecciona una opción")
                    .setItems(new CharSequence[]{"Actualizar imagen", "Eliminar imagen"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                // Acción para actualizar la imagen
                                galleryLauncher.launch("image/*");
                                break;
                            case 1:
                                // Acción para eliminar la imagen
                                btnImagen.setImageResource(R.drawable.icon); // Reemplaza con el nombre de tu imagen por defecto
                                // Eliminar la ruta de la imagen en SharedPreferences
                                SharedPreferences.Editor editor = getSharedPreferences("imagen_perfil", MODE_PRIVATE).edit();
                                editor.remove("imagen_perfil");
                                editor.apply();
                                break;
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        btnModificarDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intent = new Intent(VerDatosVecino.this, ModificarDatosVecino.class);
            startActivity(intent);
        });

        id_btn_volver_registro.setOnClickListener(v -> {
            // Abrir la vista anterior
            Intent intent = new Intent(VerDatosVecino.this, PrincipalActivity.class);
            startActivity(intent);
        });
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Aquí puedes hacer algo con la imagen seleccionada
                btnImagen.setImageURI(uri);
                // Guardar la ruta de la imagen en SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("imagen_perfil", MODE_PRIVATE).edit();
                editor.putString("imagen_perfil", uri.toString());
                editor.apply();
            }
        });
    }

    private void getUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = currentUser.getEmail();

        db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Obtiene y muestra los datos del usuario
                    String imagen = document.getString("profile_image_url");
                    String usuario = document.getString("usuario");
                    String contrasenya = document.getString("contraseña");
                    String nombre = document.getString("nombre");
                    String apellido = document.getString("apellidos");
                    String dni = document.getString("dni");
                    String telefono = document.getString("telefono");
                    String correo = document.getString("correo");
                    String direccion = document.getString("direccion");
                    String localidad = document.getString("localidad");
                    String provincia = document.getString("provincia");
                    String cp = document.getString("codigo_postal");
                    // Agrega otros campos según sea necesario

                    // Muestra los datos en TextViews o en otros elementos de la interfaz
                    TextView usuarioTextView = findViewById(R.id.id_inputusuario);
                    usuarioTextView.setText(usuario);

                    TextView contrasenyaTextView = findViewById(R.id.id_inputcontrasena);
                    contrasenyaTextView.setText(contrasenya);

                    TextView nombreTextView = findViewById(R.id.id_inputnombre);
                    nombreTextView.setText(nombre);

                    TextView apellidoTextView = findViewById(R.id.id_inputapellidos);
                    apellidoTextView.setText(apellido);

                    TextView dniTextView = findViewById(R.id.id_inputdni);
                    dniTextView.setText(dni);

                    TextView telefonoTextView = findViewById(R.id.id_inputtelefono);
                    telefonoTextView.setText(telefono);

                    TextView correoTextView = findViewById(R.id.id_inputcorreo);
                    correoTextView.setText(correo);

                    TextView direccionTextView = findViewById(R.id.id_inputdireccion);
                    direccionTextView.setText(direccion);

                    TextView localidadTextView = findViewById(R.id.id_inputlocalidad);
                    localidadTextView.setText(localidad);

                    TextView provinciaTextView = findViewById(R.id.id_inputprovincia);
                    provinciaTextView.setText(provincia);

                    TextView cpTextView = findViewById(R.id.id_inputcp);
                    cpTextView.setText(cp);

                } else {
                    Log.d(TAG, "No se encontró el documento");
                }
            } else {
                Log.d(TAG, "Error al obtener los datos del usuario: ", task.getException());
            }
        });
    }

}