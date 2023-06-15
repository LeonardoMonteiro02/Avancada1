package com.example.myfleet;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceTimeCalculator {

    private Context context;
    private LatLng startLatLng;
    private LatLng destinationLatLng;
    private String toastMessage;

    public DistanceTimeCalculator(Context context, LatLng startLatLng, LatLng destinationLatLng) {
        this.context = context;
        this.startLatLng = startLatLng;
        this.destinationLatLng = destinationLatLng;
    }

    /**
     * Atualiza a distância total e o tempo total estimado da rota e retorna a mensagem do Toast.
     */
    public String updateTotalDistanceAndTime() {
        String apiKey = "AIzaSyCQgQeznfQnTbNtdHVNF2zvrokBc0rGLRI";
        String origins = startLatLng.latitude + "," + startLatLng.longitude;
        String destinations = destinationLatLng.latitude + "," + destinationLatLng.longitude;
        String units = "metric";

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins + "&destinations=" + destinations + "&units=" + units + "&key=" + apiKey;

        // Executar a tarefa em segundo plano para buscar o JSON
        new FetchJsonTask().execute(url);

        // Retornar a mensagem do Toast (vazio por enquanto, será atualizada no onPostExecute)
        return toastMessage;
    }

    private class FetchJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    json = stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String distanceText = jsonObject.getJSONArray("rows").getJSONObject(0)
                        .getJSONArray("elements").getJSONObject(0)
                        .getJSONObject("distance").getString("text");
                String durationText = jsonObject.getJSONArray("rows").getJSONObject(0)
                        .getJSONArray("elements").getJSONObject(0)
                        .getJSONObject("duration").getString("text");
                int distanceValue = jsonObject.getJSONArray("rows").getJSONObject(0)
                        .getJSONArray("elements").getJSONObject(0)
                        .getJSONObject("distance").getInt("value");
                int durationValue = jsonObject.getJSONArray("rows").getJSONObject(0)
                        .getJSONArray("elements").getJSONObject(0)
                        .getJSONObject("duration").getInt("value");

                // Cálculo da velocidade média
                double distanceInKm = distanceValue / 1000.0; // Converter a distância de metros para quilômetros
                double timeInHours = durationValue / 3600.0; // Converter o tempo de segundos para horas
                double speedInKph = distanceInKm / timeInHours; // Calcular a velocidade média em quilômetros por hora

                toastMessage = "Distance: " + distanceText + "\nDuration: " + durationText +
                        "\nVelocidade média: " + String.format("%.2f", speedInKph) + " km/h";
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
