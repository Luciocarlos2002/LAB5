package monitor;

import java.net.HttpURLConnection;
import java.net.URL;

public class MonitorService {

    private final String URL_SERVER = "http://34.176.161.147:5000/ping";

    public boolean verificar() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(URL_SERVER).openConnection();
            conn.setConnectTimeout(3000);
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}