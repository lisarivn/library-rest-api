package library.services;

import library.model.Department;
import library.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    // Get a list of all departments
    public List<Department> getAllDepartments() {
        return repository.findAll();
    }

    // Find a specific department by ID
    public Department getDepartmentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    // Save a new department or update an existing one
    public Department saveDepartment(Department department) {
        return repository.save(department);
    }

    public void deleteDepartment(UUID id) { 
        repository.deleteById(id);
    }
    
    public List<Department> getDepartmentsByLibraryId(UUID libraryId) {
        return repository.findByLibraryId(libraryId);
    }
}