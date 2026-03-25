package br.com.fiap.aiury.configs;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * Configura serializacao e desserializacao padrao de datas na API.
 */
@Configuration
public class JacksonDateConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer dateTimeCustomizer() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateTimePatterns.DATE);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimePatterns.DATE_TIME);

        return builder -> builder
                .serializers(
                        new LocalDateSerializer(dateFormatter),
                        new LocalDateTimeSerializer(dateTimeFormatter)
                )
                .deserializers(
                        new LocalDateDeserializer(dateFormatter),
                        new LocalDateTimeDeserializer(dateTimeFormatter)
                );
    }
}
