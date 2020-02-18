package at.matthew.dbexample;

import java.util.HashMap;

public class DBStorage implements DBQueryOperations, DBMutateOperations {
    private final HashMap<String, String> storage;

    public DBStorage() {
        storage = new HashMap<>();
    }

    @Override
    public boolean knowsStateOf(String key) {
        return true;    // always true for storage layer
    }

    @Override
    public String get(String key) {
        return storage.get(key);
    }

    @Override
    public boolean exists(String key) {
        return storage.containsKey(key);
    }

    @Override
    public void set(String key, String value) {
        storage.put(key, value);
    }

    @Override
    public void unset(String key) {
        storage.remove(key);
    }
}
