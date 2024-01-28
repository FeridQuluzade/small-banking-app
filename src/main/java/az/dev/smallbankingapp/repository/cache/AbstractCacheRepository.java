package az.dev.smallbankingapp.repository.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
@RequiredArgsConstructor
public class AbstractCacheRepository<T> implements CacheRepository<T> {

    private final RedisTemplate<String, T> redisTemplate;

    @Override
    public void save(String id, T obj, long ttl) {
        redisTemplate.opsForValue().set(id, obj, ttl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(id));
    }

    @Override
    public void deleteById(String id) {
        redisTemplate.delete(id);
    }

}
