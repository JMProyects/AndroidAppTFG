package com.example.apptfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PagarComunidad extends AppCompatActivity {
    private boolean isCardFront = true;
    EditText editText1;
    private EditText editText2;
    EditText editText3;
    private EditText editText4;
    private EditText expiryEditText2;
    private ImageView cardFrontImage;
    private ImageView cardBackImage;
    private EditText cvvEditText2;
    TextView importe;
    AlertDialog progressDialog;
    private static final char space = ' ';
    private static final char barra = '/';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_comunidad);
        Button id_btn_continuar_pago_comunidad = findViewById(R.id.id_btn_continuar_pago_comunidad);

        cardFrontImage = findViewById(R.id.imageView2);
        cardBackImage = findViewById(R.id.cardBackImage);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);

        EditText cvvEditText1 = findViewById(R.id.editText5);
        //editText7 = findViewById(R.id.editText7);
        EditText expiryEditText1 = findViewById(R.id.editText6);
        //editText8 = findViewById(R.id.editText8);
        importe = findViewById(R.id.id_txt_importe_comunidad2);

        cvvEditText2 = findViewById(R.id.editText7);
        expiryEditText2 = findViewById(R.id.editText8);

        // Establecer la visibilidad inicial de los EditTexts en "invisible"
        cvvEditText2.setVisibility(View.INVISIBLE);

        //esto escribe el nombre del cliente del text view en el editText simultáneamente.
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText2.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editText1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !isCardFront) {
                flipCard(false);
            }
        });

        //esto escribe el numero de tarjeta del text view en el editText simultáneamente.
        //y ademas, mete un espacio cada 4 digitos.
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText4.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove all spacing char
                int pos = 0;
                while (pos < s.length()) {
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }

                // Insert char where needed.
                pos = 4;
                while (pos < s.length()) {
                    final char c = s.charAt(pos);
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space);
                    }
                    pos += 5;
                }
            }
        });

        editText3.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !isCardFront) {
                flipCard(false);
            }
        });

        // Agregar los TextWatchers para los EditTexts correspondientes
        cvvEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cvvEditText2.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        expiryEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                expiryEditText2.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove all spacing char
                int pos = 0;
                while (pos < s.length()) {
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }

                // Insert char where needed.
                pos = 2;
                while (pos < s.length()) {
                    final char c = s.charAt(pos);
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + barra);
                    }
                    pos += 3;
                }
            }
        });

        // Agregar los OnClickListener para los EditTexts correspondientes
        cvvEditText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardBackImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            flipCard(true);
                            cardBackImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                }
            }
        });

        expiryEditText1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !isCardFront) {
                flipCard(false);
            }
        });

    }

    private void flipCard(boolean showCardBack) {
        ObjectAnimator flipFront;
        ObjectAnimator flipBack;

        if (showCardBack) {
            flipFront = ObjectAnimator.ofFloat(cardFrontImage, "rotationY", 0f, 90f);
            flipBack = ObjectAnimator.ofFloat(cardBackImage, "rotationY", -90f, 0f);
        } else {
            flipFront = ObjectAnimator.ofFloat(cardBackImage, "rotationY", 0f, 90f);
            flipBack = ObjectAnimator.ofFloat(cardFrontImage, "rotationY", -90f, 0f);
        }

        flipFront.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (showCardBack) {
                    cardFrontImage.setVisibility(View.INVISIBLE);
                    cardBackImage.setVisibility(View.VISIBLE);
                    editText2.setVisibility(View.INVISIBLE);
                    editText4.setVisibility(View.INVISIBLE);
                    expiryEditText2.setVisibility(View.INVISIBLE);
                    cvvEditText2.setVisibility(View.VISIBLE);
                } else {
                    cardFrontImage.setVisibility(View.VISIBLE);
                    cardBackImage.setVisibility(View.INVISIBLE);
                    editText2.setVisibility(View.VISIBLE);
                    editText4.setVisibility(View.VISIBLE);
                    expiryEditText2.setVisibility(View.VISIBLE);
                    cvvEditText2.setVisibility(View.INVISIBLE);
                }
                animation.removeAllListeners();
            }
        });

        flipBack.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isCardFront) {
                    cardBackImage.setVisibility(View.VISIBLE);
                } else {
                    cardFrontImage.setVisibility(View.VISIBLE);
                }
            }
        });

        AnimatorSet flipAnimation = new AnimatorSet();
        flipAnimation.playSequentially(flipFront, flipBack);
        flipAnimation.start();

        isCardFront = !isCardFront;
    }

    private void guardarDatosPago(String nombre, String numeroTarjeta) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        showProgressDialog();
        String userEmail = currentUser.getEmail();

        // Obtener la urbanización del usuario
        DocumentReference docRef = db.collection("vecinos").document(userEmail);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String urbanizacion = document.getString("urbanizacion");

                    // Crear el objeto pago
                    Map<String, Object> pago = new HashMap<>();
                    int idPago = new Random().nextInt(100000);
                    String idPagoFormateado = String.format("%05d", idPago);
                    pago.put("usuario", userEmail);
                    pago.put("identificador", idPagoFormateado);
                    pago.put("nombre", nombre);
                    pago.put("servicio", "Comunidad");
                    pago.put("numero_tarjeta", numeroTarjeta);
                    pago.put("importe", importe.getText().toString());
                    pago.put("fecha_pago", new Timestamp(new Date()));
                    pago.put("urbanizacion", urbanizacion);

                    // Agregar el objeto pago a la base de datos
                    db.collection("recibos").add(pago).addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Documento guardado con ID: " + documentReference.getId());
                        Intent principal = new Intent(PagarComunidad.this, PagoConfirmado.class);
                        principal.putExtra("identificador", idPagoFormateado);
                        startActivity(principal);
                    }).addOnFailureListener(e -> Log.w("Firestore", "Error al guardar el documento", e));
                } else {
                    Log.d("Firestore", "No se encontró ningún documento");
                }
            } else {
                Log.d("Firestore", "Error al obtener el documento", task.getException());
            }
        });
    }

    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar pago");
        builder.setMessage("Está a punto de realizar el pago del recibo de comunidad con un importe\nde 45€.\n\n¿Está seguro?");

        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            //Navega a la anterior actividad y muestra el toast de confirmación.
            String nombre = editText1.getText().toString();
            String numeroTarjeta = editText3.getText().toString().replaceAll(" ", ""); // Elimina los espacios en el número de tarjeta
            guardarDatosPago(nombre, numeroTarjeta);
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

    public void verificarPago(View view) {
        EditText inputNombreVecino = findViewById(R.id.editText1);
        EditText inputNumTarjeta = findViewById(R.id.editText3);
        EditText inputCvv = findViewById(R.id.editText5);
        EditText inputFechaCad = findViewById(R.id.editText6);

        if (campoVacio(inputNombreVecino) || campoVacio(inputNumTarjeta) || campoVacio(inputCvv) || campoVacio(inputFechaCad)) {
            Toast.makeText(this, "Por favor, complete todos los campos antes de continuar", Toast.LENGTH_SHORT).show();
        } else if (!longitudExacta(inputNumTarjeta, 19) || !longitudExacta(inputCvv, 3) || !longitudExacta(inputFechaCad, 5)) {
            Toast.makeText(this, "Por favor, asegúrese de que los campos tienen la longitud correcta", Toast.LENGTH_SHORT).show();
        } else if (!fechaCaducidadValida(inputFechaCad)) {
            Toast.makeText(this, "La fecha de caducidad de la tarjeta es incorrecta", Toast.LENGTH_SHORT).show();
        } else {
            mostrarDialogoConfirmacion(); // Muestra el diálogo de confirmación cuando todos los campos estén completados
        }
    }

    private boolean fechaCaducidadValida(EditText expiryDateEditText) {
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String pattern = "^(0[1-9]|1[0-2])/([0-9]{2})$"; // Formato: MM/YY
        if (!expiryDate.matches(pattern)) {
            return false;
        }
        String[] dateParts = expiryDate.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int year = Integer.parseInt(dateParts[1]);

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH empieza en 0
        int currentYear = calendar.get(Calendar.YEAR) % 100; // Obtiene los dos últimos dígitos del año

        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false;
        }
        Log.d("FECHA_CADUCIDAD", "Mes: " + month + ", Año: " + year + ", Fecha válida: " + true);
        return !(year < currentYear || (year == currentYear && month < currentMonth));
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean longitudExacta(EditText editText, int longitud) {
        return editText.getText().toString().trim().length() == longitud;
    }

    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Pagos.class);
        startActivity(anterior);
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