package screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

public class ProfileSettings extends AppCompatActivity {
    EditText editBio;
    ImageView setBio;
    String baseUrl = "http://coms-309-056.class.las.iastate.edu:8080/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);
        editBio = findViewById(R.id.editBio);
        setBio = findViewById(R.id.setBio);


        setBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editBioString = editBio.getText().toString();

                if (!editBioString.isEmpty()) {
                    String url = baseUrl + "user/" + GlobalVariables.userName + "/profile/" + editBioString;

                    JSONObject requestBody = new JSONObject();

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.PUT,
                            url,
                            requestBody,
                            null,
                            null
                    );

                    Volley.newRequestQueue(ProfileSettings.this).add(request);

                    Toast.makeText(ProfileSettings.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileSettings.this, "Set A Bio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navBar();
    }

    private void navBar(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_discover){
                    Intent discoverIntent = new Intent(ProfileSettings.this, MusicSwipe.class);
                    startActivity(discoverIntent);
                    return true;
                }else if(item.getItemId() == R.id.menu_profile){
                    Intent discoverIntent = new Intent(ProfileSettings.this, ProfileScreen.class);
                    startActivity(discoverIntent);
                    return true;
                }
                return false;
            }
        });
    }



}
