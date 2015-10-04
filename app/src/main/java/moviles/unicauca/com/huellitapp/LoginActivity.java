package moviles.unicauca.com.huellitapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText username,password;
    private Button btnIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.edit_username);
        password=(EditText)findViewById(R.id.edit_password);
        btnIn=(Button)findViewById(R.id.btn_in);

        btnIn.setOnClickListener(this);
    }
    public boolean isOnline()
    {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v)
    {
        if(username.getText().toString().isEmpty() && password.getText().toString().isEmpty() )
        {
            Log.i("entroonclick:", getResources().getString(R.string.ingreseUsuarioContraseña));
            Toast.makeText(this,getResources().getString(R.string.ingreseUsuarioContraseña),Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(username.getText().toString().isEmpty())
            {
                Toast.makeText(this,getResources().getString(R.string.ingreseUsuario),Toast.LENGTH_SHORT).show();

            }
            else
            {
                if(password.getText().toString().isEmpty())
                {
                    Toast.makeText(this,getResources().getString(R.string.ingreseContraseña),Toast.LENGTH_SHORT).show();

                }
                else
                {
                    final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                    dlg.setTitle("Please wait.");
                    dlg.setMessage("Logging in.  Please wait.");
                    dlg.show();
                    // Call the Parse login method
                    if(isOnline())
                    {
                        ParseUser.logInInBackground(username.getText().toString(), password.getText()
                                .toString(), new LogInCallback()
                        {
                            @Override
                            public void done(ParseUser user, ParseException e)
                            {
                                dlg.dismiss();
                                if (e != null)
                                {
                                    // Show the error message
                                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.datosinvalidos), Toast.LENGTH_LONG).show();
                                } else
                                {
                                    // Start an intent for the dispatch activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        dlg.dismiss();
                        Toast.makeText(this,"verifique la conexion a internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
