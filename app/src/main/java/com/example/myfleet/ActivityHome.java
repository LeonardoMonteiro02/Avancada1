/*********************************************************************

 Nome do arquivo: ActivityHome.java

 Descrição: O código desenvolve a tela inicial do aplicativo MyFleet para Android, com recursos
            de autocompletar locais, exibição de mapas e cálculo de rotas. Ele verifica se o usuário
            está logado e redireciona para a tela de login, se necessário. O código utiliza a
            biblioteca Places da Google para previsões de autocompletar. Ao calcular a rota, ele
            obtém as coordenadas dos pontos de partida e chegada e as exibe no mapa, adicionando marcadores.
            Também oferece interações ao pressionar botões de carros e iniciar a simulação, exibindo
            mensagens ao usuário. Implementa uma confirmação dupla ao pressionar o botão "Voltar" para
            sair do aplicativo.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/


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

        // Recuperar informações de login armazenadas no SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        // Verificar se o usuário está logado
        if (!isLoggedIn) {
            // Redirecionar para a tela de login se o usuário não estiver logado
            Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        }

        // Inicializar as views
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

        // Definir a fonte personalizada para os textos
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        textRota.setTypeface(customFont);
        textTipoVeiculo.setTypeface(customFont);

        // Configurar a navegação inferior
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

        // Lidar com o clique no botão de calcular rota
        calculateRouteButton.setOnClickListener(v -> {
            String startPoint = startingPointAutoComplete.getText().toString();
            String destination = arrivalPointAutoComplete.getText().toString();

            // Obter as coordenadas do ponto de partida e chegada
            getCoordinates(startPoint, destination);
        });

        // Lidar com o clique nos botões dos carros
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

        // Lidar com o clique no botão de iniciar simulação
        startSimulationButton.setOnClickListener(v -> {
            Toast.makeText(ActivityHome.this, "Iniciando simulação...", Toast.LENGTH_SHORT).show();
            // Realizar a lógica para iniciar a simulação
            // ...
        });
    }



    /**
     * Sobrescreve o método onBackPressed() para controlar o comportamento do botão de voltar.
     * Se o usuário pressionar o botão de voltar duas vezes dentro de um intervalo curto,
     * a atividade é encerrada. Caso contrário, exibe um Toast informando ao usuário que
     * precisa pressionar novamente para sair.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // Se o usuário pressionar o botão de voltar duas vezes, a atividade é encerrada
            super.onBackPressed();
            return;
        }

        // Define doubleBackToExitPressedOnce como true para indicar que o botão de voltar foi pressionado uma vez
        this.doubleBackToExitPressedOnce = true;
        // Exibe um Toast informando ao usuário que precisa pressionar novamente para sair
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        // Cria um novo Handler para reverter o status de doubleBackToExitPressedOnce após um determinado tempo
        mHandler = new Handler();
        mHandler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    /**
     * Classe interna PlacesAutoCompleteAdapter que estende ArrayAdapter<AutocompletePrediction>.
     * Essa classe é responsável por fornecer um adaptador personalizado para o AutoCompleteTextView
     * utilizado para o autocompletar de lugares no aplicativo.
     */
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> {

        private PlacesClient placesClient;
        private AutocompleteSessionToken sessionToken;

        PlacesAutoCompleteAdapter(PlacesClient placesClient, AutocompleteSessionToken sessionToken) {
            super(ActivityHome.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
            this.placesClient = placesClient;
            this.sessionToken = sessionToken;
        }

        /**
         * Sobrescreve o método getFilter() para retornar um filtro personalizado para o AutoCompleteTextView.
         * Esse filtro é responsável por realizar as solicitações de previsões de autocompletar para o Places SDK
         * e retornar os resultados filtrados.
         */
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

    /**
     * Método getCoordinates() responsável por obter as coordenadas geográficas (latitude e longitude)
     * dos pontos de partida e destino fornecidos como parâmetros. As coordenadas são usadas para calcular
     * a rota no mapa.
     */
    private void getCoordinates(String startPoint, String destination) {
        List<Address> startAddresses = null;
        List<Address> destinationAddresses = null;
        Geocoder geocoder = new Geocoder(ActivityHome.this, Locale.getDefault());

        try {
            // Obter os endereços dos pontos de partida e destino usando o Geocoder
            startAddresses = geocoder.getFromLocationName(startPoint, 1);
            destinationAddresses = geocoder.getFromLocationName(destination, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verificar se os endereços foram obtidos com sucesso
        if (startAddresses != null && startAddresses.size() > 0 && destinationAddresses != null && destinationAddresses.size() > 0) {
            // Obter o primeiro endereço de cada lista (considerando apenas o primeiro resultado)
            Address startAddress = startAddresses.get(0);
            Address destinationAddress = destinationAddresses.get(0);

            // Obter as coordenadas (latitude e longitude) dos endereços
            double startLatitude = startAddress.getLatitude();
            double startLongitude = startAddress.getLongitude();
            double destinationLatitude = destinationAddress.getLatitude();
            double destinationLongitude = destinationAddress.getLongitude();

            // Utilizar as coordenadas obtidas para a lógica de cálculo da rota
            LatLng startLatLng = new LatLng(startLatitude, startLongitude);
            LatLng destinationLatLng = new LatLng(destinationLatitude, destinationLongitude);

            // Inicializar o MapFragment
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

                    // Adicionar marcador para o ponto de partida
                    map.addMarker(new MarkerOptions()
                            .position(startLatLng)
                            .title("Ponto de Partida")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    // Adicionar marcador para o ponto de chegada
                    map.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Ponto de Chegada")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    // Criar um objeto LatLngBounds para incluir os dois marcadores
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(startLatLng);
                    builder.include(destinationLatLng);
                    LatLngBounds bounds = builder.build();

                    // Calcular o tamanho da janela de visualização do mapa para incluir os dois marcadores
                    int padding = 100; // Valor em pixels para adicionar espaço ao redor dos marcadores
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    // Aplicar o zoom e mover a câmera para exibir os marcadores
                    map.moveCamera(cu);

                    // Criar uma instância da classe RouteCalculator
                    RouteCalculator routeCalculator = new RouteCalculator(map, startLatLng, destinationLatLng, getApplicationContext());

                    // Chamar o método calculateRoute() da classe RouteCalculator para calcular a rota
                    routeCalculator.calculateRoute();
                }
            });

        } else {
            Toast.makeText(ActivityHome.this, "Não foi possível obter as coordenadas", Toast.LENGTH_SHORT).show();
        }
    }
}


