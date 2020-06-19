
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
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
    private Map<ChatInterface, ArrayList<String>> connectedClients;
    private JTextArea messageArea;
    private DefaultListModel clientsModel;

    public Chat() throws RemoteException {
        this("", null, null);
    }

    public Chat(String name, JTextArea messageArea, DefaultListModel clientsModel) throws RemoteException {
        this.name = name;
        this.messageArea = messageArea;
        this. clientsModel = clientsModel;
        connectedClients = new HashMap<>();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(ChatInterface sender, String msg) throws RemoteException {
        msg = sender.getName() + msg;
        if (messageArea == null) {
            System.out.println(msg);
        } else {
            messageArea.append(msg);
        }
        if (sender != null) {
            connectedClients.get(sender).add(msg);
        }
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void addClient(ChatInterface client) throws RemoteException {
        connectedClients.put(client, new ArrayList<>(50));
        if (clientsModel != null) {
            clientsModel.addElement(client.getName());
        }
        for (ChatInterface c : getClients()) {
            if (!c.getName().equals(client.getName())) {
                c.addClient(client);
            }
        }
    }

    @Override
    public ArrayList<ChatInterface> getClients() throws RemoteException {
        return new ArrayList(connectedClients.keySet());
    }

    @Override
    public ArrayList<String> getMessages(String name) throws RemoteException {
        return connectedClients.get(getClient(name));
    }

    @Override
    public void setMessageArea(JTextArea messageArea) throws RemoteException {
        this.messageArea = messageArea;
    }

    @Override
    public void setClientsArea(DefaultListModel clientsModel) throws RemoteException {
        this.clientsModel = clientsModel;
    }

    @Override
    public void getClientsFromHost(ChatInterface host) throws RemoteException {
        if ((host != null) && (clientsModel != null)) {
            ArrayList<ChatInterface> clients = host.getClients();
            if (!clients.isEmpty()) {
                for (ChatInterface client : host.getClients()) {
                    addClient(client);
                }
            }
        }
    }

    @Override
    public ChatInterface getClient(String name) throws RemoteException {
        for (ChatInterface c : connectedClients.keySet()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
}
