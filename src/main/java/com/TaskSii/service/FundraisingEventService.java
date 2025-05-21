package com.TaskSii.service;

import com.TaskSii.mapper.FundraisingEventMapper;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.repository.FundraisingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;


    @Autowired
    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
    }

    public FundraisingEvent createFundraisingEvent(String eventName, Currency currency) {
        FundraisingEvent f = new FundraisingEvent(eventName, currency);
        return fundraisingEventRepository.save(f);
    }
    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventRepository.findAll();
    }
}
