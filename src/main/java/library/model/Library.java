package library.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "libraries")
public class Library {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
	
	private String name;
	private int totalDepartments;
	
	@OneToMany(mappedBy = "library")
	private List<Department> departments;

	public int getTotalDepartments() {
	    return departments != null ? departments.size() : 0;
	}
	
	// An empty constructor. Spring Boot will definitely need this later, 
    // so that the framework can create this object when receiving JSON requests.
    public Library() {
    }
    
    // Full-fledged constructor
    public Library(String name, int totalDepartments) {
        this.name = name;
        this.totalDepartments = totalDepartments;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
	
    public void setName(String name) {
        this.name = name;
    }
    
	public void setTotalDepartments(int totalDepartments) {
        this.totalDepartments = totalDepartments;
    }
	
    
    @Override
    public String toString() {
        return "Library name: " + getName() + ", Total Departments: " + totalDepartments;
    }
    
}