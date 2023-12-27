package screens;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.R;

import org.json.JSONObject;

import java.util.HashMap;
import screens.GlobalVariables;

public class LoginScreen extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    LinearLayout logoRelative;
    Button registerButton;
    Button registerGuestButton;
    String baseUrl = "http://coms-309-056.class.las.iastate.edu:8080/";
    String isUserValid = "false";
    private HashMap<String, String> userCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        logoRelative = findViewById(R.id.logoRelative);
        registerButton = findViewById(R.id.signUpButton);
        registerGuestButton = findViewById(R.id.signUpGuest);
        userCredentials = new HashMap<>();
        userCredentials.put("username", "password");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                GlobalVariables.userName = enteredUsername;

                //ONLY USE WHEN SERVER ISNT RUNNING AND YOU NEED TO GET INTO THE APP ONLY FOR TESTING !!!
//                    Intent intent = new Intent(LoginScreen.this, SeedSetter.class);
//                    startActivity(intent);

                if(validateLogin(enteredUsername, enteredPassword)){
                    Intent intent = new Intent(LoginScreen.this, SeedSetter.class);
                    startActivity(intent);
                }
            }
        });

                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernameSend = username.getText().toString();
                        String passwordSend = password.getText().toString();

                        if (!usernameSend.isEmpty() && !passwordSend.isEmpty()) {
                            String url = baseUrl + "user/" + usernameSend + "/" + passwordSend;

                            JSONObject requestBody = new JSONObject();

                            JsonObjectRequest request = new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    requestBody,
                                    null,
                                    null
                            );

                            Volley.newRequestQueue(LoginScreen.this).add(request);

                            Toast.makeText(LoginScreen.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                            GlobalVariables.isGuestUser = false;
                            String spotifyRedirect = "http://coms-309-056.class.las.iastate.edu:8080/register/" + usernameSend;
                            Uri uri = Uri.parse(spotifyRedirect);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginScreen.this, "Please Register", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        registerGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameSend = username.getText().toString();
                String passwordSend = password.getText().toString();

                if (!usernameSend.isEmpty() && !passwordSend.isEmpty()) {
                    String url = baseUrl + "user/" + usernameSend + "/" + passwordSend;

                    JSONObject requestBody = new JSONObject();

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            requestBody,
                            null,
                            null
                    );

                    Volley.newRequestQueue(LoginScreen.this).add(request);
                    GlobalVariables.isGuestUser = true;
                    Toast.makeText(LoginScreen.this, "Registered Successfully, Login Now", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginScreen.this, "Please Register", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validateLogin(String enteredUsername, String enteredPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);

        String url = "http://coms-309-056.class.las.iastate.edu:8080/user/" + enteredUsername + "/" + enteredPassword;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        isUserValid = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(stringRequest);
        return isUserValid.equals("true");

    }


}
