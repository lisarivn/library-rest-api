package library.controller;
import library.model.Department;
import library.model.Library;
import library.services.LibraryService;
import library.services.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID; // Не забудь добавить
import java.util.List;

@RestController
@RequestMapping("/api/v1/libraries")
public class LibraryController {

	private final DepartmentService departmentService;
    private final LibraryService service;

    public LibraryController(LibraryService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<Library>> getLibraries() {
        return ResponseEntity.ok(service.getAllLibraries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getLibraryById(id));
    }
    
    @GetMapping("/{id}/departments")
    public ResponseEntity<List<Department>> getDepartmentsByLibrary(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getDepartmentsByLibraryId(id));
    }

    @PostMapping
    public ResponseEntity<Library> createLibrary(@RequestBody Library library) {
        Library savedLibrary = service.saveLibrary(library);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLibrary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable UUID id, @RequestBody Library library) { // UUID
        library.setId(id);
        return ResponseEntity.ok(service.saveLibrary(library));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLibrary(@PathVariable UUID id) {
        try {
            service.deleteLibrary(id); 
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body("Error deleting the library");
        }
    }
}