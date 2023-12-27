package screens;
import static android.icu.util.LocaleData.getInstance;
import static okhttp3.internal.Internal.instance;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.json.JSONException;

public class FriendProfileScreen extends AppCompatActivity {

    String baseUrl = "http://coms-309-056.class.las.iastate.edu:8080/";
    TextView bioF; //set with user/(friends username)/bio
    TextView usernameF; //set with user/(friends username)/username
    ImageView profilePictureF; //set with user/(friends username)/image/url
    ImageView topSongImageF;
    ImageView topArtistImageF;
    TextView topSongNameF;
    TextView topArtistNameF;
    private MediaPlayer mediaPlayer;
    String songPreview;
    ImageView playSnippetF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        bioF = findViewById(R.id.bioF);
        usernameF = findViewById(R.id.usernameF);
        profilePictureF = findViewById(R.id.profilePictureF);
        topSongImageF = findViewById(R.id.topSongImageF);
        topSongNameF = findViewById(R.id.topSongNameF);
        topArtistImageF = findViewById(R.id.topArtistImageF);
        topArtistNameF = findViewById(R.id.topArtistNameF);
        playSnippetF = findViewById(R.id.playSnippetF);
        navBar();
        getTopSong();
        getTopArtist();
        getUser(GlobalVariables.friendUserName);
        getProfilePicture();

        playSnippetF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSnippet();
            }
        });

    }

    private void getUser(String username) {//will need a get request to retrieve username from profile screen
        RequestQueue requestQueue = Volley.newRequestQueue(FriendProfileScreen.this);//might need to grab request from ProfileScreen directly
        String url = baseUrl + "user/" + username; //make sure global variable stays the same for each user request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        try {
                            String yall = res.getString("userName");
                            usernameF.setText(yall);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void navBar(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_profile){
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    Intent profileIntent = new Intent(FriendProfileScreen.this, ProfileScreen.class);
                    startActivity(profileIntent);
                    return true;
                }
                if(item.getItemId() == R.id.menu_discover){
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    Intent profileIntent = new Intent(FriendProfileScreen.this, MusicSwipe.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void getProfilePicture(){
        RequestQueue requestQueue = Volley.newRequestQueue(FriendProfileScreen.this);
        String url = baseUrl + "user/" + GlobalVariables.friendUserName + "/picture";

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        Picasso.get().load(response).into(profilePictureF);
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
    }

    private void getTopSong() {
        RequestQueue requestQueue = Volley.newRequestQueue(FriendProfileScreen.this);
        String url = baseUrl + "user/" + GlobalVariables.friendUserName + "/topTrack";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String songImage = response.getString("image");
                            String songName = response.getString("name");
                            String songSnip = response.getString("preview");
                            songPreview = songSnip;
                            Picasso.get().load(songImage).into(topSongImageF);
                            topSongNameF.setText(songName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void playSnippet(){
        mediaPlayer = MediaPlayer.create(FriendProfileScreen.this, Uri.parse(songPreview));
        mediaPlayer.start();
    }

    private void getTopArtist(){
        RequestQueue requestQueue = Volley.newRequestQueue(FriendProfileScreen.this);
        String url = baseUrl + "user/" + GlobalVariables.friendUserName + "/topArtist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String songImage = response.getString("image");
                            String songName = response.getString("name");
                            Picasso.get().load(songImage).into(topArtistImageF);
                            topArtistNameF.setText(songName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}
