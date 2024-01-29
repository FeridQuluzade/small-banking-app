package az.dev.smallbankingapp.config;

import az.dev.smallbankingapp.domain.Otp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Otp> otpRedisTemplate() {
        return getRedisTemplate(Otp.class);
    }

    protected <T> RedisTemplate<String, T> getRedisTemplate(Class<T> clazz) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(getSerializerFor(clazz));
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer<?> getSerializerFor(Class<?> clazz) {
        var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(clazz);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

}