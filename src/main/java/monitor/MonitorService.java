package monitor;

import java.net.HttpURLConnection;
import java.net.URL;

public class MonitorService {

    private static final String URL_SERVER = "http://34.176.161.147:5000/ping";

    public boolean verificarServidor() {
        try {
            URL url = new URL(URL_SERVER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }
}