package library.controller;

import library.model.Subject;
import library.services.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID; // Не забудь добавить
import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<Subject> getAll() {
        return service.getAllSubjects();
    }

    @PostMapping
    public Subject create(@RequestBody Subject subject) {
        return service.saveSubject(subject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) { // UUID
        service.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/by-department/{depId}")
    public List<Subject> getByDepartment(@PathVariable UUID depId) {
        return service.getSubjectsByDepartmentId(depId);
    }
    
    @PutMapping("/{id}")
    public Subject update(@PathVariable UUID id, @RequestBody Subject subject) {
        subject.setId(id);
        return service.saveSubject(subject);
    }
}