package com.brena.ulsacommunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.brena.ulsacommunity.parse.Building;
import com.parse.ParseObject;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealidadAumentada extends AppCompatActivity {

    ArchitectView architectView;

    LocationManager locationManager;
    double latitude = 0;
    double longitude = 0;
    ArchitectView.ArchitectUrlListener urlListener;
    private JSONArray poiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_realidad_aumentada);

        // la ruta del architectView en nuestro XML
        this.architectView = (ArchitectView) this
                .findViewById(R.id.architectView);
        StartupConfiguration config = new StartupConfiguration(WikitudeSDKConstants.WIKITUDE_SDK_KEY);
        this.architectView.onCreate(config);

        this.urlListener = this.getUrlListener();

        // register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
        if (this.urlListener != null && this.architectView != null) {
            this.architectView.registerUrlListener( this.getUrlListener() );
        }
        // actualziamos las variables latitude y longitude de la aplicacion
        getLocalization();

    }

	/*
	 * Ciclo de vida en nuestra activida
	 */

    @Override
    protected void onResume() {
        super.onResume();
        if (this.architectView != null) {
            this.architectView.setLocation(latitude, longitude, 1f);
            this.architectView.onResume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.architectView != null) {
            this.architectView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.architectView != null) {
            this.architectView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (this.architectView != null) {
            this.architectView.onLowMemory();
        }
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // IMPORTANTE cargamos el ARchitect worlds (codigo web: HTML CSS
        // javaScript)
        this.architectView.onPostCreate();
        poiData = getPoiInformation();
        try {
            this.architectView.load("base/index.html");
            this.architectView.callJavascript("World.loadPoisFromJsonData(" + poiData.toString() + ")");

            //this.architectView.onResume();
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_realidad_aumentada, menu);
        return true;
    }

    private void getLocalization() {

        final Activity activity = this;

        // Obtenemos una referencia al LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Nos registramos para recibir actualizaciones de la posición
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }



                /*Toast.makeText(
                        activity,
                        "nueva posicion obtenida " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();*/

                architectView.setLocation(latitude, longitude, 1f);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(activity, "Debe de habilitar el \"Acceso a su ubicación\" ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {

                // muestro un mensaje segun el estatus erroneo de la senal
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        Toast.makeText(
                                activity,
                                "Se a perdido la comunicación con el GPS y/o redes teléfonicas.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Toast.makeText(
                                activity,
                                "Se a perdido la comunicación con el GPS y/o redes teléfonicas.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case LocationProvider.AVAILABLE:
                        Toast.makeText(
                                activity,
                                "Se a establecido la conexión con el GPS y/o redes teléfonicas.",
                                Toast.LENGTH_LONG).show();
                        break;
                }

            }
        };

        // Si esta habilitada el GPS en el dispositivo
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // obtengo la ultima localizacion del usuario, si no la hay, es null
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            // actualizo la posicion del usuario cada 5 min = 80000 ms
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 80000, 0, locationListener);

        } else if (locationManager // Puntos Wifi o senal telefonica
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            // obtengo la ultima localizacion del usuario, si no la hay, es null
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            // actualizo la posicion del usuario cada 5 min = 80000 ms
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 80000, 0,
                    locationListener);

        } else {
            // servicio desactivado
           Toast.makeText(
                    this,
                    "Por favor active el \"Acceso a su ubicación\" desde las configuraciones.",
                    Toast.LENGTH_LONG).show();

        }

    }

    private  JSONArray getPoiInformation() {

        List<ParseObject> building = new Building().getAll();

        final JSONArray pois = new JSONArray();

        // ensure these attributes are also used in JavaScript when extracting POI data
        final String ATTR_ID = "id";
        final String ATTR_NAME = "name";
        final String ATTR_DESCRIPTION = "description";
        final String ATTR_LATITUDE = "latitude";
        final String ATTR_LONGITUDE = "longitude";
        final String ATTR_ALTITUDE = "altitude";
        final String ATTR_IMAGE = "image";
        final String ATTR_DATE = "date";

        for (ParseObject p : building){
            final HashMap<String, String> poiInformation = new HashMap<String, String>();
            poiInformation.put(ATTR_ID,p.getObjectId());
            poiInformation.put(ATTR_NAME, p.get("name").toString());
            poiInformation.put(ATTR_DESCRIPTION, p.get("description").toString());

           // double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
            poiInformation.put(ATTR_LATITUDE, String.valueOf(p.getParseGeoPoint("location").getLatitude()));
            poiInformation.put(ATTR_LONGITUDE,String.valueOf(p.getParseGeoPoint("location").getLongitude()));
           // final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
            // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
            poiInformation.put(ATTR_ALTITUDE, String.valueOf(100));
            poiInformation.put(ATTR_IMAGE, p.getParseFile("photo").getUrl().toString());
            poiInformation.put(ATTR_DATE, p.get("building_date").toString());
            pois.put(new JSONObject(poiInformation));
        }

        return pois;
    }


    public ArchitectView.ArchitectUrlListener getUrlListener() {
        final Context activity = getApplicationContext();
        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);

                // pressed "More" button on POI-detail panel
                if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
                    Intent i = new Intent(activity,PerfilActivity.class);

                    ArrayList<String> res = new ArrayList<String>();
                    res.add(String.valueOf(invokedUri.getQueryParameter("id")));
                    res.add(String.valueOf(invokedUri.getQueryParameter("title")));
                    res.add(String.valueOf(invokedUri.getQueryParameter("date")));
                    res.add(String.valueOf(invokedUri.getQueryParameter("description")));
                    res.add(String.valueOf(invokedUri.getQueryParameter("image")));

                   // Intent i = new Intent(context,PerfilActivity.class);
                    Bundle extras = new Bundle();
                    extras.putStringArrayList("marker", res);

                    i.putExtras(extras);
                   // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                    /*final Intent poiDetailIntent = new Intent(SampleCamActivity.this, SamplePoiDetailActivity.class);
                    poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
                    poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
                    poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
                    SampleCamActivity.this.startActivity(poiDetailIntent);*/
                    //startActivity(i);
                    return true;
                }

                // pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene

                return true;
            }
        };
    }
}
