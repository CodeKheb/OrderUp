package com.orderup.Models;

import java.util.List;

public class ProcessQueue {
    private List<CustomerProcess> processList;

    public ProcessQueue(List<CustomerProcess> processList) {
        this.processList = processList;
    }

    
    public List<CustomerProcess> getProcessList() {
        return processList;
    }

    public void setProcessList(List<CustomerProcess> processList) {
        this.processList = processList;
    }

}
