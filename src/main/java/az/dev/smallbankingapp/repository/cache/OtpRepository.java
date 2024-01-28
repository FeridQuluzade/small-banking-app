package az.dev.smallbankingapp.repository.cache;

import az.dev.smallbankingapp.domain.Otp;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OtpRepository extends AbstractCacheRepository<Otp> {

    public OtpRepository(RedisTemplate<String, Otp> otpRedisTemplate) {
        super(otpRedisTemplate);
    }

}
