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

    public Response transfer(Request rq, Response rs) {
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(rq.body(), Transaction.class);
        transaction.state = "PENDING";
        transactionService.transfer(transaction);
        return rs;
    }

    public Object transactionById(Request rq, Response rs) {
        Long id = Long.valueOf(rq.params("id"));
        Transaction transaction = transactionService.transactionById(id);
        Gson gson = new Gson();
        return gson.toJson(transaction);
    }

    public Object transactions(Request rq, Response rs) {
        List<Transaction> transactions = transactionService.transactions();
        Gson gson = new Gson();
        return gson.toJson(transactions);
    }
}
