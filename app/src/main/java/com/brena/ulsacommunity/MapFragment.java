package com.brena.ulsacommunity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brena.ulsacommunity.parse.Building;
import com.brena.ulsacommunity.parse.MiMarker;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;
import com.parse.ParseObject;
/**
 * Created by DanielBrena on 25/09/15.
 */
public class MapFragment extends Fragment {
    Button btn;
    Context context;
    MapView mapView;
    DialogUsuario dialogUsuario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();

        return inflater.inflate(R.layout.map_layout, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.carga_mapa();
        this.carga_puntos();



    }

    public void carga_mapa(){
        mapView = (MapView) getView().findViewById(R.id.mapview);
        mapView.setTileSource(new MapboxTileLayer("mapbox.streets"));
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(mapView.getTileProvider().getCenterCoordinate());
        mapView.setZoom(5);

        UserLocationOverlay myLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(getActivity()), mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);
        Marker marker = new Marker(mapView, "Tu ubicación", "", myLocationOverlay.getMyLocation());

        mapView.addMarker(marker);
        mapView.setCenter(myLocationOverlay.getMyLocation());


    }

    public void carga_puntos(){
        Building building = new Building();
        for(ParseObject b : building.getAll()){
            LatLng coordenada = new LatLng(b.getParseGeoPoint("location").getLatitude(),b.getParseGeoPoint("location").getLongitude());
            final MiMarker marker = new MiMarker(b.getObjectId(),mapView, b.get("name").toString(), b.get("description").toString().substring(0,10) +" ...", coordenada);

            marker.setDate(b.get("building_date").toString());
            marker.setUrl_img(b.getParseFile("photo").getUrl().toString());
            marker.setDesc_large(b.get("description").toString());

            final InfoWindow toolTip = marker.getToolTip(mapView);
            toolTip.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    Intent i = new Intent(context,PerfilActivity.class);
                    Bundle extras = new Bundle();
                    extras.putStringArrayList("marker",marker.getArrayList());
                    /*MiMarker marker_aux = (MiMarker) toolTip.getBoundMarker();
                    extras.putString("id", marker_aux.getId());
                    extras.putString("name", marker_aux.getTitle());
                    extras.putString("date", marker_aux.getDate());
                    extras.putString("description", marker_aux.getDesc_large());
                    extras.putString("url_img", marker_aux.getUrl_img());*/

                    // 4. add bundle to intent
                    i.putExtras(extras);
                    startActivity(i);
                    return false;
                }
            });
            mapView.addMarker(marker);
        }
    }




}
