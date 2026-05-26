package library.model;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "literature")
public class Literature {

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	public Subject getSubject() { return subject; }
	public void setSubject(Subject subject) { this.subject = subject; }
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
	private String name;
    private String author;
    @Column(name = "publication_year")
    private int publicationYear;

    // An empty constructor. Spring Boot will definitely need this later, 
    // so that the framework can create this object when receiving JSON requests.
    public Literature() {}

    public Literature(String name, String author, int publicationYear) {
    	this.name = name;
        this.author = author;
        this.publicationYear = publicationYear;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return "Title: " + getName() + ", Author: " + author + ", Publication Year: " + publicationYear;
    }

}