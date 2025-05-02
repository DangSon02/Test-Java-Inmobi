package burundi.ilucky.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListService {
    private final StringRedisTemplate redisTemplate;

    public void addTokenToBlacklist(String token, long durationInSeconds) {
        String blacklistKey = "blacklist:token:" + token;
        redisTemplate.opsForValue().set(blacklistKey, "blacklisted", Duration.ofSeconds(durationInSeconds));
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:token:" + token);
    }

}
