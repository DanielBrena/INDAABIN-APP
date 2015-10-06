package com.brena.ulsacommunity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.brena.ulsacommunity.parse.Building;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetBuilding extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int widgetId = appWidgetIds[i];
            ParseObject b = get();
            String number =b.get("name").toString();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_building);
            remoteViews.setTextViewText(R.id.titulo_widget, number);

            Picasso.with(context)
                    .load(b.getParseFile("photo").getUrl())
                    .into(remoteViews, R.id.foto_perfil_widget, new int[] {widgetId});

            Intent intent = new Intent(context, WidgetBuilding.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btn_recargar_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private ParseObject get(){

        List<ParseObject> lista = new Building().getAll();
        int n = randInt(0, lista.size());
        ParseObject aleatorio = lista.get(n);
        return aleatorio;

    }
    private  int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) ) + min;

        return randomNum;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

