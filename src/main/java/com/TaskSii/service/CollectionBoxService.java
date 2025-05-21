package com.TaskSii.service;

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
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository, FundraisingEventRepository fundraisingEventRepository, ExchangeRateService exchangeRateService) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.exchangeRateService = exchangeRateService;
    }

    public CollectionBox registerBox() {
        CollectionBox box = new CollectionBox();
        return collectionBoxRepository.save(box);
    }

    public List<CollectionBox> getAllBoxes() {
        return collectionBoxRepository.findAll();
    }

    public void deleteBox(Long boxId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        collectionBoxRepository.delete(box);
    }

    public void assignBoxToEvent(Long boxId, Long eventId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        if (box.getFundraisingEvent() != null) {
            throw new InvalidOperationException("Box is already assigned to a fundraising event");
        }
        if (!box.isEmpty()) {
            throw new InvalidOperationException("Only empty boxes can be assigned to an event.");
        }
        FundraisingEvent event = fundraisingEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
        box.setFundraisingEvent(event);
        collectionBoxRepository.save(box);
    }

    public void addMoney(Long boxId, Currency currency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Amount must be greater than zero");
        }
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));
        box.getMoney().merge(currency, amount, BigDecimal::add);

        box.setEmpty(false);
        collectionBoxRepository.save(box);
    }

    public void transferMoneyToEvent(Long boxId){
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box with id " + boxId + " not found"));

        FundraisingEvent event = box.getFundraisingEvent();
        if (event == null) {
            throw new ResourceNotFoundException("Box is not assigned to any event");
        }

        BigDecimal total = BigDecimal.ZERO;
        Currency eventCurrency = event.getCurrency();

        for(Map.Entry<Currency, BigDecimal> entry: box.getMoney().entrySet()) {
            Currency currency = entry.getKey();
            BigDecimal amt = entry.getValue();

            BigDecimal converted = exchangeRateService.getRate(currency, eventCurrency)
                    .multiply(amt)
                    .setScale(2, RoundingMode.HALF_UP);

            total = total.add(converted);
        }
        event.setAccountBalance(event.getAccountBalance().add(total));

        box.getMoney().clear();
        box.setEmpty(true);

        fundraisingEventRepository.save(event);
        collectionBoxRepository.save(box);
    }
}
