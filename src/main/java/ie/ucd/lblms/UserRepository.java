package ie.ucd.lblms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsernameAndPassword(String username, String password);
    User findById(long id);
    List<User> findByUsernameContaining(String username);
}
