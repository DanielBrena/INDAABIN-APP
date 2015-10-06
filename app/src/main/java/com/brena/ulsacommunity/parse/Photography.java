package com.brena.ulsacommunity.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by DanielBrena on 26/09/15.
 */
public class Photography implements PhotographyImpl {

    @Override
    public List<ParseObject> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photography");
        query.whereEqualTo("status", true);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ParseObject searchComentarios(String id) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photography");
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
