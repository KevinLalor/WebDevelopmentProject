package ie.ucd.lblms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long>
{
    Optional<Librarian> findLibrarianByUsernameAndPassword(String username, String password);
}

