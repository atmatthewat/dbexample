package at.matthew.dbexample;

interface DBQuery {
    boolean knowsStateOf(String key);
    String get(String key);
    boolean exists(String key);
}
