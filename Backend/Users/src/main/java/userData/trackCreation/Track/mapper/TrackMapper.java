package userData.trackCreation.Track.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import userData.trackCreation.Track.Track;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class TrackMapper {

    // Creates a GSON Object for JSON Mapping.
    static Gson gson = new Gson();

    /**
     * Creates a clean JSON file returned from the Spotify API
     * Contains the preprocessor and filter
     * @return cleaned JSON file
     */
    public static List<Track> trackData(String jsonData) {

        /* Create a TypeToken for the class RawTrack for JSON preprocessing */
        Type RawTrackType = new TypeToken<List<RawTrack>>(){}.getType();
        List<RawTrack> rawTracks = gson.fromJson(jsonData, RawTrackType);

        /* Return a list of tracks based on the Spotify JSON file */
        return filter(rawTracks);
    }

    /**
     * A filter method that only gets the required JSON data
     * @return clean tracks
     */
    private static List<Track> filter(List<RawTrack> rawTracks) {

        /* New Track array list */
        List<Track> tracks = new ArrayList<>();

        /* Iterate through all tracks */
        for(RawTrack t : rawTracks) {
            String id = t.getId();
            String name = t.getName();
            String albumName = t.getAlbumName();
            String artist = t.getArtist();
            String image = t.getImage();
            String preview = t.getPreviewUrl();

            Track e = new Track(id, name, albumName, artist, image, preview);
            tracks.add(e);
        }

        /* Return a JSON string */
        return tracks;
    }
}
