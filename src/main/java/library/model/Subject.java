package library.model;
import java.util.UUID;

import jakarta.persistence.*;
@Entity
@Table(name = "subjects")
public class Subject {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
	
    @Column(nullable = false)
	private String name;
	private String syllabus; 
    private int credits; 
    
    public Subject(String name, String syllabus, int credits) {
        this.name = name; 
        this.syllabus = syllabus;
        this.credits = credits;
    }
    
    // An empty constructor. Spring Boot will definitely need this later, 
    // so that the framework can create this object when receiving JSON requests.
    public Subject() {
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
    
    public String getSyllabus() {
        return syllabus;
    }
    
    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    @Override
    public String toString() {
        return "Subject name: " + getName() + ", Syllabus: " + syllabus + ", Credits: " + credits;
    }

}
