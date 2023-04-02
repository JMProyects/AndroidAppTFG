package com.example.apptfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PagarComunidad extends AppCompatActivity {
    private boolean isCardFront = true;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText expiryEditText2;
    private ImageView cardFrontImage;
    private ImageView cardBackImage;
    private EditText cvvEditText1;
    private EditText cvvEditText2;
    private EditText expiryEditText1;
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

        cvvEditText1 = findViewById(R.id.editText5);
        //editText7 = findViewById(R.id.editText7);
        expiryEditText1 = findViewById(R.id.editText6);
        //editText8 = findViewById(R.id.editText8);

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

                //expiryEditText1.setText(s.toString());
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

    public void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar pago");
        builder.setMessage("Está a punto de realizar el pago del recibo de comunidad con un importe de 45€. ¿Está seguro?");

        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            //Navega a la anterior actividad y muestra el toast de confirmación.

            Intent principal = new Intent(this, PagoConfirmado.class);
            startActivity(principal);
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
        } else {
            mostrarDialogoConfirmacion(); // Muestra el diálogo de confirmación cuando todos los campos estén completados
        }
    }

    private boolean campoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public void anteriorVentana(View view) {
        Intent anterior = new Intent(this, Pagos.class);
        startActivity(anterior);
    }
}