package userData.trackCreation.TopFields;


import com.fasterxml.jackson.annotation.JsonIgnore;
import userData.users.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TopArtist {

    @GeneratedValue
    @Id
    int id;
    private String image;
    private String name;

    public TopArtist(){

    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
