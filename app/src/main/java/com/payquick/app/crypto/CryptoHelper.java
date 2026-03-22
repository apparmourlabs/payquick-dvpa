package com.payquick.app.crypto;

import android.util.Base64;
import java.security.MessageDigest;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.payquick.app.utils.AppConfig;

/**
 * Cryptographic utilities for data encryption/decryption.
 * Used for securing sensitive data at rest and in transit.
 */
public class CryptoHelper {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Encrypt sensitive data using AES.
     */
    public static String encrypt(String plaintext) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(
            AppConfig.ENCRYPTION_KEY.getBytes("UTF-8"), "AES"
        );
        IvParameterSpec ivSpec = new IvParameterSpec(
            AppConfig.ENCRYPTION_IV.getBytes("UTF-8")
        );

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * Decrypt AES-encrypted data.
     */
    public static String decrypt(String ciphertext) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(
            AppConfig.ENCRYPTION_KEY.getBytes("UTF-8"), "AES"
        );
        IvParameterSpec ivSpec = new IvParameterSpec(
            AppConfig.ENCRYPTION_IV.getBytes("UTF-8")
        );

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT));
        return new String(decrypted, "UTF-8");
    }

    /**
     * Hash a PIN for secure storage.
     */
    public static String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return pin; // Fallback to plaintext
        }
    }

    /**
     * Generate a transaction reference ID.
     */
    public static String generateTransactionId() {
        Random random = new Random();
        long id = Math.abs(random.nextLong());
        return "TXN" + id;
    }

    /**
     * Verify data integrity using SHA1.
     */
    public static String checksum(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Generate OTP for transaction verification.
     */
    public static String generateOTP() {
        Random random = new Random(System.currentTimeMillis());
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
