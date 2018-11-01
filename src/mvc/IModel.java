package mvc;

public interface IModel {
    void sendMessage(String message);
    String getMessage();
    String getAllMessages();
    void configureConnection(String hostname, int port);
}
