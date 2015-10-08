package moviles.unicauca.com.huellitapp.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import moviles.unicauca.com.huellitapp.modelo.FotoMascota;
import moviles.unicauca.com.huellitapp.modelo.Mascota;
import moviles.unicauca.com.huellitapp.modelo.TipoMascota;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMascotaFragment extends TitleFragment implements DialogInterface.OnClickListener{


    public static String POSITIONLIST="poslist";

    private TipoMascota tipoMascota;
    private ListView lst_mascotas;
    private List<Mascota> data;
    private MascotaAdapter adapter;
    private  int poslist;
    private int pos;
    private AlertDialog delete;

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

        registerForContextMenu(lst_mascotas);

        delete= new AlertDialog.Builder(v.getContext())
                .setTitle(getResources().getString(R.string.dialog_eliminar))
                .setMessage(getResources().getString(R.string.dialog_pregunta))
                .setPositiveButton(getResources().getString(R.string.dialog_aceptar), this)
                .setNegativeButton(getResources().getString(R.string.dialog_cancelar), this)
                .create();

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TipoMascota.TIPONOMBRE, tipoMascota.getTiponombre());
        outState.putString(TipoMascota.TIPONOMBREINGLES, tipoMascota.getTiponombreingles());
        outState.putInt(POSITIONLIST, lst_mascotas.getFirstVisiblePosition());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.clear();
        menu.add(Menu.NONE, R.id.action_edit, Menu.NONE, getResources().getString(R.string.action_edit));
        menu.add(Menu.NONE, R.id.action_delete, Menu.NONE, getResources().getString(R.string.action_delete));
        super.onCreateContextMenu(menu, v, menuInfo);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        pos=info.position;
        if (getUserVisibleHint()) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    Log.i("Entro", "Editar");
                    break;
                case R.id.action_delete:
                    Log.i("pos:", "" + pos);
                    Log.i("tamaño array:", "" + data.size());
                    delete.show();
                    Log.i("Entro", "Eliminar");
                    break;
            }
        }
        return  super.onContextItemSelected(item);

    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if(which==DialogInterface.BUTTON_POSITIVE)
        {
            ParseObject.createWithoutData(Mascota.TABLA, ((Mascota) data.get(pos)).getId()).deleteEventually();

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FotoMascota.TABLA);
            query.whereEqualTo(FotoMascota.MASCOTAID, ((Mascota) data.get(pos)).getId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject foto : list) {
                        ParseObject.createWithoutData(FotoMascota.TABLA, foto.getObjectId()).deleteEventually();
                    }
                }
            } );
            data.remove(pos);
            adapter.notifyDataSetChanged();

        }

    }

}



