package com.payquick.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.payquick.app.utils.AppConfig;

/**
 * Background sync service.
 * Periodically syncs local transaction data with the server.
 */
public class SyncService extends Service {

    private static final String TAG = "SyncService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getStringExtra("action") : null;
        String filePath = intent != null ? intent.getStringExtra("file_path") : null;

        if ("upload".equals(action) && filePath != null) {
            uploadFile(filePath);
        } else if ("sync".equals(action)) {
            syncTransactions();
        }

        return START_STICKY;
    }

    private void uploadFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return;

            URL url = new URL(AppConfig.API_BASE_URL + "/upload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            fis.close();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Upload response: " + responseCode + " for " + filePath);
        } catch (Exception e) {
            Log.e(TAG, "Upload failed for " + filePath, e);
        }
    }

    private void syncTransactions() {
        Log.d(TAG, "Syncing transactions...");
        // Sync logic
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
