package com.payquick.app.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.payquick.app.utils.AppConfig;

/**
 * Local transaction history database.
 * Caches recent transactions for offline viewing.
 */
public class TransactionDatabase extends SQLiteOpenHelper {

    private static final String TAG = "TransactionDB";

    public TransactionDatabase(Context context) {
        super(context, AppConfig.DB_NAME, null, AppConfig.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "txn_id TEXT, " +
            "type TEXT, " +
            "amount REAL, " +
            "to_upi TEXT, " +
            "from_upi TEXT, " +
            "status TEXT, " +
            "pin_used TEXT, " +
            "timestamp INTEGER, " +
            "note TEXT)");

        db.execSQL("CREATE TABLE accounts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "account_number TEXT, " +
            "ifsc TEXT, " +
            "balance REAL, " +
            "holder_name TEXT, " +
            "phone TEXT, " +
            "aadhaar TEXT, " +
            "pan TEXT)");

        db.execSQL("CREATE TABLE saved_beneficiaries (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "upi_id TEXT, " +
            "account_number TEXT, " +
            "ifsc TEXT, " +
            "phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS accounts");
        db.execSQL("DROP TABLE IF EXISTS saved_beneficiaries");
        onCreate(db);
    }

    /**
     * Record a completed transaction.
     */
    public void saveTransaction(String txnId, String type, double amount,
                                String toUpi, String fromUpi, String status, String pin) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO transactions (txn_id, type, amount, to_upi, from_upi, status, pin_used, timestamp) " +
            "VALUES ('" + txnId + "', '" + type + "', " + amount + ", '" + toUpi + "', '" +
            fromUpi + "', '" + status + "', '" + pin + "', " + System.currentTimeMillis() + ")");
        Log.d(TAG, "Transaction saved: " + txnId + " amount=" + amount + " pin=" + pin);
    }

    /**
     * Search transactions by note or UPI ID.
     */
    public Cursor searchTransactions(String query) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM transactions WHERE note LIKE '%" + query + "%' " +
            "OR to_upi LIKE '%" + query + "%' ORDER BY timestamp DESC", null);
    }

    /**
     * Save account information for quick access.
     */
    public void saveAccount(String accountNumber, String ifsc, double balance,
                           String holderName, String phone, String aadhaar, String pan) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM accounts");
        db.execSQL("INSERT INTO accounts (account_number, ifsc, balance, holder_name, phone, aadhaar, pan) " +
            "VALUES ('" + accountNumber + "', '" + ifsc + "', " + balance + ", '" +
            holderName + "', '" + phone + "', '" + aadhaar + "', '" + pan + "')");
    }
}
