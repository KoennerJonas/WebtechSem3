package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.ConfirmationTokenEntity;
import htw.webtech.bringify.persistence.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveConfirmationToken(ConfirmationTokenEntity token){
        confirmationTokenRepository.save(token);
    }
}
