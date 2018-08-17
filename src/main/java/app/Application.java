package app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.Accounts;
import controllers.Transactions;
import guice.BasicModule;

import static spark.Spark.*;

public class Application {

    private static Injector injector = Guice.createInjector(new BasicModule());

    public static void main(String[] args) {

        //Database.init();
        //int port = Integer.valueOf(args[0]);
        int port = 8080;
        Accounts accounts = injector.getInstance(Accounts.class);
        Transactions transactions = injector.getInstance(Transactions.class);

        port(port);

        get("/accounts", accounts::accounts);
        post("/accounts", accounts::addAccount);
        get("/accounts/:id", accounts::accountById);

        get("/transactions", transactions::transactions);
        post("/transactions", transactions::transfer);
        get("/transactions/:id", transactions::transactionById);

    }

}