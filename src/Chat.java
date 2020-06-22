
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private int vectorStamp;
    public int[] snapshot;
    private Map<ChatInterface, ArrayList<String>> connectedClients;
    private JTextArea messageArea;
    private DefaultListModel clientsModel;
    private ChatInterface selectedClient;

    public Chat() throws RemoteException {
        this("", null, null, 0);
    }

    public Chat(String name, JTextArea messageArea, DefaultListModel clientsModel, int arrayPosition) throws RemoteException {
        this.vectorStamp = arrayPosition;
        this.snapshot = new int[]{0, 0, 0, 0};

        this.name = name;
        this.messageArea = messageArea;
        this.clientsModel = clientsModel;
        connectedClients = new HashMap<>();
        selectedClient = null;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(ChatInterface sender, String msg) throws RemoteException {

        sender.incrementSnapshot(sender.getVector());
        this.snapshot[vectorStamp]++;
        int[] tempArray = sender.getSnapshot();

        for (int i = 0; i < snapshot.length; i++) {
            if (snapshot[i] < tempArray[i]) {
                snapshot[i] = tempArray[i];
            }
        }

        msg = sender.getName() + msg;
        if (messageArea == null) {
            System.out.println(msg);
        } else {
            if (getSelectedClient() != null && getSelectedClient().equals(sender)) {
                messageArea.append(msg);
            }
        }
        connectedClients.get(sender).add(msg);
        sender.addClient(sender);

    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void addClient(ChatInterface client) throws RemoteException {
        if (!connectedClients.containsKey(client)) {
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

    @Override
    public void clientQuitting() throws RemoteException {
        this.connectedClients.remove(ref, name);
    }

    @Override
    public int getVector() throws RemoteException {
        return this.vectorStamp; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getSnapshot() throws RemoteException {
        return this.snapshot;
    }

    @Override
    public void incrementSnapshot(int vector) throws RemoteException {
        this.snapshot[vector]++;
    }
  
    public void setSelectedClient(ChatInterface client) throws RemoteException {
        selectedClient = client;
    }

    @Override
    public ChatInterface getSelectedClient() throws RemoteException {
        return selectedClient;
    }
}
