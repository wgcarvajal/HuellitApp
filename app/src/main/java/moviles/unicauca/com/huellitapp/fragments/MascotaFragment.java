package moviles.unicauca.com.huellitapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import moviles.unicauca.com.huellitapp.modelo.TipoMascota;

public class MascotaFragment extends TitleFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static String POSITIONLIST="poslist";

    private TipoMascota tipoMascota;
    private ListView lst_mascotas;
    private List<Mascota> data;
    private MascotaAdapter adapter;
    private ImageView imgUbicacion;
    private  int poslist;

    public MascotaFragment()
    {
        // Required empty public constructor
    }




    public interface OnItemSelected
    {
        void onItemSelected(String idmascota);
    }

    OnItemSelected onItemSelected;


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
        onItemSelected = (OnItemSelected) context;
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
        lst_mascotas.setOnItemClickListener(this);



        loadData();

        return v;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.i("Entro", "onitemclick");
        onItemSelected.onItemSelected(data.get(position).getId());
    }
    @Override
    public void onClick(View v) {
        Log.i("entro imagen", "click");
    }
    public void loadData()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Mascota.TABLA);
        query.whereEqualTo(Mascota.TIPO, tipoMascota.getTiponombre());
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
    public String getTitle() {
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
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Mascota.TABLA);
        query.whereEqualTo(Mascota.TIPO, tipoMascota.getTiponombre());
        query.whereContains(Mascota.NOMBRE, nombremascota);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject mascota : parseObjects) {
                        Mascota mas = new Mascota();
                        mas.setNombre(mascota.getString(Mascota.NOMBRE));
                        mas.setId(mascota.getObjectId());
                        mas.setTipo(mascota.getString(Mascota.TIPO));
                        mas.setDescripcion(mascota.getString(Mascota.DESCRIPCION));
                        mas.setEdad((Integer)mascota.getNumber(Mascota.EDAD));
                        data.add(mas);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
