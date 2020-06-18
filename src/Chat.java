
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.JTextArea;

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
    private ArrayList<String> messages;
    private JTextArea messageArea;

    public Chat() throws RemoteException {
        this("");
    }

    public Chat(String name) throws RemoteException {
        this.name = name;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        messageArea = null;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(String s) throws RemoteException {
        if (messageArea == null) {
            System.out.println(s);
        } else {
            messageArea.append(s);
        }
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

    @Override
    public void setMessageArea(JTextArea messageArea) throws RemoteException {
        this.messageArea = messageArea;
    }
}
