package moviles.unicauca.com.huellitapp.modelo;

import moviles.unicauca.com.huellitapp.fragments.TitleFragment;

/**
 * Created by geovanny on 29/09/15.
 */
public class Mascota
{
    public static String TABLA="mascota";
    public static String ID="objectid";
    public static String NOMBRE="masnombre";
    public static String TIPO="tiponombre";
    public static String FECHACREACION="createdAt";
    public static String USERNAME="username";



    private String nombre;
    private String tipo;
    private String id;

    public Mascota()
    {

    }
    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
