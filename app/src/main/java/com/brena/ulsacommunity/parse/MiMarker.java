package com.brena.ulsacommunity.parse;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;

/**
 * Created by DanielBrena on 20/09/15.
 */
public class MiMarker extends Marker {
    private String id;
    private String date;
    private String desc_large;
    private String desc_small;
    private String url_img;

    public String getDesc_small() {
        return desc_small;
    }

    public ArrayList<String> getArrayList(){
       ArrayList<String> res = new ArrayList<String>();
        res.add(getId());
        res.add(getTitle());
        res.add(getDate());
        res.add(getDesc_large());
        res.add(getUrl_img());


        return res;
    }

    public void setDesc_small(String desc_small) {
        this.desc_small = desc_small;
    }

    public String getDesc_large() {
        return desc_large;
    }

    public void setDesc_large(String desc_large) {
        this.desc_large = desc_large;
    }

    public MiMarker(String id, String title, String description, LatLng latLng) {
        super(title, description, latLng);
        this.id = id;
    }

    public MiMarker(String id, MapView mv, String aTitle, String aDescription, LatLng aLatLng) {
        super(mv, aTitle, aDescription, aLatLng);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }
}
