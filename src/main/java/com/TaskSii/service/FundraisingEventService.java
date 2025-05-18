package com.TaskSii.service;

import com.TaskSii.repository.FundraisingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;

    @Autowired
    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
    }
    // TODO - dokończyć serwis do tworzenia eventów, po czym wrócić do testowania przypisywania pudełek do eventów

}
