package at.matthew.dbexample;

class DBMutationRecord {
    private final Operation op;
    private final String key;
    private final String value;

    DBMutationRecord(Operation op, String key, String value) {
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
