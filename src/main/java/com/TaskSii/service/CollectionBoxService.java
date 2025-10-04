package com.TaskSii.service;

import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.*;
import com.TaskSii.repository.CollectionBoxRepository;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;
    private final ExchangeRateService exchangeRateService;
    private final OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, 
                               FundraisingEventRepository fundraisingEventRepository, 
                               ExchangeRateService exchangeRateService,
                               OwnerProfileRepository ownerProfileRepository) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.exchangeRateService = exchangeRateService;
        this.ownerProfileRepository = ownerProfileRepository;
    }

    @Transactional
    public CollectionBox registerBox(Long eventId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();

        if (!fundraisingEventRepository.existsByIdAndOwnerProfileId(eventId, ownerProfileId)) {
            throw new ResourceNotFoundException("Event not found or access denied");
        }

        FundraisingEvent eventRef = fundraisingEventRepository.getReferenceById(eventId);

        CollectionBox box = CollectionBox.builder()
                .fundraisingEvent(eventRef)
                .empty(true)
                .build();

        return collectionBoxRepository.save(box);
    }


    public List<CollectionBox> getAllBoxesForOwner(Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        return collectionBoxRepository.findAllByFundraisingEventOwnerProfileId(ownerProfileId);
    }


    public List<CollectionBox> getAllBoxes() {
        return collectionBoxRepository.findAll();
    }

    public void deleteBoxForOwner(Long boxId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        

        if (!collectionBoxRepository.existsByIdAndFundraisingEventOwnerProfileId(boxId, ownerProfileId)) {
            throw new ResourceNotFoundException("Box with id " + boxId + " not found or access denied");
        }
        
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        collectionBoxRepository.delete(box);
    }


    @Transactional
    public void assignBoxToEventForOwner(Long boxId, Long eventId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        CollectionBox box = collectionBoxRepository.findByIdAndFundraisingEventOwnerProfileId(boxId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found or access denied"));
        
        if (box.getFundraisingEvent() != null) {
            throw new InvalidOperationException("Box is already assigned to a fundraising event");
        }
        if (!box.isEmpty()) {
            throw new InvalidOperationException("Only empty boxes can be assigned to an event.");
        }

        FundraisingEvent event = fundraisingEventRepository.findByIdAndOwnerProfileId(eventId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found or access denied"));
        
        event.addCollectionBox(box);
        collectionBoxRepository.save(box);
    }



    @Transactional
    public void addMoney(Long boxId, Currency currency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Amount must be greater than zero");
        }

        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));

        BoxMoney entry = new BoxMoney();
        entry.setCollectionBox(box);
        entry.setCurrency(currency);
        entry.setAmount(amount);
        entry.setTransferred(false);

        box.addTransfer(entry);

        collectionBoxRepository.save(box);
    }


    public CollectionBox getBoxByIdForOwner(@NonNull Long boxId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        return collectionBoxRepository.findByIdAndFundraisingEventOwnerProfileId(boxId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found or access denied"));
    }


    CollectionBox getBoxById(@NonNull Long boxId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        return box;
    }

    @Transactional
    public void transferMoneyToEventForOwner(Long boxId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        CollectionBox box = collectionBoxRepository.findByIdAndFundraisingEventOwnerProfileId(boxId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found or access denied"));

        FundraisingEvent event = box.getFundraisingEvent();
        if (event == null) {
            throw new ResourceNotFoundException("Box is not assigned to any event");
        }

        List<BoxMoney> pending = box.getTransfers().stream()
                .filter(transfer -> !transfer.isTransferred())
                .toList();

        if (pending.isEmpty()) {
            throw new InvalidOperationException("No money to transfer from this box");
        }

        BigDecimal total = BigDecimal.ZERO;
        Currency eventCurrency = event.getCurrency();

        for (BoxMoney entry : pending) {
            BigDecimal converted = exchangeRateService.getRate(entry.getCurrency(), eventCurrency)
                    .multiply(entry.getAmount())
                    .setScale(2, RoundingMode.HALF_UP);

            total = total.add(converted);

            entry.setTransferred(true);
        }

        event.setAccountBalance(event.getAccountBalance().add(total));
        collectionBoxRepository.save(box);
        fundraisingEventRepository.save(event);
    }

}