package DBTool;



import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.*;

/**
 * 1.帳號密碼記憶功能
 * 2.讀取資料進度條
 */

class AddressDialog extends JDialog implements ActionListener, ItemListener {

    JLabel label1 = new JLabel("Server");
    JLabel label2 = new JLabel("Port");
    JLabel label3 = new JLabel("Database");
    JLabel label4 = new JLabel("User");
    JLabel label5 = new JLabel("Password");
    JTextField serverField = new JTextField();
    JTextField portField = new JTextField();
    JTextField databaseField = new JTextField();
    JTextField userField = new JTextField();
    JTextField passwordField = new JTextField();
    String[] address = new String[5];
    JButton loginin = new JButton("login");
    JButton cancel = new JButton("cancel");
    JRadioButton radio = new JRadioButton("記憶帳號密碼");
    private boolean remember = false;

    public AddressDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();

    }

    private void init() {
        this.setTitle("Address Dialog");
        this.setLayout(new GridLayout(7, 2));
        //
        this.add(label1);
        this.add(serverField);

        serverField.setText(DBRecorder.getServer());
        System.out.println(DBRecorder.getServer());
        //
        this.add(label2);
        this.add(portField);
        portField.setText(DBRecorder.getPort());
        System.out.println(DBRecorder.getPort());
        //
        this.add(label3);
        this.add(databaseField);
        databaseField.setText(DBRecorder.getDatabase());
        System.out.println(DBRecorder.getDatabase());
        //
        this.add(label4);
        this.add(userField);
        userField.setText(DBRecorder.getUser());
        System.out.println(DBRecorder.getUser());
        //
        this.add(label5);
        this.add(passwordField);
        passwordField.setText(DBRecorder.getPassword());
        System.out.println(DBRecorder.getPassword());
        //
        this.add(loginin);
        loginin.addActionListener(this);
        this.add(cancel);
        cancel.addActionListener(this);
        //
        this.add(radio);
        radio.setSelected(DBRecorder.isRemember());
        radio.addItemListener(this);

    }


    public String[] getAddress() {
        address[0] = serverField.getText() + ":";

        address[1] = portField.getText();
        address[2] = databaseField.getText();
        address[3] = userField.getText();
        address[4] = passwordField.getText();
        return address;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginin) {

            DBRecorder.updateParams(getAddress());
            if (!remember) {
                DBRecorder.deleteFile();
            }else{
                DBRecorder.setRemember(true);
                DBRecorder.writeData();

            }
            Connection conn = DBRecorder.getConnect();
            if (conn != null) {
                JOptionPane.showMessageDialog(null, "connected", "DB", JOptionPane.INFORMATION_MESSAGE);

                DBRecorder.executeQuery(conn);

//                DBRecorder.executeUpdate(conn);
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "connect fail", "DB", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == cancel) {
            this.dispose();
        }
        if (e.getSource() == radio) {

        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            remember = true;//當remember為true,執行login時建立紀錄檔

        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            remember = false;//當remember為false,執行login時刪除紀錄檔

        }


    }
}

public class DBDialog extends JFrame {
    AddressDialog dialog = new AddressDialog(this, false);

    public DBDialog(String title) {
        super(title);
        init();
    }

    public DBDialog() {
        super();
        init();

    }

    private void init() {
        this.getContentPane().setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final AddressDialog dialog = new AddressDialog(this, false);
        JButton button = new JButton("Show Dialog");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dialog.setSize(500, 250);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
        this.getContentPane().add(button);
    }

    public static void main(String[] args) {

        DBRecorder.getInstance();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        DBDialog frame = new DBDialog();
        frame.pack();
        frame.setVisible(true);

    }
}