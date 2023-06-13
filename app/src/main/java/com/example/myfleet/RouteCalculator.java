/*********************************************************************

 Nome do arquivo: RouteCalculator.java

 Descrição: Classe responsável por calcular e exibir uma rota no mapa utilizando o serviço de direções
 do Google Maps. O cálculo da rota é realizado de forma assíncrona em segundo plano, utilizando a classe AsyncTask.
 Após o cálculo, a rota é desenhada no mapa e as informações de distância e tempo estimado são exibidas em um Toast.
 Em caso de erro no cálculo da rota, uma mensagem de erro é exibida.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/

package com.example.myfleet;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RouteCalculator {

    private static final String TAG = RouteCalculator.class.getSimpleName();

    private GoogleMap map;
    private LatLng startLatLng;
    private LatLng destinationLatLng;
    private Context context;
    private float totalDistance;
    private long totalTime;

    /**
     * Construtor da classe RouteCalculator.
     *
     * @param map                Instância do GoogleMap para exibir a rota.
     * @param startLatLng        Coordenadas de latitude e longitude do ponto de partida da rota.
     * @param destinationLatLng  Coordenadas de latitude e longitude do destino da rota.
     * @param context            Contexto da aplicação.
     */
    public RouteCalculator(GoogleMap map, LatLng startLatLng, LatLng destinationLatLng, Context context) {
        this.map = map;
        this.startLatLng = startLatLng;
        this.destinationLatLng = destinationLatLng;
        this.context = context;
    }

    /**
     * Inicia o cálculo da rota.
     */
    public void calculateRoute() {
        AsyncTask<Void, Integer, Boolean> task = new AsyncTask<Void, Integer, Boolean>() {
            private static final String TOAST_ERR_MSG = "Unable to calculate route";

            private final ArrayList<LatLng> lstLatLng = new ArrayList<>();

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    // Constrói a URL da requisição para o serviço de direções do Google Maps
                    String apiKey = "YOUR_API_KEY";
                    String url = "https://maps.googleapis.com/maps/api/directions/xml?origin=" +
                            startLatLng.latitude + "," + startLatLng.longitude +
                            "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude +
                            "&sensor=false&language=pt" +
                            "&mode=driving" +
                            "&key=" + apiKey;

                    // Realiza a requisição HTTP e obtém o stream de resposta
                    InputStream stream = new URL(url).openStream();

                    // Cria um parser de documentos XML
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setIgnoringComments(true);

                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    // Faz o parsing do stream de resposta para um documento XML
                    Document document = documentBuilder.parse(stream);
                    document.getDocumentElement().normalize();

                    // Verifica o status da resposta
                    String status = document.getElementsByTagName("status").item(0).getTextContent();
                    if (!"OK".equals(status)) {
                        return false;
                    }

                    // Obtém a lista de etapas da rota
                    Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
                    NodeList nodeListStep = elementLeg.getElementsByTagName("step");
                    int length = nodeListStep.getLength();

                    // Itera sobre as etapas da rota
                    for (int i = 0; i < length; i++) {
                        Node nodeStep = nodeListStep.item(i);

                        if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementStep = (Element) nodeStep;
                            decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
                            updateTotalDistanceAndTime(elementStep.getElementsByTagName("distance").item(0).getTextContent(), elementStep.getElementsByTagName("duration").item(0).getTextContent());
                        }
                    }

                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "Error calculating route", e);
                    return false;
                }
            }

            /**
             * Decodifica as coordenadas da rota codificadas em polylines.
             *
             * @param encodedPoints String contendo as coordenadas codificadas.
             */
            private void decodePolylines(String encodedPoints) {
                int index = 0;
                int lat = 0, lng = 0;

                while (index < encodedPoints.length()) {
                    int b, shift = 0, result = 0;

                    do {
                        b = encodedPoints.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);

                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lat += dlat;
                    shift = 0;
                    result = 0;

                    do {
                        b = encodedPoints.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);

                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lng += dlng;

                    lstLatLng.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
                }
            }

            /**
             * Atualiza a distância total e o tempo total estimado da rota.
             *
             * @param distanceText Texto contendo a distância da etapa.
             * @param durationText Texto contendo a duração da etapa.
             */
            private void updateTotalDistanceAndTime(String distanceText, String durationText) {
                String distance = distanceText.replaceAll("\\D+", "");
                String duration = durationText.replaceAll("\\D+", "");

                totalDistance += Float.parseFloat(distance) / 1000; // Convert to kilometers
                totalTime += Long.parseLong(duration);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    // Exibe uma mensagem de erro caso não seja possível calcular a rota
                    Toast.makeText(context, TOAST_ERR_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    // Desenha a rota no mapa
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.BLUE);

                    for (LatLng latLng : lstLatLng) {
                        polylineOptions.add(latLng);
                    }
                    map.addPolyline(polylineOptions);

                    // Exibe as informações de distância e tempo estimado em um Toast
                    String distanceMessage = String.format(Locale.getDefault(), "Distância total: %.2f km", totalDistance);
                    String timeMessage = String.format(Locale.getDefault(), "Tempo total: %d segundos", totalTime);
                    String combinedMessage = distanceMessage + "\n" + timeMessage;

                    Toast.makeText(context, combinedMessage, Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();
    }
}
