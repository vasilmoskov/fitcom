package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.repositories.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TokenCleanerScheduler {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenCleanerScheduler.class);

    private final TokenRepository tokenRepository;

    public TokenCleanerScheduler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    // At 00:00 delete tokens if expired
    @Scheduled(cron = "0 0 0 * * *")
    public void clean() {
        List<TokenEntity> toDelete = new ArrayList<>();

        for (TokenEntity token : tokenRepository.findAll()) {
            if (token.getExpiryDate().after(new Date())) {
                LOGGER.info("Deleting token {}", token.getToken());
                toDelete.add(token);
            }
        }

        tokenRepository.deleteAll(toDelete);
    }
}
