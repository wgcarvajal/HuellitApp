package moviles.unicauca.com.huellitapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import moviles.unicauca.com.huellitapp.modelo.FotoMascota;
import moviles.unicauca.com.huellitapp.modelo.Mascota;
import moviles.unicauca.com.huellitapp.modelo.TipoMascota;

public class AddpetActivity extends AppCompatActivity implements View.OnClickListener
{

    private String APP_DIRECTORY="myPictureApp/";
    private String MEDIA_DIRECTORY=APP_DIRECTORY+"media";
    private String TEMPORAL_PICTUARE_NAME="temporal.jpg";

    private int SELECTION;

    private final int PHOTO_CODE=100;
    private final int SELECT_PICTURE=200;

    private TipoMascota tipoMascota;
    private ImageView imgphotoPet;
    private EditText editNamePet;
    private Button btnAddChangePhoto;
    private Button btnSavePet;
    private Uri path;
    private Bitmap bitmap;
    private Spinner spEdad;
    private TextView txtEdad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);
        tipoMascota=new TipoMascota();
        Bundle extras = getIntent().getExtras();
        tipoMascota.setTiponombre(extras.getString(TipoMascota.TIPONOMBRE));
        tipoMascota.setTiponombreingles(extras.getString(TipoMascota.TIPONOMBREINGLES));

        setTitle(getTitle() + " " + tipoMascota.getTiponombreingles().toLowerCase().replace("s", ""));


        imgphotoPet=(ImageView)findViewById(R.id.img_photopet);
        editNamePet=(EditText)findViewById(R.id.edit_petname);
        spEdad=(Spinner)findViewById(R.id.sp_edad);
        txtEdad=(TextView)findViewById(R.id.txt_edad);

        if(tipoMascota.getTiponombre().equals("Cachorros"))
        {
            txtEdad.setText(getResources().getString(R.string.edad)+"("+getResources().getString(R.string.meses)+")");
        }
        else
        {
            txtEdad.setText(getResources().getString(R.string.edad)+"("+getResources().getString(R.string.anos)+")");

        }

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
            intent.putExtra("resultado","N");
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
                    if(imgphotoPet.getDrawable()==null)
                    {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.ingreseFoto),Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        final ParseObject mascota= new ParseObject(Mascota.TABLA);
                        mascota.put(Mascota.TIPO,tipoMascota.getTiponombre() );
                        mascota.put(Mascota.USERNAME, ParseUser.getCurrentUser().getUsername());
                        mascota.put(Mascota.NOMBRE, editNamePet.getText().toString());
                        mascota.put(Mascota.EDAD,Integer.parseInt(spEdad.getSelectedItem().toString()));
                        mascota.saveInBackground(new SaveCallback()
                        {
                            @Override
                            public void done(ParseException e)
                            {
                                if (e == null)
                                {
                                    byte[] inputData=getByteImagen();
                                    ParseFile file = new ParseFile("foto.jpg", inputData);
                                    file.saveInBackground();
                                    ParseObject f = new ParseObject(FotoMascota.TABLA);
                                    f.put(FotoMascota.IMAGEN,file);
                                    f.put(FotoMascota.MASCOTAID, mascota.getObjectId());
                                    f.saveInBackground(new SaveCallback()
                                    {
                                        @Override
                                        public void done(ParseException e)
                                        {
                                            if (e == null)
                                            {
                                                Intent intent = new Intent();
                                                intent.putExtra("resultado","S");
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }

                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            break;
            case R.id.btn_addchangephote:

                final CharSequence[] options={getResources().getString(R.string.tomarFoto),getResources().getString(R.string.seleccionar),getResources().getString(R.string.cancelar)};
                final AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.eligeOpcion));
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion)
                    {
                        switch (seleccion)
                        {
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
        File file= new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdirs();

        String path= Environment.getExternalStorageDirectory()+File.separator
                +MEDIA_DIRECTORY+ File.separator+TEMPORAL_PICTUARE_NAME;

        File newFile= new File(path);

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));

        startActivityForResult(intent, PHOTO_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PHOTO_CODE:
                if(resultCode==RESULT_OK)
                {
                    String dir= Environment.getExternalStorageDirectory()+ File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTUARE_NAME;
                    decodeBitmap(dir);
                }
                break;
            case SELECT_PICTURE:

                if(resultCode==RESULT_OK)
                {
                    path=data.getData();
                    SELECTION=SELECT_PICTURE;
                    imgphotoPet.setImageURI(path);
                }
            break;
        }
    }

    public void decodeBitmap(String dir)
    {
        bitmap= BitmapFactory.decodeFile(dir);
        SELECTION=PHOTO_CODE;
        imgphotoPet.setImageBitmap(bitmap);
    }

    public byte[] getByteImagen()
    {
        if(SELECTION==PHOTO_CODE)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();
            return data;
        }
        else
        {
            InputStream iStream = null;
            byte[] inputData=null;
            try {
                iStream = getContentResolver().openInputStream(path);
                inputData= getBytes(iStream);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return inputData;

        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1)
        {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
