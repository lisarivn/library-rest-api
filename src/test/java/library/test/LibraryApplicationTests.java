package library.test;

import library.controller.LibraryController;
import library.model.Library;
import library.services.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional 
class LibraryApplicationTests {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibraryController libraryController;

    // Check that the Spring Context loads and components are injected
    @Test
    void contextLoads() {
        assertNotNull(libraryService, "LibraryService should be created and injected");
        assertNotNull(libraryController, "LibraryController should be created and injected");
    }

    // Unit test for model logic
    @Test
    void testLibraryModelLogic() {
    	
    	// Check initial state
        Library library = new Library("Test Library", 5);
        assertEquals("Test Library", library.getName(), "Library name should be match the expected value ");
        assertEquals(5, library.getTotalDepartments(), "Total departments should be 5");

        // Change state
        library.setTotalDepartments(10);

        // Check the change
        assertEquals(10, library.getTotalDepartments(), "Total departments should be 10 after update");
    }

    // Integration test interacting with the database
    @Test
    void testRealDatabaseInsertAndRead() {
    	// Create a library with a unique name
        String uniqueName = "Integration Test Lib " + System.currentTimeMillis();
        
        Library lib = new Library();
        lib.setName(uniqueName);
        lib.setTotalDepartments(3);
        
        // Save to DB
        libraryService.saveLibrary(lib);
        // Fetch all libraries from DB
        List<Library> allLibraries = libraryService.getAllLibraries();

        // Search for our library in the list
        boolean exists = allLibraries.stream()
                .anyMatch(l -> uniqueName.equals(l.getName()));

        // Assert that it was found
        assertTrue(exists, "The saved library should be exist in the database");
    }
    
}