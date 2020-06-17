
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
    public ArrayList<ChatInterface> clients;
    
    public Chat() throws RemoteException {
        this("");
    }

    public Chat(String name) throws RemoteException {
        this.name = name;
        clients = new ArrayList<>();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(String s) throws RemoteException {
        System.out.println(s);
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void addClient(ChatInterface client) throws RemoteException {
        clients.add(client);
    }

    @Override
    public ArrayList<ChatInterface> getClients() throws RemoteException {
        return clients;
    }
}
