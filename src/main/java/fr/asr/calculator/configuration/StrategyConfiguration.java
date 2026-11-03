package fr.asr.calculator.configuration;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.service.impl.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfiguration {

    @Bean
    public AutolaveuseStrategy autolaveuseStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new AutolaveuseStrategy(strategyProperties);
    }

    @Bean
    public CheckActivityStrategy checkActivityStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new CheckActivityStrategy(strategyProperties);
    }

    @Bean
    public CheckArmchairStrategy checkArmchairStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new CheckArmchairStrategy(strategyProperties);
    }

    @Bean
    public CheckParkingStrategy checkParkingStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new CheckParkingStrategy(strategyProperties);
    }

    @Bean
    public CheckPartialStrategy checkPartialStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new CheckPartialStrategy(strategyProperties);
    }

    @Bean
    public CleaningXTimesStrategy cleaningXTimesStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new CleaningXTimesStrategy(strategyProperties);
    }

    @Bean
    public CustomCadenceStrategy customCadenceStrategy() {
        return new CustomCadenceStrategy();
    }

    @Bean
    public InputContainersStrategy inputContainersStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new InputContainersStrategy(strategyProperties);
    }

    @Bean
    public SelectCardioStrategy selectCardioStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new SelectCardioStrategy(strategyProperties);
    }

    @Bean
    public SelectKitchenCleanTypeStrategy selectKitchenCleanTypeStrategy(
            StrategyValuesConfigurationProperties strategyProperties) {
        return new SelectKitchenCleanTypeStrategy(strategyProperties);
    }

    @Bean
    public SelectRestroomCleanTypeStrategy selectRestroomCleanTypeStrategy(
            StrategyValuesConfigurationProperties strategyProperties) {
        return new SelectRestroomCleanTypeStrategy(strategyProperties);
    }

    @Bean
    public SelectWeigthsStrategy selectWeigthsStrategy(StrategyValuesConfigurationProperties strategyProperties) {
        return new SelectWeigthsStrategy(strategyProperties);
    }
}
