package userData.trackCreation.Track;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import userData.stations.Station;

@Entity
public class Track {

    @Id
    /* Track ID */
    private String id;

    /* Track name */
    private String name;

    /* Album name that Track is in */
    private String album;

    /* Image URL */
    private String image;

    /* Artist name */
    private String artist;

    /* Preview URL */
    private String preview;

    @OneToOne
    @JsonIgnore
    private Station station;

    public Track(){

    }

    /* Constructor */
    public Track(String id, String name, String album,
                 String artist, String image, String preview) {

        this.id = id;
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.image = image;
        this.preview = preview != null ? preview : "null";
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns song ID.
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the Track
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the Album that the
     * Track is associated with
     * @return album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Gets the cover art of the track
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets the artist of the Track
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Gets the preview URL of the song, normally 30 seconds.
     * @return preview
     */
    public String getPreview() {
        return preview;
    }
}
