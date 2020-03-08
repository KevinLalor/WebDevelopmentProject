package ie.ucd.lblms;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>
{
    List<Loan> findByUserId(Long userId);
    List<Loan> findByArtifactId(Long artifactId);
    List<Loan> findByArtifactIdIn(List<Long> artifactId);

}
