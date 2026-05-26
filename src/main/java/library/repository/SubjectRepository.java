package library.repository;

import library.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
	List<Subject> findByDepartmentId(UUID departmentId);
}