package at.matthew.dbexample;

class DBMutation {
    private final Operation op;
    private final String key;
    private final String value;

    DBMutation(Operation op, String key, String value) {
        this.op = op;
        this.key = key;
        this.value = value;
    }

    Operation getOp() {
        return op;
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }

    enum Operation {
        SET, UNSET
    }


}
