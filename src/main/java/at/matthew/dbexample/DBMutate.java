package at.matthew.dbexample;

interface DBMutate {
    void set(String key, String value);
    void unset(String key);
}
