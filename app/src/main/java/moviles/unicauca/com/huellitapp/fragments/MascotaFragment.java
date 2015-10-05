package moviles.unicauca.com.huellitapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.R;
import moviles.unicauca.com.huellitapp.adapters.MascotaAdapter;
import moviles.unicauca.com.huellitapp.modelo.Mascota;

public class MascotaFragment extends TitleFragment
{
    public static String POSITIONLIST="poslist";

    private String tipo;
    private String tipoIdioma;
    private ListView lst_mascotas;
    private List<Mascota> data;
    private MascotaAdapter adapter;
    private  int poslist;

    public MascotaFragment()
    {
        // Required empty public constructor
    }
    public void init(String tipo,String tipoIdioma)
    {
        this.tipo=tipo;
        this.tipoIdioma=tipoIdioma;
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
            tipo = savedInstanceState.getString("tipo");
            tipoIdioma=savedInstanceState.getString("tipoIdioma");
            poslist=savedInstanceState.getInt(POSITIONLIST);
            Log.i("focus item:",""+ poslist);
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
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("mascota");
        query.whereEqualTo("tiponombre", tipo);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject mascota : parseObjects) {
                        Mascota mas = new Mascota();
                        mas.setNombre(mascota.getString("masnombre"));
                        mas.setId(mascota.getObjectId());
                        data.add(mas);
                        adapter.notifyDataSetChanged();
                    }
                    lst_mascotas.setSelection(poslist);
                }
            }
        });
    }
    @Override
    public String getTitle() {
        return tipoIdioma;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("tipo", tipo);
        outState.putString("tipoIdioma", tipoIdioma);
        outState.putInt(POSITIONLIST,lst_mascotas.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }


    public void buscar(String nombre)
    {
        data.clear();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("mascota");
        query.whereEqualTo("tiponombre", tipo);
        query.whereContains("masnombre", nombre);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject mascota : parseObjects) {
                        Mascota mas = new Mascota();
                        mas.setNombre(mascota.getString("masnombre"));
                        mas.setId(mascota.getObjectId());
                        data.add(mas);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
