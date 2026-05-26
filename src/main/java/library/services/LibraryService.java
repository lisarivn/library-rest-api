package library.services;

import library.model.Library;
import library.repository.LibraryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class LibraryService {
    private final LibraryRepository repository;

    public LibraryService(LibraryRepository repository) {
        this.repository = repository;
    }

    public List<Library> getAllLibraries() {
        return repository.findAll();
    }

    public Library getLibraryById(UUID id) { 
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Library not found with id: " + id));
    }

    public Library saveLibrary(Library library) {
        return repository.save(library);
    }

    public void deleteLibrary(java.util.UUID id) {
        repository.deleteById(id);
    }
}