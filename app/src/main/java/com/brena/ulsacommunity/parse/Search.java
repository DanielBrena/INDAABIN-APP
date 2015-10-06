package com.brena.ulsacommunity.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by DanielBrena on 26/09/15.
 */
public class Search implements SearchImpl {
    @Override
    public List<ParseObject> searchByName(String class_,String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(class_);
        query.whereEqualTo("name", name);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ParseObject> searchBuidlingByCategory(String name) {
        return null;
    }

    @Override
    public List<ParseObject> searchPhotosByBuilding(String id) {
        return null;
    }
}
