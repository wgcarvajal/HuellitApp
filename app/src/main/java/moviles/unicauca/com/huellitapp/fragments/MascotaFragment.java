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

import java.util.ArrayList;
import java.util.List;

import moviles.unicauca.com.huellitapp.R;
import moviles.unicauca.com.huellitapp.adapters.MascotaAdapter;
import moviles.unicauca.com.huellitapp.adapters.PagerAdapter;
import moviles.unicauca.com.huellitapp.modelo.Mascota;

/**
 * A simple {@link Fragment} subclass.
 */
public class MascotaFragment extends TitleFragment {

    private String tipo;
    private ListView lst_mascotas;
    private List<Mascota> data;
    private MascotaAdapter adapter;

    public MascotaFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    public void init(String tipo)
    {
        this.tipo=tipo;
    }

    public void putTipo(String tipo)
    {
        this.tipo=tipo;
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
                }
            }
        });
    }


    @Override
    public String getTitle() {
        return tipo;
    }
}
