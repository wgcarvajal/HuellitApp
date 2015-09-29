package moviles.unicauca.com.huellitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_LOGIN="login";
    public static final String KEY_USER="user";
    public static final String PREFERENCE="preference";

    private EditText username,password;
    private Button btnIn;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.edit_username);
        password=(EditText)findViewById(R.id.edit_password);
        btnIn=(Button)findViewById(R.id.btn_in);

        btnIn.setOnClickListener(this);

        preferences=getSharedPreferences(PREFERENCE,MODE_PRIVATE);
        editor=preferences.edit();
    }

    @Override
    public void onClick(View v)
    {
        editor.putBoolean(KEY_LOGIN, true);

        editor.putString(KEY_USER, username.getText().toString());
        editor.commit();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
