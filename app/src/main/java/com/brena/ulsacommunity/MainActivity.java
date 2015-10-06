package com.brena.ulsacommunity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brena.ulsacommunity.preferences.MyPreference;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;

public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_map));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_photo));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(!MyPreference.exist(getApplicationContext(), "nombre")){
            dialog = createDialog();
            this.dialog.show();
        }

    }

    private AlertDialog createDialog(){
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.activity_bienvenida, null);
        Button button_cancelar = (Button)v.findViewById(R.id.principal_btn_cancelar);
        Button button_guardar = (Button)v.findViewById(R.id.principal_btn_guardar);

        builder.setView(v);
        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        button_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((EditText)dialog.findViewById(R.id.principal_nombre)).getText().toString() != "" && ((EditText)dialog.findViewById(R.id.principal_apellidos)).getText().toString() != ""){
                    guardar();
                }

            }
        });
        return builder.create();
    }
    private void guardar(){
        String nombre = ((EditText)this.dialog.findViewById(R.id.principal_nombre)).getText().toString();
        String apellidos = ((EditText)this.dialog.findViewById(R.id.principal_apellidos)).getText().toString();
        MyPreference.add(getApplicationContext(),"nombre",nombre);
        MyPreference.add(getApplicationContext(),"apellidos",apellidos);


        dismiss();

    }

    private void dismiss(){
        this.dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(listener);

        return true;
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent i = new Intent(getApplicationContext(),BusquedaActivity.class);
            Bundle b = new Bundle();
            b.putString("search",query);
            i.putExtras(b);
            startActivity(i);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ra) {
            Intent i = new Intent(getApplicationContext(),RealidadAumentada.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
