package moviles.unicauca.com.huellitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilActivity extends AppCompatActivity
{
    private ProfilePictureView imgFotoUsuario;
    private TextView txtNombre;
    private TextView txtGenero;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        imgFotoUsuario=(ProfilePictureView)findViewById(R.id.img_fotouser);
        txtNombre=(TextView)findViewById(R.id.txt_nombre);
        txtGenero=(TextView)findViewById(R.id.txt_genero);
        txtEmail=(TextView)findViewById(R.id.txt_email);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.has("profile"))
        {

            JSONObject userProfile = currentUser.getJSONObject("profile");
            try
            {
                if(userProfile.has("facebookId"))
                {
                    imgFotoUsuario.setProfileId(userProfile.getString("facebookId"));
                }
                else
                {
                    imgFotoUsuario.setProfileId(userProfile.getString(null));
                }
                if(userProfile.has("name"))
                {
                    txtNombre.setText(userProfile.getString("name"));
                }
                else
                {
                    txtNombre.setText("");
                }
                if(userProfile.has("gender"))
                {
                    if(userProfile.getString("gender").equals("male"))
                    {
                        txtGenero.setText(getResources().getString(R.string.genero_masculino));
                    }
                    else
                    {
                        txtGenero.setText(getResources().getString(R.string.genero_femenino));
                    }
                }
                else
                {
                    txtGenero.setText("");
                }
                if(userProfile.has("email"))
                {
                    txtEmail.setText(userProfile.getString("email"));
                }
                else
                {
                    txtEmail.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
