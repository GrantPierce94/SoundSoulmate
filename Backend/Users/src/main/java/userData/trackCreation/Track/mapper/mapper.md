Here is a markdown file for the documentation of the `TrackMapper` and `ArtistMapper` classes:

---

# Documentation

### TrackMapper

The `TrackMapper` class is responsible for mapping and displaying track data.

**Imports:**

```java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.lang.reflect.Type;
```

**Static Methods:**

- `trackData(String jsonData)`: Extracts track data from a JSON string and prints it.

**Static Nested Classes:**

1. **Track**
    - Fields:
        - `id`: Unique identifier for the track.
        - `name`: Name of the track.
        - `album`: Album details for the track.
        - `artists`: List of artists associated with the track.
        - `previewUrl`: URL for the track preview.
    - Methods:
        - `getId()`: Returns the track ID.
        - `getName()`: Returns the track name.
        - `getAlbumName()`: Returns the album name.
        - `getArtist()`: Returns the primary artist's name or "Unknown Artist" if not available.
        - `getImage()`: Returns the URL of the image with dimensions 640x640 or "null" if not available.
        - `getPreviewUrl()`: Returns the preview URL for the track.

2. **Album**
    - Fields:
        - `images`: List of images associated with the album.
        - `name`: Name of the album.
    - Methods:
        - `getImages()`: Returns the list of images.
        - `getName()`: Returns the album name.

3. **Image**
    - Fields:
        - `height`: Height of the image.
        - `width`: Width of the image.
        - `url`: URL of the image.
    - Methods:
        - `getHeight()`: Returns the height of the image.
        - `getWidth()`: Returns the width of the image.
        - `getUrl()`: Returns the image URL.

---

### ArtistMapper

The `ArtistMapper` class is responsible for mapping and displaying artist data.

**Imports:**

```java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
```

**Static Methods:**

- `artistData(String data)`: Extracts artist data from a JSON string and prints it.

**Static Nested Classes:**

1. **Artist**
    - Fields:
        - `id`: Unique identifier for the artist.
        - `name`: Name of the artist.
    - Methods:
        - `getName()`: Returns the artist name.
        - `getId()`: Returns the artist ID.

---

You can save the above content to a `.md` file for better readability and presentation.