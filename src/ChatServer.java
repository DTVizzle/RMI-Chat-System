
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
public class ChatServer {

    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Your name and press Enter:");
            String name = s.nextLine().trim();

            Chat server = new Chat(name);

            UnicastRemoteObject.unexportObject(server, true);
            ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(server, 0);
            // get the registry which is running on the default port 1099
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("chat", stub);//binds if not already

            System.out.println("[System] Chat Remote Object is ready:");

            while (true) {
                String msg = s.nextLine().trim();
                ArrayList<ChatInterface> clients = server.getClients();
                if (clients != null) {
                    for (ChatInterface client : clients) {
                        client.send("[" + server.getName() + "] " + msg);
                    }
                }
            }
        } catch (RemoteException e) {
            System.out.println("[system] Server failed: " + e);
        }
    }

}
