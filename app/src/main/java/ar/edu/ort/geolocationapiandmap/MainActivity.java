package ar.edu.ort.geolocationapiandmap;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText direccion;
    TextView dirEncontrada, coordenadas;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        direccion = (EditText) findViewById(R.id.direccion);
        dirEncontrada = (TextView) findViewById(R.id.dirEncontrada);
        coordenadas = (TextView) findViewById(R.id.coordenadas);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map=map;
        map.getUiSettings().setZoomControlsEnabled(true);

    }

    public void consultarDireccion(View v) {
        String dirStr = direccion.getText().toString();
        if (!dirStr.isEmpty()) {
            new GeolocalizacionTask().execute(dirStr);  // Llamo a clase async con url
        }
    }


    // Utiliza la clase android.location.Geocoder
    // Parametros
    // String - la direccion a buscar que recibe doInBackground
    // Void -  Progreso (no se usa)
    // List<Address> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void,List<Address>> {

        @Override
        protected void onPostExecute(List<Address> direcciones) {
            super.onPostExecute(direcciones);

            if (!direcciones.isEmpty()) {
                // Muestro la primera direccion recibida
                Address dirRecibida = direcciones.get(0);  // La primera direccion
                String addressStr=dirRecibida.getAddressLine(0);  // Primera linea del texto
                dirEncontrada.setText(addressStr);

                // Muestro coordenadas
                double lat = dirRecibida.getLatitude(); //
                double lng = dirRecibida.getLongitude();
                String coordStr = lat+ "," + lng;
                coordenadas.setText(coordStr);  // Muestro coordenadas en pantalla

                // Ubico la direccion en el mapa
                if (map != null) {
                    CameraUpdate center=
                            CameraUpdateFactory.newLatLng(new LatLng(lat,lng));
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
                    map.moveCamera(center);
                    map.animateCamera(zoom);   // Posiciono la camara en las coordenadas recibidas

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title(dirRecibida.getAddressLine(0)));  // Dibujo el marker
                }
            }
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            String address = params[0];

            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = null;
            try {
                // Utilizo la clase Geocoder para buscar la direccion. Limito a 10 resultados
                addresses = geocoder.getFromLocationName(address, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }

    }
}
