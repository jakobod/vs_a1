package rmi;

public class ClientConnection {
    public ClientConnection (int messageID, long timeStamp) {
        this.lastMessageID = messageID;
        this.timeStamp = timeStamp;
    }

    public ClientConnection () {
        this(0, System.currentTimeMillis());
    }

    int lastMessageID;
    long timeStamp;
}
