package com.brena.ulsacommunity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brena.ulsacommunity.gridlist.Comentario;
import com.brena.ulsacommunity.gridlist.ListComentarioAdapter;
import com.brena.ulsacommunity.parse.Photography;
import com.brena.ulsacommunity.preferences.MyPreference;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetail extends AppCompatActivity {
    TextView titulo;
    EditText texto;
    ImageButton enviar;
    Bundle  b;
    Toolbar toolbar;
    Dialog dialog;
    ListView listViewComentarios;
    ListComentarioAdapter listComentarioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar_photo_detail);
        setSupportActionBar(toolbar);



         b= getIntent().getExtras();
        titulo = (TextView) findViewById(R.id.photo_detail_titulo);
        texto = (EditText) findViewById(R.id.photo_detail_comentario);
        enviar = (ImageButton) findViewById(R.id.photo_detail_btn);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviar();
            }
        });
        getSupportActionBar().setTitle(b.get("titulo").toString());
        titulo.setText(b.get("titulo").toString());
        Picasso.with(getApplicationContext()).load(b.getString("url_img")).into((ImageView) findViewById(R.id.photo_detail));



        this.listaComentarios();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        return true;
    }

    private void enviar(){
        if(MyPreference.exist(this,"nombre") && MyPreference.exist(this,"apellidos")){
            final ParseObject comentary = new ParseObject("Commentary");
            String nombre = MyPreference.get(getApplicationContext(), "nombre") + " " + MyPreference.get(getApplicationContext(), "apellidos");
            comentary.put("name",nombre );
            comentary.put("texto",texto.getText().toString() );
            comentary.put("uuid", MyPreference.get(getApplicationContext(),"deviceId"));

            comentary.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseObject photography = new ParseObject("Photography");
                        photography.setObjectId(b.get("id").toString());
                        comentary.getObjectId();
                        ParseRelation relation = photography.getRelation("Comments");
                        relation.add(comentary);
                        photography.saveInBackground();
                        texto.setText("");
                        listaComentarios();
                    }
                }
            });
        }else{
            this.dialog = createDialog();
            this.dialog.show();
        }


    }

    private void listaComentarios(){


        ParseObject photography = new Photography().searchComentarios(b.getString("id").toString());
        ArrayList<Comentario> comentarios = new ArrayList<>();



        ParseRelation<ParseObject> relation = photography.getRelation("Comments");
        List<ParseObject> lista_comentarios =new Photography().getRelation(relation);


        for(ParseObject p:lista_comentarios){
            Comentario c = new Comentario(p.get("name").toString(),p.get("texto").toString());

            comentarios.add(c);
        }
        if(comentarios.size() > 0){
            this.listViewComentarios = (ListView) findViewById(R.id.listView_comentarios);
            listComentarioAdapter = new ListComentarioAdapter(getApplicationContext(),R.layout.comentarios_item_layout,comentarios);
            this.listViewComentarios.setAdapter(listComentarioAdapter);
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
        MyPreference.add(getApplicationContext(), "nombre", nombre);
        MyPreference.add(getApplicationContext(), "apellidos", apellidos);


        dismiss();

    }

    private void dismiss(){
        this.dialog.dismiss();
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
