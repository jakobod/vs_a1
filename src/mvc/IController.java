package mvc;

public interface IController{
    void sendMessage(String message);
    String getMessage();
    String getAllMessages();
    void configureConnection(String hostname, int port);
}
