package screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendInteractions extends AppCompatActivity {
    EditText friendText;
    Button friendList;
    Button openChat;
    Button addFriend;
    Button removeFriend;
    TextView fullFriendList;
    String baseUrl = "http://coms-309-056.class.las.iastate.edu:8080/";
    private final ArrayList<String> friendNames = new ArrayList<>();
    private final ArrayList<String> friendPfps = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_interactions);
        friendList = findViewById(R.id.friendList);
        openChat = findViewById(R.id.openChat);
        addFriend = findViewById(R.id.addFriend);
        removeFriend = findViewById(R.id.removeFriend);
        friendText = findViewById(R.id.friendUsername);
//        fullFriendList = findViewById(R.id.fullFriendList);

        openChat.setOnClickListener(new View.OnClickListener() {//open a chat with the inputted username
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendInteractions.this, ChatScreen.class);
                startActivity(intent);
                setContentView(R.layout.activity_friends);
            }
        });
        friendList.setOnClickListener(new View.OnClickListener() {//want this to send a username to friendprofilescreen for it to build other user profile
            @Override   //if i want all friends friends/Global.username  will grab the list of all users added to friendslist
            public void onClick(View v) {
                GlobalVariables.friendUserName = friendText.getText().toString();
                if (!GlobalVariables.friendUserName.isEmpty()) {

                    Toast.makeText(FriendInteractions.this, "Request sent successfully open friend", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(FriendInteractions.this, FriendProfileScreen.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(FriendInteractions.this, "Enter Friend Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {//add the submitted username into users friend list
            @Override
            public void onClick(View v) {
                String editFriendString = friendText.getText().toString();
                if (!editFriendString.isEmpty()) {
                    String url = baseUrl + "add/" + GlobalVariables.userName + "/" + editFriendString;
                    JSONObject requestBody = new JSONObject();
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.PUT, url, requestBody, null, null
                    );

                    Volley.newRequestQueue(FriendInteractions.this).add(request);

                    Toast.makeText(FriendInteractions.this, "Request sent successfully"+ editFriendString, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FriendInteractions.this, "Enter Friend Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        removeFriend.setOnClickListener(new View.OnClickListener() {//remove a friend from the user repository
            @Override
            public void onClick(View v) {
                String editFriendString = friendText.getText().toString();
                if (!editFriendString.isEmpty()) {
                    String url = baseUrl + "remove/" + GlobalVariables.userName + "/" + editFriendString;
                    JSONObject requestBody = new JSONObject();
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.PUT, url, requestBody, null, null
                    );

                    Volley.newRequestQueue(FriendInteractions.this).add(request);

                    Toast.makeText(FriendInteractions.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FriendInteractions.this, "Enter Friend Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getFriendList();
        navBar();
    }

    private void getFriendList() {
        RequestQueue requestQueue = Volley.newRequestQueue(FriendInteractions.this);
        String org = baseUrl + "friendslist/" + GlobalVariables.userName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, org, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Volley Response", response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject friendObject = response.getJSONObject(i);
                                String friendName = friendObject.getString("userName");
                                String friendPfp = friendObject.getString("profilePicture");

                                friendNames.add(friendName);
                                friendPfps.add(friendPfp);
                            }

                            LinearLayout containerLayout = findViewById(R.id.friendsContainer);

                            for (int i = 0; i < friendNames.size(); i++) {
                                RelativeLayout friendLayout = new RelativeLayout(FriendInteractions.this);
                                friendLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                                ((RelativeLayout.LayoutParams) friendLayout.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);

                                CircleImageView profilePicture = new CircleImageView(FriendInteractions.this);
                                profilePicture.setId(View.generateViewId());
                                profilePicture.setLayoutParams(new RelativeLayout.LayoutParams(120, 120));
                                Picasso.get().load(friendPfps.get(i)).into(profilePicture);
                                profilePicture.setBorderWidth(1);
                                profilePicture.setBorderColor(Color.WHITE);

                                TextView textView = new TextView(FriendInteractions.this);
                                textView.setLayoutParams(new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                                textView.setText(friendNames.get(i));
                                textView.setTextColor(Color.WHITE);

                                friendLayout.addView(profilePicture);
                                friendLayout.addView(textView);

                                RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                textParams.addRule(RelativeLayout.END_OF, profilePicture.getId());
                                textParams.addRule(RelativeLayout.CENTER_VERTICAL);
                                textParams.setMarginStart(16);

                                containerLayout.addView(friendLayout);

                                Log.d("Friend Names", friendNames.toString());
                            }
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
        requestQueue.add(jsonArrayRequest);
    }






    private void navBar(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_discover){
                    Intent discoverIntent = new Intent(FriendInteractions.this, MusicSwipe.class);
                    startActivity(discoverIntent);
                    return true;
                }else if(item.getItemId() == R.id.menu_profile){
                    Intent profileIntent = new Intent(FriendInteractions.this, ProfileScreen.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });
    }

}
