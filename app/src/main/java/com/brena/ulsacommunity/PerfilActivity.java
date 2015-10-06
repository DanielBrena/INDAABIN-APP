package com.brena.ulsacommunity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    Dialog dialog;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_perfil);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Informacion"));
        tabLayout.addTab(tabLayout.newTab().setText("Fotos"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        b = getIntent().getExtras();
        this.setInfo(b);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final PageAdapterPerfil adapter = new PageAdapterPerfil
                (getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.setParams(b);

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

        dialog = createDialog();
    }

    public void setInfo(Bundle b){
        ArrayList<String> params = b.getStringArrayList("marker");
        ((TextView) findViewById(R.id.titulo)).setText(params.get(1));
        ((TextView) findViewById(R.id.fecha)).setText(params.get(2));
        Picasso.with(getApplicationContext()).load(params.get(4)).into((ImageView)findViewById(R.id.foto_perfil));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				/*create instance of File with name img.jpg*/
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
				/*put uri as extra in intent object*/
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				/*start activity for result pass intent as argument and request code */
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public AlertDialog createDialog(){
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_photo, null);
        Button button_cancelar = (Button)v.findViewById(R.id.dialog_cancelar);

        builder.setView(v);
        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }

    private void dismiss(){
        this.dialog.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request code is same we pass as argument in startActivityForResult
        if(requestCode==1){
            //create instance of File with same name we created before to get image from storage
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
            //Crop the captured image using an other intent
            try {
				/*the user's device may not support cropping*/
                cropCapturedImage(Uri.fromFile(file));
            }
            catch(ActivityNotFoundException aNFE){
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if(requestCode==2){
            Bundle extras = data.getExtras();
            this.dialog.show();

            final Bitmap bitmap = extras.getParcelable("data");
            ImageView imageView = (ImageView) this.dialog.findViewById(R.id.dialog_foto);
            final EditText texto = (EditText) this.dialog.findViewById(R.id.dialog_texto);
            imageView.setImageBitmap(bitmap);
            Button boton_enviar = (Button) this.dialog.findViewById(R.id.dialog_enviar);

            boton_enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nombre = UUID.randomUUID().toString();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

                    final ParseObject photography = new ParseObject("Photography");
                    ParseFile file = new ParseFile(nombre,bytes.toByteArray());
                    file.saveInBackground();

                    photography.put("name",texto.getText().toString() );
                    photography.put("photo", file);
                    photography.put("status", true);


                    photography.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ArrayList<String> params = b.getStringArrayList("marker");
                                ParseObject building = new ParseObject("Building");
                                building.setObjectId(params.get(0));
                                photography.getObjectId();
                                ParseRelation relation = building.getRelation("Photographies");
                                relation.add(photography);
                                building.saveInBackground();


                            }else{
                                Log.d(getClass().getSimpleName(), "error: " + e);
                            }
                            dismiss();
                        }
                    });
                }
            });
        }
    }
    //create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri){
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 800);
        cropIntent.putExtra("outputY", 800);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }
}
