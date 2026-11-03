package fr.asr.calculator.configuration;

import fr.asr.calculator.configuration.properties.TechnicalCadenceConfigurationProperties;
import fr.asr.calculator.configuration.properties.sector.*;
import fr.asr.calculator.data.dao.estimate.EstimateDao;
import fr.asr.calculator.data.dao.profile.ProfileDao;
import fr.asr.calculator.data.dao.refreshtoken.RefreshTokenDao;
import fr.asr.calculator.service.*;
import fr.asr.calculator.service.auth.AuthService;
import fr.asr.calculator.service.auth.AuthServiceImpl;
import fr.asr.calculator.service.auth.TokenService;
import fr.asr.calculator.service.auth.TokenServiceImpl;
import fr.asr.calculator.service.impl.*;
import fr.asr.calculator.service.impl.strategy.Strategy;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class ServiceConfiguration {

    @Bean
    public AlertService alertService() {
        return new AlertServiceImpl();
    }

    @Bean
    public StrategyService strategyService(List<Strategy> strategies) {
        return new StrategyServiceImpl(strategies);
    }

    @Bean
    public CadenceService cadenceService(SectorService sectorService, StrategyService strategyService) {
        return new CadenceServiceImpl(sectorService, strategyService);
    }

    @Bean
    public CalculateService calculateService(
            AlertService alertService,
            ExportService exportService,
            ZoneMultiPurposeCalculateService zoneMultiPurposeCalculateService,
            CadenceService cadenceService,
            SectorService sectorService,
            EstimateService estimateService,
            ZoneHotelCalculateService zoneHotelCalculateService,
            EstimateDao estimateDao,
            ProfileDao profileDao) {
        return new CalculateServiceImpl(
                alertService,
                exportService,
                zoneMultiPurposeCalculateService,
                cadenceService,
                sectorService,
                estimateService,
                zoneHotelCalculateService,
                estimateDao,
                profileDao);
    }

    @Bean
    public AuthService authService(
            ProfileDao profileDao,
            RefreshTokenDao refreshTokenDao,
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            JwtDecoder jwtDecoder) {
        return new AuthServiceImpl(profileDao, refreshTokenDao, tokenService, authenticationManager, jwtDecoder);
    }

    @Bean
    public TokenService tokenService(JwtEncoder jwtEncoder) {
        return new TokenServiceImpl(jwtEncoder);
    }

    @Bean
    public ProfileService profileService(ProfileDao profileDao, PasswordEncoder passwordEncoder) {
        return new ProfileServiceImpl(profileDao, passwordEncoder);
    }

    @Bean
    public EstimateService estimateService(ProfileDao profileDao, EstimateDao estimateDao) {
        return new EstimateServiceImpl(estimateDao, profileDao);
    }

    @Bean
    public ZoneHotelCalculateService zoneHotelCalculateService() {
        return new ZoneHotelCalculateServiceImpl();
    }

    @Bean
    public TechnicalCadenceService technicalCadenceService(
            TechnicalCadenceConfigurationProperties cadenceConfigurationProperties) {
        return new TechnicalCadenceServiceImpl(cadenceConfigurationProperties);
    }

    @Bean
    public ZoneMultiPurposeCalculateService zoneMultiPurposeCalculateService(
            TechnicalCadenceConfigurationProperties cadenceConfigurationProperties) {
        return new ZoneMultiPurposeCalculateServiceImpl(cadenceConfigurationProperties);
    }

    @Bean
    public ExportService exportService() throws IOException {
        return new ExportServiceImpl();
    }

    @Bean
    public SectorService sectorService(
            TertiaireConfigurationProperties tertiaireConfigurationProperties,
            LifeBasedConfigurationProperties lifeBasedConfigurationProperties,
            CoproprietesConfigurationProperties coproprietesConfigurationProperties,
            IndustrieConfigurationProperties industrieConfigurationProperties,
            AgroConfigurationProperties agroConfigurationProperties,
            BioConfigurationProperties bioConfigurationProperties,
            MedicalConfigurationProperties medicalConfigurationProperties,
            CrecheConfigurationProperties crecheConfigurationProperties,
            SchoolConfigurationProperties schoolConfigurationProperties,
            ConcessionsConfigurationProperties concessionsConfigurationProperties,
            GSSMarketConfigurationProperties gssMarketConfigurationProperties,
            SportConfigurationProperties sportConfigurationProperties,
            HotelConfigurationProperties hotelConfigurationProperties,
            RestaurantConfigurationProperties restaurantConfigurationProperties,
            VeterinaireConfigurationProperties veterinaireConfigurationProperties) {
        return new SectorServiceImpl(
                tertiaireConfigurationProperties,
                lifeBasedConfigurationProperties,
                coproprietesConfigurationProperties,
                industrieConfigurationProperties,
                agroConfigurationProperties,
                bioConfigurationProperties,
                medicalConfigurationProperties,
                crecheConfigurationProperties,
                schoolConfigurationProperties,
                concessionsConfigurationProperties,
                gssMarketConfigurationProperties,
                sportConfigurationProperties,
                hotelConfigurationProperties,
                restaurantConfigurationProperties,
                veterinaireConfigurationProperties);
    }
}
