package at.matthew.dbexample;

import java.util.ArrayDeque;

public class DB {

    private DBStorage storage;
    private ArrayDeque<DBTransaction> transactions;

    public DB() {
        transactions = new ArrayDeque<>();
        storage = new DBStorage();
    }

    public String get(String key) {
        for (DBTransaction transaction : transactions) {
            if (transaction.knowsStateOf(key))
                return transaction.get(key);
        }

        return storage.get(key);
    }

    public void set(String key, String value) {
        if (transactions.isEmpty())
            storage.set(key, value);
        else
            transactions.peek().set(key, value);
    }

    public void unset(String key) {
        if (transactions.isEmpty())
            storage.unset(key);
        else
            transactions.peek().unset(key);
    }

    public boolean exists(String key) {
        for (DBTransaction transaction : transactions) {
            if (transaction.knowsStateOf(key))
                return transaction.exists(key);
        }

        return storage.exists(key);
    }

    public void begin() {
        DBTransaction transaction = new DBTransaction();
        transactions.push(transaction);
    }

    public boolean rollback() {
        if (transactions.isEmpty())
            return false;

        transactions.pop();
        return true;
    }

    public void commit() {
        DBTransaction thisTransaction = transactions.pop();
        if (transactions.isEmpty())
            thisTransaction.applyMutationsTo(storage);
        else
            thisTransaction.applyMutationsTo(transactions.peek());
    }
}
