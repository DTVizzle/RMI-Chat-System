
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
    public ArrayList<ChatInterface> clients;
    private ArrayList<String> messages;
    private JTextArea messageArea;
    private DefaultListModel clientsModel;

    public Chat() throws RemoteException {
        this("");
    }

    public Chat(String name) throws RemoteException {
        this.name = name;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        messageArea = null;
        clientsModel = null;
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
        if (clientsModel != null) {
            clientsModel.addElement(client.getName());
        }
        for (ChatInterface c : clients) {
            if (!c.getName().equals(client.getName())) {
                c.addClient(client);
            } else {
                System.out.println(client.getName());
            }
        }
    }

    @Override
    public ArrayList<ChatInterface> getClients() throws RemoteException {
        return clients;
    }

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
            for (ChatInterface client : host.getClients()) {
                if (!client.getName().equalsIgnoreCase("log")) {
                    clientsModel.addElement(client.getName());
                }
            }
        }
    }

}
