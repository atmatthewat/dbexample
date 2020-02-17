package at.matthew.dbexample;

public interface DBMutate {
    void set(String key, String value);

    void unset(String key);
}
