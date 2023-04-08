package com.example.apptfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistrarDatosVecino extends AppCompatActivity {
    private ActivityResultLauncher<String> galleryLauncher;
    private TextView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_vecino);

        fotoPerfil = findViewById(R.id.id_lblfotoperfilperfil);
        setupGalleryLauncher();
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Guarda la imagen seleccionada en un archivo
                    File imageFile = saveImageToExternalStorage(bitmap);
                    // Guardar la ruta de la imagen en SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("imagen_perfil", MODE_PRIVATE).edit();
                    editor.putString("imagen_perfil", imageFile.getAbsolutePath());
                    editor.apply();

                    // Muestra el ícono de tick
                    ImageView tickIcon = findViewById(R.id.image_tick);
                    tickIcon.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fotoPerfil.setOnClickListener(v -> galleryLauncher.launch("image/*"));
    }

    private File saveImageToExternalStorage(Bitmap bitmap) {
        String fileName = "userProfileImage.jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }


    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, MainActivity.class);
        startActivity(anterior);
    }

    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar registro");
        builder.setMessage("Está a punto de darse de alta en el sistema. ¿Está seguro?");


        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            // Aquí puedes realizar las acciones necesarias al confirmar el registro
            // Por ejemplo, navegar a la siguiente actividad
            Intent principal = new Intent(RegistrarDatosVecino.this, PrincipalActivity.class);

            // Pasa la ruta de la imagen seleccionada a la siguiente actividad
            File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "userProfileImage.jpg");
            if (imageFile.exists()) {
                principal.putExtra("profile_image_path", imageFile.getAbsolutePath());
            }

            startActivity(principal);
            Toast.makeText(this, "¡Usuario creado correctamente!", Toast.LENGTH_SHORT).show();
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
        EditText inputLocalidad = findViewById(R.id.id_inputlocalidad);
        EditText inputProvincia = findViewById(R.id.id_inputprovincia);
        EditText inputCp = findViewById(R.id.id_inputcp);

        if (campoVacio(inputUsuario) || campoVacio(inputContrasena) || campoVacio(inputNombre) || campoVacio(inputApellidos) || campoVacio(inputDni) || campoVacio(inputTelefono) || campoVacio(inputCorreo) || campoVacio(inputDireccion) || campoVacio(inputLocalidad) || campoVacio(inputProvincia) || campoVacio(inputCp)) {
            Toast.makeText(this, "Por favor, complete todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
        } else if (!longitudExacta(inputDni, 9) || !longitudExacta(inputTelefono, 9) || !longitudExacta(inputCp, 5)) {
            Toast.makeText(this, "Por favor, asegúrese de que los campos tienen la longitud correcta", Toast.LENGTH_SHORT).show();
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

}
