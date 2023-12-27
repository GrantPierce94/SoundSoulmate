package screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.R;

import org.json.JSONObject;
import screens.GlobalVariables;

public class SeedSetter extends AppCompatActivity {
    EditText seed;
    ImageView setSeed;
    Button musicSwipeSend;
    SeekBar volume;
    SeekBar tempo;
    SeekBar popularity;
    String baseUrl = "http://coms-309-056.class.las.iastate.edu:8080/";
//    String baseUrl = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seedsetter);

        volume = findViewById(R.id.volume_slider);
        tempo = findViewById(R.id.tempo_slider);
        popularity = findViewById(R.id.popularity_slider);
        seed = findViewById(R.id.seed);
        setSeed = findViewById(R.id.seedSetter);
        musicSwipeSend = findViewById(R.id.musicSwipeSend);

        setSeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songText = seed.getText().toString();

                if (!songText.isEmpty()) {
                    if(GlobalVariables.userName == null){
                        return;
                    }
                    String url = baseUrl + "user/" + GlobalVariables.userName + "/station/" + songText;

                    JSONObject requestBody = new JSONObject();

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.PUT,
                            url,
                            requestBody,
                            null,
                            null
                    );

                    Volley.newRequestQueue(SeedSetter.this).add(request);

                    Toast.makeText(SeedSetter.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SeedSetter.this, "Must Enter A Song Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(GlobalVariables.userName == null){
                    return;
                }
                String volume = baseUrl + "user/" + GlobalVariables.userName + "/volume/" + (seekBar.getProgress() * 10);//sends the volume as a string
                JSONObject requestBody = new JSONObject();

                JsonObjectRequest reqVolume = new JsonObjectRequest(
                        Request.Method.PUT, volume, requestBody, null, null
                );
                Volley.newRequestQueue(SeedSetter.this).add(reqVolume);
                Toast.makeText(SeedSetter.this, "Volume at " + seekBar.getProgress() + "sent", Toast.LENGTH_SHORT).show();
            }
        });

        tempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(GlobalVariables.userName == null){
                    return;
                }
                String Tempo = baseUrl + "user/" + GlobalVariables.userName + "/tempo/" + (seekBar.getProgress() * 10);//sends the volume as a string
                JSONObject requestBody = new JSONObject();

                JsonObjectRequest reqTempo = new JsonObjectRequest(
                        Request.Method.PUT, Tempo, requestBody, null, null
                );
                Volley.newRequestQueue(SeedSetter.this).add(reqTempo);
                Toast.makeText(SeedSetter.this, "Tempo at " + seekBar.getProgress() + "sent", Toast.LENGTH_SHORT).show();
            }
        });

        popularity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(GlobalVariables.userName == null){
                    return;
                }
                String Popularity = baseUrl + "user/" + GlobalVariables.userName + "/popularity/" + (seekBar.getProgress() * 10);//sends the volume as a string
                JSONObject requestBody = new JSONObject();

                JsonObjectRequest reqPopularity = new JsonObjectRequest(
                        Request.Method.PUT, Popularity, requestBody, null, null
                );
                Volley.newRequestQueue(SeedSetter.this).add(reqPopularity);
                Toast.makeText(SeedSetter.this, "Popularity at " + seekBar.getProgress() + "sent", Toast.LENGTH_SHORT).show();
            }
        });

        musicSwipeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeedSetter.this, MusicSwipe.class);
                startActivity(intent);
            }
        });


    }
}
