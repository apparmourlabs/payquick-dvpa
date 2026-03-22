package com.payquick.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * WebView activity for rendering payment pages and partner content.
 * Supports deep linking via payquick://webview?url=...
 */
public class WebViewActivity extends Activity {

    private static final String TAG = "WebViewActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setAllowContentAccess(true);

        // JavaScript bridge for native calls
        webView.addJavascriptInterface(new PayQuickBridge(), "PayQuick");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("payquick://")) {
                    handleDeepLink(Uri.parse(url));
                    return true;
                }
                return false;
            }
        });

        // Load URL from intent
        Intent intent = getIntent();
        String url = null;

        if (intent.getData() != null) {
            url = intent.getData().getQueryParameter("url");
        }
        if (url == null) {
            url = intent.getStringExtra("url");
        }

        if (url != null) {
            Log.d(TAG, "Loading URL: " + url);
            webView.loadUrl(url);
        }
    }

    private void handleDeepLink(Uri uri) {
        String action = uri.getHost();
        if ("transfer".equals(action)) {
            Intent transferIntent = new Intent(this, TransferActivity.class);
            transferIntent.putExtra("to_upi", uri.getQueryParameter("to"));
            transferIntent.putExtra("amount", uri.getQueryParameter("amount"));
            startActivity(transferIntent);
        }
    }

    /**
     * JavaScript bridge interface — allows web pages to invoke native functionality.
     */
    private class PayQuickBridge {
        @JavascriptInterface
        public String getAuthToken() {
            return getSharedPreferences("payquick_prefs", MODE_WORLD_READABLE)
                .getString("auth_token", "");
        }

        @JavascriptInterface
        public String getUserInfo() {
            return getSharedPreferences("payquick_prefs", MODE_WORLD_READABLE)
                .getString("user_phone", "") + "|" +
                getSharedPreferences("payquick_prefs", MODE_WORLD_READABLE)
                .getString("user_upi_id", "");
        }

        @JavascriptInterface
        public void makePayment(String toUpi, String amount) {
            runOnUiThread(() -> {
                Toast.makeText(WebViewActivity.this,
                    "Payment of ₹" + amount + " to " + toUpi, Toast.LENGTH_SHORT).show();
            });
        }

        @JavascriptInterface
        public void showToast(String message) {
            runOnUiThread(() -> {
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
