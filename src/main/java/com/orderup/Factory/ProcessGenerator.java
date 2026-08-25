package com.orderup.Factory;

import java.util.ArrayList;
import java.util.List;

import com.orderup.Models.CustomerProcess;

public class ProcessGenerator {
    private final int MINIMUM_SIZE = 2;
    private final int MAXIMUM_SIZE = 6;

    public List<CustomerProcess> createRandom(){
        int rangeSize = (MAXIMUM_SIZE - MINIMUM_SIZE) + 1;
        int listSize = (int) (Math.random() * rangeSize) + MINIMUM_SIZE;
        List<CustomerProcess> processes = new ArrayList<>();

        processes.add(new CustomerProcess(1, 0 , (int) Math.random()));

        for (int i = 1; i < listSize; i++) {
            processes.add(new CustomerProcess((i + 1), (int) (Math.random() * 8) + 1, (int) (Math.random() * 8) + 1));
        }

        return processes;
    }

    public CustomerProcess createManual(int customerId, int AT, int BT){
        return new CustomerProcess(customerId, AT, BT);
    }
}
