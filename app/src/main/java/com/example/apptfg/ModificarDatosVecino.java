package com.example.apptfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModificarDatosVecino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos_vecino);
        Button btnCancelar = findViewById(R.id.id_btn_volver_registro);

        btnCancelar.setOnClickListener(view -> {
            Intent anterior = new Intent(this, VerDatosVecino.class);
            startActivity(anterior);
        });

    }

    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar modificación");
        builder.setMessage("Sus datos serán actualizados.   ¿Está seguro?");

        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            //Navega a la anterior actividad y muestra el toast de confirmación.

            Intent principal = new Intent(this, VerDatosVecino.class);
            startActivity(principal);
            Toast.makeText(this, "¡Datos actualizados correctamente!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, id) -> {
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

    public void actualizarCampos(View view) {
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
            mostrarDialogoConfirmacion(); // Muestra el diálogo de confirmación cuando todos los campos estén completados
        }
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}
