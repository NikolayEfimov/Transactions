import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class Utils {

    static int PORT = 8080;

    static UrlResponse doMethod(String requestMethod, String path, String body) {
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
}
