package kr.or.ddit.util.parse;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeStringTypeHandler extends BaseTypeHandler<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter != null ? parameter.format(FORMATTER) : null);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseDateTime(rs.getString(columnName));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseDateTime(rs.getString(columnIndex));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseDateTime(cs.getString(columnIndex));
    }

    /**
     * 문자열을 LocalDateTime으로 변환 (Z 지원)
     */
    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) return null;

        // 1) ISO_LOCAL_DATE_TIME
        if (value.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")) {
            try {
                return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception ignored) {}
        }

        // 2) Zulu UTC 시간 (예: 2025-08-03T03:14:43.535Z)
        if (value.endsWith("Z")) {
            try {
                return LocalDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
            } catch (Exception ignored) {}
        }

        // 3) yy/MM/dd HH:mm:ss.SSSSSSSSS 형식
        if (value.matches("\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")) {
            DateTimeFormatter oracleFormatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss.SSSSSSSSS");
            return LocalDateTime.parse(value, oracleFormatter);
        }

        throw new IllegalArgumentException("지원하지 않는 날짜 형식: " + value);
    }

}
