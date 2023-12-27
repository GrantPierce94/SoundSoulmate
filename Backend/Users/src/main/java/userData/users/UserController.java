package userData.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import userData.stations.Station;
import userData.trackCreation.TopFields.TopArtist;
import userData.trackCreation.TopFields.TopTrack;
import userData.trackCreation.Track.TrackRepository;
import userData.stations.StationRepository;
//import userData.trackCreation.Spotify.SpotifyController;
import userData.trackCreation.Track.Track;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import userData.authentication.AuthController;
import static userData.trackCreation.Spotify.SpotifyController.searchTrack;
//import static userData.trackCreation.Spotify.SpotifyController.searchTrack;

/**
 * Controller used for all user action requests
 */
@RestController
public class UserController {

    /**
     * Autowired repository for user data
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Autowired repository for station data
     */
    @Autowired
    StationRepository stationRepository;

    /**
     * Autowired repository for track data
     */
    @Autowired
    TrackRepository trackRepository;

    /**
     * @return list of all users in database
     */
    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Get mapping for getting user by id
     * @param username, the id of the user
     * @return json data of user of id
     */
    @GetMapping(path = "/user/{username}")
    public User getUserById( @PathVariable String  username){
        return userRepository.findByUserName(username);
    }

    /**
     * Get mapping for getting user by id
     * @param user the id of the user
     * @return json data of user of id
     */
    @GetMapping(path = "user/{user}/username")
    public String getUsername(@PathVariable String user){
        return userRepository.findByUserName(user).getUserName();
    }

    /**
     * Get mapping for getting user by id
     * @param name the id of the user
     * @param password the id of the user
     * @return json data of user of id
     * @throws IOException if user already exists
     */

    @PostMapping(path = "/user/{name}/{password}")
    public String createUser(@PathVariable String name, @PathVariable String password) throws IOException {
        if (userRepository.findByUserName(name) != null) {
            return null;
        }
        Random r = new Random();
        User u = new User();
        u.setId(Math.abs(r.nextInt() % 100000));
        Station s = new Station(u.getId() + 1); // same id as owner
        s.setTempo(50);
        s.setPopularity(50);
        s.setVolume(50);
        u.setUserName(name);
        u.setPassword(password);
        u.setStation(s);
        stationRepository.save(s);
        userRepository.save(u);
        return "success";
    }

    /**
     * Get mapping for getting user by id
     * @param username the id of the user
     * @return json data of user of id
     */
    @GetMapping(path = "/login/{username}")
    public String getUserId(@PathVariable String username){
        return String.valueOf(userRepository.findByUserName(username).getId());
    }

    /**
     * Get mapping for getting user by id
     * @param username the id of the user
     * @param song the id of the user
     */
    @PutMapping(path = "user/{username}/station/{song}")
    public void setStationSeed(@PathVariable String username, @PathVariable String song) throws IOException, SpotifyWebApiException, ParseException {
        Track t;
        User u = userRepository.findByUserName(username);
        try {
            t = searchTrack(song).get(0);
        } catch (NullPointerException E) {
            return;
        }
        u.getStation().setSeed(t);
        userRepository.save(u);
        trackRepository.save(t);
    }

    /**
     * Request for getting users station
     * @param username of user refered to.
     * @return station of user
     */
    @GetMapping(path = "user/{username}/station")
    public List<Track> getTracks(@PathVariable String username) throws IOException, SpotifyWebApiException, ParseException {
        return userRepository.findByUserName(username).getStation().generateTacks();
    }
    /**
     * Request for getting users station
     * @param username of user refered to.
     * @param tempo of the station
     */
    @PutMapping(path = "user/{username}/tempo/{tempo}")
    public void setTempo(@PathVariable String username, @PathVariable int tempo){
        User u = userRepository.findByUserName(username);
        Station s = u.getStation();
        s.setTempo(tempo);
        stationRepository.save(s);
        userRepository.save(u);
    }

    /**
     * Request for getting users station
     * @param username of user refered to.
     * @param pop of the station
     */
    @PutMapping(path = "user/{username}/popularity/{pop}")
    public void setPopularity(@PathVariable String username, @PathVariable int pop){
        User u = userRepository.findByUserName(username);
        Station s = u.getStation();
        s.setPopularity(pop);
        stationRepository.save(s);
        userRepository.save(u);
    }

    /**
     * Request for getting users station
     * @param username of user refered to.
     * @param vol of the station
     */
    @PutMapping(path = "user/{username}/volume/{vol}")
    public void setVolume(@PathVariable String username, @PathVariable int vol){
        User u = userRepository.findByUserName(username);
        Station s = u.getStation();
        s.setVolume(vol);
        stationRepository.save(s);
        userRepository.save(u);
    }

    /**
     * Request for getting users profile
     * @param username of user refered to.
     * @return profile of user
     */
    @GetMapping(path = "user/{username}/profile")
    public String getBio(@PathVariable String username){
        return userRepository.findByUserName(username).getProfileStr();
    }

    /**
     * Request for setting users profile bio
     * @param username of user refered to.
     * @param string of bio
     */
    @PutMapping(path = "user/{username}/profile/{string}")
    public void setBio(@PathVariable String username, @PathVariable String string){
        User u = userRepository.findByUserName(username);
        u.setProfileStr(string);
        userRepository.save(u);
    }

    /**
     * Request for setting users profile picture
     * @param username of user refered to.
     * @param string URL of the picture.
     */
    @PutMapping(path = "user/{username}/picture/{string}")
    public void setPfp(@PathVariable String username, @PathVariable String string){
        User u = userRepository.findByUserName(username);
        u.setProfilePicture(string);
        userRepository.save(u);
    }

    /**
     * Request for getting users profile picture
     * @param username of user refered to.
     * @return URL of the picture.
     */
    @GetMapping(path = "user/{username}/picture")
    public String getPfp(@PathVariable String username){
        return userRepository.findByUserName(username).getProfilePicture();
    }

    /**
     * Request for logging in
     * @param username of user refered to.
     * @param password of user refered to.
     * @return true if user exists and password is correct, false otherwise
     */
    @GetMapping(path = "user/{username}/{password}")
    public String loginAuth(@PathVariable String username, @PathVariable String password){
        User u = userRepository.findByUserName(username);
        if(u == null){
            return "error: user not found";
        }
        else if(u.getPassword().equals(password)){
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Request for adding a friend
     * @param user1 of user refered to.
     * @param user2 of the friend
     */
    @PutMapping(path = "add/{user1}/{user2}")
    public void addFriend(@PathVariable String user1, @PathVariable String user2){
        User u1 = userRepository.findByUserName(user1);
        User u2 = userRepository.findByUserName(user2);
        if(u1 == null || u2 == null){
            return;
        }
        u1.addFriend(u2);
        u2.addFriend(u1);
        userRepository.save(u1);
        userRepository.save(u2);
    }

    /**
     * Request for removing a friend
     * @param user1 of user refered to.
     * @param user2 of the friend
     */
    @PutMapping(path = "remove/{user1}/{user2}")
    public void removeFriend(@PathVariable String user1, @PathVariable String user2){
        User u1 = userRepository.findByUserName(user1);
        User u2 = userRepository.findByUserName(user2);
        if(u1 == null || u2 == null){
            return;
        }
        u1.removeFreind(u2);
        u2.removeFreind(u1);
        userRepository.save(u1);
        userRepository.save(u2);
    }

    /**
     * Request for getting a users friends
     * @param username of user refered to.
     * @return list of friends
     */
    @GetMapping(path = "friends/{username}")
    public List<String> getFriendUserNames(@PathVariable String username){
        List<String> names = new ArrayList<String>();
        User u = userRepository.findByUserName(username);
        if(u == null) {
            return null;
        }
        for(User E : u.getFriends()){
            names.add(E.getUserName());
        }
        return  names;
    }

     @GetMapping(path = "friendslist/{username}")
    List<User> getFriends(@PathVariable String username){
        User u = userRepository.findByUserName(username);
        return u.getFriends();
    }

    /**
     * Request for getting a Track
     * @param name Track song
     * @return track
     */
    @GetMapping(path = "song/{name}")
    public Track getSong(@PathVariable String name) throws IOException, SpotifyWebApiException, ParseException {
        Track t;
        t = searchTrack(name).get(0);
        return t;
    }

    @GetMapping(path="user/{username}/topTrack")
    public TopTrack getTopTrack(@PathVariable String username){
        return userRepository.findByUserName(username).getTopTrack();
    }

    @GetMapping(path="user/{username}/topArtist")
    public TopArtist getTopArist(@PathVariable String username){
        return userRepository.findByUserName(username).getTopArtist();
    }

    /**
     * Request for deleting a user
     * @param id of user refered to.
     * @return success if user exists, failure otherwise
     */
    @Transactional
    @DeleteMapping(path = "/user/{id}")
    public String deleteUser(@PathVariable int id){
        if(userRepository.existsById(id)){
            User u = userRepository.findById(id);
            Station s = u.getStation();
            Track t = s.getSeed();
            s.setSeed(null);
            u.setStation(null);

            trackRepository.deleteById(t.getId());
            stationRepository.deleteById(s.getId());
            userRepository.deleteById(id);
            return "success";
        } else {
            return "failure";
        }
    }
}

