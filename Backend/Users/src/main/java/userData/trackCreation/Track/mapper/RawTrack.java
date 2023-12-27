package userData.trackCreation.Track.mapper;
import java.util.List;

/**
 * Raw Track Mapper for preprocessing raw JSON data
 */
public class RawTrack {

    /* Track ID */
    public String id;

    /* Track name */
    public String name;

    /* Album name that track is in */
    public Album album;

    /* Artist name */
    public List<Artist> artists;

    /* Preview URL */
    public String previewUrl;

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
    public String getAlbumName() {
        return album.getName();
    }

    /**
     * Returns the main artist
     * If artist are not null and the artists array is not empty
     * return the name of the artists, else return "Unknown Artist".
     * @return artist
     */
    public String getArtist() {
        return  artists != null && !artists.isEmpty() ?
                artists.get(0).getName() : "Unknown Artist";
    }

    /**
     * Gets the highest quality cover art of the track
     * @return image
     */
    public String getImage() {
        for(AlbumImage i : album.getImages()) {
            if(i.getWidth() == 640 && i.getHeight() == 640) {
                return i.getUrl();
            }
        }
        return "null";
    }

    /**
     * Gets the preview url
     * @return previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * Nested Album Class
     */
    public static class Album {

        /* Returns a list of images */
        public List<AlbumImage> images;

        /* Returns the name of the album */
        public String name;

        /* Returns a list of images */
        public List<AlbumImage> getImages() {
            return images;
        }

        /* Returns the name of the album */
        public String getName() {
            return name;
        }
    }

    /**
     * Nested AlbumImage class
     * Required as Spotify returns an array of Images
     */
    public static class AlbumImage {

        /* Store image height */
        public int height;

        /* Store image width */
        public int width;

        /* Store image url */
        public String url;

        /**
         * gets the height of the image
         * @return height
         */
        public int getHeight() {
            return height;
        }

        /**
         * Gets the width of the image
         * @return width
         */
        public int getWidth() {
            return width;
        }

        /**
         * Gets the image url
         * @return url
         */
        public String getUrl() {
            return url;
        }

    }

    /** Nested Artist class
     * Required for RawTrack as Spotify returns an array of artists
     * If there is more than one artist.
     */
    public static class Artist {

        /* Artist ID */
        public String id;

        /* Artist name */
        public String name;

        /**
         * Gets the name of the artist
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the name of the ID
         * @return id;
         */
        public String getId() {
            return id;
        }
    }

}
