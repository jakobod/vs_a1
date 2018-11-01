package rmi;

import java.util.Objects;

public class Message {
    private static int ID_STATIC = 0;

    public Message (String message, String client_ID) {
        this.message = message;
        this.id = ID_STATIC++;
        this.client_ID = client_ID;
        this.timestamp = System.currentTimeMillis();
    }

    String message;
    int id;
    String client_ID;// wer hat gesendet
    long timestamp;

    @Override
    public String toString() {
        return "<" + id +"> <" + client_ID + "> : <" + message + "> <" + timestamp + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id &&
                timestamp == message1.timestamp &&
                Objects.equals(message, message1.message) &&
                Objects.equals(client_ID, message1.client_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, id, client_ID, timestamp);
    }
}
