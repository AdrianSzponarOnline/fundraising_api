package com.TaskSii.service;

import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.*;
import com.TaskSii.repository.CollectionBoxRepository;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBoxServiceTest {
    private CollectionBoxRepository boxRepo;
    private FundraisingEventRepository eventRepo;
    private ExchangeRateService rateService;
    private OwnerProfileRepository ownerProfileRepository;
    private CollectionBoxService service;

    @BeforeEach
    void setup() {
        boxRepo = mock(CollectionBoxRepository.class);
        eventRepo = mock(FundraisingEventRepository.class);
        rateService = mock(ExchangeRateService.class);
        ownerProfileRepository = mock(OwnerProfileRepository.class);
        service = new CollectionBoxService(boxRepo, eventRepo, rateService, ownerProfileRepository);
    }

    @Test
    void registerBox_createsEmptyBox() {
        // Mock owner profile
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        // Mock fundraising event
        FundraisingEvent event = FundraisingEvent.builder().id(2L).build();
        when(eventRepo.existsByIdAndOwnerProfileId(2L, 1L)).thenReturn(true);
        when(eventRepo.getReferenceById(2L)).thenReturn(event);
        
        when(boxRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        CollectionBox box = service.registerBox(2L, 1L);
        assertTrue(box.isEmpty());
    }

    @Test
    void assignBoxToEventForOwner_success() {
        // Mock owner profile
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        CollectionBox box = new CollectionBox();
        box.setEmpty(true);
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        
        FundraisingEvent event = FundraisingEvent.builder().build();
        when(eventRepo.findByIdAndOwnerProfileId(2L, 1L)).thenReturn(Optional.of(event));

        service.assignBoxToEventForOwner(1L, 2L, 1L);

        assertEquals(event, box.getFundraisingEvent());
        verify(boxRepo).save(box);
    }

    @Test
    void assignBoxToEventForOwner_boxNotFound() {
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(99L, 1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.assignBoxToEventForOwner(99L, 1L, 1L));
    }

    @Test
    void assignBoxToEventForOwner_notEmpty_throws() {
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        CollectionBox box = new CollectionBox();
        box.setEmpty(false);
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        assertThrows(InvalidOperationException.class, () -> service.assignBoxToEventForOwner(1L, 2L, 1L));
    }

    @Test
    void addMoney_success() {
        CollectionBox box = new CollectionBox();
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        service.addMoney(1L, Currency.PLN, new BigDecimal("10.00"));
        assertFalse(box.isEmpty());
        assertEquals(1, box.getTransfers().size());
        assertEquals(new BigDecimal("10.00"), box.getTransfers().get(0).getAmount());
        verify(boxRepo).save(box);
    }

    @Test
    void addMoney_invalidAmount_throws() {
        CollectionBox box = new CollectionBox();
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        assertThrows(InvalidOperationException.class, () -> service.addMoney(1L, Currency.PLN, new BigDecimal("0.00")));
    }

    @Test
    void assignBoxToEventForOwner_eventNotFound() {
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        CollectionBox box = new CollectionBox();
        box.setEmpty(true);
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        when(eventRepo.findByIdAndOwnerProfileId(2L, 1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.assignBoxToEventForOwner(1L, 2L, 1L));
        verify(boxRepo, never()).save(any());
    }

    @Test
    void addMoney_boxNotFound() {
        when(boxRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.addMoney(1L, Currency.PLN, new BigDecimal("10.00")));
        verify(boxRepo, never()).save(any());
    }

    @Test
    void transferMoneyToEventForOwner_success_withConversion() {
        // Mock owner profile
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        // Box with two pending transfers in different currencies
        CollectionBox box = new CollectionBox();
        FundraisingEvent event = FundraisingEvent.builder().currency(Currency.PLN).accountBalance(new BigDecimal("100.00")).build();
        box.setFundraisingEvent(event);

        BoxMoney m1 = new BoxMoney();
        m1.setCurrency(Currency.PLN);
        m1.setAmount(new BigDecimal("10.00"));
        m1.setTransferred(false);
        m1.setCollectionBox(box);

        BoxMoney m2 = new BoxMoney();
        m2.setCurrency(Currency.USD);
        m2.setAmount(new BigDecimal("5.00"));
        m2.setTransferred(false);
        m2.setCollectionBox(box);

        box.addTransfer(m1);
        box.addTransfer(m2);

        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        when(rateService.getRate(Currency.PLN, Currency.PLN)).thenReturn(new BigDecimal("1.00"));
        when(rateService.getRate(Currency.USD, Currency.PLN)).thenReturn(new BigDecimal("4.00"));

        service.transferMoneyToEventForOwner(1L, 1L);

        // 10 PLN + (5 USD * 4 PLN) = 30 PLN, added to 100 = 130
        assertEquals(0, event.getAccountBalance().compareTo(new BigDecimal("130.00")));
        assertTrue(box.getTransfers().stream().allMatch(BoxMoney::isTransferred));
        verify(boxRepo).save(box);
        verify(eventRepo).save(event);
    }

    @Test
    void transferMoneyToEventForOwner_boxNotAssigned_throws() {
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        CollectionBox box = new CollectionBox();
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        assertThrows(ResourceNotFoundException.class, () -> service.transferMoneyToEventForOwner(1L, 1L));
    }

    @Test
    void transferMoneyToEventForOwner_noPendingTransfers_throws() {
        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        
        CollectionBox box = new CollectionBox();
        FundraisingEvent event = FundraisingEvent.builder().currency(Currency.PLN).accountBalance(new BigDecimal("0.00")).build();
        box.setFundraisingEvent(event);
        when(boxRepo.findByIdAndFundraisingEventOwnerProfileId(1L, 1L)).thenReturn(Optional.of(box));
        assertThrows(InvalidOperationException.class, () -> service.transferMoneyToEventForOwner(1L, 1L));
    }
}


