package library.model; 

import jakarta.persistence.*;
import java.util.UUID;

@Entity 
@Table(name = "departments") 
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "department_name")
    private String name;
    
    private String dean; 
    private boolean spec; 

    // ManyToOne "A single library may have many departments"
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    public Department() {
    }

    public Department(String name, String dean, boolean spec, Library library) {
        this.name = name;
        this.dean = dean;
        this.spec = spec;
        this.library = library;
    }
    
    // Getters and setters for fields
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDean() { return dean; }
    public void setDean(String dean) { this.dean = dean; }
    
    public boolean isSpec() { return spec; }
    public void setSpec(boolean spec) { this.spec = spec; }

    // Getters and setters for library
    public Library getLibrary() { return library; }
    public void setLibrary(Library library) { this.library = library; }

    @Override
    public String toString() {
        return "Department name: " + name + ", Dean: " + dean + ", Spec: " + spec;
    }
}