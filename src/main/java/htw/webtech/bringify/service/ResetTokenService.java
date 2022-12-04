package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.ConfirmationTokenEntity;
import htw.webtech.bringify.persistence.ConfirmationTokenRepository;
import htw.webtech.bringify.persistence.ResetTokenEntity;
import htw.webtech.bringify.persistence.ResetTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetTokenService {
    private final ResetTokenRepository resetTokenRepository;

    public ResetTokenService(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    public void saveResetToken(ResetTokenEntity token){
        resetTokenRepository.save(token);
    }

    public Optional<ResetTokenEntity> getToken(String token) {
        return resetTokenRepository.findByToken(token);
    }

}
