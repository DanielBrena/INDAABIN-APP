package com.brena.ulsacommunity.gridlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.brena.ulsacommunity.PhotoDetail;
import com.brena.ulsacommunity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DanielBrena on 22/09/15.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    Bitmap bitmap;
    ViewHolder holder = null;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) row.findViewById(R.id.image_grid);
            //holder.text = (TextView) row.findViewById(R.id.text_grid);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ImageItem item = (ImageItem) data.get(position);


        Picasso.with(context).load(item.getImage()).into(holder.image);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PhotoDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",item.getId());
                bundle.putString("url_img",item.getImage());
                bundle.putString("titulo",item.getNombre());
                i.putExtras(bundle);
                context.startActivity(i);

            }
        });

        //holder.text.setText(item.getName());
        return row;
    }

    static class ViewHolder {
        ImageView image;
        //TextView text;
    }


}