package userData.trackCreation.Track;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long>{
    void deleteById(String id);
}