package com.brena.ulsacommunity.parse;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by DanielBrena on 26/09/15.
 */
public interface SearchImpl {
    public List<ParseObject> searchByName(String class_,String name);
    public List<ParseObject> searchBuidlingByCategory(String name);
    public List<ParseObject> searchPhotosByBuilding(String id);
}
