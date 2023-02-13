package com.example.apptfg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PagarLuz extends AppCompatActivity {

    private EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_luz);

        EditText editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

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
    }
}