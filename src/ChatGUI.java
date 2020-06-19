
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sinan
 */
public class ChatGUI extends JPanel implements ActionListener, ListSelectionListener {

    private static final String TITLE = "Chat Box";
    private static final Font PRIMARY_FONT = new Font("Arial", Font.BOLD, 15);

    private String recipient;

    private static JFrame frame;
    private static ChatInterface chatClient;

    private JTextField urlField, portField, nameField;
    private JButton connectBtn, startBtn, sendBttn;
    private JLabel statusLabel;
    private JTextArea messageArea;
    private JTextField messageField;
    private DefaultListModel clientsModel;
    private JList clients;

    public ChatGUI() {
        super(new BorderLayout());
        recipient = null;
        
        JPanel ConnectionPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        JLabel label;
        JPanel panel;
        JScrollPane scrollPane;

        panel = new JPanel(new BorderLayout(6, 0));
        label = new JLabel("Registry URL:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        urlField = new JTextField("localhost", 15);
        panel.add(urlField, BorderLayout.CENTER);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(6, 0));
        label = new JLabel("Registry Port:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        portField = new JTextField("1099", 15);
        panel.add(portField, BorderLayout.CENTER);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(5, 0));
        label = new JLabel("Name:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        nameField = new JTextField(15);
        panel.add(nameField, BorderLayout.CENTER);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(5, 0));
        connectBtn = new JButton("Connect to a host");
        connectBtn.addActionListener(this);
        panel.add(connectBtn, BorderLayout.WEST);
        startBtn = new JButton("Start Hosting");
        startBtn.addActionListener(this);
        panel.add(startBtn, BorderLayout.EAST);
        formPanel.add(panel);

        statusLabel = new JLabel("Not Connected!");
        statusLabel.setFont(PRIMARY_FONT);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setVerticalAlignment(JLabel.CENTER);
        formPanel.add(statusLabel);

        ConnectionPanel.add(formPanel, BorderLayout.NORTH);

        clientsModel = new DefaultListModel();
        clients = new JList(clientsModel);
        clients.setFont(PRIMARY_FONT);
        clients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clients.addListSelectionListener(this);
        scrollPane = new JScrollPane(clients);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        ConnectionPanel.add(scrollPane, BorderLayout.CENTER);
        add(ConnectionPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setColumns(70);
        messageArea.setRows(25);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(PRIMARY_FONT);
        scrollPane = new JScrollPane(messageArea);
        scrollPane.setHorizontalScrollBarPolicy(scrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel();

        messageField = new JTextField(30);
        messageField.setEnabled(false);
        messageField.setFont(PRIMARY_FONT);
        scrollPane = new JScrollPane(messageField);
        scrollPane.setHorizontalScrollBarPolicy(scrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messagePanel.add(scrollPane);

        sendBttn = new JButton("Send");
        sendBttn.setEnabled(false);
        sendBttn.addActionListener(this);
        messagePanel.add(sendBttn);

        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        super.add(mainPanel, BorderLayout.CENTER);
    }

    private void displayMessage(String title, String str) {
        JOptionPane.showMessageDialog(new Frame(title), str);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == connectBtn) {
            String url = urlField.getText();
            String port = portField.getText();
            String name = nameField.getText();
            if ((url.equals("")) || (port.equals("")) || (name.equals(""))) {
                displayMessage("Error", "Please fill in the fields");
            } else {
                try {
                    startClient(url, port, name);
                } catch (RemoteException | NotBoundException ex) {
                    displayMessage("Connection Error", "Failed to connect to a host");
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (src == startBtn) {
            String name = nameField.getText();
            String port = portField.getText();
            if (name.equals("") || port.equals("")) {
                displayMessage("Error", "Please fill in the fields");
            } else {
                try {
                    startHost(name, port);
                } catch (RemoteException | NotBoundException ex) {
                    displayMessage("Connection Error", "Error initializing host");
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (src == sendBttn) {
            try {
                String msg = ": " + messageField.getText();
                messageField.setText("");
//                sendBttn.setEnabled(false);
                messageArea.append("You" + msg + "\n");

//                if (!isLeader) {
//                    host.send(client.getName() + msg + "\n");
//                } else {
//                    for (ChatInterface client : host.getClients()) {
//                        client.send(host.getName() + msg);
//                    }
//                }
                ChatInterface c = chatClient.getClient(recipient);
                System.out.println("Reciever: " + c);
                c.send(chatClient, msg);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startClient(String url, String port, String name) throws RemoteException, NotBoundException {
        chatClient = new Chat(name, messageArea, clientsModel);
        
        Registry registry = LocateRegistry.getRegistry(url, Integer.parseInt(port));
        ChatInterface host = (ChatInterface) registry.lookup("chat");

        chatClient.addClient(host);
        chatClient.getClientsFromHost(host);
        host.addClient(chatClient);
        
        chatClient.send(host, "[" + chatClient.getName() + "] successfuly connected\n");
        
        System.out.println("Client is Ready!");

        finishSetup(name);
    }

    private void startHost(String name, String port) throws RemoteException, NotBoundException {
        chatClient = new Chat(name, messageArea, clientsModel);

//        UnicastRemoteObject.unexportObject(host, true);
//        ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(host, 0);
        // get the registry which is running on the default port 1099
        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(port));
        registry.rebind("chat", chatClient);//binds if not already

        finishSetup(name);
    }

    private void finishSetup(String name) throws RemoteException {        
        frame.setTitle(name + "'s " + TITLE);
        urlField.setEditable(false);
        portField.setEditable(false);
        nameField.setEditable(false);
        connectBtn.setEnabled(false);
        startBtn.setEnabled(false);
        statusLabel.setText("Conencted");
        messageField.setEnabled(true);
        sendBttn.setEnabled(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        recipient = (String) clients.getSelectedValue();
        if (recipient != null) {
            messageField.setEnabled(true);
//            sendBttn.setEnabled(false);
        } else {
            sendBttn.setEnabled(false);
            messageField.setEnabled(false);
        }
        messageArea.setText("");

        ArrayList<String> messages;
        try {
            if (chatClient == null) {
                messages = chatClient.getMessages(recipient);
            } else {
                messages = chatClient.getMessages(recipient);
            }

            if (messages != null) {
                for (String msg : messages) {
                    messageArea.append(msg);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ChatGUI chatGUI = new ChatGUI();

        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chatGUI);
        frame.pack();
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width - frameDimension.width) / 2,
                (screenDimension.height - frameDimension.height) / 2);
        frame.setVisible(true);
    }
}
