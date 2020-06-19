
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this("");
    }

    public Chat(String name) throws RemoteException {
        this.name = name;
        connectedClients = new HashMap<>();
        messageArea = null;
        clientsModel = null;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(String msg) throws RemoteException {
        if (messageArea == null) {
            System.out.println(msg);
        } else {
            messageArea.append(msg);
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
    public Set<ChatInterface> getClients() throws RemoteException {
        return connectedClients.keySet();
    }

    @Override
    public ArrayList<String> getMessages(ChatInterface client) throws RemoteException {
        return connectedClients.get(client);
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
        if (host != null) {
            clientsModel.clear();
            clientsModel.addElement(host.getName());
//            for (ChatInterface client : host.getClients()) {
//                clientsModel.addElement(client.getName());
//            }
        }
    }

    @Override
    public String toString() {
        try {
            return getName();
        } catch (RemoteException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
