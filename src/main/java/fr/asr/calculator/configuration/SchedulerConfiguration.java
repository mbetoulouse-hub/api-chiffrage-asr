package fr.asr.calculator.configuration;

import fr.asr.calculator.TokenCleanupScheduler;
import fr.asr.calculator.data.dao.refreshtoken.RefreshTokenDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    @Bean
    public TokenCleanupScheduler tokenCleanupScheduler(RefreshTokenDao refreshTokenDao) {
        return new TokenCleanupScheduler(refreshTokenDao);
    }
}
