package com.example.apptfg;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class VerDatosVecino extends AppCompatActivity {

    private ActivityResultLauncher<String> galleryLauncher;
    private ImageButton btnImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_vecino);

        btnImagen = findViewById(R.id.id_btn_imagen_perfil);
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
        Button btnModificarDatos = findViewById(R.id.id_btn_modificar_datos);
        btnModificarDatos.setOnClickListener(v -> {
            // Abrir la nueva vista con los datos del vecino
            Intent intent = new Intent(VerDatosVecino.this, ModificarDatosVecino.class);
            startActivity(intent);
        });
    }

    //Función para volver a la ventana principal
    public void anteriorVentanaPerfil(View view){
        Intent anterior = new Intent(this,PrincipalActivity.class);
        startActivity(anterior);
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Aquí puedes hacer algo con la imagen seleccionada
            }
        });
    }
}
