import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.Account;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {

    private static int PORT = 8080;

    @BeforeClass
    public static void setup() {
        String[] args = {String.valueOf(PORT)};
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
    public void canGetAccounts() {
        Gson gson = new Gson();
        Account account = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);
        Account account2 = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);
        Account account3 = gson.fromJson(createAccountViaPOST().body.trim(), Account.class);

        UrlResponse response = doMethod("GET", "/accounts", null);

        assertThat(response).isNotNull();
        TypeToken<List<Account>> typeToken = new TypeToken<>() {};
        List<Account> accounts = gson.fromJson(response.body.trim(), typeToken.getType());

        String body = response.body.trim();
        assertThat(body).isNotNull();
        assertThat(accounts).contains(account, account2, account3);
        assertThat(response.status).isEqualTo(200);
    }

    @Test
    public void canGetAccountById() {
        Gson gson = new Gson();
        createAccountViaPOST();
        createAccountViaPOST();
        createAccountViaPOST();

        UrlResponse response = doMethod("GET", "/accounts/1", null);

        assertThat(response).isNotNull();
        Account account = gson.fromJson(response.body.trim(), Account.class);

        String body = response.body.trim();
        assertThat(body).isNotNull();
        assertThat(account.id).isEqualTo(1);
        assertThat(response.status).isEqualTo(200);
    }

    private static UrlResponse doMethod(String requestMethod, String path, String body) {
        UrlResponse response = new UrlResponse();

        try {
            getResponse(requestMethod, path, body, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static void getResponse(String requestMethod, String path, String body, UrlResponse response)
            throws IOException {
        URL url = new URL("http://localhost:" + PORT + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        if (body != null) {
            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body);
            wr.flush();
        }

        connection.connect();
        response.body = IOUtils.toString(connection.getInputStream());
        response.status = connection.getResponseCode();
        response.headers = connection.getHeaderFields();
    }

    private static class UrlResponse {
        public Map<String, List<String>> headers;
        private String body;
        private int status;
    }

    private UrlResponse createTransactionViaPOST() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fromAccountId", "1");
        jsonObject.addProperty("toAccountId", "2");
        jsonObject.addProperty("amount", "100");
        return doMethod("POST", "/transactions", jsonObject.toString());
    }

    private UrlResponse createAccountViaPOST() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("balance", "100");
        jsonObject.addProperty("name", "Sam");
        return doMethod("POST", "/accounts", jsonObject.toString());
    }

}