package com.example.apptfg;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModificarDatosAdmin extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modificar_datos);
        // Obtiene los datos del usuario y establece el contenido de los EditText
        getUserDataForEditing();

        Button btnCancelar = findViewById(R.id.id_btn_volver_perfil);
        btnCancelar.setOnClickListener(view -> {
            Intent anterior = new Intent(this, VerDatosAdministrador.class);
            startActivity(anterior);
        });
    }

    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar modificación");
        builder.setMessage("Sus datos serán actualizados.\n ¿Está seguro?");

        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            // Actualiza los datos en Firestore
            updateUserData();

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

    private void getUserDataForEditing() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();

        db.collection("vecinos").document(userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Obtiene los datos del usuario
                    String usuario = document.getString("usuario");
                    String contrasenya = document.getString("contraseña");
                    String nombre = document.getString("nombre");
                    String apellidos = document.getString("apellidos");
                    String dni = document.getString("dni");
                    String telefono = document.getString("telefono");
                    String correo = document.getString("correo");
                    String direccion = document.getString("direccion");
                    String portal = document.getString("portal");
                    String puerta = document.getString("puerta");
                    String localidad = document.getString("localidad");
                    String provincia = document.getString("provincia");
                    String cp = document.getString("codigo_postal");

                    // Establece los datos en EditTexts
                    EditText usuarioEditText = findViewById(R.id.id_inputusuario);
                    usuarioEditText.setText(usuario);

                    EditText contrasenyaEditText = findViewById(R.id.id_inputcontrasena);
                    contrasenyaEditText.setText(contrasenya);

                    EditText nombreEditText = findViewById(R.id.id_inputnombre);
                    nombreEditText.setText(nombre);

                    EditText apellidosEditText = findViewById(R.id.id_inputapellidos);
                    apellidosEditText.setText(apellidos);

                    EditText dniEditText = findViewById(R.id.id_inputdni);
                    dniEditText.setText(dni);

                    EditText telefonoEditText = findViewById(R.id.id_inputtelefono);
                    telefonoEditText.setText(telefono);

                    EditText correoEditText = findViewById(R.id.id_inputcorreo);
                    correoEditText.setText(correo);

                    EditText direccionEditText = findViewById(R.id.id_inputdireccion);
                    direccionEditText.setText(direccion);

                    EditText portalEditText = findViewById(R.id.id_inputportal);
                    portalEditText.setText(portal);

                    EditText puertaEditText = findViewById(R.id.id_inputpuerta);
                    puertaEditText.setText(puerta);

                    EditText localidadEditText = findViewById(R.id.id_inputlocalidad);
                    localidadEditText.setText(localidad);

                    EditText provinciaEditText = findViewById(R.id.id_inputprovincia);
                    provinciaEditText.setText(provincia);

                    EditText cpEditText = findViewById(R.id.id_inputcp);
                    cpEditText.setText(cp);

                } else {
                    Log.d(TAG, "No se encontró el documento");
                }
            } else {
                Log.d(TAG, "Error al obtener los datos del usuario: ", task.getException());
            }
        });
    }

    private void updateUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();

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

        String usuario = inputUsuario.getText().toString();
        String contrasena = inputContrasena.getText().toString();
        String nombre = inputNombre.getText().toString();
        String apellidos = inputApellidos.getText().toString();
        String dni = inputDni.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String correo = inputCorreo.getText().toString();
        String direccion = inputDireccion.getText().toString();
        String portal = inputPortal.getText().toString();
        String puerta = inputPuerta.getText().toString();
        String localidad = inputLocalidad.getText().toString();
        String provincia = inputProvincia.getText().toString();
        String cp = inputCp.getText().toString();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("usuario", usuario);
        updatedData.put("contraseña", contrasena);
        updatedData.put("nombre", nombre);
        updatedData.put("apellidos", apellidos);
        updatedData.put("dni", dni);
        updatedData.put("telefono", telefono);
        updatedData.put("correo", correo);
        updatedData.put("direccion", direccion);
        updatedData.put("portal", portal);
        updatedData.put("puerta", puerta);
        updatedData.put("localidad", localidad);
        updatedData.put("provincia", provincia);
        updatedData.put("codigo_postal", cp);

        db.collection("vecinos").document(userEmail).update(updatedData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Datos actualizados correctamente");
                updatePassword(contrasena);
            } else {
                Log.d(TAG, "Error al actualizar los datos: ", task.getException());
            }
        });
    }

    public void actualizarCamposVecino(View view) {
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
        } else {
            mostrarDialogoConfirmacion(); // Muestra el diálogo de confirmación cuando todos los campos estén completados
        }
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void updatePassword(String newPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Contraseña actualizada correctamente.");
                } else {
                    Log.d(TAG, "Error al actualizar la contraseña: ", task.getException());
                }
            });
        }
    }
}
