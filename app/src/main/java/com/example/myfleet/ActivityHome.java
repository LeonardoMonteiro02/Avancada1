package com.example.myfleet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;
    private SharedPreferences sharedPreferences;
    private boolean isLoggedIn;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler;
    private TextView textRota;
    private TextView textTipoVeiculo;
    private TextInputEditText startingPointEditText;
    private TextInputEditText arrivalPointEditText;
    private Button calculateRouteButton;
    private ImageButton car1ImageButton;
    private ImageButton car2ImageButton;
    private ImageButton car3ImageButton;
    private ImageButton car4ImageButton;
    private Button startSimulationButton;
    private AutoCompleteTextView startingPointAutoComplete;
    private AutoCompleteTextView arrivalPointAutoComplete;
    private String[] suggestionsArray = {"Location 1", "Location 2", "Location 3", "Location 4"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView2 = findViewById(R.id.bottom_navigation2);
        textRota = findViewById(R.id.textRota);
        textTipoVeiculo = findViewById(R.id.texttipo_veiculo);
        // startingPointEditText = findViewById(R.id.starting_point);
        //arrivalPointEditText = findViewById(R.id.arrival_point);
        calculateRouteButton = findViewById(R.id.buttonCalculateRoute);
        car1ImageButton = findViewById(R.id.imageButtonCar1);
        car2ImageButton = findViewById(R.id.imageButtonCar2);
        car3ImageButton = findViewById(R.id.imageButtonCar3);
        car4ImageButton = findViewById(R.id.imageButtonCar4);
        startSimulationButton = findViewById(R.id.buttonSimulacao);
        startingPointAutoComplete = findViewById(R.id.starting_point);
        arrivalPointAutoComplete = findViewById(R.id.arrival_point);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        textRota.setTypeface(customFont);
        textTipoVeiculo.setTypeface(customFont);

        BottomNavigationConfig.configureNavigation(this, bottomNavigationView, bottomNavigationView2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestionsArray);
        startingPointAutoComplete.setAdapter(adapter);
        arrivalPointAutoComplete.setAdapter(adapter);

        startingPointAutoComplete.setThreshold(1);
        arrivalPointAutoComplete.setThreshold(1);

        calculateRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPoint = startingPointAutoComplete.getText().toString();
                String destination = arrivalPointAutoComplete.getText().toString();

                String toastMessage = "Ponto de Partida: " + startPoint + "\n" +
                        "Ponto de Chegada: " + destination;
                Toast.makeText(ActivityHome.this, toastMessage, Toast.LENGTH_SHORT).show();

                // Realize a lógica para calcular a rota com os pontos de partida e chegada
                // ...
            }
        });

        car1ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityHome.this, "Clicou no Carro 1", Toast.LENGTH_SHORT).show();
            }
        });

        car2ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityHome.this, "Clicou no Carro 2", Toast.LENGTH_SHORT).show();
            }
        });

        car3ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityHome.this, "Clicou no Carro 3", Toast.LENGTH_SHORT).show();
            }
        });

        car4ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityHome.this, "Clicou no Carro 4", Toast.LENGTH_SHORT).show();
            }
        });

        startSimulationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realize a lógica para iniciar a simulação
                // ...
            }
        });

        mHandler = new Handler();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}