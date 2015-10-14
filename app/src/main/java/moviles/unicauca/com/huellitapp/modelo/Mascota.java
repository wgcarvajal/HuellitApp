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
    public static String EDAD="masedad";
    public static String DESCRIPCION="masdescripcion";
    public static String RESPONSABLE="username";



    private String nombre;
    private String tipo;
    private String id;
    private String descripcion;
    private int edad;
    private String responsable;

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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}
