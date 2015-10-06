package moviles.unicauca.com.huellitapp.modelo;

/**
 * Created by geovanny on 5/10/15.
 */
public class TipoMascota
{
    public static String TABLA="tipomascota";
    public static String ID="objectid";
    public static String TIPONOMBRE="tiponombre";
    public static String TIPONOMBREINGLES="tiponombreingles";

    private String tiponombre;
    private String tiponombreingles;
    private String id;

    public TipoMascota()
    {

    }

    public String getTiponombre() {
        return tiponombre;
    }

    public void setTiponombre(String tiponombre) {
        this.tiponombre = tiponombre;
    }

    public String getTiponombreingles() {
        return tiponombreingles;
    }

    public void setTiponombreingles(String tiponombreingles) {
        this.tiponombreingles = tiponombreingles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
