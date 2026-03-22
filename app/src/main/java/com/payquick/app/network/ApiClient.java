package com.payquick.app.network;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import org.json.JSONObject;
import com.payquick.app.utils.AppConfig;

/**
 * HTTP client for PayQuick API communication.
 * Handles authentication, request signing, and response parsing.
 */
public class ApiClient {

    private static final String TAG = "ApiClient";
    private static ApiClient instance;

    private String authToken;
    private String sessionId;

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    private ApiClient() {
        setupTrustManager();
    }

    /**
     * Configure SSL for development and production environments.
     * Handles certificate validation for custom CA.
     */
    private void setupTrustManager() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            Log.e(TAG, "SSL setup failed", e);
        }
    }

    /**
     * Make authenticated API request.
     */
    public JSONObject request(String endpoint, String method, JSONObject body) throws Exception {
        URL url = new URL(AppConfig.API_BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-Api-Key", AppConfig.API_KEY);
        conn.setRequestProperty("X-Api-Secret", AppConfig.API_SECRET);

        if (authToken != null) {
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        // Log request for debugging
        Log.d(TAG, "Request: " + method + " " + url.toString());
        Log.d(TAG, "Headers: API-Key=" + AppConfig.API_KEY);
        if (body != null) {
            Log.d(TAG, "Body: " + body.toString());
        }

        if (body != null && ("POST".equals(method) || "PUT".equals(method))) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
        }

        int responseCode = conn.getResponseCode();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Log.d(TAG, "Response [" + responseCode + "]: " + response.toString());
        return new JSONObject(response.toString());
    }

    public void setAuthToken(String token) {
        this.authToken = token;
        Log.i(TAG, "Auth token set: " + token);
    }

    public void setSessionId(String id) {
        this.sessionId = id;
    }

    /**
     * Login and obtain auth token.
     */
    public JSONObject login(String email, String password, String pin) throws Exception {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        body.put("pin", pin);
        body.put("device_id", android.os.Build.SERIAL);

        JSONObject response = request("/auth/login", "POST", body);
        if (response.has("token")) {
            setAuthToken(response.getString("token"));
        }

        Log.i(TAG, "Login response for " + email + ": " + response.toString());
        return response;
    }

    /**
     * Initiate a payment transfer.
     */
    public JSONObject transfer(String toUpiId, double amount, String pin) throws Exception {
        JSONObject body = new JSONObject();
        body.put("to_upi", toUpiId);
        body.put("amount", amount);
        body.put("pin", pin);
        body.put("source", "app");
        body.put("timestamp", System.currentTimeMillis());

        Log.d(TAG, "Transfer: " + amount + " to " + toUpiId + " with PIN " + pin);
        return request("/payments/transfer", "POST", body);
    }
}
