
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sinan
 */
public class ChatImpl implements ChatInterface {

    private final static String prefex = "[SERVER]";

    private String name;
    public ChatInterface client;

    public ChatImpl() {
        this("");
    }

    public ChatImpl(String name) {
        this.name = name;
        client = null;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void send(String msg) throws RemoteException {
        System.out.println(msg);
    }

    @Override
    public ChatInterface getClient() {
        return client;
    }

    @Override
    public void setClient(ChatInterface client) throws RemoteException {
        this.client = client;
    }
    
    public static void main(String[] args) {
        ChatImpl remoteObject = new ChatImpl();
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Your name and press Enter:");
            String name = s.nextLine().trim();

            remoteObject.setName(name);

            // create stub (note prior to Java 5.0 must use rmic utility)
            ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(remoteObject, 0);
            // get the registry which is running on the default port 1099
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("chat", stub);//binds if not already

            System.out.println(prefex + " Chat Remote Object is ready:");

            while (true) {
                String msg = s.nextLine().trim();
                if (remoteObject.getClient() != null) {
                    ChatInterface client = remoteObject.getClient();
                    msg = "[" + remoteObject.getName() + "] " + msg;
                    client.send(msg);
                }
            }

        } catch (Exception e) {
            System.out.println(prefex + " Server failed: " + e);
        }
    }

}
