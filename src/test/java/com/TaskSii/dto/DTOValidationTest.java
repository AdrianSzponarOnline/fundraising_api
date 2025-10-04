package com.TaskSii.dto;

import com.TaskSii.dto.auth.AuthRequestDTO;
import com.TaskSii.dto.collectionbox.AddMoneyDTO;
import com.TaskSii.dto.collectionbox.AssignBoxDTO;
import com.TaskSii.dto.collectionbox.AssignVolunteerRequestDTO;
import com.TaskSii.model.Currency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DTOValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    @Test
    void registerRequestDTO_valid() {
        RegisterRequestDTO dto = new RegisterRequestDTO("john@example.com", "Password1");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void registerRequestDTO_invalidEmailAndPassword() {
        RegisterRequestDTO dto = new RegisterRequestDTO("bademail", "short");
        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void authRequestDTO_valid() {
        AuthRequestDTO dto = new AuthRequestDTO("user@example.com", "Password1");
        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void authRequestDTO_blankFields() {
        AuthRequestDTO dto = new AuthRequestDTO("", "");
        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        boolean hasEmailViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        boolean hasPasswordViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(hasEmailViolation);
        assertTrue(hasPasswordViolation);
    }

    @Test
    void createFundraisingEventDTO_valid() {
        CreateFundraisingEventDTO dto = new CreateFundraisingEventDTO("Charity Run", Currency.PLN);
        Set<ConstraintViolation<CreateFundraisingEventDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFundraisingEventDTO_invalid() {
        CreateFundraisingEventDTO dto = new CreateFundraisingEventDTO("", null);
        Set<ConstraintViolation<CreateFundraisingEventDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("eventName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
    }

    @Test
    void addMoneyDTO_valid() {
        AddMoneyDTO dto = new AddMoneyDTO(Currency.USD, new BigDecimal("10.50"));
        Set<ConstraintViolation<AddMoneyDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void addMoneyDTO_invalid() {
        AddMoneyDTO dto = new AddMoneyDTO(null, new BigDecimal("0.00"));
        Set<ConstraintViolation<AddMoneyDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void assignBoxDTO_valid() {
        AssignBoxDTO dto = new AssignBoxDTO(1L);
        Set<ConstraintViolation<AssignBoxDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void assignBoxDTO_invalid() {
        AssignBoxDTO dto = new AssignBoxDTO(0L);
        Set<ConstraintViolation<AssignBoxDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("eventId")));
    }

    @Test
    void addressDTO_valid() {
        AddressDTO dto = new AddressDTO("Main St", "City", "State", "Country", "12-345");
        Set<ConstraintViolation<AddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void addressDTO_invalid() {
        AddressDTO dto = new AddressDTO("", "", "", "", "");
        Set<ConstraintViolation<AddressDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("streetName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("city")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("country")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("postalCode")));
    }

    @Test
    void volunteerCreateDTO_valid() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO(
                "John", "Doe", "john@example.com", "+48123123123", "Password1", 1L
        );
        Set<ConstraintViolation<VolunteerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void volunteerCreateDTO_invalid() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO(
                "", "", "bad", "notaphone", "123", 0L
        );
        Set<ConstraintViolation<VolunteerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ownerProfileId")));
    }

    @Test
    void volunteerUpdateDTO_valid() {
        VolunteerUpdateDTO dto = new VolunteerUpdateDTO(
                "John", "Doe", "john@example.com", "+48123123123", "Password1"
        );
        Set<ConstraintViolation<VolunteerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void volunteerUpdateDTO_invalidEmailAndPassword() {
        VolunteerUpdateDTO dto = new VolunteerUpdateDTO(
                null, null, "bad", "notaphone", "123"
        );
        Set<ConstraintViolation<VolunteerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void registerOwnerDTO_valid() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO(
                "owner@example.com",
                "Password1",
                "Good Org",
                "1234567890",
                "123456789",
                "1234567890",
                "+48123123123",
                null
        );
        Set<ConstraintViolation<RegisterOwnerDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void registerOwnerDTO_invalid() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO(
                "bad",
                "123",
                "<bad>",
                "abc",
                "notdigits",
                "short",
                "bad",
                null
        );
        Set<ConstraintViolation<RegisterOwnerDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("organizationName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nip")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("regon")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("krs")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void fundraisingEventDTO_valid() {
        FundraisingEventDTO dto = new FundraisingEventDTO(1L, "Event", Currency.EUR, new BigDecimal("0.00"));
        Set<ConstraintViolation<FundraisingEventDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void fundraisingEventDTO_invalid() {
        FundraisingEventDTO dto = new FundraisingEventDTO(1L, "", null, null);
        Set<ConstraintViolation<FundraisingEventDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountBalance")));
    }

    @Test
    void assignVolunteerRequestDTO_valid() {
        AssignVolunteerRequestDTO dto = new AssignVolunteerRequestDTO(1L, 1L);
        Set<ConstraintViolation<AssignVolunteerRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void assignVolunteerRequestDTO_invalid() {
        AssignVolunteerRequestDTO dto = new AssignVolunteerRequestDTO(-1L, null);
        Set<ConstraintViolation<AssignVolunteerRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("volunteerId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("boxId")));
    }
}


