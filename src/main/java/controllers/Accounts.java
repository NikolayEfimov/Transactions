package controllers;

import com.google.gson.Gson;
import model.Account;
import services.AccountService;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

public class Accounts {

    @Inject private AccountService accountService;

    public String accounts(Request rq, Response rs) {
        List<Account> accounts = accountService.accounts();
        Gson gson = new Gson();
        return gson.toJson(accounts);
    }

    public String accountById(Request rq, Response rs) {
        Long id = Long.valueOf(rq.params("id"));
        Account account = accountService.accountById(id);
        Gson gson = new Gson();
        return gson.toJson(account);
    }

    public String addAccount(Request rq, Response rs) {
        Gson gson = new Gson();
        Account account = gson.fromJson(rq.body(), Account.class);

        account = accountService.create(account);
        return gson.toJson(account);

    }
}
