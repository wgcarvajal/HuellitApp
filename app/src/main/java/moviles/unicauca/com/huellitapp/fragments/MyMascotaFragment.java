package moviles.unicauca.com.huellitapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.R;
import moviles.unicauca.com.huellitapp.adapters.MascotaAdapter;
import moviles.unicauca.com.huellitapp.modelo.Mascota;
import moviles.unicauca.com.huellitapp.modelo.TipoMascota;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMascotaFragment extends TitleFragment{


    public static String POSITIONLIST="poslist";

    private TipoMascota tipoMascota;
    private ListView lst_mascotas;
    private List<Mascota> data;
    private MascotaAdapter adapter;
    private  int poslist;

    public MyMascotaFragment()
    {
        // Required empty public constructor
    }
    public void init(String tiponombre,String tiponombreingles)
    {
        tipoMascota=new TipoMascota();
        tipoMascota.setTiponombre(tiponombre);
        tipoMascota.setTiponombreingles(tiponombreingles);
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        poslist=0;
        if(savedInstanceState!=null)
        {
            tipoMascota=new TipoMascota();
            tipoMascota.setTiponombre(savedInstanceState.getString(TipoMascota.TIPONOMBRE));
            tipoMascota.setTiponombreingles(savedInstanceState.getString(TipoMascota.TIPONOMBREINGLES));
            poslist=savedInstanceState.getInt(POSITIONLIST);
            Log.i("focus item:", "" + poslist);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_mascota, container, false);
        lst_mascotas=(ListView) v.findViewById(R.id.lst_mascotas);

        data=new ArrayList<>();
        adapter= new MascotaAdapter(v.getContext(),data);
        lst_mascotas.setAdapter(adapter);

        loadData();

        return v;
    }
    public void loadData()
    {
        String user=ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Mascota.TABLA);
        query.whereEqualTo(Mascota.TIPO, tipoMascota.getTiponombre());
        query.whereEqualTo(Mascota.USERNAME, user);
        query.orderByDescending(Mascota.FECHACREACION);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject mascota : parseObjects) {
                        Mascota mas = new Mascota();
                        mas.setNombre(mascota.getString(Mascota.NOMBRE));
                        mas.setId(mascota.getObjectId());
                        mas.setEdad((Integer) mascota.getNumber(Mascota.EDAD));
                        mas.setTipo(mascota.getString(Mascota.TIPO));
                        mas.setDescripcion(mascota.getString(Mascota.DESCRIPCION));
                        data.add(mas);
                        adapter.notifyDataSetChanged();
                    }
                    lst_mascotas.setSelection(poslist);
                }
            }
        });
    }
    @Override
    public String getTitle()
    {
        return tipoMascota.getTiponombreingles();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString(TipoMascota.TIPONOMBRE, tipoMascota.getTiponombre());
        outState.putString(TipoMascota.TIPONOMBREINGLES, tipoMascota.getTiponombreingles());
        outState.putInt(POSITIONLIST,lst_mascotas.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    public void buscar(String nombremascota)
    {
        data.clear();
        String user=ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Mascota.TABLA);
        query.whereEqualTo(Mascota.TIPO, tipoMascota.getTiponombre());
        query.whereEqualTo(Mascota.USERNAME, user);
        query.whereContains(Mascota.NOMBRE, nombremascota);
        query.orderByDescending(Mascota.FECHACREACION);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject mascota : parseObjects) {
                        Mascota mas = new Mascota();
                        mas.setNombre(mascota.getString(Mascota.NOMBRE));
                        mas.setId(mascota.getObjectId());
                        mas.setEdad((Integer) mascota.getNumber(Mascota.EDAD));
                        mas.setTipo(mascota.getString(Mascota.TIPO));
                        mas.setDescripcion(mascota.getString(Mascota.DESCRIPCION));
                        data.add(mas);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public String getTipo()
    {
        return tipoMascota.getTiponombre();
    }
    public String getTipoIdioma()
    {
        return tipoMascota.getTiponombreingles();
    }

}
