package kr.or.ddit.util.conf;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule())       // ✅ LocalDate, LocalDateTime 처리
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 👈 "2024-01-01" 형식
    }
}
