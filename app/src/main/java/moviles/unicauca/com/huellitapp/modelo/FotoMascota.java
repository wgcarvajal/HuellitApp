package moviles.unicauca.com.huellitapp.modelo;

/**
 * Created by geovanny on 5/10/15.
 */
public class FotoMascota
{
    public static String TABLA="fotomascota";
    public static String ID="objectId";
    public static String IMAGEN="foto";
    public static String MASCOTAID="mascota";

    private String id;
    private String mascotaid;


    public FotoMascota()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMascotaid() {
        return mascotaid;
    }

    public void setMascotaid(String mascotaid) {
        this.mascotaid = mascotaid;
    }
}
