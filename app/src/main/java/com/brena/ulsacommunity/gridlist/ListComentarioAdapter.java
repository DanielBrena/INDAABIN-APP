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
import android.widget.TextView;

import com.brena.ulsacommunity.PerfilActivity;
import com.brena.ulsacommunity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DanielBrena on 03/10/15.
 */
public class ListComentarioAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    ViewHolder holder = null;
    private ArrayList data = new ArrayList();

    public ListComentarioAdapter(Context context, int layoutResourceId, ArrayList data) {
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
            LayoutInflater inflater = LayoutInflater.from(context);


            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.nombre = (TextView) row.findViewById(R.id.comentario_nombre);
            holder.comentario = (TextView) row.findViewById(R.id.comentario_texto);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

         Comentario comentario = (Comentario) data.get(position);






        holder.nombre.setText(comentario.getNombre());
        holder.comentario.setText(comentario.getComentario());
        return row;
    }

    static class ViewHolder {

        TextView nombre;
        TextView comentario;
    }
}
