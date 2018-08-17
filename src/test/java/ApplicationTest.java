import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.Account;
import model.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {

    @BeforeClass
    public static void setup() {
        String[] args = {String.valueOf(Utils.PORT)};
        app.Application.main(args);
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    public void createAccount() {
        UrlResponse response = createAccountViaPOST();
        assertThat(response).isNotNull();
        assertThat(response.body).isNotNull();
        assertThat(response.status).isEqualTo(200);
    }

    @Test
    public void canGetAccountById() {
        createAccountViaPOST();

        UrlResponse response = Utils.doMethod("GET", "/accounts/1", null);

        String body = response.body.trim();
        assertThat(response).isNotNull();
        assertThat(response.status).isEqualTo(200);
        assertThat(body).isNotNull();

        Account account = new Gson().fromJson(response.body.trim(), Account.class);
        assertThat(account).isEqualTo(account);
    }

    @Test
    public void canGetAccounts() {
        Gson gson = new Gson();
        Account account = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);
        Account account2 = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);
        Account account3 = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);

        UrlResponse response = Utils.doMethod("GET", "/accounts", null);

        assertThat(response).isNotNull();
        TypeToken<List<Account>> typeToken = new TypeToken<>() {};
        List<Account> accounts = gson.fromJson(response.body.trim(), typeToken.getType());

        String body = response.body.trim();
        assertThat(body).isNotNull();
        assertThat(accounts).contains(account, account2, account3);
        assertThat(response.status).isEqualTo(200);
    }



    @Test
    public void createTransaction() {
        UrlResponse response = createAccountViaPOST();
        assertThat(response).isNotNull();
        assertThat(response.body).isNotNull();
        assertThat(response.status).isEqualTo(200);
    }

    @Test
    public void canGetTransactionById() {
        createTransactionViaPOST();

        UrlResponse response = Utils.doMethod("GET", "/transactions/1", null);

        String body = response.body.trim();
        assertThat(response).isNotNull();
        assertThat(response.status).isEqualTo(200);
        assertThat(body).isNotNull();

        Transaction transaction = new Gson().fromJson(response.body.trim(), Transaction.class);
        assertThat(transaction).isEqualTo(transaction);
    }

    @Test
    public void canGetTransactions() {
        createAccountViaPOST();
        createAccountViaPOST();

        Gson gson = new Gson();
        Transaction tx1 = gson.fromJson(createTransactionViaPOST().body.trim(), Transaction.class);
        Transaction tx2 = gson.fromJson(createTransactionViaPOST().body.trim(), Transaction.class);
        Transaction tx3 = gson.fromJson(createTransactionViaPOST().body.trim(), Transaction.class);

        UrlResponse response = Utils.doMethod("GET", "/transactions", null);

        assertThat(response).isNotNull();
        TypeToken<List<Transaction>> typeToken = new TypeToken<>(){};
        List<Transaction> transactions = gson.fromJson(response.body.trim(), typeToken.getType());

        String body = response.body.trim();
        assertThat(body).isNotNull();
        assertThat(transactions).contains(tx1, tx2, tx3);
        assertThat(response.status).isEqualTo(200);
    }

    private UrlResponse createTransactionViaPOST() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fromAccountId", "1");
        jsonObject.addProperty("toAccountId", "2");
        jsonObject.addProperty("amount", "100");
        return Utils.doMethod("POST", "/transactions", jsonObject.toString());
    }

    private UrlResponse createAccountViaPOST() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("balance", "100");
        jsonObject.addProperty("name", "Sam");
        return Utils.doMethod("POST", "/accounts", jsonObject.toString());
    }
}