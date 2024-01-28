package az.dev.smallbankingapp.repository.cache;

import java.util.Optional;

public interface CacheRepository<T> {

    void save(String id, T obj, long ttl);

    Optional<T> findById(String id);

    void deleteById(String id);

}