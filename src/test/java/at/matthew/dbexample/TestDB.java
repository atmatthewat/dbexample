package at.matthew.dbexample;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestDB {
    private final String key0 = "foo";
    private final String key1 = "bar";
    private final String value0 = "zero";
    private final String value1 = "one";
    private final String value2 = "two";


    @Test
    public void testBasicOperations() {
        DB db = new DB();

        assertFalse(db.exists(key0));
        assertNull(db.get(key0));

        db.set(key0, value0);

        assertTrue(db.exists(key0));
        assertEquals(db.get(key0), value0);

        db.unset(key0);

        assertFalse(db.exists(key0));
        assertNull(db.get(key0));
    }

    @Test
    public void testRollback() {
        DB db = new DB();

        assertFalse(db.rollback());

        db.set(key0, value0);

        assertTrue(db.exists(key0));
        assertEquals(db.get(key0), value0);

        db.begin();

        db.set(key0, value1);
        db.set(key1, value1);

        assertTrue(db.exists(key0));
        assertTrue(db.exists(key1));
        assertEquals(db.get(key0), value1);
        assertEquals(db.get(key1), value1);

        assertTrue(db.rollback());

        assertTrue(db.exists(key0));
        assertFalse(db.exists(key1));
        assertEquals(db.get(key0), value0);
    }

    @Test
    public void testCommit() {
        DB db = new DB();

        assertFalse(db.commit());

        db.set(key0, value0);

        assertTrue(db.exists(key0));
        assertEquals(db.get(key0), value0);

        db.begin();

        db.set(key0, value1);
        db.set(key1, value1);

        assertTrue(db.exists(key0));
        assertTrue(db.exists(key1));
        assertEquals(db.get(key0), value1);
        assertEquals(db.get(key1), value1);

        assertTrue(db.commit());

        assertTrue(db.exists(key0));
        assertTrue(db.exists(key1));
        assertEquals(db.get(key0), value1);
    }

    @Test
    public void testNestedRollbackThenCommit() {
        DB db = new DB();

        db.set(key0, value0);

        db.begin();

        db.set(key0, value1);

        db.begin();

        db.set(key0, value2);
        db.set(key1, value2);

        assertEquals(db.get(key0), value2);
        assertEquals(db.get(key1), value2);

        assertTrue(db.rollback());

        assertFalse(db.exists(key1));
        assertEquals(db.get(key0), value1);

        assertTrue(db.commit());

        assertFalse(db.exists(key1));
        assertEquals(db.get(key0), value1);
    }

    @Test
    public void testNestedCommitThenRollback() {
        DB db = new DB();

        db.set(key0, value0);

        db.begin();

        db.set(key0, value1);

        db.begin();

        db.set(key0, value2);
        db.set(key1, value2);

        assertEquals(db.get(key0), value2);
        assertEquals(db.get(key1), value2);

        assertTrue(db.commit());

        assertEquals(db.get(key0), value2);
        assertEquals(db.get(key1), value2);

        assertTrue(db.rollback());

        assertFalse(db.exists(key1));
        assertEquals(db.get(key0), value0);
    }
}
