package at.matthew.dbexample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDBStorage {
    private final String key0 = "foo";
    private final String value0 = "bar";
    private DBStorage storage;

    @Before
    public void init() {
        this.storage = new DBStorage();
        this.storage.set(key0, value0);
    }

    @Test
    public void checkKnowsStateOf() {
        String unknownKey = "fie";

        assertTrue(storage.knowsStateOf(key0));
        assertTrue(storage.knowsStateOf(unknownKey));    // true for base layer, never for transaction layers
    }

    @Test
    public void checkGet() {
        String unknownKey = "fie";

        assertEquals(storage.get(key0), value0);
        assertNull(storage.get(unknownKey));
    }

    @Test
    public void checkExists() {
        String unknownKey = "fie";

        assertTrue(storage.exists(key0));
        assertFalse(storage.exists(unknownKey));
    }

    @Test
    public void checkUnset() {
        String key = "baz";
        String value = "baa";

        assertFalse(storage.exists(key));
        storage.set(key, value);
        assertTrue(storage.exists(key));
        assertEquals(storage.get(key), value);
        storage.unset(key);
        assertFalse(storage.exists(key));
    }
}
