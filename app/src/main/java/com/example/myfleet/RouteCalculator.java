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



    public RouteCalculator(GoogleMap map, LatLng startLatLng, LatLng destinationLatLng, Context context) {
        this.map = map;
        this.startLatLng = startLatLng;
        this.destinationLatLng = destinationLatLng;
        this.context = context;
    }

    public void calculateRoute() {
        AsyncTask<Void, Integer, Boolean> task = new AsyncTask<Void, Integer, Boolean>() {
            private static final String TOAST_ERR_MSG = "Unable to calculate route";

            private final ArrayList<LatLng> lstLatLng = new ArrayList<>();

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    String apiKey = "AIzaSyCQgQeznfQnTbNtdHVNF2zvrokBc0rGLRI";
                    String url = "https://maps.googleapis.com/maps/api/directions/xml?origin=" +
                            startLatLng.latitude + "," + startLatLng.longitude +
                            "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude +
                            "&sensor=false&language=pt" +
                            "&mode=driving" +
                            "&key=" + apiKey;

                    InputStream stream = new URL(url).openStream();

                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setIgnoringComments(true);

                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    Document document = documentBuilder.parse(stream);
                    document.getDocumentElement().normalize();

                    String status = document.getElementsByTagName("status").item(0).getTextContent();
                    if (!"OK".equals(status)) {
                        return false;
                    }

                    Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
                    NodeList nodeListStep = elementLeg.getElementsByTagName("step");
                    int length = nodeListStep.getLength();

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

            private void updateTotalDistanceAndTime(String distanceText, String durationText) {
                String distance = distanceText.replaceAll("\\D+", "");
                String duration = durationText.replaceAll("\\D+", "");

                totalDistance += Float.parseFloat(distance) / 1000; // Convert to kilometers
                totalTime += Long.parseLong(duration);


            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    Toast.makeText(context, TOAST_ERR_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.BLUE);

                    for (LatLng latLng : lstLatLng) {
                        polylineOptions.add(latLng);
                    }
                    map.addPolyline(polylineOptions);

                    // Exibir as informações de distância e tempo
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
