package userData;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static userData.chat.Chat.chatSession;
import static userData.trackCreation.Spotify.SpotifyController.*;

import org.apache.hc.core5.http.ParseException;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import userData.chat.Chat;
import userData.chat.ChatRoom.ChatRoom;
import userData.chat.ChatRoom.ChatRoomRepository;
import userData.chat.ChatRoom.Message;
import userData.stations.Station;
import userData.stations.StationRepository;
import userData.trackCreation.Spotify.SpotifyController;
import userData.trackCreation.TopFields.TopArtist;
import userData.trackCreation.TopFields.TopTrack;
import userData.trackCreation.Track.Track;
import userData.trackCreation.Track.TrackRepository;
import userData.trackCreation.Track.mapper.RawTrack;
import userData.trackCreation.Track.mapper.TrackMapper;
import userData.users.User;
import userData.users.UserController;
import userData.users.UserRepository;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void spotifyLoginTest() {
        Response r = given()
                .header("Content-Type", "text/plain")
                .header("charset", "utf-8")
                .when()
                .get("login");

        int statusCode = r.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void spotifyRegisterTest() {
        Response r = given()
                .header("Content-Type", "text/plain")
                .header("charset", "utf-8")
                .when()
                .get("register/user");

        int statusCode = r.getStatusCode();
        assertEquals(200, statusCode);
    }


    @Test
    public void station_creation() {
        Track t = null;
        Station s = new Station(1);
        s.setSeed(t);
        s.setPopularity(10);
        s.setTempo(10);
        s.setVolume(10);
        s.setId(2);

        assertNull(s.getSeed());
        assertEquals(10, s.getTempo());
        assertEquals(10, s.getVolume());
        assertEquals(10, s.getPopularity());
        assertEquals(2, s.getId());
    }

    @Test
    public void user_creation() {
        User u = new User(1, "1", "1", "1", "1");
        u.setPassword("1");
        u.setPlaylistId("1");
        u.setUuid("1");
        u.setName("1");
        u.setProfileStr("1");
        u.setAccessKey("1");
        u.setStation(null);
        u.setTopTrack(null);
        u.setTopArtist(null);
        u.setId(1);
        u.setUserName("1");
        u.setProfilePicture("1");

        assertEquals("1", u.getUserName());
        assertEquals(1, u.getId());
        assertEquals("1", u.getPlaylistId());
        assertEquals("1", u.getAccessKey());
        assertEquals("1", u.getName());
        assertEquals("1", u.getProfilePicture());
        assertEquals("1", u.getUuid());
        assertEquals("1", u.getProfileStr());
        assertEquals("1", u.getPassword());
        assertNull(u.getTopArtist());
        assertNull(u.getTopTrack());
        assertNull(u.getFriends());
        assertNull(u.getStation());
    }

    @Test
    public void testTrack(){
        Track t = new Track("id", "song", "album", "artist", "image", "preview");
        assertEquals("id", t.getId());
        assertEquals("song", t.getName());
        assertEquals("album", t.getAlbum());
        assertEquals("artist", t.getArtist());
        assertEquals("image", t.getImage());
        assertEquals("preview", t.getPreview());
    }

    @Test
    public void testMessage(){
        Message m = new Message("tom", "hi");
        assertEquals("hi", m.getContent());
        assertEquals("tom", m.getUser());
        m.setId(1);
        assertEquals(1,m.getId());
    }

    @Test
    public void testChatRoom(){
        ChatRoom c = new ChatRoom(1, "tom", "bob");
        assertEquals(1, c.getId());
        assertEquals("tom", c.getUserOne());
        assertEquals("bob", c.getUserTwo());
        c.setUserOne("bob");
        c.setUserTwo("tom");
        assertEquals("bob", c.getUserOne());
        assertEquals("tom", c.getUserTwo());
        c.setId(1);
        assertEquals(1, c.getId());

    }

    @Test
    public void TestTopTracks() {
        TopTrack t = new TopTrack();
        t.setImage("1");
        t.setId(1);
        t.setName("1");
        t.setArtist("1");
        t.setPreview("1");

        assertEquals("1", t.getArtist());
        assertEquals("1", t.getName());
        assertEquals("1", t.getPreview());
        assertEquals("1", t.getImage());
        assertEquals(1, t.getId());
    }

    @Test
    public void testGetId() {
        RawTrack track = new RawTrack();
        track.id = "123";
        assertEquals("123", track.getId());
    }

    @Test
    public void testGetName() {
        RawTrack track = new RawTrack();
        track.name = "Test Track";
        assertEquals("Test Track", track.getName());
    }

    @Test
    public void testGetAlbumName() {
        RawTrack track = new RawTrack();
        RawTrack.Album album = new RawTrack.Album();
        album.name = "Test Album";
        track.album = album;
        assertEquals("Test Album", track.getAlbumName());
    }

    @Test
    public void testGetArtist() {
        RawTrack track = new RawTrack();
        RawTrack.Artist artist = new RawTrack.Artist();
        artist.name = "Test Artist";
        track.artists = Collections.singletonList(artist);
        assertEquals("Test Artist", track.getArtist());
    }

    @Test
    public void testGetArtistUnknown() {
        RawTrack track = new RawTrack();
        track.artists = Collections.emptyList();
        assertEquals("Unknown Artist", track.getArtist());
    }

    @Test
    public void testGetImage() {
        RawTrack track = new RawTrack();
        RawTrack.Album album = new RawTrack.Album();
        RawTrack.AlbumImage image = new RawTrack.AlbumImage();
        image.width = 640;
        image.height = 640;
        image.url = "http://example.com/image.png";
        album.images = Collections.singletonList(image);
        track.album = album;
        assertEquals("http://example.com/image.png", track.getImage());
    }

    @Test
    public void testGetImageNull() {
        RawTrack track = new RawTrack();
        RawTrack.Album album = new RawTrack.Album();
        album.images = Collections.emptyList();
        track.album = album;
        assertEquals("null", track.getImage());
    }

    @Test
    public void testGetPreviewUrl() {
        RawTrack track = new RawTrack();
        track.previewUrl = "http://example.com/preview.mp3";
        assertEquals("http://example.com/preview.mp3", track.getPreviewUrl());
    }

    @Test
    public void testTrackMapper() {
        // Sample JSON data mimicking Spotify API response
        String sampleJson = "[{\"id\":\"1\",\"name\":\"Track 1\",\"album\":{\"name\":\"Album 1\",\"images\":[{\"height\":640,\"width\":640,\"url\":\"http://example.com/image1.png\"}]},\"artists\":[{\"id\":\"A1\",\"name\":\"Artist 1\"}],\"previewUrl\":\"http://example.com/preview1.mp3\"}, {\"id\":\"2\",\"name\":\"Track 2\",\"album\":{\"name\":\"Album 2\",\"images\":[{\"height\":640,\"width\":640,\"url\":\"http://example.com/image2.png\"}]},\"artists\":[{\"id\":\"A2\",\"name\":\"Artist 2\"}],\"previewUrl\":\"http://example.com/preview2.mp3\"}]";

        // Convert JSON to Track objects
        List<Track> tracks = TrackMapper.trackData(sampleJson);

        // Assertions
        assertEquals(2, tracks.size());

        // Test first track
        Track track1 = tracks.get(0);
        assertEquals("1", track1.getId());
        assertEquals("Track 1", track1.getName());
        assertEquals("Album 1", track1.getAlbum());
        assertEquals("Artist 1", track1.getArtist());
        assertEquals("http://example.com/image1.png", track1.getImage());
        assertEquals("http://example.com/preview1.mp3", track1.getPreview());
    }

    @Test
    public void testSetAndGetId() {
        TopArtist artist = new TopArtist();
        artist.setId(123);
        assertEquals(123, artist.getId());
    }

    @Test
    public void testSetAndGetName() {
        TopArtist artist = new TopArtist();
        artist.setName("Artist Name");
        assertEquals("Artist Name", artist.getName());
    }

    @Test
    public void testSetAndGetImage() {
        TopArtist artist = new TopArtist();
        artist.setImage("http://example.com/image.png");
        assertEquals("http://example.com/image.png", artist.getImage());
    }

    @Mock
    private ChatRoomRepository mockChatRoomRepository;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setup_two() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnOpenNewUser() throws IOException {
        // Assuming chatRepo and userRepo are mocked

        Session mockSession = mock(Session.class);
        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(mockSession.getBasicRemote()).thenReturn(mockRemote);

        Chat chat = new Chat();
        chat.setChatRoomRepository(mockChatRoomRepository);
        chat.setUserRepository(mockUserRepository);

        chat.onOpen(mockSession, "testUser", "testFriend");

    }

    @Test
    public void testOnOpenDuplicateUsername() throws IOException {
        // setup
        Session mockSession = mock(Session.class);
        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(mockSession.getBasicRemote()).thenReturn(mockRemote);

        Chat chat = new Chat();
        chat.setChatRoomRepository(mockChatRoomRepository);
        chat.setUserRepository(mockUserRepository);

        // Simulate existing user
        chat.onOpen(mockSession, "existingUser", "friend");

        // act
        chat.onOpen(mockSession, "existingUser", "anotherFriend");

        // assert
        verify(mockSession).close();
    }

    @Test
    public void testOnClose() throws IOException {
        // setup
        Session mockSession = mock(Session.class);

        Chat chat = new Chat();
        chat.setChatRoomRepository(mockChatRoomRepository);
        chat.setUserRepository(mockUserRepository);

        // Simulate user connection
        chat.onOpen(mockSession, "user", "friend");

        // act
        chat.onClose(mockSession);

        // assert
        assertThat(chatSession.containsKey(mockSession), is(false));
    }

    @Test
    public void testOnError() {
        // setup
        Session mockSession = mock(Session.class);
        Throwable mockThrowable = mock(Throwable.class);

        Chat chat = new Chat();
        chat.setChatRoomRepository(mockChatRoomRepository);
        chat.setUserRepository(mockUserRepository);

        // act
        chat.onError(mockSession, mockThrowable);

        // assert
        // Verify logger or other error handling logic
    }

    @Test
    public void testOnMessageToSelf() throws IOException {
        // setup
        Session mockSession = mock(Session.class);
        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(mockSession.getBasicRemote()).thenReturn(mockRemote);

        Chat chat = new Chat();
        chat.setChatRoomRepository(mockChatRoomRepository);
        chat.setUserRepository(mockUserRepository);

        chat.onOpen(mockSession, "user", "friend");

        // act
        chat.onMessage(mockSession, "Hello");

        // assert
        verify(mockRemote).sendText("user: Hello");
    }

    @Test
    public void testOnMessageToFriend() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Setup
        Session mockUserSession = mock(Session.class);
        Session mockFriendSession = mock(Session.class);
        RemoteEndpoint.Basic mockUserRemote = mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic mockFriendRemote = mock(RemoteEndpoint.Basic.class);
        when(mockUserSession.getBasicRemote()).thenReturn(mockUserRemote);
        when(mockFriendSession.getBasicRemote()).thenReturn(mockFriendRemote);

        ChatRoom mockChatRoom = mock(ChatRoom.class);
        when(mockChatRoomRepository.existsById(anyInt())).thenReturn(true);
        when(mockChatRoomRepository.findById(anyInt())).thenReturn(mockChatRoom);

        Chat chatUser = new Chat();
        chatUser.setChatRoomRepository(mockChatRoomRepository);
        chatUser.setUserRepository(mockUserRepository);

        Chat chatFriend = new Chat();
        chatFriend.setChatRoomRepository(mockChatRoomRepository);
        chatFriend.setUserRepository(mockUserRepository);

        // Inject static maps
        Field chatSessionField = Chat.class.getDeclaredField("chatSession");
        Field searchChatField = Chat.class.getDeclaredField("searchChat");
        chatSessionField.setAccessible(true);
        searchChatField.setAccessible(true);
        Map<Session, String> chatSession = new Hashtable<>();
        Map<String, Session> searchChat = new Hashtable<>();
        chatSessionField.set(null, chatSession); // Assuming these are static fields
        searchChatField.set(null, searchChat);

        // Simulate both users opening a chat session
        chatUser.onOpen(mockUserSession, "user", "friend");
        chatFriend.onOpen(mockFriendSession, "friend", "user");

        // Act
        chatUser.onMessage(mockUserSession, "Hello");

        // Assert
        verify(mockUserRemote).sendText("user: Hello");
        verify(mockFriendRemote).sendText("user: Hello");
    }

    @Mock
    UserRepository userRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    TrackRepository trackRepository;

    @InjectMocks
    UserController userController;

    @Test
    public void getAllUsers_ShouldReturnUserList() {
        List<User> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userController.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository).findAll();
    }

    @Test
    public void getUserById_ShouldReturnUser_WhenUserExists() {
        String username = "testUser";
        User expectedUser = new User();
        when(userRepository.findByUserName(username)).thenReturn(expectedUser);

        User actualUser = userController.getUserById(username);

        assertEquals(expectedUser, actualUser);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {
        String username = "nonExistentUser";
        when(userRepository.findByUserName(username)).thenReturn(null);

        User actualUser = userController.getUserById(username);

        assertNull(actualUser);
        verify(userRepository).findByUserName(username);
    }


    @Test
    public void createUser_ShouldReturnSuccess_WhenUserDoesNotExist() throws IOException {
        String username = "newUser";
        String password = "password";
        when(userRepository.findByUserName(username)).thenReturn(null);

        String result = userController.createUser(username, password);

        assertEquals("success", result);
        verify(userRepository).findByUserName(username);
        verify(userRepository).save(any(User.class));
        verify(stationRepository).save(any(Station.class));
    }

    @Test
    public void createUser_ShouldReturnNull_WhenUserAlreadyExists() throws IOException {
        String username = "existingUser";
        String password = "password";
        when(userRepository.findByUserName(username)).thenReturn(new User());

        String result = userController.createUser(username, password);

        assertNull(result);
        verify(userRepository).findByUserName(username);
        verify(userRepository, never()).save(any(User.class));
        verify(stationRepository, never()).save(any(Station.class));
    }

    @Test
    public void getUserId_ShouldReturnUserId_WhenUserExists() {
        String username = "testUser";
        User user = new User();
        user.setId(123);
        when(userRepository.findByUserName(username)).thenReturn(user);

        String userId = userController.getUserId(username);

        assertEquals("123", userId);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void deleteUser_ShouldReturnFailure_WhenUserDoesNotExist() {
        int userId = 123;
        when(userRepository.existsById(userId)).thenReturn(false);

        String result = userController.deleteUser(userId);

        assertEquals("failure", result);
        verify(userRepository, never()).deleteById(anyInt());
        verify(stationRepository, never()).deleteById(anyInt());
        verify(trackRepository, never()).deleteById((long) anyInt());
    }

    @Test
    public void setTempo_ShouldUpdateTempo_WhenUserExists() {
        String username = "testUser";
        int newTempo = 70;
        User user = new User();
        Station station = new Station();
        user.setStation(station);
        when(userRepository.findByUserName(username)).thenReturn(user);

        userController.setTempo(username, newTempo);

        assertEquals(newTempo, station.getTempo());
        verify(userRepository).findByUserName(username);
        verify(stationRepository).save(station);
        verify(userRepository).save(user);
    }

    @Test
    public void setPopularity_ShouldUpdatePopularity_WhenUserExists() {
        String username = "testUser";
        int newPopularity = 80;
        User user = new User();
        Station station = new Station();
        user.setStation(station);
        when(userRepository.findByUserName(username)).thenReturn(user);

        userController.setPopularity(username, newPopularity);

        assertEquals(newPopularity, station.getPopularity());
        verify(userRepository).findByUserName(username);
        verify(stationRepository).save(station);
        verify(userRepository).save(user);
    }

    @Test
    public void setVolume_ShouldUpdateVolume_WhenUserExists() {
        String username = "testUser";
        int newVolume = 60;
        User user = new User();
        Station station = new Station();
        user.setStation(station);
        when(userRepository.findByUserName(username)).thenReturn(user);

        userController.setVolume(username, newVolume);

        assertEquals(newVolume, station.getVolume());
        verify(userRepository).findByUserName(username);
        verify(stationRepository).save(station);
        verify(userRepository).save(user);
    }

    @Test
    public void getBio_ShouldReturnUserBio_WhenUserExists() {
        String username = "testUser";
        String bio = "User biography";
        User user = new User();
        user.setProfileStr(bio);
        when(userRepository.findByUserName(username)).thenReturn(user);

        String returnedBio = userController.getBio(username);

        assertEquals(bio, returnedBio);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void setBio_ShouldUpdateBio_WhenUserExists() {
        String username = "testUser";
        String newBio = "Updated biography";
        User user = new User();
        when(userRepository.findByUserName(username)).thenReturn(user);

        userController.setBio(username, newBio);

        assertEquals(newBio, user.getProfileStr());
        verify(userRepository).findByUserName(username);
        verify(userRepository).save(user);
    }

    @Test
    public void setPfp_ShouldUpdateProfilePicture() {
        String username = "testUser";
        String pictureUrl = "http://example.com/pic.jpg";
        User user = new User();
        when(userRepository.findByUserName(username)).thenReturn(user);

        userController.setPfp(username, pictureUrl);

        assertEquals(pictureUrl, user.getProfilePicture());
        verify(userRepository).findByUserName(username);
        verify(userRepository).save(user);
    }

    @Test
    public void getPfp_ShouldReturnProfilePictureUrl() {
        String username = "testUser";
        String pictureUrl = "http://example.com/pic.jpg";
        User user = new User();
        user.setProfilePicture(pictureUrl);
        when(userRepository.findByUserName(username)).thenReturn(user);

        String result = userController.getPfp(username);

        assertEquals(pictureUrl, result);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void loginAuth_ShouldReturnTrue_WhenCredentialsAreCorrect() {
        String username = "testUser";
        String password = "password";
        User user = new User();
        user.setPassword(password);
        when(userRepository.findByUserName(username)).thenReturn(user);

        String result = userController.loginAuth(username, password);

        assertEquals("true", result);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void removeFriend_ShouldDoNothing_WhenOneUserDoesNotExist() {
        // Setup
        String existingUserName = "existingUser";
        String nonExistingUserName = "nonExistingUser";
        User existingUser = new User();

        when(userRepository.findByUserName(existingUserName)).thenReturn(existingUser);
        when(userRepository.findByUserName(nonExistingUserName)).thenReturn(null);

        // Execute
        userController.removeFriend(existingUserName, nonExistingUserName);

        // Assert
        verify(userRepository).findByUserName(existingUserName);
        verify(userRepository).findByUserName(nonExistingUserName);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void getTopTrack_ShouldReturnTopTrackOfUser() {
        String username = "testUser";
        TopTrack topTrack = new TopTrack();
        User user = new User();
        user.setTopTrack(topTrack);
        when(userRepository.findByUserName(username)).thenReturn(user);

        TopTrack result = userController.getTopTrack(username);

        assertEquals(topTrack, result);
        verify(userRepository).findByUserName(username);
    }

    @Test
    public void getTopArtist_ShouldReturnTopArtistOfUser() {
        String username = "testUser";
        TopArtist topArtist = new TopArtist();
        User user = new User();
        user.setTopArtist(topArtist);
        when(userRepository.findByUserName(username)).thenReturn(user);

        TopArtist result = userController.getTopArist(username);
    }

    @Mock
    private SpotifyApi test_api = new SpotifyApi.Builder()
            .setClientId("ae02bde4d6ef4bc395502d8f76e38f04")
            .setClientSecret("0d0a994ae7f842feb33dfa163b56bacd")
            .build();

    @InjectMocks
    private SpotifyController spotifyController;

    @Test
    public void testSearchTrack() throws IOException, SpotifyWebApiException, ParseException {
        // Mock the response of the Spotify API for searchItem
        List<Track> tracks = searchTrack("testTrack");
        assertNotNull(tracks);
    }

    @Test
    public void testRecommendations() throws IOException, SpotifyWebApiException, ParseException {
        List<Track> tracks = getRecommendations("11dFghVXANMlKmJXsNCbNl", 1);
        assertNotNull(tracks);
    }

    @Test
    public void testAnalysis() throws IOException, SpotifyWebApiException, ParseException {
        List<String> analysis = trackAnalysis("11dFghVXANMlKmJXsNCbNl");
        assertNotNull(analysis);
    }

    @Test
    public void testFeatures() throws IOException, SpotifyWebApiException, ParseException {
        List<String> features = trackFeatures("11dFghVXANMlKmJXsNCbNl");
        assertNotNull(features);
    }
}