package services;

import model.Transaction;

import java.util.List;

public interface TransactionService {

    void transfer(Transaction tx);

    List<Transaction> transactions();

    Transaction transactionById(Long id);

}
