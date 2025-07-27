package kr.or.ddit.util.parse;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SafeCasterTest {

    @Test
    void testAsString() {
        assertEquals("hello", SafeCaster.asString("hello").orElseThrow());
        assertTrue(SafeCaster.asString(123).isEmpty());
    }

    @Test
    void testAsInteger() {
        assertEquals(42, SafeCaster.asInteger(42).orElseThrow());
        assertTrue(SafeCaster.asInteger("not an int").isEmpty());
    }

    @Test
    void testAsLong() {
        assertEquals(100L, SafeCaster.asLong(100L).orElseThrow());
        assertTrue(SafeCaster.asLong("string").isEmpty());
    }

    @Test
    void testAsDouble() {
        assertEquals(3.14, SafeCaster.asDouble(3.14).orElseThrow());
        assertTrue(SafeCaster.asDouble(true).isEmpty());
    }

    @Test
    void testAsBoolean() {
        assertTrue(SafeCaster.asBoolean(true).orElseThrow());
        assertFalse(SafeCaster.asBoolean("true").isPresent());
    }

    @Test
    void testAsByte() {
        assertEquals((byte) 127, SafeCaster.asByte((byte) 127).orElseThrow());
        assertTrue(SafeCaster.asByte(128).isEmpty());
    }

    @Test
    void testAsStringList() {
        List<?> input = List.of("a", "b", "c");
        List<String> result = SafeCaster.asStringList(input).orElseThrow();
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void testAsIntegerList() {
        Object input = List.of(1, 2, 3);
        List<Integer> result = SafeCaster.asIntegerList(input).orElseThrow();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void testAsList() {
        List<?> input = List.of(true, false, true);
        List<Boolean> result = SafeCaster.asList(input, Boolean.class).orElseThrow();
        assertEquals(3, result.size());
        assertFalse(SafeCaster.asList(List.of(1, "2", 3), Integer.class).isPresent());
    }

    @Test
    void testAsObjectMap() {
        Map<String, Object> input = Map.of("x", 1, "y", true);
        Map<String, Object> result = SafeCaster.asObjectMap(input).orElseThrow();
        assertEquals(1, result.get("x"));
        assertEquals(true, result.get("y"));
    }

    @Test
    void testAsStringMap() {
        Map<String, String> input = Map.of("a", "1", "b", "2");
        Map<String, String> result = SafeCaster.asStringMap(input).orElseThrow();
        assertEquals("2", result.get("b"));
    }

    @Test
    void testAsMap() {
        Map<String, Object> input = Map.of("a", 1, "b", 2);
        Map<String, Integer> result = SafeCaster.asMap(input, Integer.class).orElseThrow();
        assertEquals(2, result.get("b"));
        assertTrue(SafeCaster.asMap(Map.of("a", 1, "b", "str"), Integer.class).isEmpty());
    }

    @Test
    void testAsListOfObjectMap() {
        List<Map<String, Object>> input = List.of(
            Map.of("id", 1, "name", "Alice"),
            Map.of("id", 2, "name", "Bob")
        );
        List<Map<String, Object>> result = SafeCaster.asListOfObjectMap(input).orElseThrow();
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).get("name"));
    }

    @Test
    void testAsListOfMap() {
        List<Map<String, Object>> input = List.of(
            Map.of("a", "x", "b", "y"),
            Map.of("c", "z")
        );
        List<Map<String, String>> result = SafeCaster.asListOfMap(input, String.class).orElseThrow();
        assertEquals("y", result.get(0).get("b"));
        assertTrue(SafeCaster.asListOfMap(List.of(Map.of("a", 1)), String.class).isEmpty());
    }
}
