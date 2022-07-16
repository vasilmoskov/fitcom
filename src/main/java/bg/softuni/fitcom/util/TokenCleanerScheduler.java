package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.entities.ResetTokenEntity;
import bg.softuni.fitcom.models.entities.VerificationTokenEntity;
import bg.softuni.fitcom.repositories.ResetTokenRepository;
import bg.softuni.fitcom.repositories.VerificationTokenRepository;
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

    private final ResetTokenRepository resetTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public TokenCleanerScheduler(ResetTokenRepository resetTokenRepository,
                                 VerificationTokenRepository verificationTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    // At 00:00 delete tokens if expired
    @Scheduled(cron = "0 0 0 * * *")
    public void clean() {
        deleteResetTokens();
        deleteVerificationTokens();
    }

    private void deleteResetTokens() {
        List<ResetTokenEntity> toDelete = new ArrayList<>();

        for (ResetTokenEntity token : resetTokenRepository.findAll()) {
            if (token.getExpiryDate().before(new Date())) {
                LOGGER.info("Deleting token {}", token.getToken());
                toDelete.add(token);
            }
        }

        resetTokenRepository.deleteAll(toDelete);
    }

    private void deleteVerificationTokens() {
        List<VerificationTokenEntity> toDelete = new ArrayList<>();

        for (VerificationTokenEntity token : verificationTokenRepository.findAll()) {
            if (token.getExpiryDate().after(new Date())) {
                LOGGER.info("Deleting token {}", token.getToken());
                toDelete.add(token);
            }
        }

        verificationTokenRepository.deleteAll(toDelete);
    }
}
