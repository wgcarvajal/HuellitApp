package moviles.unicauca.com.huellitapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnFacebook;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnFacebook=(Button)findViewById(R.id.btn_facebook);
        btnFacebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        progressDialog = ProgressDialog.show(this, "Logueando con Facebook", "Espere un momento", true);

        List<String> permissions = Arrays.asList("public_profile", "user_about_me",
                "user_birthday", "user_location", "email");



        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {



            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();


                if (user == null) {
                    Log.d("MyApp", "El usuario cancelo el Loggin");
                } else if (user.isNew()) {

                    Log.d("MyApp", "Primer loggin del Usuario");
                    makeMeRequest();
                    //startActivity(new Intent(getApplication(), MainActivity.class));

                } else {
                    Log.d("MyApp", "El usuario ya estaba logueado");
                    makeMeRequest();
                    //startActivity(new Intent(getApplication(), MainActivity.class));
                }
            }
        });









    }
    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();

                            try {
                                userProfile.put("facebookId", jsonObject.getLong("id"));
                                userProfile.put("name", jsonObject.getString("name"));

                                if (jsonObject.getString("gender") != null)
                                    userProfile.put("gender", jsonObject.getString("gender"));

                                if (jsonObject.getString("email") != null)
                                    userProfile.put("email", jsonObject.getString("email"));

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();


                            } catch (JSONException e) {
                                Log.d("Myapp",
                                        "Error parsing returned user data. " + e);
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    Log.d("myapp",
                                            "Authentication error: " + graphResponse.getError());
                                    break;

                                case TRANSIENT:
                                    Log.d("myapp",
                                            "Transient error. Try again. " + graphResponse.getError());
                                    break;

                                case OTHER:
                                    Log.d("myapp",
                                            "Some other error: " + graphResponse.getError());
                                    break;
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
