package at.matthew.dbexample;

interface DBQueryOperations {
    boolean knowsStateOf(String key);
    String get(String key);
    boolean exists(String key);
}
