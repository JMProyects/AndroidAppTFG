package com.example.apptfg;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class VerDatosVecino extends AppCompatActivity {

    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_vecino);
        Button btnModificarDatos = findViewById(R.id.id_btn_modificar_datos);
        Button id_btn_volver_registro = findViewById(R.id.id_btn_volver_registro);
        ImageButton btnImagen = findViewById(R.id.id_btn_imagen_perfil);
        setupGalleryLauncher();

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
            }
        });
    }
}