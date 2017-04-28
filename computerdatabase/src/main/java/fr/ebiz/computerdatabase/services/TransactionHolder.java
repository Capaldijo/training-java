package fr.ebiz.computerdatabase.services;
import java.sql.Connection;

public class TransactionHolder {

    private static final ThreadLocal<Connection> THREADLOCAL = new ThreadLocal<>();

    /**
     * Set the transaction in the ThreadLocal.
     * @param transaction new transaction
     */
    public static void set(Connection transaction) {
        if (THREADLOCAL.get() == null) {
            THREADLOCAL.set(transaction);
        }
    }

    /**
     * Remove the transaction when done.
     */
    public static void unset() {
        THREADLOCAL.remove();
    }

    /**
     * Get the transaction.
     * @return transaction
     */
    public static Connection get() {
        return THREADLOCAL.get();
    }
}
