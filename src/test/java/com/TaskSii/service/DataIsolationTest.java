package com.TaskSii.service;

import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.*;
import com.TaskSii.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DataIsolationTest {

    @Autowired
    private CollectionBoxService collectionBoxService;
    
    @Autowired
    private FundraisingEventService fundraisingEventService;
    
    @Autowired
    private VolunteerService volunteerService;
    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OwnerProfileRepository ownerProfileRepository;
    
    @Autowired
    private FundraisingEventRepository fundraisingEventRepository;
    
    @Autowired
    private CollectionBoxRepository collectionBoxRepository;
    
    @Autowired
    private VolunteerRepository volunteerRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private OwnerProfile owner1;
    private OwnerProfile owner2;
    private FundraisingEvent event1;
    private FundraisingEvent event2;
    private CollectionBox box1;
    private CollectionBox box2;
    private Volunteer volunteer1;
    private Volunteer volunteer2;

    @BeforeEach
    void setUp() {
        // Clear roles to prevent constraint violation on repeated test runs
        roleRepository.deleteAll();
        userRepository.deleteAll();
        ownerProfileRepository.deleteAll();
        
        // Create test data
        setupTestData();
    }

    private void setupTestData() {
        // Create roles
        Role ownerRole = new Role();
        ownerRole.setRole(ERole.ROLE_ADMIN);
        roleRepository.save(ownerRole);

        // Create users and owner profiles
        User user1 = createUser("owner1@test.com", "password");
        User user2 = createUser("owner2@test.com", "password");
        
        owner1 = createOwnerProfile(user1, "Organization 1", "1234567890");
        owner2 = createOwnerProfile(user2, "Organization 2", "0987654321");
        
        // Create fundraising events
        event1 = createFundraisingEvent("Event 1", Currency.PLN, owner1);
        event2 = createFundraisingEvent("Event 2", Currency.EUR, owner2);
        
        // Create collection boxes
        box1 = createCollectionBox(event1);
        box2 = createCollectionBox(event2);
        
        // Create volunteers
        volunteer1 = createVolunteer("Volunteer 1", "vol1@test.com", owner1);
        volunteer2 = createVolunteer("Volunteer 2", "vol2@test.com", owner2);
    }

    @Test
    void testCollectionBoxDataIsolation() {
        // Get user1 ID for testing
        User user1 = userRepository.findByEmail("owner1@test.com").orElseThrow();
        
        // Owner1 should only see their own collection boxes
        List<CollectionBox> owner1Boxes = collectionBoxService.getAllBoxesForOwner(user1.getUser_id());
        assertEquals(1, owner1Boxes.size());
        assertEquals(box1.getId(), owner1Boxes.get(0).getId());
        
        // Owner1 should not be able to delete owner2's box
        assertThrows(ResourceNotFoundException.class, () -> 
            collectionBoxService.deleteBoxForOwner(box2.getId(), user1.getUser_id())
        );
        
        // Owner1 should not be able to get owner2's box
        assertThrows(ResourceNotFoundException.class, () -> 
            collectionBoxService.getBoxByIdForOwner(box2.getId(), user1.getUser_id())
        );
    }

    @Test
    void testFundraisingEventDataIsolation() {
        // Get user1 ID for testing
        User user1 = userRepository.findByEmail("owner1@test.com").orElseThrow();
        
        // Owner1 should only see their own events
        List<FundraisingEvent> owner1Events = fundraisingEventService.getAllEventsForOwner(user1.getUser_id());
        assertEquals(1, owner1Events.size());
        assertEquals(event1.getId(), owner1Events.get(0).getId());
    }

    @Test
    void testVolunteerDataIsolation() {
        // Get user1 ID for testing
        User user1 = userRepository.findByEmail("owner1@test.com").orElseThrow();
        
        // Owner1 should only see their own volunteers
        List<VolunteerResponseDTO> owner1Volunteers = volunteerService.listByOwner(user1.getUser_id());
        assertEquals(1, owner1Volunteers.size());
        assertEquals(volunteer1.getId(), owner1Volunteers.get(0).id());
        
        // Owner1 should not be able to get owner2's volunteer
        assertThrows(ResourceNotFoundException.class, () -> 
            volunteerService.getByIdForOwner(volunteer2.getId(), user1.getUser_id())
        );
        
        // Owner1 should not be able to delete owner2's volunteer
        assertThrows(ResourceNotFoundException.class, () -> 
            volunteerService.deleteForOwner(volunteer2.getId(), user1.getUser_id())
        );
    }

    @Test
    void testCrossOwnerAccessDenied() {
        // Get user1 ID for testing
        User user1 = userRepository.findByEmail("owner1@test.com").orElseThrow();
        
        // Owner1 should not be able to access owner2's resources
        assertThrows(ResourceNotFoundException.class, () -> 
            collectionBoxService.deleteBoxForOwner(box2.getId(), user1.getUser_id())
        );
        
        assertThrows(ResourceNotFoundException.class, () -> 
            collectionBoxService.getBoxByIdForOwner(box2.getId(), user1.getUser_id())
        );
        
        assertThrows(ResourceNotFoundException.class, () -> 
            volunteerService.getByIdForOwner(volunteer2.getId(), user1.getUser_id())
        );
        
        assertThrows(ResourceNotFoundException.class, () -> 
            volunteerService.deleteForOwner(volunteer2.getId(), user1.getUser_id())
        );
    }


    private User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private OwnerProfile createOwnerProfile(User user, String orgName, String nip) {
        OwnerProfile profile = new OwnerProfile();
        profile.setUser(user);
        profile.setOrganizationName(orgName);
        profile.setNip(nip);
        profile.setEmail(user.getEmail());
        return ownerProfileRepository.save(profile);
    }

    private FundraisingEvent createFundraisingEvent(String name, Currency currency, OwnerProfile owner) {
        FundraisingEvent event = new FundraisingEvent();
        event.setName(name);
        event.setCurrency(currency);
        event.setAccountBalance(BigDecimal.ZERO);
        event.setOwnerProfile(owner);
        return fundraisingEventRepository.save(event);
    }

    private CollectionBox createCollectionBox(FundraisingEvent event) {
        CollectionBox box = new CollectionBox();
        box.setEmpty(true);
        box.setFundraisingEvent(event);
        return collectionBoxRepository.save(box);
    }

    private Volunteer createVolunteer(String name, String email, OwnerProfile owner) {
        // Create a User first
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setEnabled(true);
        user = userRepository.save(user);
        
        Volunteer volunteer = new Volunteer();
        volunteer.setUser(user); // Set the User relationship
        volunteer.setFirstName(name);
        volunteer.setLastName("Test");
        volunteer.setEmail(email);
        volunteer.setPassword("password");
        volunteer.setOwnerProfile(owner);
        return volunteerRepository.save(volunteer);
    }
}
