package com.brena.ulsacommunity.parse;

import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by DanielBrena on 26/09/15.
 */
public interface PhotographyImpl {
    public List<ParseObject> getAll();
    public ParseObject searchComentarios(String id);
    public List<ParseObject> getRelation(ParseRelation<ParseObject> relation);
}
