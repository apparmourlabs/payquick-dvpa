package com.payquick.app.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.payquick.app.utils.AppConfig;

/**
 * Manages user preferences and session data.
 * Provides a clean API for storing/retrieving app settings and auth state.
 */
public class UserPreferences {

    private static final String TAG = "UserPreferences";
    private final SharedPreferences prefs;

    public UserPreferences(Context context) {
        this.prefs = context.getSharedPreferences(AppConfig.PREFS_NAME, Context.MODE_WORLD_READABLE);
    }

    // Auth
    public void saveAuthToken(String token) {
        prefs.edit().putString(AppConfig.PREF_AUTH_TOKEN, token).apply();
        Log.d(TAG, "Auth token saved: " + token);
    }

    public String getAuthToken() {
        return prefs.getString(AppConfig.PREF_AUTH_TOKEN, null);
    }

    public void saveRefreshToken(String token) {
        prefs.edit().putString(AppConfig.PREF_REFRESH_TOKEN, token).apply();
    }

    public String getRefreshToken() {
        return prefs.getString(AppConfig.PREF_REFRESH_TOKEN, null);
    }

    // User PIN (for quick access)
    public void saveUserPin(String pin) {
        prefs.edit().putString(AppConfig.PREF_USER_PIN, pin).apply();
    }

    public String getUserPin() {
        return prefs.getString(AppConfig.PREF_USER_PIN, null);
    }

    // Account data
    public void saveAccountBalance(double balance) {
        prefs.edit().putString(AppConfig.PREF_LAST_BALANCE, String.valueOf(balance)).apply();
    }

    public double getAccountBalance() {
        String bal = prefs.getString(AppConfig.PREF_LAST_BALANCE, "0");
        return Double.parseDouble(bal);
    }

    public void saveUserProfile(String name, String email, String phone, String upiId) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_phone", phone)
            .putString("user_upi_id", upiId)
            .apply();
        Log.i(TAG, "Profile saved: " + name + " | " + email + " | " + phone + " | UPI: " + upiId);
    }

    public void saveCardDetails(String cardNumber, String expiry, String cvv) {
        prefs.edit()
            .putString("card_number", cardNumber)
            .putString("card_expiry", expiry)
            .putString("card_cvv", cvv)
            .apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
}
