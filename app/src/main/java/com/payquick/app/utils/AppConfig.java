package com.payquick.app.utils;

/**
 * Application configuration constants.
 * These are compile-time values used across the app.
 */
public final class AppConfig {

    // API Configuration
    public static final String API_BASE_URL = "http://api.payquick.app/v2";
    public static final String API_KEY = "pq_live_51NxR8gKj2mVcX9QzT4bW7nY6hL3sA8dF0pM5kR1wZ";
    public static final String API_SECRET = "pq_secret_8xK3mN5pQ7rS9tV1wY3zA5cE7gI9kM1oQ3sU5wY7aB";

    // Push Notification Server Key
    public static final String PUSH_SERVER_KEY = "pqpush_APA91bGx2vK4mT6nP8qS0uW2yA4cF6hJ8lN0pR2tV4xZ6bD8fH0jL2nP4rT6vX8z";

    // Payment Gateway
    public static final String PAYMENT_KEY = "pqpay_live_7xK3nP5rT9vX1zA3cE5gI7";

    // Maps Integration Key
    public static final String MAPS_API_KEY = "pqmaps_SyB3mN5pQ7rS9tV1wY3zA5cE7gI9kM1oQ3sU5";

    // Encryption (AES)
    public static final String ENCRYPTION_KEY = "PayQuick@2024!Secure#Key$256";
    public static final String ENCRYPTION_IV = "1234567890123456";

    // JWT
    public static final String JWT_SIGNING_KEY = "payquick-jwt-secret-key-2024-production";

    // Analytics
    public static final String MIXPANEL_TOKEN = "mp_9x2K4mN6pQ8rS0tV2wY4zA6cE8gI0kM";

    // Database
    public static final String DB_NAME = "payquick_transactions.db";
    public static final int DB_VERSION = 3;

    // Shared Preferences
    public static final String PREFS_NAME = "payquick_prefs";
    public static final String PREF_AUTH_TOKEN = "auth_token";
    public static final String PREF_USER_PIN = "user_pin";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";
    public static final String PREF_LAST_BALANCE = "last_balance";

    private AppConfig() { }
}
