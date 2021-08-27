package DBTool;

import DBTool.DBRecorder;
import DBTool.DB_tb_select;
import DBTool.Meta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class AddressDialog extends JDialog implements ActionListener, ItemListener {

    JLabel label1 = new JLabel("Server");
    JLabel label2 = new JLabel("Port");
    JLabel label3 = new JLabel("Database");
    JLabel label4 = new JLabel("User");
    JLabel label5 = new JLabel("Password");
    JTextField serverField = new JTextField();
    JTextField portField = new JTextField();
    JTextField databaseField = new JTextField();
    JTextField userField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    String[] address = new String[5];
    JButton loginin = new JButton("login");
    JButton cancel = new JButton("cancel");
    JRadioButton radio = new JRadioButton("記憶帳號密碼");


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
        radio.setSelected(DBRecorder.getRemember());
        radio.addItemListener(this);

    }


    public String[] getAddress() {
        address[0] = serverField.getText();

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
            DBRecorder.updateFile();

            try (
                    Statement st = DBRecorder.getStatement();
                    ResultSet rs = st.executeQuery("use " + DBRecorder.getDatabase() + " select TABLE_SCHEMA,table_name,column_name,data_type from INFORMATION_SCHEMA.COLUMNS")

            ) {
                JOptionPane.showMessageDialog(null, "Connected to " + DBRecorder.getDatabase(), "message", JOptionPane.INFORMATION_MESSAGE);
                //概念是不重複的table_name,內含column_name & data_type
                String tempName = "null";//暫存表格
                Vector<Meta.Table> tableList = new Vector<>();//預先準備這個DB的所有表格資料
                Meta.Table tempTable = null;
                while (rs.next()) {
                    //餵資料給DBRecorder,並準備selectTable
                    String table_name = rs.getString("TABLE_SCHEMA")+"."+rs.getString("table_name");//
                    String column_name = rs.getString("column_name");
                    String data_type = rs.getString("data_type");

                    if (!tempName.equals(table_name)) {
                        //目前假設抓回來的資料依表格名稱排序,
                        // 那麼如果出現新的表格名稱,就新增一個table物件,

                        Meta.Table table = new Meta.Table(table_name);
                        tableList.add(table);//將這張表加進去
                        tempTable = table;
                        tempName = table_name;

                    }
                    //將資料寫入目前的table
                    tempTable.addColumn(new Meta.Column(column_name, data_type));
                }

                DB_tb_select select = new DB_tb_select(tableList);
                select.setVisible(true);
                select.setTitle("選擇表格");
                select.setSize(500,100);
//                Meta.Table pick = select.getKey();

//                DBRecorder.executeQuery(pick);

                //將table資料另開視窗顯示*/

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this,Dialog_Message.INVALID_CONNECTION,Dialog_MsgTitle.ERROR, JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }

        }
        if (e.getSource() == cancel) {
            this.dispose();
        }
        if (e.getSource() == radio) {

        }
    }

    @Override
    //radio button,改寫紀錄檔狀態
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {

            DBRecorder.setRemember(true);
            DBRecorder.updateFile();

        } else if (e.getStateChange() == ItemEvent.DESELECTED) {

            DBRecorder.setRemember(false);
            DBRecorder.updateFile();

        }


    }
}

