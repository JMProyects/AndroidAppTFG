package com.example.apptfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class RegistrarDatosVecino extends AppCompatActivity {
    private ActivityResultLauncher<String> galleryLauncher;
    TextView fotoPerfil;
    EditText id_inputusuario;
    EditText id_inputcontrasena;
    EditText id_inputnombre;
    EditText id_inputapellidos;
    EditText id_inputdni;
    EditText id_inputtelefono;
    EditText id_inputcorreo;
    EditText id_inputdireccion;
    EditText id_inputportal; // Número del edificio
    EditText id_inputpuerta; // Número de la puerta
    EditText id_inputlocalidad;
    EditText id_inputprovincia;
    EditText id_inputcp;
    private Uri selectedImageUri;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_vecino);
        id_inputusuario = findViewById(R.id.id_inputusuario);
        id_inputcontrasena = findViewById(R.id.id_inputcontrasena);
        id_inputnombre = findViewById(R.id.id_inputnombre);
        id_inputapellidos = findViewById(R.id.id_inputapellidos);
        id_inputdni = findViewById(R.id.id_inputdni);
        id_inputtelefono = findViewById(R.id.id_inputtelefono);
        id_inputcorreo = findViewById(R.id.id_inputcorreo);
        id_inputdireccion = findViewById(R.id.id_inputdireccion);
        id_inputportal = findViewById(R.id.id_inputportal);
        id_inputpuerta = findViewById(R.id.id_inputpuerta);
        id_inputlocalidad = findViewById(R.id.id_inputlocalidad);
        id_inputprovincia = findViewById(R.id.id_inputprovincia);
        id_inputcp = findViewById(R.id.id_inputcp);
        fotoPerfil = findViewById(R.id.id_lblfotoperfilperfil);
        setupGalleryLauncher();
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            ImageView tickIcon = findViewById(R.id.image_tick);
            if (uri != null) {
                selectedImageUri = uri;
                tickIcon.setVisibility(View.VISIBLE);
            } else {
                selectedImageUri = null;
                tickIcon.setVisibility(View.GONE);
            }
        });

        fotoPerfil.setOnClickListener(v -> galleryLauncher.launch("image/*"));
    }


    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, MainActivity.class);
        startActivity(anterior);
    }

    private void registrarUsuario(String email, String password) {
        Log.d("Auth", "Iniciando registrarUsuario");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d("Auth", "Usuario registrado exitosamente");
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Cargar la imagen de perfil en Firebase Storage y guardar datos en Firestore
                    guardarImagenFirebaseStorage(user);
                } else {
                    Log.e("Auth", "El usuario es nulo después del registro");
                }
            } else {
                Log.e("Auth", "Error en el registro", task.getException());
                Toast.makeText(RegistrarDatosVecino.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void guardarImagenFirebaseStorage(FirebaseUser user) {
        try {
            // Obtiene la referencia al almacenamiento de Firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/" + user.getUid() + ".jpg");

            // Sube la imagen a Firebase Storage
            if (selectedImageUri != null) {
                UploadTask uploadTask = profileImageRef.putFile(selectedImageUri);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Obtiene la URL de la imagen subida
                    profileImageRef.getDownloadUrl().addOnSuccessListener(imageUrl -> {
                        // Guarda la URL de la imagen en Firestore
                        guardarDatosPersonales(user, imageUrl.toString());
                    });
                }).addOnFailureListener(e -> {
                    // Muestra un mensaje de error
                    Toast.makeText(RegistrarDatosVecino.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseStorage", "Error al subir la imagen", e);
                });
            } else {
                // Si no hay una imagen de perfil, guarda los datos en Firestore sin la URL de la imagen
                String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/home-cloud-bd.appspot.com/o/profile_images%2Ficon.png?alt=media&token=722fc61f-b3d2-4ca2-bf8d-b584be99608d";
                guardarDatosPersonales(user, defaultImageUrl);
            }
        } catch (Exception e) {
            Log.e("FirebaseStorage", "Error en guardarDatosPersonales", e);
        }
    }

    private void guardarDatosPersonales(FirebaseUser user, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String correoEnMinusculas = id_inputcorreo.getText().toString().toLowerCase();
        String direccion = id_inputdireccion.getText().toString();
        String direccionMayus = mayusPrimerCaracter(direccion);
        String localidad = id_inputlocalidad.getText().toString();
        String localidadMayus = mayusPrimerCaracter(localidad);
        String provincia = id_inputprovincia.getText().toString();
        String provinciaMayus = mayusPrimerCaracter(provincia);

        String urbanizacion = id_inputdireccion.getText().toString().toLowerCase() + ", " + id_inputportal.getText().toString().toLowerCase() + ", " + id_inputlocalidad.getText().toString().toLowerCase() + ", " + id_inputprovincia.getText().toString().toLowerCase();
        String puerta = id_inputpuerta.getText().toString();

        // Verificar si ya existe una cuenta con la misma urbanizacion y puerta
        db.collection("vecinos").whereEqualTo("urbanizacion", urbanizacion).whereEqualTo("puerta", puerta).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    // Si ya existe una cuenta con la misma urbanizacion y puerta, muestra un mensaje de error
                    Toast.makeText(RegistrarDatosVecino.this, "Ya existe una cuenta asociada a esta puerta en la urbanización", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no existe una cuenta con la misma urbanizacion y puerta, guarda los datos

                    Map<String, Object> userData = new HashMap<String, Object>() {{
                        put("usuario", id_inputusuario.getText().toString());
                        put("contraseña", id_inputcontrasena.getText().toString());
                        put("nombre", id_inputnombre.getText().toString());
                        put("apellidos", id_inputapellidos.getText().toString());
                        put("dni", id_inputdni.getText().toString());
                        put("telefono", id_inputtelefono.getText().toString());
                        put("correo", correoEnMinusculas);
                        put("direccion", direccionMayus);
                        put("portal", id_inputportal.getText().toString());
                        put("puerta", puerta);
                        put("urbanizacion", urbanizacion);
                        put("localidad", localidadMayus);
                        put("provincia", provinciaMayus);
                        put("codigo_postal", id_inputcp.getText().toString());
                    }};

                    if (imageUrl != null) {
                        userData.put("profile_image_url", imageUrl);
                    }

                    db.collection("vecinos").document(correoEnMinusculas).set(userData).addOnSuccessListener(aVoid -> {
                        Toast.makeText(RegistrarDatosVecino.this, "¡Usuario creado correctamente!", Toast.LENGTH_SHORT).show();
                        Intent principal = new Intent(RegistrarDatosVecino.this, PrincipalActivity.class);
                        startActivity(principal);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(RegistrarDatosVecino.this, "Error al guardar datos personales: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al guardar el documento", e);
                    });
                }
            } else {
                Toast.makeText(RegistrarDatosVecino.this, "Error al comprobar la existencia de la cuenta: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firestore", "Error al realizar la consulta", task.getException());
            }
        });
    }


    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar registro");

        // Infla el View personalizado
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_checkbox, null);
        CheckBox checkBox = dialogView.findViewById(R.id.dialog_checkbox);
        TextView textView = dialogView.findViewById(R.id.dialog_textView);

        // Establecer el enlace
        String rgpd = "\t\tReglamento General de Protección de Datos \n\t\t(RGPD)";
        SpannableString ss = new SpannableString(rgpd);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://eur-lex.europa.eu/legal-content/ES/TXT/?uri=celex%3A32016R0679";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };
        ss.setSpan(clickableSpan, 0, rgpd.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // Agrega el View personalizado al builder
        builder.setView(dialogView);

        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            if (checkBox.isChecked()) {
                registrarUsuario(id_inputcorreo.getText().toString(), id_inputcontrasena.getText().toString());
            } else {
                // El usuario no ha aceptado los términos y condiciones, haz algo aquí
                Toast.makeText(this, "Debe aceptar los términos y condiciones para registrarse.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Atrás", (dialog, id) -> {
            // No es necesario realizar ninguna acción, simplemente se cierra el diálogo
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Obtener los botones del AlertDialog
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // Establecer el estilo de fuente en negrita
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        positiveButton.setTypeface(boldTypeface);
        negativeButton.setTypeface(boldTypeface);

        // Establecer el color hexadecimal del texto en los botones
        int greenColor = Color.parseColor("#66BB00");
        int redColor = Color.parseColor("#FF0000");
        positiveButton.setTextColor(greenColor);
        negativeButton.setTextColor(redColor);
    }



    public void verificarCampos(View view) {
        EditText inputUsuario = findViewById(R.id.id_inputusuario);
        EditText inputContrasena = findViewById(R.id.id_inputcontrasena);
        EditText inputNombre = findViewById(R.id.id_inputnombre);
        EditText inputApellidos = findViewById(R.id.id_inputapellidos);
        EditText inputDni = findViewById(R.id.id_inputdni);
        EditText inputTelefono = findViewById(R.id.id_inputtelefono);
        EditText inputCorreo = findViewById(R.id.id_inputcorreo);
        EditText inputDireccion = findViewById(R.id.id_inputdireccion);
        EditText inputPortal = findViewById(R.id.id_inputportal);
        EditText inputPuerta = findViewById(R.id.id_inputpuerta);
        EditText inputLocalidad = findViewById(R.id.id_inputlocalidad);
        EditText inputProvincia = findViewById(R.id.id_inputprovincia);
        EditText inputCp = findViewById(R.id.id_inputcp);

        if (campoVacio(inputUsuario) || campoVacio(inputContrasena) || campoVacio(inputNombre) || campoVacio(inputApellidos) || campoVacio(inputDni) || campoVacio(inputTelefono) || campoVacio(inputCorreo) || campoVacio(inputDireccion) || campoVacio(inputPortal) || campoVacio(inputPuerta) || campoVacio(inputLocalidad) || campoVacio(inputProvincia) || campoVacio(inputCp)) {
            Toast.makeText(this, "Por favor, complete todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
        } else if (!longitudExacta(inputDni, 9) || !longitudExacta(inputTelefono, 9) || !longitudExacta(inputCp, 5)) {
            Toast.makeText(this, "Por favor, asegúrese de que los campos tienen la longitud correcta", Toast.LENGTH_SHORT).show();
        } else if (!validarContrasenya(inputContrasena, 6)) {
            Toast.makeText(this, "La longitud mínima de la contraseña debe ser de 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            // Muestra el diálogo de confirmación cuando todos los campos estén completados y validados
            mostrarDialogoConfirmacion();
        }
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean longitudExacta(EditText editText, int longitud) {
        return editText.getText().toString().trim().length() == longitud;
    }

    private boolean validarContrasenya(EditText editText, int longitud) {
        return editText.getText().toString().trim().length() >= longitud;
    }

    public String mayusPrimerCaracter(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }

        String firstChar = input.substring(0, 1).toUpperCase();
        String restOfString = input.substring(1).toLowerCase();

        return firstChar + restOfString;
    }


    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }
}
