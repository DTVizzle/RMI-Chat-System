
import java.rmi.Remote;
import java.rmi.RemoteException;
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
public interface ChatInterface extends Remote {

    public void setName(String name) throws RemoteException;
    
    public void setMessageArea(JTextArea messageArea) throws RemoteException;
    
    public void setClientsArea(DefaultListModel clientsModel) throws RemoteException;

    public String getName() throws RemoteException;

    public void send(ChatInterface sender, String msg) throws RemoteException;
    
    public void addClient(ChatInterface client) throws RemoteException;

    public ArrayList<ChatInterface> getClients() throws RemoteException;
    
    public ChatInterface getClient(String name) throws RemoteException;
    
    public ArrayList<String> getMessages(String client) throws RemoteException;
    
    public void getClientsFromHost(ChatInterface host) throws RemoteException;
    
    public void setSelectedClient(ChatInterface client) throws RemoteException;
    
    public ChatInterface getSelectedClient() throws RemoteException;
}
