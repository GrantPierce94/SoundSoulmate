package userData.stations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    Station findById(int id);
    void deleteById(int id);
    boolean existsById(int id);

}
