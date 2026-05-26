package library.controller;

import library.model.Literature;
import library.services.LiteratureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/literature")
public class LiteratureController {

    private final LiteratureService service;

    public LiteratureController(LiteratureService service) {
        this.service = service;
    }

    @GetMapping
    public List<Literature> getAll() {
        return service.getAllLiterature();
    }

    @PostMapping
    public Literature create(@RequestBody Literature literature) {
        return service.saveLiterature(literature);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Literature> update(@PathVariable UUID id, @RequestBody Literature literature) {
        try {
            // Check, whether the book exists so we don't accidentally create a new one
            Literature existing = service.getLiteratureById(id);
            
            existing.setName(literature.getName());
            existing.setAuthor(literature.getAuthor());
            existing.setPublicationYear(literature.getPublicationYear());
            
            //If a subject reference was included in the request, update it as well
            if (literature.getSubject() != null) {
                existing.setSubject(literature.getSubject());
            }

            Literature updated = service.saveLiterature(existing);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteLiterature(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/by-subject/{subjectId}")
    public List<Literature> getBySubject(@PathVariable UUID subjectId) {
        return service.getLiteratureBySubjectId(subjectId);
    }
}