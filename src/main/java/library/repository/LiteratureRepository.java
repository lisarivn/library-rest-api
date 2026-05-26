package library.repository;
import java.util.List;
import library.model.Literature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface LiteratureRepository extends JpaRepository<Literature, UUID> {
	List<Literature> findBySubjectId(UUID subjectId);
}