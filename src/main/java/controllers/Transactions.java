package controllers;

import com.google.gson.Gson;
import model.Transaction;
import services.TransactionService;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

public class Transactions {

    @Inject private TransactionService transactionService;

    public String transfer(Request rq, Response rs) {
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(rq.body(), Transaction.class);
        transaction.state = "PENDING";
        transactionService.transfer(transaction);
        return new Gson().toJson(transaction);
    }

    public String transactionById(Request rq, Response rs) {
        Long id = Long.valueOf(rq.params("id"));
        Transaction transaction = transactionService.transactionById(id);
        return new Gson().toJson(transaction);
    }

    public String transactions(Request rq, Response rs) {
        List<Transaction> transactions = transactionService.transactions();
        return new Gson().toJson(transactions);
    }
}
