
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
public class ChatGUI extends JPanel {

    private static final Font PRIMARY_FONT = new Font("Arial", Font.BOLD, 15);
    
    private static JFrame frame;
    private ChatInterface chat;
    
    private JTextField hostField, portField, userField;
    
    private JButton loginBttn, sendBttn;
    private JLabel statusLabel;

    public ChatGUI(ChatInterface chat) {
        super(new BorderLayout());
        
        JPanel ConnectionPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        JLabel label;
        JPanel panel;

        panel = new JPanel(new BorderLayout(6, 0));
        label = new JLabel("Host:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        hostField = new JTextField("localhost", 15);
        panel.add(hostField, BorderLayout.CENTER);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(9, 0));
        label = new JLabel("Port:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        portField = new JTextField(15);
        panel.add(portField);
        formPanel.add(panel);

        panel = new JPanel(new BorderLayout(5, 0));
        label = new JLabel("User:");
        label.setFont(PRIMARY_FONT);
        panel.add(label, BorderLayout.WEST);
        userField = new JTextField(15);
        panel.add(userField, BorderLayout.CENTER);
        formPanel.add(panel);

        loginBttn = new JButton("Connect");
        formPanel.add(loginBttn);

        statusLabel = new JLabel("Not Connected!");
        statusLabel.setFont(PRIMARY_FONT);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setVerticalAlignment(JLabel.CENTER);
        formPanel.add(statusLabel);
        
        ConnectionPanel.add(formPanel, BorderLayout.NORTH);
        add(ConnectionPanel, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        try {
            ChatInterface chat = new Chat();
            ChatGUI chatGUI = new ChatGUI(chat);

            frame = new JFrame("Chat Box");
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
        } catch (RemoteException ex) {
            Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
