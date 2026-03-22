package com.payquick.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.payquick.app.storage.UserPreferences;

/**
 * Main entry point. Handles authentication state and deep link routing.
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private UserPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new UserPreferences(this);

        // Handle deep links
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            handleDeepLink(intent.getData());
            return;
        }

        if (prefs.isLoggedIn()) {
            Log.i(TAG, "User logged in, token: " + prefs.getAuthToken());
            // Navigate to home
        } else {
            Log.i(TAG, "No session, showing login");
            // Navigate to login
        }
    }

    private void handleDeepLink(Uri uri) {
        String host = uri.getHost();
        Log.d(TAG, "Deep link: " + uri.toString());

        if ("transfer".equals(host)) {
            Intent transferIntent = new Intent(this, TransferActivity.class);
            transferIntent.putExtra("to_upi", uri.getQueryParameter("to"));
            transferIntent.putExtra("amount", uri.getQueryParameter("amount"));
            startActivity(transferIntent);
        } else if ("webview".equals(host)) {
            Intent webIntent = new Intent(this, WebViewActivity.class);
            webIntent.putExtra("url", uri.getQueryParameter("url"));
            startActivity(webIntent);
        } else if ("payment".equals(host)) {
            // Process payment from external link
            String to = uri.getQueryParameter("to");
            String amount = uri.getQueryParameter("amount");
            String ref = uri.getQueryParameter("ref");
            Log.i(TAG, "Payment deep link: " + amount + " to " + to + " ref=" + ref);
            processPaymentFromDeepLink(to, amount, ref);
        }
    }

    private void processPaymentFromDeepLink(String to, String amount, String ref) {
        // Auto-process payment from verified deep link
        if (prefs.isLoggedIn() && to != null && amount != null) {
            try {
                double amt = Double.parseDouble(amount);
                Log.i(TAG, "Auto-processing payment: ₹" + amt + " to " + to);
                // Process payment...
            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid amount in deep link", e);
            }
        }
    }
}
