package moviles.unicauca.com.huellitapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

import moviles.unicauca.com.huellitapp.R;
import moviles.unicauca.com.huellitapp.modelo.FotoMascota;
import moviles.unicauca.com.huellitapp.modelo.Mascota;

/**
 * Created by geovanny on 29/09/15.
 */
public class MascotaAdapter extends BaseAdapter
{
    private Context context;
    private List<Mascota> data;

    public MascotaAdapter(Context context, List<Mascota> data)
    {
        this.context=context;
        this.data=data;
    }

    public void inicializar(List<Mascota> data)
    {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = null;

        if (convertView == null)
        {
            v = View.inflate(context, R.layout.template_mascota, null);
        }
        else
        {
            v = convertView;
        }

        Mascota m = (Mascota) getItem(position);
        TextView txt_nombremascota = (TextView) v.findViewById(R.id.txt_nombremascota);
        TextView txt_edad=(TextView)v.findViewById(R.id.txt_edad_mascota);
        TextView txt_descripcion=(TextView)v.findViewById(R.id.txt_desc_mascota);
        txt_nombremascota.setText(m.getNombre().toUpperCase());
        if(m.getDescripcion().length()>100)
        {
            txt_descripcion.setText(m.getDescripcion().substring(0,100));
        }
        else
        {
            txt_descripcion.setText(m.getDescripcion());
        }


        if(m.getTipo().equals("Cachorros"))
        {
            txt_edad.setText(m.getEdad()+" "+v.getResources().getString(R.string.meses));
        }
        else
        {
            txt_edad.setText(m.getEdad()+" "+v.getResources().getString(R.string.anos));
        }


        final View c=v;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FotoMascota.TABLA);
        query.whereEqualTo(FotoMascota.MASCOTAID, m.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e)
            {
                if (!list.isEmpty())
                {
                    ParseFile fileObject = (ParseFile) list.get(0).get(FotoMascota.IMAGEN);
                    fileObject.getDataInBackground(new GetDataCallback()
                    {
                        public void done(byte[] data, ParseException e)
                        {
                            if (e == null)
                            {
                                Log.d("test", "Llegaron datos.");
                                // Decode the Byte[] into
                                // Bitmap
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                // Get the ImageView from main.xml
                                //ImageView image = (ImageView) findViewById(R.id.ad1);
                                ImageView ad1 = (ImageView) c.findViewById(R.id.img_mascota);
                                // Set the Bitmap into the
                                // ImageView
                                ad1.setImageBitmap(bmp);
                                // Close progress dialog

                            }
                            else
                            {
                                Log.d("test", "Hubo un problema al traer los datos.");
                            }
                        }
                    });
                }
            }
        });
        return v;
    }
}
