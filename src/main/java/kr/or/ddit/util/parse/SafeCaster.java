package kr.or.ddit.util.parse;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Java 21 기준 타입 안전성을 고려한 범용 타입변환 유틸
 *
 * 사용 예)
 * 
 * Object obj = parsedRequest.get("tempPdfFileIds");
 * List<String> tempPdfFileIds = SafeCaster.asStringList(obj)
 *     .orElseThrow(() -> new IllegalArgumentException("Expected a List<String>"));
 * 
 * Object nameObj = parsedRequest.get("name");
 * String name = SafeCaster.asString(nameObj)
 *     .orElse("Unknown");
 * 
 * Object mapObj = parsedRequest.get("meta");
 * Map<String, Object> meta = SafeCaster.asMap(mapObj)
 *     .orElse(Collections.emptyMap());
 * 
 * List<String> strings = SafeCaster.asList(obj, String.class).orElseThrow();
 * List<Integer> integers = SafeCaster.asList(obj, Integer.class).orElseThrow();
 * List<Boolean> bools = SafeCaster.asList(obj, Boolean.class).orElseThrow();
 * 
 * Map<String, String> headers = SafeCaster.asTypedMap(obj, String.class).orElseThrow();
 * Map<String, Object> payload = SafeCaster.asObjectMap(obj).orElseThrow();
 * 
 * List<Map<String, String>> listOfStringMaps 
 *  = SafeCaster.asListOfTypedMap(obj, String.class).orElseThrow();
 * 
 * List<Map<String, Object>> listOfObjectMaps
 *  = SafeCaster.asListOfStringObjectMap(obj).orElseThrow();
 * 
 */
public class SafeCaster {

    // --- 단일 값 캐스팅 ---

    public static Optional<String> asString(Object obj) {
        return obj instanceof String str ? Optional.of(str) : Optional.empty();
    }

    public static Optional<Integer> asInteger(Object obj) {
        return obj instanceof Integer i ? Optional.of(i) : Optional.empty();
    }

    public static Optional<Long> asLong(Object obj) {
        return obj instanceof Long l ? Optional.of(l) : Optional.empty();
    }

    public static Optional<Double> asDouble(Object obj) {
        return obj instanceof Double d ? Optional.of(d) : Optional.empty();
    }

    public static Optional<Boolean> asBoolean(Object obj) {
        return obj instanceof Boolean b ? Optional.of(b) : Optional.empty();
    }

    public static Optional<Byte> asByte(Object obj) {
        return obj instanceof Byte b ? Optional.of(b) : Optional.empty();
    }

    // --- 리스트 캐스팅 ---

    public static Optional<List<String>> asStringList(Object obj) {
        Predicate<Object> allStrings = e -> e instanceof String;

        if (obj instanceof List<?> list && list.stream().allMatch(allStrings)) {
            Function<Object, String> toString = e -> (String) e;
            return Optional.of(list.stream().map(toString).toList());
        }
        return Optional.empty();
    }

    public static Optional<List<Integer>> asIntegerList(Object obj) {
        Predicate<Object> allIntegers = e -> e instanceof Integer;

        if (obj instanceof List<?> list && list.stream().allMatch(allIntegers)) {
            Function<Object, Integer> toInteger = e -> (Integer) e;
            return Optional.of(list.stream().map(toInteger).toList());
        }
        return Optional.empty();
    }
    
    public static <T> Optional<List<T>> asList(Object obj, Class<T> type) {
        if (obj instanceof List<?> list && list.stream().allMatch(type::isInstance)) {
            Function<Object, T> mapper = type::cast;
            return Optional.of(list.stream().map(mapper).toList());
        }
        return Optional.empty();
    }


    // --- Map 캐스팅 ---

    public static Optional<Map<String, Object>> asObjectMap(Object obj) {
        Predicate<Object> isStringKeyedMap = o ->
            o instanceof Map<?, ?> map && map.keySet().stream().allMatch(k -> k instanceof String);

        if (isStringKeyedMap.test(obj)) {
            Map<?, ?> raw = (Map<?, ?>) obj;
            Function<Map.Entry<?, ?>, String> toKey = e -> (String) e.getKey();
            Function<Map.Entry<?, ?>, Object> toVal = Map.Entry::getValue;

            Map<String, Object> result = raw.entrySet().stream()
                .collect(Collectors.toMap(toKey, toVal));

            return Optional.of(result);
        }

        return Optional.empty();
    }

    public static Optional<Map<String, String>> asStringMap(Object obj) {
        Predicate<Object> isStringStringMap = o ->
            o instanceof Map<?, ?> map &&
            map.keySet().stream().allMatch(k -> k instanceof String) &&
            map.values().stream().allMatch(v -> v instanceof String);

        if (isStringStringMap.test(obj)) {
            Map<?, ?> raw = (Map<?, ?>) obj;
            Function<Map.Entry<?, ?>, String> toKey = e -> (String) e.getKey();
            Function<Map.Entry<?, ?>, String> toVal = e -> (String) e.getValue();

            Map<String, String> result = raw.entrySet().stream()
                .collect(Collectors.toMap(toKey, toVal));

            return Optional.of(result);
        }

        return Optional.empty();
    }
    
    public static <T> Optional<Map<String, T>> asMap(Object obj, Class<T> valueType) {
        Predicate<Object> isStringKeyedMapWithTypedValues = o ->
            o instanceof Map<?, ?> map &&
            map.keySet().stream().allMatch(k -> k instanceof String) &&
            map.values().stream().allMatch(valueType::isInstance);

        if (isStringKeyedMapWithTypedValues.test(obj)) {
            Map<?, ?> raw = (Map<?, ?>) obj;
            Function<Map.Entry<?, ?>, String> toKey = e -> (String) e.getKey();
            Function<Map.Entry<?, ?>, T> toVal = e -> valueType.cast(e.getValue());

            Map<String, T> result = raw.entrySet().stream()
                .collect(Collectors.toMap(toKey, toVal));

            return Optional.of(result);
        }

        return Optional.empty();
    }


    // --- List<Map<String, Object>> 캐스팅 ---

    public static Optional<List<Map<String, Object>>> asListOfObjectMap(Object obj) {
        Predicate<Object> isStringKeyedMap = item ->
            item instanceof Map<?, ?> map &&
            map.keySet().stream().allMatch(k -> k instanceof String);

        if (obj instanceof List<?> list && list.stream().allMatch(isStringKeyedMap)) {
            Function<Object, Map<String, Object>> toStringObjectMap = item -> {
                Map<?, ?> raw = (Map<?, ?>) item;
                Function<Map.Entry<?, ?>, String> toKey = e -> (String) e.getKey();
                Function<Map.Entry<?, ?>, Object> toVal = Map.Entry::getValue;

                return raw.entrySet().stream()
                    .collect(Collectors.toMap(toKey, toVal));
            };

            List<Map<String, Object>> result = list.stream()
                .map(toStringObjectMap)
                .toList();

            return Optional.of(result);
        }

        return Optional.empty();
    }
    
    public static <T> Optional<List<Map<String, T>>> asListOfMap(Object obj, Class<T> valueType) {
        // 각 항목이 Map<String, T>인지 검사
        Predicate<Object> isTypedMap = item ->
            item instanceof Map<?, ?> map &&
            map.keySet().stream().allMatch(k -> k instanceof String) &&
            map.values().stream().allMatch(valueType::isInstance);

        if (obj instanceof List<?> list && list.stream().allMatch(isTypedMap)) {
            // 변환 함수 정의
            Function<Object, Map<String, T>> convertMap = item -> {
                Map<?, ?> raw = (Map<?, ?>) item;
                return raw.entrySet().stream()
                    .collect(Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> valueType.cast(e.getValue())
                    ));
            };

            List<Map<String, T>> result = list.stream()
                .map(convertMap)
                .toList();

            return Optional.of(result);
        }

        return Optional.empty();
    }

}
