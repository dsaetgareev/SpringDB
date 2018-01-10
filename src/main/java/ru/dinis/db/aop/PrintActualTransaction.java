package ru.dinis.db.aop;


import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Create by dinis of 11.01.18.
 */
@Component
public class PrintActualTransaction {

    public void printActualTransaction() {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
    }
}
