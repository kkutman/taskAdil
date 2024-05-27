package c.task.taskadil.config.jwt;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenBlacklist {
    private final Set<String> blacklist = new HashSet<>();

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void blacklistToken(String token) {
        blacklist.add(token);
    }
}
