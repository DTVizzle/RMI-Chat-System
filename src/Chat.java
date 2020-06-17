
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sinan
 */
public class Chat extends UnicastRemoteObject implements ChatInterface {

    public String name;
    public ChatInterface client;
    
    public Chat() throws RemoteException {
        this("");
    }

    public Chat(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void setClient(ChatInterface client) {
        this.client = client;
    }

    @Override
    public ChatInterface getClient() {
        return client;
    }

    @Override
    public void send(String s) throws RemoteException {
        System.out.println(s);
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }
}
