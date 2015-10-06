package com.brena.ulsacommunity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brena.ulsacommunity.gridlist.ImageItem;
import com.brena.ulsacommunity.gridlist.ListViewAdapter;
import com.brena.ulsacommunity.parse.Building;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class BusquedaActivity extends AppCompatActivity {
    ListView listView;
    ListViewAdapter listViewAdapter;
    TextView textView;
    Bundle bundle;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_busqueda);

        toolbar = (Toolbar) findViewById(R.id.toolbar_busqueda);
        toolbar.setTitle("Búsqueda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        textView = (TextView) findViewById(R.id.visible);


        getSearch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    private void getSearch(){
        List<ParseObject> building = new Building().search(bundle.getString("search"));
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        if(building.size() >= 1){

            for(ParseObject p : building){
                ImageItem item = new ImageItem(p.getObjectId(),p.getParseFile("photo").getUrl().toString());
                item.setFecha(p.get("building_date").toString());
                item.setNombre(p.get("name").toString());
                item.setDescripcion(p.get("description").toString());
                imageItems.add(item);
            }
            listView = (ListView)findViewById(R.id.listview);
            listViewAdapter = new ListViewAdapter(getApplicationContext(),R.layout.activity_busqueda_item,imageItems);
            listView.setAdapter(listViewAdapter);
        }else{
            textView.setVisibility(View.VISIBLE);
        }
        /**/
       // Toast.makeText(getApplicationContext(), String.valueOf(building.size()), Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
