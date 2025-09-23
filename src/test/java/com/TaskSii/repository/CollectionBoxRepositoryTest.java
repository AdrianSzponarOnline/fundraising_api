package com.TaskSii.repository;

import com.TaskSii.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CollectionBoxRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CollectionBoxRepository collectionBoxRepository;

    @Autowired
    private FundraisingEventRepository fundraisingEventRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OwnerProfileRepository profileRepository;

    @Test
    void testFindById() {
        User user = new User();
        user.setEmail("owner@test.com");
        user.setPassword("secret");
        user.setEnabled(true);
        entityManager.persist(user);

        User volunteerUser = new User();
        volunteerUser.setEmail("volunteer@test.com");
        volunteerUser.setPassword("secret");
        volunteerUser.setEnabled(true);
        entityManager.persist(volunteerUser);

        OwnerProfile owner = new OwnerProfile();
        owner.setUser(user);
        owner.setOrganizationName("Fundacja Testowa");
        owner.setNip("1234567890");
        owner.setEmail("owner@test.com");
        owner.setRegon("12345678901234");
        owner.setKrs("1234567890");
        owner.setPhoneNumber("123456789");
        entityManager.persist(owner);

        FundraisingEvent event = new FundraisingEvent();
        event.setOwnerProfile(owner);
        event.setName("Test Event");
        event.setCurrency(Currency.PLN);
        event.setAccountBalance(BigDecimal.ZERO);

        entityManager.persist(event);

        Volunteer volunteer = new Volunteer();
        volunteer.setUser(volunteerUser);
        volunteer.setEmail("test@volunteer.com");
        volunteer.setFirstName("Jan");
        volunteer.setLastName("Kowalski");
        volunteer.setPassword("password");
        volunteer.setOwnerProfile(owner);
        entityManager.persist(volunteer);

        CollectionBox box = new CollectionBox();
        box.setFundraisingEvent(event);
        box.setVolunteer(volunteer);
        box.setEmpty(true);
        box.setCreated(LocalDateTime.now());
        entityManager.persist(box);

        entityManager.flush();
        entityManager.clear();

        Optional<CollectionBox> found = collectionBoxRepository.findById(box.getId());

        assertTrue(found.isPresent());
        assertThat(found.get().getVolunteer().getEmail()).isEqualTo("test@volunteer.com");
        assertThat(found.get().getFundraisingEvent().getName()).isEqualTo("Test Event");
    }
}
