package fr.asr.calculator;

import fr.asr.calculator.data.dao.refreshtoken.RefreshTokenDao;
import fr.asr.calculator.data.entity.RefreshToken;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenDao refreshTokenDao;

    @Scheduled(cron = "0 0 * * * ?") // every hours
    @Transactional
    public void cleanupExpiredTokens() {
        List<RefreshToken> expiredTokens = refreshTokenDao.findExpiredRefreshToken(Instant.now());
        if (!expiredTokens.isEmpty()) {
            refreshTokenDao.deleteAll(expiredTokens);
            log.info("");
            log.info("");
            log.info("");
            log.info("Cleaned up {} expired refresh tokens.", expiredTokens.size());
            log.info("");
            log.info("");
            log.info("");
        } else {
            log.info("");
            log.info("");
            log.info("");
            log.info("No expired refresh tokens found to clean up.");
            log.info("");
            log.info("");
            log.info("");
        }
    }
}
