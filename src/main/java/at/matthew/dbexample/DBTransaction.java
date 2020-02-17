package at.matthew.dbexample;

import java.util.ArrayDeque;
import java.util.Iterator;

public class DBTransaction implements DBQuery, DBMutate {
    private ArrayDeque<DBMutation> mutations;

    public DBTransaction() {
        mutations = new ArrayDeque<>();
    }

    public void applyMutationsTo(DBMutate target)  // commit a transaction to the next level down
    {

        for (DBMutation mutation : mutations) {
            switch (mutation.getOp()) {
                case SET:
                    target.set(mutation.getKey(), mutation.getValue());
                    break;
                case UNSET:
                    target.unset(mutation.getKey());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void set(String key, String value) {
        DBMutation mutation = new DBMutation(DBMutation.Operation.SET, key, value);
        mutations.add(mutation);
    }

    @Override
    public void unset(String key) {
        DBMutation mutation = new DBMutation(DBMutation.Operation.UNSET, key, null);
        mutations.add(mutation);
    }

    @Override
    public boolean knowsStateOf(String key) {

        for (DBMutation mutation : mutations) {
            if (mutation.getKey().equals(key))
                return true;
        }
        return false;
    }

    @Override
    public String get(String key) {
        Iterator<DBMutation> itr = mutations.descendingIterator();  // newest first

        while (itr.hasNext()) {
            DBMutation mutation = itr.next();
            if (mutation.getKey().equals(key)) {
                switch (mutation.getOp()) {
                    case SET:
                        return mutation.getValue();
                    case UNSET:
                        return null;
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    @Override
    public boolean exists(String key) {
        Iterator<DBMutation> itr = mutations.descendingIterator();  // newest first

        while (itr.hasNext()) {
            DBMutation mutation = itr.next();
            if (mutation.getKey().equals(key)) {
                switch (mutation.getOp()) {
                    case SET:
                        return true;
                    case UNSET:
                        return false;
                    default:
                        return false;
                }
            }
        }
        return false;
    }
}
