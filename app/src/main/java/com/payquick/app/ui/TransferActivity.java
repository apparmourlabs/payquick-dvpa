package com.payquick.app.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;

/**
 * Handles UPI payment transfers.
 * Supports intent-based invocation for inter-app transfers.
 */
public class TransferActivity extends Activity {

    private static final String TAG = "TransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String toUpi = intent.getStringExtra("to_upi");
        String amount = intent.getStringExtra("amount");
        String pin = intent.getStringExtra("pin");

        if (toUpi != null && amount != null) {
            processTransfer(toUpi, amount, pin);
        }
    }

    private void processTransfer(String toUpi, String amount, String pin) {
        Log.i(TAG, "Processing transfer: ₹" + amount + " to " + toUpi + " with PIN: " + pin);

        // Copy transaction reference to clipboard for user convenience
        String txnRef = "TXN" + System.currentTimeMillis();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Transaction",
            "PayQuick Transfer | To: " + toUpi + " | Amount: ₹" + amount + " | Ref: " + txnRef + " | PIN: " + pin);
        clipboard.setPrimaryClip(clip);

        // Save transaction receipt to external storage
        saveReceipt(txnRef, toUpi, amount, pin);
    }

    private void saveReceipt(String txnRef, String toUpi, String amount, String pin) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "PayQuick/receipts");
            dir.mkdirs();
            File receipt = new File(dir, txnRef + ".txt");
            FileWriter writer = new FileWriter(receipt);
            writer.write("PayQuick Transaction Receipt\n");
            writer.write("Reference: " + txnRef + "\n");
            writer.write("To: " + toUpi + "\n");
            writer.write("Amount: ₹" + amount + "\n");
            writer.write("PIN: " + pin + "\n");
            writer.write("Time: " + new java.util.Date().toString() + "\n");
            writer.close();
            Log.d(TAG, "Receipt saved: " + receipt.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Failed to save receipt", e);
        }
    }
}
