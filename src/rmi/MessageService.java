package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageService extends Remote {
    String nextMessage(String clientID) throws RemoteException;
    void newMessage(String clientID, String message) throws RemoteException;
}