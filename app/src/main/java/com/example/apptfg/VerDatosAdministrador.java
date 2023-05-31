package com.example.apptfg;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerDatosAdministrador extends AppCompatActivity {
    Button id_btn_volver_menuAdm;
    ImageButton btnImagen;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    // Crear ActivityResultLauncher para abrir la galería
    ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            // Actualizar la imagen de perfil en la base de datos y actualizar el ImageButton
            updateProfileImage(uri);
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perfil);
        id_btn_volver_menuAdm = findViewById(R.id.id_btn_volver_menuAdm);

        //Recoger los datos del usuario actual logeado:
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            getUserData();
        }

        Button btnModificarDatos = findViewById(R.id.id_btn_modificar_datos);
        btnImagen = findViewById(R.id.id_btn_imagen_perfil);

        btnImagen.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerDatosAdministrador.this);
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
                                // Eliminar la ruta de la imagen en la base de datos
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userEmail = currentUser.getEmail();
                                db.collection("vecinos").document(userEmail).update("profile_image_url", null).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "¡Foto de perfil eliminada con éxito!", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "Imagen de perfil eliminada");
                                    } else {
                                        Log.d(TAG, "Error al eliminar la imagen de perfil: ", task.getException());
                                    }
                                });
                                break;
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        btnModificarDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intent = new Intent(VerDatosAdministrador.this, ModificarDatosAdmin.class);
            startActivity(intent);
        });

        id_btn_volver_menuAdm.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainAdminActivity.class);
            startActivity(intent);
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
                    String portal = document.getString("portal");
                    String puerta = document.getString("puerta");
                    String localidad = document.getString("localidad");
                    String provincia = document.getString("provincia");
                    String cp = document.getString("codigo_postal");

                    // Muestra los datos en TextViews o en otros elementos de la interfaz
                    RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.icon).error(R.drawable.icon);
                    Glide.with(this)
                            .load(imagen)
                            .apply(requestOptions)
                            .into(btnImagen);

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

                    TextView portalTextView = findViewById(R.id.id_inputportal);
                    portalTextView.setText(portal);

                    TextView puertaTextView = findViewById(R.id.id_inputpuerta);
                    puertaTextView.setText(puerta);

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

    private void updateProfileImage(Uri uri) {
        // Actualizar la imagen de perfil en la base de datos
        String userEmail = currentUser.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String imageUriString = uri.toString();

        db.collection("vecinos").document(userEmail).update("profile_image_url", imageUriString).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Actualizar la imagen en el ImageButton
                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.icon).error(R.drawable.icon);
                Glide.with(this)
                        .load(uri)
                        .apply(requestOptions)
                        .into(btnImagen);
                Toast.makeText(this, "¡Foto de perfil actualizada con éxito!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Error al actualizar la imagen de perfil: ", task.getException());
            }
        });
    }

}
