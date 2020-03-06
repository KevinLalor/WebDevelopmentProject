package ie.ucd.lblms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUsernameAndPassword(String username, String password);
    // Add query to find username and password here
    public List<Credentials> getCredentials(@Param("user") String username, @Param("password") String password);
}
