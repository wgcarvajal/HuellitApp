package moviles.unicauca.com.huellitapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.modelo.FotoMascota;
import moviles.unicauca.com.huellitapp.modelo.Usuario;

public class PetActivity extends AppCompatActivity implements View.OnClickListener {
    public static String IDMASCOTA="idMascota";
    public static String NOMBREMASCOTA="nombreMascota";
    public static String EDADMASCOTA="edadMascota";
    public static String DESCRIPCIONMASCOTA="descripcionMascota";
    public static String TIPO="tipo";
    public static String INDICE="indice";
    public static String TAMANO="tamano";
    public static String RESPONSABLE="responsable";
    private String idMascota;
    private String nombreMascota;
    private String edadMascota;
    private String descripcionMascota;
    private List <Bitmap> bitmaps;
    private ImageView imgFotoMascota;
    private TextView txtNombreMascota;
    private TextView txtEdadMascota;
    private Button btnSiguiente;
    private Button btnAnterior;
    private TextView txtPosicionImagen;
    private TextView txtDescripcionMascota;
    private TextView txtPropietario;
    private TextView txtCorreo;
    private int indice;
    private int tamano;
    private String tipo;
    private String responsable;


    @Override
    protected void onResume()
    {
        super.onResume();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FotoMascota.TABLA);
        query.whereEqualTo(FotoMascota.MASCOTAID, idMascota);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    tamano = list.size();
                    for (ParseObject parseObject : list) {

                        ParseFile fileObject = (ParseFile) parseObject.get(FotoMascota.IMAGEN);
                        fileObject.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Log.d("test", "Llegaron datos.");
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    bitmaps.add(bmp);
                                    if (bitmaps.size() - 1 == indice) {
                                        imgFotoMascota.setImageBitmap(bitmaps.get(indice));
                                        txtPosicionImagen.setText("" + (indice + 1) + "/" + tamano);

                                    }

                                }
                            }
                        });
                    }
                }
                Log.i("responsable", responsable);
                ParseQuery<ParseUser> user = ParseUser.getQuery();
                user.whereEqualTo(Usuario.USERNAME, responsable);
                user.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e)
                    {
                        if(!list.isEmpty())
                        {
                            JSONObject userProfile = list.get(0).getJSONObject("profile");
                            try {
                                txtCorreo.setText(userProfile.getString("email"));
                                txtPropietario.setText(userProfile.getString("name"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }
                });
            }
        });





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        indice=0;
        if(savedInstanceState!=null)
        {
            indice=savedInstanceState.getInt(INDICE);
        }

        Bundle extras=getIntent().getExtras();
        idMascota=extras.getString(IDMASCOTA);
        nombreMascota=extras.getString(NOMBREMASCOTA);
        edadMascota=extras.getString(EDADMASCOTA);
        descripcionMascota=extras.getString(DESCRIPCIONMASCOTA);
        responsable=extras.getString(RESPONSABLE);
        tipo=extras.getString(TIPO);

        imgFotoMascota=(ImageView)findViewById(R.id.img_ver_foto_macota);
        txtNombreMascota=(TextView)findViewById(R.id.txt_ver_nombre_mascota);
        txtPosicionImagen=(TextView)findViewById(R.id.txt_posicion_foto);
        txtDescripcionMascota=(TextView)findViewById(R.id.txt_ver_desc_mascota);
        txtDescripcionMascota.setText(descripcionMascota);
        txtNombreMascota.setText(nombreMascota.toUpperCase());
        txtEdadMascota=(TextView)findViewById(R.id.txt_ver_edad_mascota);
        txtPropietario=(TextView)findViewById(R.id.txt_propietario);
        txtCorreo=(TextView)findViewById(R.id.txt_correo);
        if(tipo.equals("Cachorros"))
        {
            txtEdadMascota.setText(edadMascota+" "+getResources().getString(R.string.meses));
        }
        else
        {
            txtEdadMascota.setText(edadMascota+" "+getResources().getString(R.string.anos));
        }


        bitmaps=new ArrayList<>();

        btnSiguiente=(Button)findViewById(R.id.btn_siguiente);
        btnAnterior=(Button)findViewById(R.id.btn_anterior);

        btnSiguiente.setOnClickListener(this);
        btnAnterior.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == android.R.id.home)
        {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_siguiente:
                indice=indice+1;
                if(indice>bitmaps.size()-1)
                {
                    indice=0;
                }
                imgFotoMascota.setImageBitmap(bitmaps.get(indice));
                txtPosicionImagen.setText(""+(indice+1)+"/"+bitmaps.size());
                break;
            case R.id.btn_anterior:
                indice=indice-1;
                if(indice<0)
                {
                    indice=bitmaps.size()-1;
                }
                imgFotoMascota.setImageBitmap(bitmaps.get(indice));
                txtPosicionImagen.setText(""+(indice + 1) + "/" + bitmaps.size());
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INDICE,indice);
        super.onSaveInstanceState(outState);
    }
}
