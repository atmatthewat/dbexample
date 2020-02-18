package at.matthew.dbexample;

interface DBMutateOperations {
    void set(String key, String value);
    void unset(String key);
}
