package com.zzyl.framework.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局 Jackson 配置：将 LocalDateTime 统一按 yyyy-MM-dd HH:mm:ss 序列化/反序列化，
 * 以兼容前端 Element Plus 日期组件与 BaseEntity 的 @JsonFormat 格式。
 */
@Configuration
public class JacksonConfig {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonDateTimeCustomizer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
    }
}
