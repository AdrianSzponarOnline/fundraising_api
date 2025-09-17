package com.TaskSii.service;

import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.*;
import com.TaskSii.repository.CollectionBoxRepository;
import com.TaskSii.repository.FundraisingEventRepository;
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
    private CollectionBoxService service;

    @BeforeEach
    void setup() {
        boxRepo = mock(CollectionBoxRepository.class);
        eventRepo = mock(FundraisingEventRepository.class);
        rateService = mock(ExchangeRateService.class);
        service = new CollectionBoxService(boxRepo, eventRepo, rateService);
    }

    @Test
    void registerBox_createsEmptyBox() {
        when(boxRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        CollectionBox box = service.registerBox();
        assertTrue(box.isEmpty());
    }

    @Test
    void assignBoxToEvent_success() {
        CollectionBox box = new CollectionBox();
        box.setEmpty(true);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        FundraisingEvent event = FundraisingEvent.builder().build();
        when(eventRepo.findById(2L)).thenReturn(Optional.of(event));

        service.assignBoxToEvent(1L, 2L);

        assertEquals(event, box.getFundraisingEvent());
        verify(boxRepo).save(box);
    }

    @Test
    void assignBoxToEvent_boxNotFound() {
        when(boxRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.assignBoxToEvent(99L, 1L));
    }

    @Test
    void assignBoxToEvent_notEmpty_throws() {
        CollectionBox box = new CollectionBox();
        box.setEmpty(false);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        assertThrows(InvalidOperationException.class, () -> service.assignBoxToEvent(1L, 2L));
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
}


