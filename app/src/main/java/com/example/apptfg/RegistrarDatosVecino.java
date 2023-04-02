package com.example.apptfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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
                // Aquí puedes hacer algo con la imagen seleccionada
            }
        });

        fotoPerfil.setOnClickListener(v -> galleryLauncher.launch("image/*"));
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
        } else {
            // Aquí puedes agregar el código para cambiar a la siguiente vista.
            mostrarDialogoConfirmacion(); // Muestra el diálogo de confirmación cuando todos los campos estén completados
        }
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

}
