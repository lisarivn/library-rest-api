package library.services;

import library.model.Subject;

import library.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID; // Добавлено

@Service
public class SubjectService {
    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public List<Subject> getAllSubjects() {
        return repository.findAll();
    }

    public Subject getSubjectById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    public Subject saveSubject(Subject subject) {
        return repository.save(subject);
    }

    public void deleteSubject(UUID id) { 
        repository.deleteById(id);
    }
    
    public List<Subject> getSubjectsByDepartmentId(UUID departmentId) { 
        return repository.findByDepartmentId(departmentId);
    }
}