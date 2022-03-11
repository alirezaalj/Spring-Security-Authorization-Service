package ir.alirezaalijani.security.springauthorizationservice.security.service.auth;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginAttemptServiceImpl  implements LoginAttemptService{

    private final LoadingCache<String, Integer> attemptsCache;

    private final int maxAttempt;
    private final int expireDuration;
    private final String expireUnit;
    public LoginAttemptServiceImpl(@Value("${application.security.login.fall.max-attempt:10}") int maxAttempt,
                                   @Value("${application.security.login.fall.expire-after.duration:1}") int expireDuration,
                                   @Value("${application.security.login.fall.expire-after.unit:DAYS}") String expireUnit) {
        this.maxAttempt = maxAttempt;
        this.expireDuration=expireDuration;
        this.expireUnit=expireUnit;
        this.attemptsCache = buildCache();
    }

    private LoadingCache<String, Integer> buildCache(){
        TimeUnit timeUnit;
        try {
            timeUnit =TimeUnit.of(ChronoUnit.valueOf(this.expireUnit));
        }catch (Exception e){
            log.error("LoginAttemptService error : {}",e.getMessage());
            timeUnit=TimeUnit.DAYS;
        }
        return CacheBuilder.newBuilder().expireAfterWrite(this.expireDuration, timeUnit).build(new CacheLoader<>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(final String key) {
        this.attemptsCache.invalidate(key);
    }

    public void loginFailed(final String key) {

        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            log.error(e.getMessage());
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= this.maxAttempt;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}
