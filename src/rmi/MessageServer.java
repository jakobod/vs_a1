package rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MessageServer implements MessageService {
    private static final int MAX_QUEUE_SIZE = 30;
    private List<Message> deliveryQueue;
    private Map<String, ClientConnection> connections;
    private Set<Request> requests;
    private final int MAX_CONN_AGE_MILLISECONDS = 30_000;
    private static final int PORT = 4242;
    private final Logger logger = Logger.getLogger(Thread.class.getName());
    private FileHandler fh;

    public MessageServer(){
        deliveryQueue = new LinkedList<>();
        connections = new HashMap<>();
        requests = new HashSet<>();
        initLogger();
        logger.info("log created");
    }

    public void initLogger(){
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("./log.txt",  true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the next message from the delivery queue
     * @param clientID the clientID of the requesting client
     * @return the next message from the queue or null if no further message is present
     */
    public String nextMessage(final String clientID) {
        var cc = manageConnection(clientID);

        Message message = null;
        int messageID = cc.lastMessageID;
        for (var m : deliveryQueue) {
            if (m.id == messageID) {
                message = m;
                break;
            }
        }

        if(message != null) {
            System.out.println(message);
            ++cc.lastMessageID;
            return message.toString();
        } else {
            return null;
        }
    }

    /**
     * takes a new message and places it into the delivery queue
     * @param clientID the clientID of the sender
     * @param message the message that is delivered
     */
    public void newMessage(final String clientID, final String message) {
        logger.info("new message request: " + clientID + " " + message);
        manageConnection(clientID); // set timestamp or instantiate new ClientConnection Object.

        Request request = new Request(clientID, message);
        if (!requests.add(request)) {
            logger.info("message request identified as duplicate: " + clientID);
            // ignore duplicate requests
            return ;
        }

        // now insert message to send.
        Message m = new Message(message, clientID);
        if (deliveryQueue.size() >= MAX_QUEUE_SIZE) {
            deliveryQueue.remove(0);
        }
        deliveryQueue.add(m);
        logger.info("message added to queue: " + message);
    }

    /**
     * manages creating and updating of the ClientConnection objects
     * @param clientID the ID of the client
     * @return the Object that was created or updated
     */
    private ClientConnection manageConnection(final String clientID) {
        ClientConnection cc ;
        long timestamp = System.currentTimeMillis();
        if (!connections.containsKey(clientID)) {
            cc = new ClientConnection();
            logger.info("connection created: " + clientID);
            connections.put(clientID, cc);
        } else {
            cc = connections.get(clientID);
            cc.timeStamp = timestamp;
        }
        return cc;
    }

    /**
     * keeps server alive and keeps client connections sorted.
     * when a client timed out, this routine 'forgets' about that client and removes its
     * ClientConnection Object.
     */
    public void run() {
        while(true) {
            // forget about timed out connections.
            long now = System.currentTimeMillis();
            for (var key : connections.keySet()) {
                ClientConnection conn = connections.get(key);
                if(now - conn.timeStamp >= MAX_CONN_AGE_MILLISECONDS) {
                    connections.remove(key);
                    logger.info("connection removed: " + key);
                }
            }
            try {
                Thread.sleep(1000); // sleep a second
            } catch (InterruptedException ex) {
                System.out.println("sleep was interrupted");
            }
        }
    }

    /**
     * publishes the MessageServer Object and starts the run method
     * @param args arguments for the program
     */
    public static void main(String... args) {
        MessageServer ms = null;
        try {
            // got exception when using getRegistry.. creating one works fine
            var registry = LocateRegistry.createRegistry(PORT);
            ms = new MessageServer();
            UnicastRemoteObject.exportObject(ms, PORT);
            registry.rebind("MessageServer", ms);
        }
        catch (RemoteException ex) {
            System.out.println("Exception while initializing Server");
            ex.printStackTrace();
            return ;
        }

        System.out.println("Server running");

        // keep server alive and clean up timed out clients
        ms.run();
    }
}