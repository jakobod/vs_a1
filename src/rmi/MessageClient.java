package rmi;

import mvc.IModel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MessageClient implements IModel {
    private static final String HOST = "localhost";
    private static final int PORT = 4242;
    private static final int SERVER_TIMEOUT_SECONDS = 2;
    private final String clientID;
    private MessageService ms;

    public MessageClient() {
        String tmp = null;
        try {
            tmp = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        clientID = tmp;

        configureConnection(HOST, PORT);
    }

    /**
     * sends the given message to the server. implements a 'at least once' semantic
     *
     * @param message the message that should be sent to the server
     */
    @Override
    public void sendMessage(String message) {
        int count = 0;
        boolean messageSent = false;
        do {
            try {
                ms.newMessage(clientID, message);
                messageSent = true;
            } catch (RemoteException e) {
                try {
                    Thread.sleep(SERVER_TIMEOUT_SECONDS * 100);
                } catch (InterruptedException e1) {
                    System.err.println("InterruptedException while sleeping");
                }
            }
            count++;
        } while (!messageSent && count <= 10);
    }

    /**
     * receives all pending messages at once.
     *
     * @return a list containing all received messages
     */
    @Override
    public String getAllMessages() {
        StringBuilder sb = new StringBuilder();
        String message = getMessage();
        while (message != null) {
            sb.append(message);
            sb.append("\n");
            message = getMessage();
        }

        // delete last newline
        final var indexToDelete = sb.length() - 1;
        if(indexToDelete > 0){
            sb.deleteCharAt(indexToDelete);
        }
        return sb.toString();
    }

    /**
     * gets the next message from the server. implements a 'maybe' semantic
     *
     * @return the message that was received or null if no further messages are present
     */
    @Override
    public String getMessage() {
        String message = null;
        try {
            message = ms.nextMessage(clientID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void configureConnection(String hostname, int port) {
        try {
            Registry reg = LocateRegistry.getRegistry(hostname, port);
            ms = (MessageService) reg.lookup("MessageServer");
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
        }
    }
}
