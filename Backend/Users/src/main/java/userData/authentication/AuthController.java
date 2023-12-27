package userData.authentication;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import userData.trackCreation.TopFields.TopArtist;
import userData.trackCreation.TopFields.TopArtistsRepository;
import userData.trackCreation.TopFields.TopTrack;
import userData.trackCreation.TopFields.TopTrackRepository;
import userData.users.User;
import userData.users.UserRepository;
/**
 * This class is responsible for handling the authentication of the user.
 */
@RestController
public class AuthController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    TopArtistsRepository topArtistsRepository;

    @Autowired
    TopTrackRepository topTrackRepository;

    /**
     * This is the username of the user that is currently logged in.
     */
    private String user;

    /**
     * This is the redirect URI that Spotify will send the user to after they have logged in.
     */
    private static final URI redirectURI = SpotifyHttpManager.makeUri("http://coms-309-056.class.las.iastate.edu:8080/register/api");

    /**
     * This is the Spotify API object that will be used to make requests to the Spotify API.
     */
    private static final SpotifyApi spotifyAPI = new SpotifyApi.Builder()
            .setClientId("ae02bde4d6ef4bc395502d8f76e38f04")
            .setClientSecret("0d0a994ae7f842feb33dfa163b56bacd")
            .setRedirectUri(redirectURI)
            .build();

    /**
     * This method will redirect the user to the Spotify login page.
     * @param response: The response object that will be used to redirect the user.
     * @throws IOException: If the redirect fails.
     */
    @GetMapping("login")
    @ResponseBody
    public void spotifyLogin(HttpServletResponse response) throws IOException {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyAPI.authorizationCodeUri()
                .scope("user-read-private, user-read-email, user-top-read")
                .show_dialog(false)
                .build();

        final URI uri = authorizationCodeUriRequest
                .execute();
        response
                .sendRedirect(uri.toString());
    }

    /**
     * This method will get the access code from Spotify and set the access token and refresh token in the Spotify API object.
     *
     * @param userCode The access code that Spotify sends back to the user.
     * @param response The response object that will be used to redirect the user.
     * @return spotifyAPI.getAccessToken() : The access token that will be used to make requests to the Spotify API.
     * @throws IOException If the redirect fails.
     */
    @GetMapping("login/api")
    public String getAccessCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException, SpotifyWebApiException, ParseException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyAPI.authorizationCode(userCode).build();

        final AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();
        spotifyAPI.setAccessToken(credentials.getAccessToken());
        spotifyAPI.setRefreshToken(credentials.getRefreshToken());

        return spotifyAPI.getAccessToken();
    }

    /**
     * This method will redirect the user to the Spotify registration page.
     *
     * @param response: The response object that will be used to redirect the user.
     * @throws IOException: If the redirect fails.
     */
    @GetMapping("register/{username}")
    @ResponseBody
    public void spotifyRegister(HttpServletResponse response, @PathVariable String username) throws IOException {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyAPI.authorizationCodeUri()
                .scope("user-read-private, user-top-read, playlist-modify-private")
                .show_dialog(true)
                .build();

        this.user = username;
        final URI uri = authorizationCodeUriRequest
                .execute();
        response
                .sendRedirect(uri.toString());
    }

    /**
     * This method will get the access code from Spotify and set the access token and refresh token in the Spotify API object.
     *
     * @param userCode The access code that Spotify sends back to the user.
     * @return spotifyAPI.getAccessToken() : The access token that will be used to make requests to the Spotify API.
     * @throws IOException If the redirect fails.
     */
    @GetMapping("register/api")
    public String accessCode(@RequestParam("code") String userCode) throws IOException, SpotifyWebApiException, ParseException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyAPI.authorizationCode(userCode).build();
        final AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();
        spotifyAPI.setAccessToken(credentials.getAccessToken());
        spotifyAPI.setRefreshToken(credentials.getRefreshToken());
        User u = userRepository.findByUserName(user);
        u.setAccessKey(spotifyAPI.getAccessToken());
        userRepository.save(u);
        getCurrentUuid(u);
        getProfilePicture(u);
        createSpinderFavorites(u);
        topArtist();
        topTrack();

        System.out.println("[DEBUG] | " + user + " has successfully registered. \n[DEBUG] | Access Token: " + spotifyAPI.getAccessToken());
        return "You can now go back to the app.";
    }

    public void getCurrentUuid(User u) throws IOException, SpotifyWebApiException, ParseException {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyAPI.getCurrentUsersProfile().build();
        final se.michaelthelin.spotify.model_objects.specification.User user = getCurrentUsersProfileRequest.execute();
        final String uuid = user.getId();
        u.setUuid(uuid);
        userRepository.save(u);
    }

    public void getProfilePicture(User u) throws IOException, SpotifyWebApiException, ParseException {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyAPI.getCurrentUsersProfile().build();
        final se.michaelthelin.spotify.model_objects.specification.User user = getCurrentUsersProfileRequest.execute();
        final String profilePicture = user.getImages()[0].getUrl();
        u.setProfilePicture(profilePicture);
        userRepository.save(u);
    }

    // Turned into void
    public void createSpinderFavorites(User u) throws IOException, SpotifyWebApiException, ParseException {
        String uuid = u.getUuid();
        CreatePlaylistRequest createPlaylist = spotifyAPI.createPlaylist(uuid, "Spinder Picks").collaborative(false).public_(false).description("Generated with love from the Spinder Team: JS, NG, QE, & BP").build();
        final Playlist playlist = createPlaylist.execute();
        final String playlistId = playlist.getId();
        u.setPlaylistId(playlistId);
        userRepository.save(u);
    }

    @PostMapping("add/{username}/{track}")
    public void addTrackToFavorites(@PathVariable String username, @PathVariable String track) throws IOException, SpotifyWebApiException, ParseException {
        User u = userRepository.findByUserName(username);
        String[] trackUri = new String[]{"spotify:track:" + track};
        String playlistId = u.getPlaylistId();
        spotifyAPI.setAccessToken(u.getAccessKey());
        AddItemsToPlaylistRequest addTrack = spotifyAPI.addItemsToPlaylist(playlistId, trackUri).build();
        final SnapshotResult executeAddTrack = addTrack.execute();
    }

    public void topArtist() throws IOException, SpotifyWebApiException, ParseException {
        User u = userRepository.findByUserName(user);
        GetUsersTopArtistsRequest getTopAritst = spotifyAPI.getUsersTopArtists().limit(1).time_range("long_term").build();
        final Paging<Artist> topArtist = getTopAritst.execute();
        Artist[] a = topArtist.getItems();
        TopArtist t = new TopArtist();
        t.setName(a[0].getName());
        t.setImage(a[0].getImages()[0].getUrl());
        u.setTopArtist(t);
        topArtistsRepository.save(t);
        userRepository.save(u);
    }

    public void topTrack() throws IOException, SpotifyWebApiException, ParseException {
        User u = userRepository.findByUserName(user);
        GetUsersTopTracksRequest getTopTrack = spotifyAPI.getUsersTopTracks().limit(1).time_range("long_term").build();
        final Paging<Track> topTrack = getTopTrack.execute();
        Track[] a;
        a = topTrack.getItems();
        TopTrack t = new TopTrack();
        t.setName(a[0].getName());
        t.setImage(a[0].getAlbum().getImages()[0].getUrl());
        t.setArtist(a[0].getAlbum().getArtists()[0].getName());
        t.setPreview(a[0].getPreviewUrl());
        u.setTopTrack(t);
        topTrackRepository.save(t);
        userRepository.save(u);
    }
}

