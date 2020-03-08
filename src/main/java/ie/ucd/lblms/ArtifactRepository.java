package ie.ucd.lblms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long>
{ 
    List<Artifact> findByTitleContaining(String title);
    List<Artifact> findByArtifactIdIn(List<Long> artifactId);
    List<Artifact> findByArtifactIdNotIn(List<Long> artifactId);

    List<Artifact> findByArtifactIdInAndTitleContaining(List<Long> artifactId, String title);
    List<Artifact> findByArtifactIdNotInAndTitleContaining(List<Long> artifactId, String title);
}
