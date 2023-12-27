package userData.stations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import userData.trackCreation.Spotify.SpotifyController;
import userData.trackCreation.Track.Track;
import userData.users.User;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
public class Station {

    // seed is of type seed

    @Id
    private int id;

    private int tempo;
    private int popularity;
    private int volume;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "track_id")
    private Track seed;

    @OneToOne
    @JsonIgnore
    private User station;

    public Station(){

    }

    public Station(int id){
        this.id = id;
    }

    public void setId(int i){
        this.id = i;
    }

    public int getId(){
        return id;
    }

    public void setSeed(Track T) { this.seed = T ; }

    public Track getSeed() { return this.seed; }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getTempo() {
        return tempo;
    }

    public int getVolume() {
        return volume;
    }

    public List<Track> generateTacks() throws IOException, ParseException, SpotifyWebApiException {
        return SpotifyController.getRecommendations(seed.getId(), 50);
    }

}
