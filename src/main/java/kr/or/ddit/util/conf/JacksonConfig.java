package kr.or.ddit.util.conf;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule()) // ✅ LocalDate, LocalDateTime 직렬화 지원
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,       // ISO 8601 문자열로 날짜 출력
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES     // 알 수 없는 속성 무시
                )
                .featuresToEnable(
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT // 빈 문자열을 null로 처리
                );
    }
}
