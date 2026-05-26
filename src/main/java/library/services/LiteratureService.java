package library.services;

import library.model.Literature;
import library.repository.LiteratureRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID; // Добавлено

@Service
public class LiteratureService {
    private final LiteratureRepository repository;

    public LiteratureService(LiteratureRepository repository) {
        this.repository = repository;
    }

    public List<Literature> getAllLiterature() {
        return repository.findAll();
    }

    public Literature getLiteratureById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Literature not found with id: " + id));
    }

    public Literature saveLiterature(Literature literature) {
        return repository.save(literature);
    }

    public void deleteLiterature(UUID id) {
        repository.deleteById(id);
    }
    
    public List<Literature> getLiteratureBySubjectId(UUID subjectId) {
        return repository.findBySubjectId(subjectId);
    }
}