package rmi;

import java.util.Objects;

public class Request {
    Request (String clientID, String message) {
        this.clientID = clientID;
        this.message = message;
    }

    private String clientID;
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(clientID, request.clientID) &&
                Objects.equals(message, request.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, message);
    }
}
