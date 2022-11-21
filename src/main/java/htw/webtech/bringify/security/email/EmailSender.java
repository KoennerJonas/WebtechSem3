package htw.webtech.bringify.security.email;

public interface EmailSender {
    void send(String to, String email);
}
