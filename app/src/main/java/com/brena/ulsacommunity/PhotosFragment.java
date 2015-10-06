package com.brena.ulsacommunity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.brena.ulsacommunity.gridlist.GridViewAdapter;
import com.brena.ulsacommunity.gridlist.ImageItem;
import com.brena.ulsacommunity.parse.Photography;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by DanielBrena on 25/09/15.
 */
public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    SwipeRefreshLayout swipeLayout;
    Context context;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();
        return inflater.inflate(R.layout.photos_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPhotos();


    }

    public void getPhotos(){
        List<ParseObject> photos = new Photography().getAll();
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        gridView = (GridView) getView().findViewById(R.id.gridView);

        for(ParseObject p:photos){
            ImageItem item = new ImageItem(p.getObjectId().toString(),p.getParseFile("photo").getUrl().toString());
            item.setNombre(p.get("name").toString());
            imageItems.add(item);
        }

        gridAdapter = new GridViewAdapter(context, R.layout.photos_layout_item, imageItems);
        gridView.setAdapter(gridAdapter);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getPhotos();
    }
}
