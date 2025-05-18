package com.TaskSii.service;

import com.TaskSii.dto.CollectionBoxDTO;
import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.repository.CollectionBoxRepository;
import com.TaskSii.repository.FundraisingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;

    @Autowired
    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, FundraisingEventRepository fundraisingEventRepository) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.fundraisingEventRepository = fundraisingEventRepository;
    }

    public CollectionBox registerBox(){
        CollectionBox box = new CollectionBox();
        return collectionBoxRepository.save(box);
    }
    public List<CollectionBoxDTO> getAllBoxes(){
        return collectionBoxRepository.findAll().stream()
                .map(box -> new CollectionBoxDTO(
                        box.getId(),
                        box.isEmpty(),
                        box.getFundraisingEvent() != null
                )).collect(Collectors.toList());
    }
    public void deleteBox(Long boxId){
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        collectionBoxRepository.delete(box);
    }

    public void assignBoxToEvent(Long boxId, Long eventId){
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        if(box.getFundraisingEvent() != null){
            throw new InvalidOperationException("Box is already assigned to a fundraising event");
        }
        if(!box.isEmpty()){
            throw new InvalidOperationException("Only empty boxes can be assigned to an event.");
        }
        FundraisingEvent event = fundraisingEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
    }
    public void addMoney(Long boxId, Currency currency, BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidOperationException("Amount must be greater than zero");
        }
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        box.getMoney().merge(currency, amount, BigDecimal::add);

        box.setEmpty(false);
        collectionBoxRepository.save(box);
    }

}
