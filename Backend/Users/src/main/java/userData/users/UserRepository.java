package userData.users;

import org.springframework.data.jpa.repository.JpaRepository;
//import repositories.Stations.Station;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(int id);
    void deleteById(int id);

    boolean existsByUserName(String name);

    void deleteByUserName(String name);

    User findByUserName(String name);

    boolean existsById(int id);

}
