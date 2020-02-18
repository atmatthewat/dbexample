package at.matthew.dbexample;

import java.util.ArrayDeque;
import java.util.Iterator;

public class DBTransaction implements DBQueryOperations, DBMutateOperations {
    private final ArrayDeque<DBMutationRecord> mutations;

    public DBTransaction() {
        mutations = new ArrayDeque<>();
    }

    public void applyMutationsTo(DBMutateOperations target)  // commit a transaction to the next level down
    {

        for (DBMutationRecord mutation : mutations) {
            switch (mutation.getOp()) {
                case SET:
                    target.set(mutation.getKey(), mutation.getValue());
                    break;
                case UNSET:
                    target.unset(mutation.getKey());
                    break;
            }
        }
    }

    @Override
    public void set(String key, String value) {
        DBMutationRecord mutation = new DBMutationRecord(DBMutationRecord.Operation.SET, key, value);
        mutations.add(mutation);
    }

    @Override
    public void unset(String key) {
        DBMutationRecord mutation = new DBMutationRecord(DBMutationRecord.Operation.UNSET, key, null);
        mutations.add(mutation);
    }

    @Override
    public boolean knowsStateOf(String key) {

        for (DBMutationRecord mutation : mutations) {
            if (mutation.getKey().equals(key))
                return true;
        }
        return false;
    }

    @Override
    public String get(String key) throws UnsupportedOperationException {
        Iterator<DBMutationRecord> itr = mutations.descendingIterator();  // newest first

        while (itr.hasNext()) {
            DBMutationRecord mutation = itr.next();
            if (mutation.getKey().equals(key)) {
                switch (mutation.getOp()) {
                    case SET:
                        return mutation.getValue();
                    case UNSET:
                        return null;
                }
            }
        }

        throw new UnsupportedOperationException("GET called on transaction when knowsStateOf false");
    }

    @Override
    public boolean exists(String key) throws UnsupportedOperationException {
        Iterator<DBMutationRecord> itr = mutations.descendingIterator();  // newest first

        while (itr.hasNext()) {
            DBMutationRecord mutation = itr.next();
            if (mutation.getKey().equals(key)) {
                switch (mutation.getOp()) {
                    case SET:
                        return true;
                    case UNSET:
                        return false;
                }
            }
        }
        throw new UnsupportedOperationException("EXISTS called on transaction when knowsStateOf false");
    }
}
