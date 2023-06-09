package com.example.myfleet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

        // Inicialize a biblioteca de lugares
        Places.initialize(getApplicationContext(), "AIzaSyCQgQeznfQnTbNtdHVNF2zvrokBc0rGLRI");

        // Crie um cliente do Google Places
        PlacesClient placesClient = Places.createClient(this);

        // Crie um token de sessão para as solicitações de Autocomplete
        AutocompleteSessionToken sessionToken = AutocompleteSessionToken.newInstance();

        // Crie um adaptador de sugestões de locais
        ArrayAdapter<AutocompletePrediction> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        startingPointAutoComplete.setAdapter(adapter);
        arrivalPointAutoComplete.setAdapter(adapter);

        startingPointAutoComplete.setThreshold(1);
        arrivalPointAutoComplete.setThreshold(1);

        startingPointAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            AutocompletePrediction prediction = adapter.getItem(position);
            if (prediction != null) {
                String placeId = prediction.getPlaceId();
                // Realize a lógica para obter os detalhes do local com base no ID do lugar
                // ...
            }
        });

        arrivalPointAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            AutocompletePrediction prediction = adapter.getItem(position);
            if (prediction != null) {
                String placeId = prediction.getPlaceId();
                // Realize a lógica para obter os detalhes do local com base no ID do lugar
                // ...
            }
        });

        startingPointAutoComplete.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                return text != null && !text.toString().isEmpty();
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return "";
            }
        });

        arrivalPointAutoComplete.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                return text != null && !text.toString().isEmpty();
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return "";
            }
        });

        startingPointAutoComplete.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        arrivalPointAutoComplete.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        // Defina um adaptador de filtro personalizado para o AutoCompleteTextView
        startingPointAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(placesClient, sessionToken));
        arrivalPointAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(placesClient, sessionToken));

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

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> {

        private PlacesClient placesClient;
        private AutocompleteSessionToken sessionToken;

        public PlacesAutoCompleteAdapter(PlacesClient placesClient, AutocompleteSessionToken sessionToken) {
            super(ActivityHome.this, android.R.layout.simple_dropdown_item_1line);
            this.placesClient = placesClient;
            this.sessionToken = sessionToken;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<String> predictions = new ArrayList<>(); // Lista de Strings para armazenar as previsões formatadas

                    // Execute a solicitação de previsões de autocompletar para o texto de entrada
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(sessionToken)
                            .setQuery(constraint.toString())
                            .build();

                    try {
                        // Aguarde a conclusão da solicitação de previsões de autocompletar
                        Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(request);
                        Tasks.await(task, 10, TimeUnit.SECONDS);

                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse response = task.getResult();
                            if (response != null) {
                                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                    // Formatando o nome da rua, cidade e estado
                                    String formattedAddress = prediction.getPrimaryText(null).toString();
                                    String secondaryText = prediction.getSecondaryText(null).toString();
                                    String formattedPrediction = formattedAddress + ", " + secondaryText;
                                    predictions.add(formattedPrediction); // Adiciona a previsão formatada à lista
                                }
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    results.values = predictions;
                    results.count = predictions.size();
                    return results;
                }


                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    clear();
                    if (results != null && results.count > 0) {
                        addAll((List<AutocompletePrediction>) results.values);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    if (resultValue instanceof AutocompletePrediction) {
                        return ((AutocompletePrediction) resultValue).getFullText(null);
                    }
                    return super.convertResultToString(resultValue);
                }
            };
            return filter;
        }
    }
}