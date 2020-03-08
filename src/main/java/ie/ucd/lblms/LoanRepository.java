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

    @Modifying
    @Query ("UPDATE LOANS SET return_date = DateAdd(ww, 2, return_date) WHERE loan_id = :loanId")
    int setFixedReturnDateFor(Long loanId); 
}
