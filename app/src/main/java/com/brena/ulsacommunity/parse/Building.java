package com.brena.ulsacommunity.parse;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by DanielBrena on 26/09/15.
 */
public class Building implements BuildingImpl {


    public Building(){

    }
    @Override
    public List<ParseObject> search(String nombre) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Building");
        query.whereStartsWith("name", nombre);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ParseObject> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Building");
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ParseObject searchPhotos(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Building");
        query.whereEqualTo("objectId", id);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ParseObject> getRelation(ParseRelation<ParseObject> relation) {

        ParseQuery<ParseObject> query = relation.getQuery();
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
