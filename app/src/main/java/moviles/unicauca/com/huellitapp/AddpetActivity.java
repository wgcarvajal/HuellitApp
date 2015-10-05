package moviles.unicauca.com.huellitapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddpetActivity extends AppCompatActivity implements View.OnClickListener
{

    private String APP_DIRECTORY="myPictureApp/";
    private String MEDIA_DIRECTORY=APP_DIRECTORY+"media";
    private String TEMPORAL_PICTUARE_NAME="temporal.jpg";

    private final int PHOTO_CODE=100;
    private final int SELECT_PICTURE=200;

    private ImageView imgphotoPet;
    private EditText editNamePet;
    private Button btnAddChangePhoto;
    private Button btnSavePet;
    private String tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);

        Bundle extras = getIntent().getExtras();
        tipo = extras.getString("tipo");
        Log.i("tipo:", tipo);

        imgphotoPet=(ImageView)findViewById(R.id.img_photopet);
        editNamePet=(EditText)findViewById(R.id.edit_petname);
        btnSavePet=(Button)findViewById(R.id.btn_savepet);
        btnSavePet.setOnClickListener(this);
        btnAddChangePhoto=(Button)findViewById(R.id.btn_addchangephote);
        btnAddChangePhoto.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent();
            intent.putExtra("resultado","te devuelvo el saludo desde AddpetActivity");
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_savepet:

                if(editNamePet.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.ingreseMascota),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ParseObject mascota= new ParseObject("mascota");
                    mascota.put("tiponombre",tipo );
                    mascota.put("username", ParseUser.getCurrentUser().getUsername());
                    mascota.put("masnombre", editNamePet.getText().toString());
                    mascota.saveInBackground(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            if (e == null)
                            {
                                Toast.makeText(getApplicationContext(),"Mascota id:"+mascota.getObjectId(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            break;
            case R.id.btn_addchangephote:

                final CharSequence[] options={getResources().getString(R.string.tomarFoto),getResources().getString(R.string.seleccionar),getResources().getString(R.string.cancelar)};
                final AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.eligeOpcion));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        switch (seleccion) {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent, "Seleciona app de imagen"), SELECT_PICTURE);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }

                    }
                });
                builder.show();
            break;
        }

    }

    private void openCamera()
    {

    }
}
