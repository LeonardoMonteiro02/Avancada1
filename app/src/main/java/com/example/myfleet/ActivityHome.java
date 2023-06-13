package com.example.myfleet;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
// ... outras importações ...

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;




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
    private GoogleMap map;


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
        startingPointAutoComplete = findViewById(R.id.starting_point);
        arrivalPointAutoComplete = findViewById(R.id.arrival_point);
        calculateRouteButton = findViewById(R.id.buttonCalculateRoute);
        car1ImageButton = findViewById(R.id.imageButtonCar1);
        car2ImageButton = findViewById(R.id.imageButtonCar2);
        car3ImageButton = findViewById(R.id.imageButtonCar3);
        car4ImageButton = findViewById(R.id.imageButtonCar4);
        startSimulationButton = findViewById(R.id.buttonSimulacao);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        textRota.setTypeface(customFont);
        textTipoVeiculo.setTypeface(customFont);

        BottomNavigationConfig.configureNavigation(this, bottomNavigationView, bottomNavigationView2);

        // Inicializar o Places SDK
        Places.initialize(getApplicationContext(), "AIzaSyCQgQeznfQnTbNtdHVNF2zvrokBc0rGLRI");

        // Criar um cliente do Places
        PlacesClient placesClient = Places.createClient(this);

        // Criar um token de sessão para as solicitações de previsão de autocompletar
        AutocompleteSessionToken sessionToken = AutocompleteSessionToken.newInstance();

        // Definir um filtro personalizado para remover caracteres inválidos dos textos de entrada
        startingPointAutoComplete.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        arrivalPointAutoComplete.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        // Definir um adaptador de filtro personalizado para o AutoCompleteTextView
        startingPointAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(placesClient, sessionToken));
        arrivalPointAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(placesClient, sessionToken));

        calculateRouteButton.setOnClickListener(v -> {
            String startPoint = startingPointAutoComplete.getText().toString();
            String destination = arrivalPointAutoComplete.getText().toString();

          /*  String toastMessage = "Ponto de Partida: " + startPoint + "\n" +
                    "Ponto de Chegada: " + destination;
            Toast.makeText(ActivityHome.this, toastMessage, Toast.LENGTH_SHORT).show();*/

            // Obter as coordenadas do ponto de partida e chegada
            getCoordinates(startPoint, destination);
        });

        car1ImageButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Clicou no Carro 1", Toast.LENGTH_SHORT).show();
        });

        car2ImageButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Clicou no Carro 2", Toast.LENGTH_SHORT).show();
        });

        car3ImageButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Clicou no Carro 3", Toast.LENGTH_SHORT).show();
        });

        car4ImageButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Clicou no Carro 4", Toast.LENGTH_SHORT).show();
        });

        startSimulationButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Iniciando simulação...", Toast.LENGTH_SHORT).show();
            // Realizar a lógica para iniciar a simulação
            // ...
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        mHandler = new Handler();
        mHandler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> {

        private PlacesClient placesClient;
        private AutocompleteSessionToken sessionToken;

        PlacesAutoCompleteAdapter(PlacesClient placesClient, AutocompleteSessionToken sessionToken) {
            super(ActivityHome.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
            this.placesClient = placesClient;
            this.sessionToken = sessionToken;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<String> predictions = new ArrayList<>();

                    // Verificar se o texto de entrada é vazio
                    if (constraint != null) {
                        // Criar uma solicitação para buscar previsões de autocompletar
                        FindAutocompletePredictionsRequest predictionsRequest =
                                FindAutocompletePredictionsRequest.builder()
                                        .setCountry("BR")
                                        .setSessionToken(sessionToken)
                                        .setQuery(constraint.toString())
                                        .build();

                        // Enviar a solicitação para buscar as previsões de autocompletar
                        Task<FindAutocompletePredictionsResponse> autocompletePredictions =
                                placesClient.findAutocompletePredictions(predictionsRequest);

                        try {
                            // Aguardar a resposta com as previsões de autocompletar
                            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);

                            if (autocompletePredictions.isSuccessful()) {
                                FindAutocompletePredictionsResponse response = autocompletePredictions.getResult();
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
                                Exception exception = autocompletePredictions.getException();
                                if (exception != null) {
                                    exception.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        results.values = predictions;
                        results.count = predictions.size();
                    }

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
                    } else {
                        return super.convertResultToString(resultValue);
                    }
                }
            };

            return filter;
        }
    }
    private void getCoordinates(String startPoint, String destination) {
        List<Address> startAddresses = null;
        List<Address> destinationAddresses = null;
        Geocoder geocoder = new Geocoder(ActivityHome.this, Locale.getDefault());

        try {
            startAddresses = geocoder.getFromLocationName(startPoint, 1);
            destinationAddresses = geocoder.getFromLocationName(destination, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (startAddresses != null && startAddresses.size() > 0 && destinationAddresses != null && destinationAddresses.size() > 0) {
            Address startAddress = startAddresses.get(0);
            Address destinationAddress = destinationAddresses.get(0);

            double startLatitude = startAddress.getLatitude();
            double startLongitude = startAddress.getLongitude();

            double destinationLatitude = destinationAddress.getLatitude();
            double destinationLongitude = destinationAddress.getLongitude();

            // Use as coordenadas obtidas para a lógica de cálculo da rota
            LatLng startLatLng = new LatLng(startLatitude, startLongitude);
            LatLng destinationLatLng = new LatLng(destinationLatitude, destinationLongitude);

            // Inicialize o MapFragment
            MapFragment mapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.mapFragment, mapFragment)
                    .commit();

            // Registrar o callback para ser notificado quando o mapa estiver pronto
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // A instância do GoogleMap foi obtida com sucesso
                    map = googleMap;

                    // Adicione o marcador para o ponto de partida
                    map.addMarker(new MarkerOptions()
                            .position(startLatLng)
                            .title("Ponto de Partida")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    // Adicione o marcador para o ponto de chegada
                    map.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Ponto de Chegada")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    // Crie um objeto LatLngBounds para incluir os dois marcadores
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(startLatLng);
                    builder.include(destinationLatLng);
                    LatLngBounds bounds = builder.build();

                    // Calcule o tamanho da janela de visualização do mapa para incluir os dois marcadores
                    int padding = 100; // Valor em pixels para adicionar espaço ao redor dos marcadores
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    // Aplique o zoom e mova a câmera para exibir os marcadores
                    map.moveCamera(cu);


                    // Crie uma instância da classe RouteCalculator
                    RouteCalculator routeCalculator = new RouteCalculator(map, startLatLng, destinationLatLng, getApplicationContext());

                    // Chame o método calculateRoute()
                    routeCalculator.calculateRoute();
                }
            });

        } else {
            Toast.makeText(ActivityHome.this, "Não foi possível obter as coordenadas", Toast.LENGTH_SHORT).show();
        }
    }
}


