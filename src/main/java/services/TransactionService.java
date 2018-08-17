package services;

import model.Transaction;

import java.util.List;

public interface TransactionService {

    boolean transfer(Transaction transaction);

    List<Transaction> transactions();

    Transaction transactionById(Long id);

}
