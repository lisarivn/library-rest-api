package library.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    private String role;
    
    public User (String username, String password, String role) {
    	this.username = username;
    	this.password = password;
    	this.role = role;
    }
    
    public User() {}
    
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public String getRole() {
    	return role;
    }
    
    public void setRole(String role) {
    	this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}