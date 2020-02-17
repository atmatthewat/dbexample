import at.matthew.dbexample.DBTransaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDBTransaction {
    private final String key0 = "foo";
    private final String value0 = "bar";
    private DBTransaction transaction;

    @Before
    public void init() {
        this.transaction = new DBTransaction();
        this.transaction.set(key0, value0);
    }

    @Test
    public void checkKnowsStateOf() {
        String unknownKey = "fie";

        assertTrue(transaction.knowsStateOf(key0));
        assertFalse(transaction.knowsStateOf(unknownKey));    // true for base layer, never for transaction layers
    }

    @Test
    public void checkGet() {
        String unknownKey = "fie";

        assertEquals(transaction.get(key0), value0);
        assertEquals(transaction.get(unknownKey), null);
    }

    @Test
    public void checkExists() {
        String unknownKey = "fie";

        assertTrue(transaction.exists(key0));
        assertFalse(transaction.exists(unknownKey));
    }

    @Test
    public void checkUnset() {
        String key = "baz";
        String value = "baa";

        assertFalse(transaction.exists(key));
        transaction.set(key, value);
        assertTrue(transaction.exists(key));
        assertEquals(transaction.get(key), value);
        transaction.unset(key);
        assertFalse(transaction.exists(key));
    }

    @Test
    public void checkApply() {
        DBTransaction newTransaction = new DBTransaction();

        assertFalse(newTransaction.knowsStateOf(key0));

        transaction.applyMutationsTo(newTransaction);

        assertTrue(newTransaction.knowsStateOf(key0));
        assertTrue(newTransaction.exists(key0));
        assertEquals(newTransaction.get(key0), value0);
    }
}
