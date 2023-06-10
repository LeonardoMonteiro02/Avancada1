package com.example.myfleet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalculatorActivity extends AppCompatActivity {

    private EditText etInput;
    private TextView tvResult;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);


        tvResult = findViewById(R.id.tv_result);

        Button btnEquals = findViewById(R.id.btn_equals);
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        Button btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView2 = findViewById(R.id.bottom_navigation2);

        BottomNavigationConfig.configureNavigation(this, bottomNavigationView, bottomNavigationView2);
    }

    private void calculateResult() {
        String input = etInput.getText().toString().trim();

        // Perform your calculation logic here
        // For example, you can use the built-in JavaScript evaluator or implement your own calculation logic

        // Display the result
        tvResult.setText("Result: " + input);
    }

    private void clearInput() {
        etInput.setText("");
        tvResult.setText("");
    }
}
