//
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.util.Scanner;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author sinan
// */
//public class ChatClient {
//
//    public static final String REGISTRY_URL = "localhost";
//
//    public static void main(String[] args) {
//        try {
//            Scanner s = new Scanner(System.in);
//            System.out.println("Enter Your name and press Enter:");
//            String name = s.nextLine().trim();
//            ChatInterface client = new Chat(name);
//
//            Registry registry = LocateRegistry.getRegistry(REGISTRY_URL);
//            
//            ChatInterface server = (ChatInterface) registry.lookup("chat");
//
//            String msg = "[" + client.getName() + "] got connected";
//            server.send(msg);
//            System.out.println("[System] Chat Remote Object is ready:");
//            server.addClient(client);
//
//            while (true) {
//                msg = s.nextLine().trim();
//                msg = "[" + client.getName() + "] " + msg;
//                server.send(msg);
//            }
//
//        } catch (NotBoundException | RemoteException e) {
//            System.out.println("[System] Server failed: " + e);
//        }
//    }
//}