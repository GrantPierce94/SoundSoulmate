package userData.trackCreation.Spotify;

import com.google.gson.Gson;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;

import se.michaelthelin.spotify.requests.data.tracks.GetAudioAnalysisForTrackRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;
import userData.trackCreation.Track.Track;
import userData.trackCreation.Track.mapper.TrackMapper;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class SpotifyController {

    static Gson gson = new Gson();

    private static final SpotifyApi spotifyAPI = new SpotifyApi.Builder()
            .setClientId("ae02bde4d6ef4bc395502d8f76e38f04")
            .setClientSecret("0d0a994ae7f842feb33dfa163b56bacd")
            .build();

    private static final ClientCredentialsRequest credentials = spotifyAPI.clientCredentials()
            .build();

    @PostConstruct
    public void clientCredentials() throws IOException, SpotifyWebApiException, ParseException {
        final ClientCredentials client = credentials.execute();
        spotifyAPI.setAccessToken(client.getAccessToken());
    }

    public static List<Track> searchTrack(String trackName) throws IOException, SpotifyWebApiException, ParseException {
        final SearchItemRequest search = spotifyAPI.searchItem(trackName, ModelObjectType.TRACK.getType())
                .market(CountryCode.US)
                .limit(5)
                .build();

        final SearchResult searchResult = search.execute();
        String jsonOutput = gson.toJson(searchResult.getTracks().getItems());
        return TrackMapper.trackData(jsonOutput);

    }

    public static List<String> trackAnalysis(String id) throws IOException, SpotifyWebApiException, ParseException {
        final GetAudioAnalysisForTrackRequest getAudioAnalysisForTrackRequest = spotifyAPI
                .getAudioAnalysisForTrack(id)
                .build();

        final AudioAnalysis audioAnalysis = getAudioAnalysisForTrackRequest.execute();
        List<String> analysis = new ArrayList<>();

        analysis.add(Arrays.toString(audioAnalysis.getBars()));
        analysis.add(Arrays.toString(audioAnalysis.getSections()));
        analysis.add(Arrays.toString(audioAnalysis.getBeats()));
        analysis.add(Arrays.toString(audioAnalysis.getSegments()));

        return analysis;
    }

    public static List<String> trackFeatures(String id) throws IOException, SpotifyWebApiException, ParseException {
        final GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = spotifyAPI
                .getAudioFeaturesForTrack(id)
                .build();

        final AudioFeatures audioFeatures = getAudioFeaturesForTrackRequest.execute();
        List<String> features = new ArrayList<>();

        features.add(String.valueOf(audioFeatures.getTimeSignature()));
        features.add(String.valueOf(audioFeatures.getAcousticness()));
        features.add(String.valueOf(audioFeatures.getKey()));
        features.add(String.valueOf(audioFeatures.getDanceability()));
        features.add(String.valueOf(audioFeatures.getTempo()));
        features.add(String.valueOf(audioFeatures.getEnergy()));
        features.add(String.valueOf(audioFeatures.getDurationMs()));

        return features;
    }

    public static HashMap<String, Long> getCurrentlyPlayingRecommendedSong(String id) throws IOException, SpotifyWebApiException, ParseException {
        final GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest = spotifyAPI
                .getUsersCurrentlyPlayingTrack()
                .build();

        final CurrentlyPlaying currentlyPlaying = getUsersCurrentlyPlayingTrackRequest.execute();

        String track_playing = currentlyPlaying.getItem().getName();
        Long current_time = currentlyPlaying.getTimestamp();

        HashMap<String, Long> current_track_playing = new HashMap<>();
        current_track_playing.put(track_playing, current_time);


        return current_track_playing;
    }

    public static List<Track> getRecommendations(String seeds, int numSongs/*, int popularity*/) throws IOException, SpotifyWebApiException, ParseException {

        final GetRecommendationsRequest request = spotifyAPI.getRecommendations()
                .limit(numSongs)
                .seed_tracks(seeds)
                .build();

        final Recommendations recommendations = request.execute();
        String jsonOutput = gson.toJson(recommendations.getTracks());
        return TrackMapper.trackData(jsonOutput);
    }
}
