package com.brena.ulsacommunity.gridlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.brena.ulsacommunity.PerfilActivity;
import com.brena.ulsacommunity.Prueba;
import com.brena.ulsacommunity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DanielBrena on 27/09/15.
 */
public class ListViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    Bitmap bitmap;
    ViewHolder holder = null;
    private ArrayList data = new ArrayList();

    public ListViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);// ((Activity) context).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) row.findViewById(R.id.busqueda_foto);
            holder.name = (TextView) row.findViewById(R.id.busqueda_nombre);
            holder.date = (TextView) row.findViewById(R.id.busqueda_fecha);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ImageItem item = (ImageItem) data.get(position);


        Picasso.with(context).load(item.getImage()).into(holder.image);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> res = new ArrayList<String>();
                res.add(item.getId());
                res.add(item.getNombre());
                res.add(item.getFecha());
                res.add(item.getDescripcion());
                res.add(item.getImage());

                Intent i = new Intent(context,PerfilActivity.class);
                Bundle extras = new Bundle();
                extras.putStringArrayList("marker", res);

                i.putExtras(extras);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                //Toast.makeText(context, item.getId().toString(), Toast.LENGTH_LONG).show();

            }
        });

        holder.name.setText(item.getNombre());
        holder.date.setText(item.getFecha());
        return row;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView date;
    }
}
