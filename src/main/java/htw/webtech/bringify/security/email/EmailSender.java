package htw.webtech.bringify.security.email;

public interface EmailSender {
    void sendConfirm(String to, String email);
    void sendReset(String to, String email);
}
