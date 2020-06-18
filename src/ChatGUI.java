
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sinan
 */
public class ChatGUI extends JPanel implements ActionListener {

    private static final String TITLE = "Chat Box";
    private static final Font PRIMARY_FONT = new Font("Arial", Font.BOLD, 15);

    private static JFrame frame;
    private ChatInterface client, server;

    private JTextField urlField, nameField;

    private JButton connectBtn, sendBttn;
    private JLabel statusLabel;
    private JTextArea messageArea;
    private JTextField messageField;

    public ChatGUI() {
        super(new BorderLayout());

        JPanel ConnectionPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        JLabel label;
        JPanel panel;
        JScrollPane scrollPane;

        panel = new JPanel(new BorderLayout(6, 0));
        label = new JLabel("Host:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        urlField = new JTextField("localhost", 15);
        panel.add(urlField, BorderLayout.CENTER);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(5, 0));
        label = new JLabel("User:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        nameField = new JTextField(15);
        panel.add(nameField, BorderLayout.CENTER);
        formPanel.add(panel);

        connectBtn = new JButton("Connect");
        connectBtn.addActionListener(this);
        formPanel.add(connectBtn);

        statusLabel = new JLabel("Not Connected!");
        statusLabel.setFont(PRIMARY_FONT);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setVerticalAlignment(JLabel.CENTER);
        formPanel.add(statusLabel);

        ConnectionPanel.add(formPanel, BorderLayout.NORTH);
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
            String name = nameField.getText();
            if ((url.equals("")) || (name.equals(""))) {
                displayMessage("Error", "Please fill in the fields");
            } else {
                try {
                    client = new Chat(name);
                    Registry registry = LocateRegistry.getRegistry(url);
                    server = (ChatInterface) registry.lookup("chat");

                    String msg = "[" + client.getName() + "] successfuly connected";
                    server.send(msg);
                    server.addClient(client);
                    System.out.println("Ready!");

                    frame.setTitle(name + "'s " + TITLE);
                    urlField.setEditable(false);
                    nameField.setEditable(false);
                    connectBtn.setEnabled(false);
                    statusLabel.setText("Connected");
                    messageField.setEnabled(true);
                    sendBttn.setEnabled(true);
                } catch (NotBoundException | RemoteException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (src == sendBttn) {
            try {
                String msg = ": " + messageField.getText();
                messageField.setText("");
                sendBttn.setEnabled(false);
                messageArea.append("You" + msg + "\n");
                server.send(client.getName() + msg + "\n");
            } catch (RemoteException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
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
